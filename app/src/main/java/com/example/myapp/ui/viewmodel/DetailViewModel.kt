package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.remote.MealDetail
import com.example.myapp.data.remote.MealsApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _meal = MutableStateFlow<MealDetail?>(null)
    val meal: StateFlow<MealDetail?> = _meal

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun load(id: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                _meal.value = MealsApi.service.lookupMeal(id).meals?.firstOrNull()
            } catch (t: Throwable) {
                _error.value = t.message
            } finally {
                _loading.value = false
            }
        }
    }
}
