package com.example.myapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_recipes")
data class SavedRecipe(
    @PrimaryKey val id: String,
    val name: String,
    val thumb: String?,
    val area: String?,
    val category: String?,
    val instructions: String?,
    val ingredients: List<String>,   // stored via TypeConverter
    val notes: String = ""           // user editable
)
