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
import com.example.marmitonai.models.Ingredient
import com.example.marmitonai.models.OpenFoodFactFetcher
import kotlinx.coroutines.*

class IngredientsActivity : AppCompatActivity(), IngredientAdapter.OnIngredientClickListener {
    private val openFoodFactFetcher: OpenFoodFactFetcher = OpenFoodFactFetcher()
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingredients)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.ingredientView)
        ingredientAdapter = IngredientAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ingredientAdapter
        setEventSubmit()
        fetchIngredients()
    }

    override fun onIngredientClick(ingredient: Ingredient) {
        val intent = Intent(this, IngredientDetail::class.java)
        intent.putExtra("ingredient", ingredient)
        startActivity(intent)
    }

    private fun setEventSubmit() {
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            val textInput: EditText = findViewById(R.id.barcodeInput)
            val barCode = textInput.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    openFoodFactFetcher.ingredientCreator(barCode)
                    fetchIngredients() // Refresh the list after adding an ingredient
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchIngredients() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val ingredients = openFoodFactFetcher.fetchAllIngredients()
                withContext(Dispatchers.Main) {
                    ingredientAdapter.updateIngredients(ingredients)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle fetch failure, such as showing an error message
            }
        }
    }
    override fun onResume() {
        super.onResume()
        fetchIngredients()
    }

}
