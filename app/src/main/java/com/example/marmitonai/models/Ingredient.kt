package com.example.marmitonai.models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey val barcode: String,
    val name: String,
    val servingSize: Int,
    val servingUnit: String,
    val kcal: Int,
    val fat: Int,
    val carbs: Int,
    val sugars: Int,
    val protein: Int,
    val salt: Int,
    val imageId: String?,
    @ColumnInfo(defaultValue = "0") val quantity: Int
    )