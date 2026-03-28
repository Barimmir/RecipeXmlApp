package com.example.recipexmlapp.model

import android.os.Parcel
import android.os.Parcelable

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Ingredient.CREATOR) ?: emptyList(),
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeTypedList(ingredients)
        parcel.writeStringList(method)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe = Recipe(parcel)
        override fun newArray(size: Int): Array<Recipe?> = arrayOfNulls(size)
    }
}

data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(quantity)
        parcel.writeString(unitOfMeasure)
        parcel.writeString(description)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(parcel: Parcel): Ingredient = Ingredient(parcel)
        override fun newArray(size: Int): Array<Ingredient?> = arrayOfNulls(size)
    }
}
