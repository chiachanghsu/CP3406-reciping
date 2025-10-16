package com.example.myapp.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipes",
    indices = [Index(value = ["name"], unique = true)]
)
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val ingredients: String,
    val timeMinutes: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
