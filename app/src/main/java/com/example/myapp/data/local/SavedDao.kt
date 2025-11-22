package com.example.myapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedDao {

    @Query("SELECT * FROM saved_recipes ORDER BY savedAt DESC")
    fun getAllSaved(): Flow<List<SavedRecipe>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_recipes WHERE id = :id)")
    fun isSaved(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: SavedRecipe)

    @Query("DELETE FROM saved_recipes WHERE id = :id")
    suspend fun remove(id: String)

    @Query("UPDATE saved_recipes SET notes = :notes WHERE id = :id")
    suspend fun updateNotes(id: String, notes: String)
}
