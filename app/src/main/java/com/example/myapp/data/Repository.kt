package com.example.myapp.data

import android.content.Context
import com.example.myapp.data.local.AppDatabase
import com.example.myapp.data.local.SavedRecipe
import com.example.myapp.data.model.Meal
import com.example.myapp.data.model.MealDetail
import com.example.myapp.data.remote.MealDbRaw
import com.example.myapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

object Repository {
    private lateinit var db: AppDatabase
    private val mealApi = RetrofitClient.mealApi

    fun init(context: Context) {
        db = AppDatabase.get(context)
    }

    private fun MealDbRaw.toMeal(): Meal {
        val ingredients = mutableMapOf<String, String>()
        val ingredientFields = listOf(
            strIngredient1 to strMeasure1, strIngredient2 to strMeasure2,
            strIngredient3 to strMeasure3, strIngredient4 to strMeasure4,
            strIngredient5 to strMeasure5, strIngredient6 to strMeasure6,
            strIngredient7 to strMeasure7, strIngredient8 to strMeasure8,
            strIngredient9 to strMeasure9, strIngredient10 to strMeasure10,
            strIngredient11 to strMeasure11, strIngredient12 to strMeasure12,
            strIngredient13 to strMeasure13, strIngredient14 to strMeasure14,
            strIngredient15 to strMeasure15, strIngredient16 to strMeasure16,
            strIngredient17 to strMeasure17, strIngredient18 to strMeasure18,
            strIngredient19 to strMeasure19, strIngredient20 to strMeasure20
        )
        ingredientFields.forEach { (ing, meas) ->
            if (!ing.isNullOrBlank()) {
                ingredients[ing] = meas ?: ""
            }
        }
        
        return Meal(
            id = idMeal ?: "",
            name = strMeal ?: "",
            thumb = strMealThumb ?: "",
            area = strArea,
            category = strCategory,
            instructions = strInstructions,
            ingredients = ingredients
        )
    }

    val saved: Flow<List<Meal>>
        get() = db.savedDao().getAllSaved().map { list ->
            list.map { 
                Meal(
                    id = it.id,
                    name = it.name,
                    thumb = it.thumb ?: "",
                    area = it.area,
                    category = null,
                    instructions = null,
                    ingredients = emptyMap()
                )
            }
        }

    suspend fun savedList(): List<Meal> {
        val list = db.savedDao().getAllSaved().first()
        return list.map { 
            Meal(
                id = it.id,
                name = it.name,
                thumb = it.thumb ?: "",
                area = it.area,
                category = null,
                instructions = null,
                ingredients = emptyMap()
            )
        }
    }

    fun isSaved(id: String): Flow<Boolean> = db.savedDao().isSaved(id)

    suspend fun save(meal: Meal) {
        db.savedDao().upsert(
            SavedRecipe(meal.id, meal.name, meal.thumb, meal.area, notes = "")
        )
    }

    suspend fun remove(id: String) = db.savedDao().remove(id)

    suspend fun updateNotes(id: String, notes: String) {
        db.savedDao().updateNotes(id, notes)
    }

    suspend fun getSavedRecipeWithNotes(id: String): SavedRecipe? {
        val allSaved = db.savedDao().getAllSaved().first()
        return allSaved.find { it.id == id }
    }

    suspend fun toggleSave(meal: Meal): Boolean {
        val isCurrentlySaved = db.savedDao().isSaved(meal.id).first()
        return if (isCurrentlySaved) {
            remove(meal.id)
            false
        } else {
            save(meal)
            true
        }
    }

    suspend fun random(): Meal? {
        return try {
            val response = mealApi.getRandom()
            response.meals?.firstOrNull()?.toMeal()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun search(query: String): List<Meal> {
        return try {
            val response = mealApi.search(query)
            response.meals?.map { it.toMeal() } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun lookup(id: String): Meal? {
        return try {
            val response = mealApi.lookup(id)
            response.meals?.firstOrNull()?.toMeal()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun detail(id: String): MealDetail? {
        return try {
            val meal = lookup(id) ?: return null
            MealDetail(
                id = meal.id,
                name = meal.name,
                ingredients = meal.ingredients,
                instructions = meal.instructions
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
