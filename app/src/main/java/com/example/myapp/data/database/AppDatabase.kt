package com.example.myapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapp.data.model.SavedRecipe

@Database(entities = [SavedRecipe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile private var I: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            I ?: synchronized(this) {
                I ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipes.db"
                ).build().also { I = it }
            }
    }
}
