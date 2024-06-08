package com.example.marmitonai.models
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(vararg ingredients: Ingredient)

    @Delete
    fun deleteIngredient(vararg ingredients: Ingredient)

    @Query("SELECT * FROM ingredients")
    fun loadAllIngredients():Array<Ingredient>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun loadIngredient(id: String): Ingredient
}