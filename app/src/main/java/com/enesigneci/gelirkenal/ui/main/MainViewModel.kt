package com.enesigneci.gelirkenal.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.enesigneci.gelirkenal.data.AppDatabase
import com.enesigneci.gelirkenal.data.model.Item
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class MainViewModel : ViewModel() {
    suspend fun addItem(item: Item) {
        AppDatabase.getInstance().itemDao().insert(item)
    }
    fun getAllItems(): LiveData<List<Item>> {
        return AppDatabase.getInstance().itemDao().getAll()
    }
    suspend fun deleteAllItems() {
        AppDatabase.getInstance().itemDao().deleteAllItems()
    }
    suspend fun deleteItem(item: Item) {
        return AppDatabase.getInstance().itemDao().delete(item)
    }
    fun loadAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}