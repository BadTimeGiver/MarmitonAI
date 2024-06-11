package com.example.marmitonai.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marmitonai.R
import com.example.marmitonai.models.Ingredient

class IngredientAdapter(
    private var ingredientList: List<Ingredient>,
    private val ingredientClickListener: OnIngredientClickListener
) : RecyclerView.Adapter<IngredientAdapter.IngredientHolder>() {

    interface OnIngredientClickListener {
        fun onIngredientClick(ingredient: Ingredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ingredient, parent, false)
        return IngredientHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) {
        val currentIngredient = ingredientList[position]
        holder.bind(currentIngredient, ingredientClickListener)
    }
    fun updateIngredients(newIngredients: List<Ingredient>) {
        ingredientList = newIngredients
        notifyDataSetChanged()
    }

    class IngredientHolder(val ingredientView: View) : RecyclerView.ViewHolder(ingredientView) {
        fun bind(ingredient: Ingredient, ingredientClickListener: OnIngredientClickListener) {
            ingredientView.findViewById<TextView>(R.id.ingredientNameView).text = ingredient.name
            ingredientView.findViewById<Button>(R.id.detailButtonIngredientView).setOnClickListener {
                ingredientClickListener.onIngredientClick(ingredient)
            }
        }
    }

}
