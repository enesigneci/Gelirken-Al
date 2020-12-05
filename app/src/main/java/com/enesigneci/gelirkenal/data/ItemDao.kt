package com.enesigneci.gelirkenal.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.enesigneci.gelirkenal.data.model.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): LiveData<List<Item>>

    @Insert
    suspend fun insert(vararg item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE from item")
    suspend fun deleteAllItems()
}