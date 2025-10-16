package com.example.myapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapp.data.model.Recipe

@Database(
    entities = [Recipe::class],  // add Recipe
    version = 3,                               // bump version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reciping.db"
                )
                    .fallbackToDestructiveMigration()   // drop/recreate on version change
                    .build().also { INSTANCE = it }
            }
    }
}
