package com.example.marmitonai.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marmitonai.R
import com.example.marmitonai.models.Ingredient
import com.example.marmitonai.models.Recipe

class RecipeAdapter(
    private var recipeList: List<Recipe>,
    private val recipeClickListener: OnRecipeClickListener
) : RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {

    interface OnRecipeClickListener {
        fun onRecipeClick(recipe: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_recipe, parent, false)
        return RecipeHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val currentIngredient = recipeList[position]
        holder.bind(currentIngredient, recipeClickListener)
    }
    fun updateRecipes(newRecipes: List<Recipe>) {
        recipeList = newRecipes
        notifyDataSetChanged()
    }

    class RecipeHolder(val recipeView: View) : RecyclerView.ViewHolder(recipeView) {
        fun bind(recipe: Recipe, recipeClickListener: OnRecipeClickListener) {
            recipeView.findViewById<TextView>(R.id.recipeNameView).text = recipe.name
            recipeView.findViewById<Button>(R.id.detailButtonRecipeView).setOnClickListener {
                recipeClickListener.onRecipeClick(recipe)
            }
        }
    }

}
