package com.example.data.repository

import com.example.data.local.FoodDao
import com.example.data.model.ScannedFood
import com.example.data.network.GeminiManager
import com.example.data.network.MealDbRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FoodRepository(private val foodDao: FoodDao) {
    private val geminiManager = GeminiManager()
    private val mealDbService = MealDbRetrofitClient.service

    val allHistory: Flow<List<ScannedFood>> = foodDao.getAllHistory()

    suspend fun insertFood(food: ScannedFood): Long = withContext(Dispatchers.IO) {
        foodDao.insertFood(food)
    }

    suspend fun deleteHistoryItem(id: Int) = withContext(Dispatchers.IO) {
        foodDao.deleteHistoryItem(id)
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        foodDao.clearHistory()
    }

    /**
     * Performs a full scan and returns a populated ScannedFood entity.
     * This orchestrates both Gemini API for nutrition and MealDB API for recipe!
     * It runs on Dispatchers.IO to satisfy background execution requirements.
     */
    suspend fun processFoodScan(
        predictedName: String,
        confidence: Float,
        imagePath: String
    ): ScannedFood = withContext(Dispatchers.IO) {
        // 1. Fetch Gemini Nutrition
        val nutrition = try {
            geminiManager.getNutritionInfo(predictedName)
        } catch (e: Exception) {
            null
        }

        // 2. Fetch MealDB recipe
        var hasRecipe = false
        var recipeTitle = ""
        var recipeThumb = ""
        var recipeIngredients = ""
        var recipeInstructions = ""

        try {
            // MealDB Search
            val mealResponse = mealDbService.searchMealByName(predictedName)
            val meal = mealResponse.meals?.firstOrNull()
            if (meal != null) {
                hasRecipe = true
                recipeTitle = meal.strMeal ?: ""
                recipeThumb = meal.strMealThumb ?: ""
                recipeInstructions = meal.strInstructions ?: ""
                
                // Formulate ingredients string as "Ingredient 1 (Measure 1), Ingredient 2 (Measure 2)..."
                val ingredientsList = meal.getFormattedIngredients()
                recipeIngredients = ingredientsList.joinToString("; ") { "${it.first} (${it.second})" }
            }
        } catch (e: Exception) {
            // Fail silently or log
        }

        ScannedFood(
            name = predictedName,
            confidence = confidence,
            imagePath = imagePath,
            calories = nutrition?.calories ?: 0,
            carbs = nutrition?.carbs ?: 0,
            fat = nutrition?.fat ?: 0,
            fiber = nutrition?.fiber ?: 0,
            protein = nutrition?.protein ?: 0,
            hasRecipe = hasRecipe,
            recipeTitle = recipeTitle,
            recipeThumb = recipeThumb,
            recipeIngredients = recipeIngredients,
            recipeInstructions = recipeInstructions
        )
    }
}
