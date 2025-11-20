package com.example.myapp.data.database

import androidx.room.*
import com.example.myapp.data.model.SavedRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM saved_recipes ORDER BY name")
    fun allSaved(): Flow<List<SavedRecipe>>

    @Query("SELECT * FROM saved_recipes WHERE id = :id LIMIT 1")
    fun byId(id: String): Flow<SavedRecipe?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(r: SavedRecipe)

    @Query("UPDATE saved_recipes SET notes = :notes WHERE id = :id")
    suspend fun updateNotes(id: String, notes: String)

    @Query("DELETE FROM saved_recipes WHERE id = :id")
    suspend fun delete(id: String)
}
