package com.example.marmitonai.models
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: Int,
    val description: String
)
