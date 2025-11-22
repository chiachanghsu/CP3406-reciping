package com.example.myapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_recipes")
data class SavedRecipe(
    @PrimaryKey val id: String,
    val name: String,
    val area: String? = null,
    val thumb: String? = null,
    val savedAt: Long = System.currentTimeMillis()
)
