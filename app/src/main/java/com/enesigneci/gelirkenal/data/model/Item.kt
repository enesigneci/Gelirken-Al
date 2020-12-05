package com.enesigneci.gelirkenal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(@PrimaryKey(autoGenerate = true) val id: Int, val name: String, val quantity: String, val isBought: Boolean)