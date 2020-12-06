package com.enesigneci.gelirkenal.ui.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enesigneci.gelirkenal.R
import com.enesigneci.gelirkenal.data.model.Item


class ItemAdapter(private var dataSet: ArrayList<Item>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(newData: ArrayList<Item>) {
        this.dataSet = newData
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item, viewGroup, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ItemViewHolder)

        dataSet?.let {
            viewHolder.checkBox.text = dataSet?.get(position)?.name ?: ""
            viewHolder.checkBox.isChecked = dataSet?.get(position)?.isBought ?: false
            viewHolder.quantity.text = dataSet?.get(position)?.quantity ?: ""
        }

    }

    override fun getItemCount(): Int = dataSet.let {
        return it?.size ?: 0
    }

    fun getItem(position: Int): Item? {
        return dataSet?.get(position)
    }
}