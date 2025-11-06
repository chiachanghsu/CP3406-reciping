package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.remote.MealsApi
import com.example.myapp.data.remote.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchNetworkViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _results = MutableStateFlow<List<Meal>>(emptyList())
    val results: StateFlow<List<Meal>> = _results

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun setQuery(q: String) {
        _query.value = q
        search(q)
    }

    fun search(q: String) {
        val trimmed = q.trim()
        if (trimmed.isEmpty()) {
            _results.value = emptyList()
            _error.value = null
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.value = true
                _error.value = null

                val resp = MealsApi.service.searchMeals(trimmed)
                val meals = resp.meals.orEmpty()

                // Smart sort:
                // 1) names starting with the query
                // 2) names containing the query later
                // 3) alphabetical fallback
                val qLower = trimmed.lowercase()
                val sorted = meals.sortedWith(
                    compareBy<Meal>(
                        { !it.strMeal.startsWith(qLower, ignoreCase = true) },
                        {
                            val idx = it.strMeal.lowercase().indexOf(qLower)
                            if (idx == -1) Int.MAX_VALUE else idx
                        },
                        { it.strMeal.lowercase() }
                    )
                )

                _results.value = sorted
            } catch (t: Throwable) {
                _error.value = t.message ?: "Search failed"
                _results.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}
