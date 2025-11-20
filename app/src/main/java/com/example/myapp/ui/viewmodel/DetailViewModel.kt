package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.Repository
import com.example.myapp.data.remote.MealDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repo: Repository = Repository()
) : ViewModel() {

    private val _meal = MutableStateFlow<MealDetail?>(null)
    val meal: StateFlow<MealDetail?> = _meal

    fun load(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _meal.value = repo.detail(id)
        }
    }
}
