package com.example.marmitonai.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.marmitonai.R
import com.example.marmitonai.models.Ingredient
import com.example.marmitonai.models.OpenFoodFactFetcher
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IngredientDetail : AppCompatActivity() {
    private val openFoodFactFetcher: OpenFoodFactFetcher = OpenFoodFactFetcher()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingredient_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ingredient: Ingredient? = intent.getParcelableExtra("ingredient", Ingredient::class.java)
        ingredient?.let {
            findViewById<ImageView>(R.id.detailIngredientImageView).setImageURI(Uri.parse(it.imageId))
            findViewById<TextView>(R.id.detailIngredientName).text = "Name: ${it.name}"
            findViewById<TextView>(R.id.detailIngredientServingSize).text = "Serving Size: ${it.servingSize} ${it.servingUnit}"
            findViewById<TextView>(R.id.detailIngredientKcal).text = "Kcal: ${it.kcal}/100g"
            findViewById<TextView>(R.id.detailIngredientFat).text = "Fat: ${it.fat}g /100g"
            findViewById<TextView>(R.id.detailIngredientCarbs).text = "Carbs: ${it.carbs}g /100g"
            findViewById<TextView>(R.id.detailIngredientSugars).text = "Sugars: ${it.sugars}g /100g"
            findViewById<TextView>(R.id.detailIngredientProtein).text = "Protein: ${it.protein}g /100g"
        }

        findViewById<FloatingActionButton>(R.id.deleteButton).setOnClickListener {
            ingredient?.let { deleteIngredient(it) }
        }
    }

    private fun deleteIngredient(ingredient: Ingredient) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                openFoodFactFetcher.ingredientDestroyer(ingredient.barcode)
                withContext(Dispatchers.Main) {
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
