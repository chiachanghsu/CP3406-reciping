package com.example.myapp.data.model

data class MealDetail(
    val id: String,
    val name: String,
    val ingredients: Map<String, String> = emptyMap(),
    val instructions: String? = null
)
