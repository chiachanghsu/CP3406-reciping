package com.example.myapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.database.AppDatabase
import com.example.myapp.data.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).recipeDao()

    val query = MutableStateFlow("")

    private val likeQuery = query.map { q ->
        val trimmed = q.trim()
        if (trimmed.isEmpty()) null else "%${trimmed.replace('%', '_')}%"
    }

    val results: StateFlow<List<Recipe>> =
        likeQuery.flatMapLatest { q ->
            if (q == null) dao.getAll()
            else dao.search(q)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
