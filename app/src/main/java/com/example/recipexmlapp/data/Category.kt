package com.example.recipexmlapp.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Category(
    val id: Int,
    val title: String,
    @SerialName("imageUrl") val imageName: String,
    val description: String
)
