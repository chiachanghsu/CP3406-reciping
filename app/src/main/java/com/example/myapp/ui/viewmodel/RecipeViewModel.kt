package com.example.myapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.data.database.AppDatabase
import com.example.myapp.data.model.Recipe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).recipeDao()

    val recipes: StateFlow<List<Recipe>> =
        dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            dao.insertAll(
                listOf(
                    Recipe(
                        name = "Garlic Butter Shrimp Pasta",
                        ingredients = """
                            Spaghetti
                            Shrimp
                            Garlic, Butter, Olive oil
                            Lemon juice, Parsley, Salt & Pepper
                        """.trimIndent(),
                        timeMinutes = 20
                    ),
                    Recipe(
                        name = "Veggie Fried Rice",
                        ingredients = """
                            Cooked rice
                            Eggs
                            Carrot, Peas, Spring onion
                            Soy sauce, Sesame oil, Garlic
                        """.trimIndent(),
                        timeMinutes = 15
                    ),
                    Recipe(
                        name = "Fluffy Pancakes",
                        ingredients = """
                            Flour, Sugar, Baking powder
                            Milk, Egg, Butter
                            Vanilla, Pinch of salt
                        """.trimIndent(),
                        timeMinutes = 25
                    )
                )
            )
        }
    }
}
