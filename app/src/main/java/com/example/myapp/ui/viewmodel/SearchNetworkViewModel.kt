package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.remote.MealsApi
import com.example.myapp.data.remote.Meal
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted

class SearchNetworkViewModel : ViewModel() {

    // what the user typed
    private val queryFlow = MutableStateFlow("")

    // expose query so UI can reflect it
    val query: StateFlow<String> = queryFlow

    // UI state
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // search results from network
    private val _results = MutableStateFlow<List<Meal>>(emptyList())
    val results: StateFlow<List<Meal>> = _results

    init {
        // react to typing with debounce
        viewModelScope.launch {
            queryFlow
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { q ->
                    if (q.isBlank()) {
                        _results.value = emptyList()
                        _error.value = null
                        _loading.value = false
                    } else {
                        fetchFromNetwork(q)
                    }
                }
        }
    }

    fun setQuery(newValue: String) {
        queryFlow.value = newValue
    }

    private fun fetchFromNetwork(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.value = true
                _error.value = null
                val resp = MealsApi.service.searchMeals(q)
                _results.value = resp.meals.orEmpty()
            } catch (t: Throwable) {
                _results.value = emptyList()
                _error.value = t.message ?: "Network error"
            } finally {
                _loading.value = false
            }
        }
    }
}
