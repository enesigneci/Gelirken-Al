package com.enesigneci.gelirkenal.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.enesigneci.gelirkenal.App
import com.enesigneci.gelirkenal.data.model.Item

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getInstance(): AppDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    App.applicationContext(),
                    AppDatabase::class.java,
                    "gelirkenal")
                    .build()
            }

            return INSTANCE as AppDatabase
        }
    }
}