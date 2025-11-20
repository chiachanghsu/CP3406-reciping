package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.Repository
import com.example.myapp.data.remote.MealSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeRandomViewModel(
    private val repo: Repository = Repository()
) : ViewModel() {

    private val _meals = MutableStateFlow<List<MealSummary>>(emptyList())
    val meals: StateFlow<List<MealSummary>> = _meals

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    /** Load one more random card */
    fun loadMoreOne() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                repo.randomOne()?.let { rnd ->
                    _meals.value = _meals.value + rnd
                }
            } finally {
                _loading.value = false
            }
        }
    }

    /** Initial fill (e.g., 6 cards) */
    fun ensureInitial(count: Int = 6) {
        if (_meals.value.isNotEmpty()) return
        repeat(count) { loadMoreOne() }
    }

    /** Pull-to-refresh: clear and refill */
    fun refresh() {
        _meals.value = emptyList()
        ensureInitial()
    }
}
