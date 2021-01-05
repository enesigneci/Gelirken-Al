package com.enesigneci.gelirkenal.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enesigneci.gelirkenal.App
import com.enesigneci.gelirkenal.R
import com.enesigneci.gelirkenal.data.model.Item
import com.enesigneci.gelirkenal.databinding.MainFragmentBinding
import com.enesigneci.gelirkenal.ui.component.SwipeToDeleteCallback
import com.enesigneci.gelirkenal.ui.main.list.ItemAdapter
import com.enesigneci.gelirkenal.utils.hideKeyboard
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var list = mutableListOf<Item>()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        setupViews(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        var data = activity?.intent?.getStringExtra("deeplink")
        data?.let {
            AlertDialog.Builder(context).setCancelable(false)
                .setTitle(getString(R.string.do_you_want_to_change_your_list))
                .setMessage(getString(R.string.all_of_your_list_will_be_deleted))
                .setPositiveButton(getString(R.string.yes), object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.getAllFromRemote(data)
                        }
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
                .show()
        }
    }

    private fun setupViews(view: View) {
        val rvList = MainFragmentBinding.bind(view).rvList
        rvList.adapter = ItemAdapter(list as ArrayList<Item>?)

        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.absoluteAdapterPosition
                viewModel.viewModelScope.launch {
                    (rvList.adapter as ItemAdapter).getItem(position)?.let { viewModel.deleteItem(it) }
                }
            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(rvList)
        rvList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.getAllItems().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.emptyIcon.visibility = View.VISIBLE
                binding.rvList.visibility = View.INVISIBLE
            } else {
                binding.emptyIcon.visibility = View.INVISIBLE
                binding.rvList.visibility = View.VISIBLE
            }
            (binding.rvList.adapter as ItemAdapter).setData(it as ArrayList<Item>)
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(App.uuid.toString())
            myRef.setValue(it)
        })

        viewModel.loadAd(binding.adView)
        binding.btnAdd.setOnClickListener {
            viewModel.viewModelScope.launch {
                if (binding.etName.text.isNullOrEmpty()) {
                    showErrorMessage(binding.etName, getString(R.string.you_must_set_a_name_to_the_product))
                    return@launch
                }
                addItemToList(binding.etName.text.toString(), binding.etQuantity.text.toString())
            }

        }

        binding.btnEnd.setOnClickListener{
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
        binding.rvList.adapter?.itemCount?.let { position -> binding.rvList.adapter?.notifyItemInserted(position) }
        binding.etName.text.clear()
        binding.etQuantity.text.clear()
        context?.hideKeyboard(binding.btnAdd)
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
                putExtra(Intent.EXTRA_TEXT, "https://gelirken-al-deeplink.web.app/?data=" + App.uuid)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}