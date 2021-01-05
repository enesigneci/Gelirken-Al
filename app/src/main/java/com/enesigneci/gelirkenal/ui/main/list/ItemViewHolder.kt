package com.enesigneci.gelirkenal.ui.main.list

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enesigneci.gelirkenal.databinding.ItemBinding
class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val checkBox: CheckBox
    val quantity: TextView
    private var _binding: ItemBinding? = null
    private val binding get() = _binding!!
    init {
        _binding = ItemBinding.bind(view)
        val binding = binding
        checkBox = binding.cbName
        quantity = binding.quantity
    }
}