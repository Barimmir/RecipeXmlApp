package com.example.recipexmlapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val description: String = "",
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String
)

@Serializable
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String
)
