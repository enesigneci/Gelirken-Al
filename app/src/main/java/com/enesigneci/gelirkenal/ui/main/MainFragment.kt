package com.enesigneci.gelirkenal.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.enesigneci.gelirkenal.R
import com.enesigneci.gelirkenal.data.model.Item
import com.enesigneci.gelirkenal.ui.main.list.ItemAdapter
import com.enesigneci.gelirkenal.utils.RecyclerTouchListener
import com.enesigneci.gelirkenal.utils.RecyclerTouchListener.ClickListener
import com.enesigneci.gelirkenal.utils.hideKeyboard
import kotlinx.android.synthetic.main.main_fragment.*
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
            TODO("Fetch the data by uri from Firebase Realtime Database")
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

}