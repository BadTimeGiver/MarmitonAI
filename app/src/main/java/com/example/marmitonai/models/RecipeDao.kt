package com.example.marmitonai.models
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(vararg recipes: Recipe)

    @Delete
    fun deleteRecipe(vararg recipes: Recipe)

    @Query("SELECT * FROM recipes")
    fun loadAllRecipes():Array<Recipe>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun loadRecipe(id: String): Recipe
}