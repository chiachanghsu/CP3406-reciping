package com.example.myapp.data.model

data class Meal(
    val id: String,
    val name: String,
    val thumb: String,
    val area: String? = null,
    val category: String? = null,
    val instructions: String? = null,
    val ingredients: Map<String, String> = emptyMap()
)
