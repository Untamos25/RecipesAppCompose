package com.example.composeapp.data.local.recipes.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class MethodConverter {
    @TypeConverter
    fun fromListToString(method: List<String>): String {
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun fromStringToList(methodString: String): List<String> {
        return Json.decodeFromString(methodString)
    }
}
