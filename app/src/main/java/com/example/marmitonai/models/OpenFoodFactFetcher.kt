package com.example.marmitonai.models

import android.util.Log
import com.example.marmitonai.MyApplication
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class OpenFoodFactFetcher {
    private val userAgent: String = "MarmitonAi/0.1 (zyad.zekri@efrei.net)"
    val db = AppDatabase.getDatabase(MyApplication.applicationContext())
    private val client: OkHttpClient = OkHttpClient()

    suspend fun ingredientCreator(barcode: String) {
        val url = "https://world.openfoodfacts.org/api/v0/product/$barcode.json"
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("User-Agent", userAgent)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string() ?: throw IOException("Empty response")
            val json = JSONObject(responseBody)
            val product = json.getJSONObject("product")
            val name = product.optString("product_name", "Unknown Product")
            val servingSize = product.optInt("serving_quantity", 0)
            val servingUnit = product.optString("serving_quantity_unit", "g")
            val kcal = product.getJSONObject("nutriments").optInt("energy-kcal_100g", 0)
            val fat = product.getJSONObject("nutriments").optInt("fat_100g", 0)
            val carbs = product.getJSONObject("nutriments").optInt("carbohydrates_100g", 0)
            val sugars = product.getJSONObject("nutriments").optInt("sugars_100g", 0)
            val protein = product.getJSONObject("nutriments").optInt("proteins_100g", 0)
            val salt = product.getJSONObject("nutriments").optInt("salt_100g", 0)
            val imageId = imageFetcher(barcode)

            val ingredient = Ingredient(
                barcode = barcode,
                name = name,
                servingSize = servingSize,
                servingUnit = servingUnit,
                kcal = kcal,
                fat = fat,
                carbs = carbs,
                sugars = sugars,
                protein = protein,
                salt = salt,
                imageId = imageId,
                quantity = 0
            )

            db.ingredientDao().insertIngredient(ingredient)

        } catch (e: Exception) {
            suspend fun ingredientCreator(barcode: String) {
                val url = "https://world.openfoodfacts.org/api/v0/product/$barcode.json"
                val request: Request = Request.Builder()
                    .url(url)
                    .addHeader("User-Agent", userAgent)
                    .build()

                try {
                    val response: Response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    Log.d("Response",response.toString())

                    val responseBody = response.body?.string() ?: throw IOException("Empty response")
                    val json = JSONObject(responseBody)
                    val product = json.optJSONObject("product") ?: throw IOException("Product not found in response")

                    val name = product.optString("product_name", "Unknown Product")
                    val servingSize = product.optInt("serving_quantity", 100)
                    val servingUnit = product.optString("serving_quantity_unit", "g")
                    val kcal = product.optJSONObject("nutriments")?.optInt("energy-kcal_100g", 0) ?: 0
                    val fat = product.optJSONObject("nutriments")?.optInt("fat_100g", 0) ?: 0
                    val carbs = product.optJSONObject("nutriments")?.optInt("carbohydrates_100g", 0) ?: 0
                    val sugars = product.optJSONObject("nutriments")?.optInt("sugars_100g", 0) ?: 0
                    val protein = product.optJSONObject("nutriments")?.optInt("proteins_100g", 0) ?: 0
                    val salt = product.optJSONObject("nutriments")?.optInt("salt_100g", 0) ?: 0
                    val imageId = imageFetcher(barcode)

                    val ingredient = Ingredient(
                        barcode = barcode,
                        name = name,
                        servingSize = servingSize,
                        servingUnit = servingUnit,
                        kcal = kcal,
                        fat = fat,
                        carbs = carbs,
                        sugars = sugars,
                        protein = protein,
                        salt = salt,
                        imageId = imageId,
                        quantity = 0
                    )


                    db.ingredientDao().insertIngredient(ingredient)

                    Log.d("Database", "All ingredients in DB: $ingredient")

                } catch (e: Exception) {
                    Log.e("IngredientCreatorError", "Error creating ingredient for barcode: $barcode", e)
                }
            }

        }
    }
    suspend fun ingredientDestroyer(barcode: String){
        db.ingredientDao().deleteIngredient(db.ingredientDao().loadIngredient(barcode))
    }

    suspend fun changeIngredient(barcode: String, quantity: Int){
        db.ingredientDao().changeQuantity(barcode, quantity)
    }


    fun imageFetcher(barcode: String): String {
        val formattedBarcode = formatBarcode(barcode)
        val url = "https://images.openfoodfacts.org/images/products/$formattedBarcode/1.jpg"
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("User-Agent", userAgent)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) return ""

            val inputStream = response.body?.byteStream()
            val file = File(MyApplication.applicationContext().filesDir, "image_$barcode.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            return file.absolutePath
        } catch (e: Exception) {
            return ""
        }
    }

    fun formatBarcode(barcode: String): String {
        return if (barcode.length <= 8) {
            barcode
        } else {
            val regex = Regex("^(...)(...)(...)(.*)$")
            val matchResult = regex.matchEntire(barcode)
            if (matchResult != null) {
                val (group1, group2, group3, rest) = matchResult.destructured
                "$group1/$group2/$group3/$rest"
            } else {
                barcode
            }
        }
    }
    suspend fun fetchAllIngredients(): List<Ingredient> {
        return db.ingredientDao().loadAllIngredients()
    }
}
