package com.enesigneci.gelirkenal.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout.VERTICAL
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enesigneci.gelirkenal.App
import com.enesigneci.gelirkenal.R
import com.enesigneci.gelirkenal.data.model.Item
import com.enesigneci.gelirkenal.ui.main.list.ItemAdapter
import com.enesigneci.gelirkenal.utils.RecyclerTouchListener
import com.enesigneci.gelirkenal.utils.RecyclerTouchListener.ClickListener
import com.enesigneci.gelirkenal.utils.hideKeyboard
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var list = mutableListOf<Item>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        setupViews(view)
        return view
    }

    private fun setupViews(view: View) {
        val rvList = view.findViewById<RecyclerView>(R.id.rvList)
        rvList.adapter = ItemAdapter(list as ArrayList<Item>?)

        val decoration = DividerItemDecoration(context, VERTICAL)
        context?.let { ContextCompat.getDrawable(it, R.drawable.divider)?.let { decoration.setDrawable(it) } }
        rvList.addItemDecoration(decoration)
        rvList.addOnItemTouchListener(
            RecyclerTouchListener(
                context,
                rvList,
                object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        (rvList.adapter as ItemAdapter).getItem(position)?.let {
                            viewModel.viewModelScope.launch {
                                (rvList.adapter as ItemAdapter).getItem(position)?.let { item ->
                                    viewModel.deleteItem(item)
                                }
                            }
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
        rvList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        var uri = activity?.intent?.data
        uri?.let {
            uri.path?.let { path ->
                AlertDialog.Builder(context).setCancelable(false)
                    .setTitle("Listenizi değiştirmek istiyor musunuz?")
                    .setMessage("Kendi listenizdeki veriler silinecektir.")
                    .setPositiveButton("Evet", object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            CoroutineScope(Dispatchers.Main).launch {
                                viewModel.getAllFromRemote(path)
                            }
                        }
                    })
                    .setNegativeButton("Hayır", null)
                    .create()
                    .show()
            }
        }
        viewModel.getAllItems().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                empty_icon.visibility = View.VISIBLE
                rvList.visibility = View.INVISIBLE
            } else {
                empty_icon.visibility = View.INVISIBLE
                rvList.visibility = View.VISIBLE
            }
            (rvList.adapter as ItemAdapter).setData(it as ArrayList<Item>)
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(App.uuid.toString())
            myRef.setValue(it)
        })

        viewModel.loadAd(adView)
        btnAdd.setOnClickListener {
            viewModel.viewModelScope.launch {
                if (etName.text.isNullOrEmpty()) {
                    showErrorMessage(etName, getString(R.string.you_must_set_a_name_to_the_product))
                    return@launch
                }
                addItemToList(etName.text.toString(), etQuantity.text.toString())
            }

        }

        btnEnd.setOnClickListener{
            deleteAllItemsFromList()
        }
    }

    private fun deleteAllItemsFromList() {
        viewModel.viewModelScope.launch {
            viewModel.deleteAllItems()
        }
    }

    private suspend fun addItemToList(_name: String, _quantity: String) {
        viewModel.addItem(Item(0, _name, _quantity, false))
        rvList.adapter?.itemCount?.let { position -> rvList.adapter?.notifyItemInserted(position) }
        etName.text.clear()
        etQuantity.text.clear()
        context?.hideKeyboard(btnAdd)
    }

    private fun showErrorMessage(editText: EditText, message: String) {
        val errorDrawable = context?.let { it -> ContextCompat.getDrawable(it, R.drawable.ic_error) }
        errorDrawable?.setBounds(0, 0, errorDrawable.intrinsicWidth, errorDrawable.intrinsicHeight)
        editText.setError(message, errorDrawable)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.btnShare) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "gelirkenal://" + App.uuid)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}