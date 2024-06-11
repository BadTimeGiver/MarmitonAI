package com.example.marmitonai.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.marmitonai.R
import com.example.marmitonai.models.OpenAiFetcher
import com.example.marmitonai.models.Recipe
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeDetail : AppCompatActivity() {
    private val openAiFetcher:OpenAiFetcher = OpenAiFetcher()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recipe: Recipe? = intent.getParcelableExtra("recipe", Recipe::class.java)
        recipe?.let {
            findViewById<TextView>(R.id.recipeDetailName).text = "Name: ${it.name}"
            findViewById<TextView>(R.id.recipeDetailDescription).text = it.description
        }
        findViewById<FloatingActionButton>(R.id.deleteRecipeButton).setOnClickListener {
            recipe?.let { deleteRecipe(it) }
        }
    }

    private fun deleteRecipe(recipe: Recipe) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                openAiFetcher.destroyRecipe(recipe)
                withContext(Dispatchers.Main) {
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
