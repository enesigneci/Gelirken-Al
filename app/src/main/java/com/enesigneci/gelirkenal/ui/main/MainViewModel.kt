package com.enesigneci.gelirkenal.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesigneci.gelirkenal.App
import com.enesigneci.gelirkenal.data.AppDatabase
import com.enesigneci.gelirkenal.data.model.Item
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    suspend fun addItem(item: Item) {
        AppDatabase.getInstance().itemDao().insert(item)
    }
    fun getAllItems(): LiveData<List<Item>> {
        return AppDatabase.getInstance().itemDao().getAll()
    }
    suspend fun deleteAllItems() {
        AppDatabase.getInstance().itemDao().deleteAllItems()
        App.uuid?.let { FirebaseDatabase.getInstance().getReference(it.toString()).setValue(null) }
    }
    suspend fun deleteItem(item: Item) {
        return AppDatabase.getInstance().itemDao().delete(item)
    }
    fun loadAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
    suspend fun getAllFromRemote(path: String) {
        FirebaseDatabase.getInstance().reference
            .child(path)
            .get().addOnCompleteListener { task ->
                if (task.result.hasChildren()) {

                    task.result.children.forEach {
                        snapshot ->
                        if (snapshot.hasChildren()) {

                            snapshot.children.forEach {
                                data ->
                                val itemToInsert = data.getValue(Item::class.java)
                                viewModelScope.launch {
                                    itemToInsert?.let { addItem(it) }
                                }
                            }
                        }
                    }
                }
            }
    }
}