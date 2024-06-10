package com.example.marmitonai.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(vararg ingredients: Ingredient)

    @Delete
    suspend fun deleteIngredient(vararg ingredients: Ingredient)

    @Query("SELECT * FROM ingredients")
    suspend fun loadAllIngredients(): List<Ingredient>

    @Query("SELECT * FROM ingredients WHERE barcode = :barcode")
    suspend fun loadIngredient(barcode: String): Ingredient

    @Query("UPDATE ingredients SET quantity = :quantity WHERE barcode = :barcode")
    suspend fun changeQuantity(barcode: String, quantity: Int)
}
