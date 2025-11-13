package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.remote.Meal
import com.example.myapp.data.remote.MealsApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeRandomViewModel : ViewModel() {
    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private var loading = false

    fun loadMoreRandom(count: Int = 10) {
        if (loading) return
        loading = true
        viewModelScope.launch {
            try {
                val newOnes = buildList {
                    repeat(count) {
                        MealsApi.service.randomMeal().meals?.firstOrNull()?.let { add(it) }
                    }
                }
                _meals.value = _meals.value + newOnes
            } finally {
                loading = false
            }
        }
    }

    fun refresh() {
        _meals.value = emptyList()
        loadMoreRandom()
    }
}
