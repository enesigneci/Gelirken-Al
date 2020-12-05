package com.enesigneci.gelirkenal.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.enesigneci.gelirkenal.data.AppDatabase
import com.enesigneci.gelirkenal.data.model.Item
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class MainViewModel : ViewModel() {
    suspend fun addItem(context: Context, item: Item) {
        AppDatabase.getInstance(context).itemDao().insert(item)
    }
    fun getAllItems(context: Context): LiveData<List<Item>> {
        return AppDatabase.getInstance(context).itemDao().getAll()
    }
    suspend fun deleteAllItems(context: Context) {
        AppDatabase.getInstance(context).itemDao().deleteAllItems()
    }
    suspend fun deleteItem(context: Context, item: Item) {
        return AppDatabase.getInstance(context).itemDao().delete(item)
    }
    fun loadAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}