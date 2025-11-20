package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapp.data.remote.MealSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {
    private val _saved = MutableStateFlow<List<MealSummary>>(emptyList())
    val saved: StateFlow<List<MealSummary>> = _saved

    fun save(meal: MealSummary) {
        if (_saved.value.any { it.id == meal.id }) return
        _saved.value = _saved.value + meal
    }

    fun remove(id: String) {
        _saved.value = _saved.value.filterNot { it.id == id }
    }
}
