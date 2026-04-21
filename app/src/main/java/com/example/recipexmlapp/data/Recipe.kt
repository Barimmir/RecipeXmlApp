package com.example.recipexmlapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String = "",
    @ColumnInfo("ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo("method") val method: List<String>,
    @ColumnInfo("imageUrl") val imageUrl: String,
    @ColumnInfo("categoryId") val categoryId: Int = 0,
    @ColumnInfo("isFavorite") val isFavorite: Boolean = false
)

@Serializable
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String
)
