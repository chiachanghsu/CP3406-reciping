package com.example.myapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.database.AppDatabase
import com.example.myapp.data.model.Word
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WordViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).wordDao()

    val words: StateFlow<List<Word>> =
        dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        // Always try to insert these; duplicates are ignored because text is UNIQUE
        viewModelScope.launch {
            dao.insertAll(
                listOf(
                    Word(text = "Apple"),
                    Word(text = "Banana"),
                    Word(text = "Carrot")
                )
            )
        }
    }
}
