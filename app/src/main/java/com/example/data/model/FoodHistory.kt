package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "scanned_foods")
data class ScannedFood(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val confidence: Float,
    val imagePath: String, // Local URI or File Path
    val timestamp: Long = System.currentTimeMillis(),
    
    // Nutrition Info from Gemini
    val calories: Int = 0,
    val carbs: Int = 0,
    val fat: Int = 0,
    val fiber: Int = 0,
    val protein: Int = 0,
    
    // Recipe Info from MealDB (optional cache)
    val hasRecipe: Boolean = false,
    val recipeTitle: String = "",
    val recipeThumb: String = "",
    val recipeIngredients: String = "", // Comma-separated or JSON
    val recipeInstructions: String = ""
) : Serializable
