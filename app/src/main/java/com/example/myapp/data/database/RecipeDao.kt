package com.example.myapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapp.data.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<Recipe>>

    // NEW: search by name or ingredients (case-insensitive for ASCII)
    @Query("""
        SELECT * FROM recipes
        WHERE name LIKE :q OR ingredients LIKE :q
        ORDER BY updatedAt DESC
    """)
    fun search(q: String): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<Recipe>)
}
