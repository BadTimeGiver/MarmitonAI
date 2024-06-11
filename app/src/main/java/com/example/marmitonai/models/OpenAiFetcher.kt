package com.example.marmitonai.models

import android.content.pm.PackageManager
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.marmitonai.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

class OpenAiFetcher {

    private val openAI = OpenAI(getApiKeyFromManifest(), timeout = Timeout(socket = 360.seconds))
    val db = AppDatabase.getDatabase(MyApplication.applicationContext())

    suspend fun createRecipe(name: String) {
        withContext(Dispatchers.IO) {
            val ingredientList: List<Ingredient> = db.ingredientDao().loadAllIngredients()
            if (ingredientList.isEmpty()) {
                return@withContext
            }

            val ingredientsText = StringBuilder()
            ingredientList.forEach { ingredient ->
                ingredientsText.append(ingredient.name).append(", ")
            }
            if (ingredientsText.isNotEmpty()) {
                ingredientsText.setLength(ingredientsText.length - 2)
            }
            val prompt = "Create a recipe called $name using the following possible ingredients: $ingredientsText. Format:Name, Ingredients, Step 1, Step 2..."
            val request = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = listOf(ChatMessage(role = ChatRole("user"), content = prompt)),
                maxTokens = 200,
                temperature = 0.7
            )

            try {
                val completion = openAI.chatCompletions(request)
                val recipeText = completion.toString()
                val recipe = parseRecipeText(recipeText, name)
                db.recipeDao().insertRecipe(recipe)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun destroyRecipe(recipe: Recipe) {
        db.recipeDao().deleteRecipe(recipe)
    }

    private fun getApiKeyFromManifest(): String {
        return try {
            val packageManager: PackageManager = MyApplication.applicationContext().packageManager
            val packageName: String = MyApplication.applicationContext().packageName
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val apiKey = appInfo.metaData?.getString("com.google.android.API_KEY")
            apiKey ?: "API Key not found"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "API Key not found"
        }
    }

    private fun parseRecipeText(recipeText: String, name: String): Recipe {
        val lines = recipeText.split("\n")
        val description = lines.joinToString("\n").trim()
        val uuid: UUID = UUID.randomUUID()
        return Recipe(
            id = uuid.hashCode(),
            name = name,
            description = description
        )
    }
}
