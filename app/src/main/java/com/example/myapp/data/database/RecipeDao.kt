package com.example.myapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapp.data.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    // Keep the name your ViewModel expects:
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): Flow<List<Recipe>>

    @Query("""
        SELECT * FROM recipes
        WHERE name LIKE '%' || :q || '%' 
           OR ingredients LIKE '%' || :q || '%'
        ORDER BY id DESC
    """)
    fun search(q: String): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Recipe>)
}
