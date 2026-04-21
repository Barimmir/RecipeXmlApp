package com.example.recipexmlapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Entity
@Serializable
data class Category(
    @PrimaryKey val id: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("imageName") @SerialName("imageUrl") val imageName: String,
    @ColumnInfo("description") val description: String
)
