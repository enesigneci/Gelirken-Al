package com.enesigneci.gelirkenal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(@PrimaryKey(autoGenerate = true) var id: Int = 0, val name: String = "", val quantity: String = "", val isBought: Boolean = false)