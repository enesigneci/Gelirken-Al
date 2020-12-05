package com.enesigneci.gelirkenal.ui.main.list

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enesigneci.gelirkenal.R

class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val checkBox: CheckBox
    val quantity: TextView
    val buttonDelete: Button

    init {
        checkBox = view.findViewById(R.id.cbName)
        quantity = view.findViewById(R.id.quantity)
        buttonDelete = view.findViewById(R.id.btnDelete)
    }
}