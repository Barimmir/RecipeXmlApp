package com.example.recipexmlapp.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIngredientList(value: String): List<Ingredient> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}
