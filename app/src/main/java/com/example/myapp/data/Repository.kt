package com.example.myapp.data

import com.example.myapp.data.remote.MealsApi
import com.example.myapp.data.remote.MealDetail
import com.example.myapp.data.remote.MealSummary
import com.example.myapp.data.remote.ingredients

class Repository {
    private val api = MealsApi.service

    suspend fun searchByName(q: String): List<MealSummary> {
        if (q.isBlank()) return emptyList()
        return api.searchByName(q).meals.orEmpty()
    }

    suspend fun detail(id: String): MealDetail? {
        return api.detail(id).meals?.firstOrNull()
    }

    suspend fun randomOne(): MealSummary? {
        return api.random().meals?.firstOrNull()?.let { d ->
            // convert detail to summary for lists
            MealSummary(
                id = d.id,
                name = d.name,
                thumb = d.thumb,
                category = d.category,
                area = d.area
            )
        }
    }
}
