package com.example.myapp.data.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun listToString(list: List<String>?): String = list?.joinToString("||") ?: ""
    @TypeConverter
    fun stringToList(s: String?): List<String> = s?.takeIf { it.isNotBlank() }?.split("||") ?: emptyList()
}
