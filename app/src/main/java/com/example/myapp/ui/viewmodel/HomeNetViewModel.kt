package com.example.myapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.remote.Meal
import com.example.myapp.data.remote.MealsApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeNetViewModel(app: Application) : AndroidViewModel(app) {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Load an initial list to prove networking works
        load("chicken")
    }

    fun load(query: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val resp = MealsApi.service.searchMeals(query)
                _meals.value = resp.meals ?: emptyList()
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
                _meals.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}
