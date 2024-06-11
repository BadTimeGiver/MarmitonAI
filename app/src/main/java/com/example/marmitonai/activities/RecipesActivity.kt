package com.example.marmitonai.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marmitonai.R
import com.example.marmitonai.models.OpenAiFetcher
import com.example.marmitonai.models.Recipe
import kotlinx.coroutines.*

class RecipesActivity : AppCompatActivity(), RecipeAdapter.OnRecipeClickListener {
    private val openAiFetcher: OpenAiFetcher = OpenAiFetcher()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recipeView)
        recipeAdapter = RecipeAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recyclerView.adapter = recipeAdapter
        fetchRecipes()
        setEventSubmit()
        val ingredientsButton: Button = findViewById(R.id.ingredientsButton)
        ingredientsButton.setOnClickListener {
            val intent = Intent(this, IngredientsActivity::class.java)
            startActivity(intent)        }
    }

    override fun onRecipeClick(recipe: Recipe) {
        val intent = Intent(this, RecipeDetail::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }

    private fun setEventSubmit() {
        val submitButton: Button = findViewById(R.id.submitRecipeButton)
        submitButton.setOnClickListener {
            val textInput: EditText = findViewById(R.id.recipeNameInput)
            val name = textInput.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    openAiFetcher.createRecipe(name)
                    fetchRecipes()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchRecipes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val recipes = openAiFetcher.db.recipeDao().loadAllRecipes()
                withContext(Dispatchers.Main) {
                    recipeAdapter.updateRecipes(recipes.toList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        fetchRecipes()
    }
}
