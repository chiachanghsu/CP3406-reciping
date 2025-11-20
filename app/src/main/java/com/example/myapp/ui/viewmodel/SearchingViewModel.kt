package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.Repository
import com.example.myapp.data.remote.MealSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchingViewModel(
    private val repo: Repository = Repository()
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _results = MutableStateFlow<List<MealSummary>>(emptyList())
    val results: StateFlow<List<MealSummary>> = _results

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun setQuery(q: String) {
        _query.value = q
        search(q)
    }

    fun search(q: String) {
        if (q.isBlank()) {
            _results.value = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val items = repo.searchByName(q)
                val ranked = items.sortedWith(
                    compareBy<MealSummary>({ !it.name.startsWith(q, true) })
                        .thenBy { !it.name.contains(q, true) }
                        .thenBy { it.name }
                )
                _results.value = ranked
            } finally {
                _loading.value = false
            }
        }
    }
}
