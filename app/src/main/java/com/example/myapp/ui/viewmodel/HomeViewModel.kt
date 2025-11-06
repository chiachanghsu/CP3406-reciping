package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.remote.MealsApi
import com.example.myapp.data.remote.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadRandom() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.value = true
                _error.value = null
                val resp = MealsApi.service.randomMeal()
                _meal.value = resp.meals?.firstOrNull()
            } catch (t: Throwable) {
                _error.value = t.message ?: "Failed to load recipe"
                _meal.value = null
            } finally {
                _loading.value = false
            }
        }
    }
}
