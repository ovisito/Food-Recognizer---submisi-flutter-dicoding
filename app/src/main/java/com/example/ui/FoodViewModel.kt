package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FoodDatabase
import com.example.data.model.ScannedFood
import com.example.data.repository.FoodRepository
import com.example.ml.FoodClassifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

sealed interface ScanUiState {
    object Idle : ScanUiState
    object Loading : ScanUiState
    data class Success(val food: ScannedFood) : ScanUiState
    data class Error(val message: String) : ScanUiState
}

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FoodDatabase.getDatabase(application)
    private val repository = FoodRepository(database.foodDao())
    private val classifier = FoodClassifier(application)

    // Expose all scanned history flow
    val historyState: StateFlow<List<ScannedFood>> = repository.allHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _scanUiState = MutableStateFlow<ScanUiState>(ScanUiState.Idle)
    val scanUiState: StateFlow<ScanUiState> = _scanUiState.asStateFlow()

    // Temporarily stored image for current scanning process
    private val _currentImagePath = MutableStateFlow<String?>(null)
    val currentImagePath: StateFlow<String?> = _currentImagePath.asStateFlow()

    fun resetScanState() {
        _scanUiState.value = ScanUiState.Idle
        _currentImagePath.value = null
    }

    /**
     * Copy selected Uri (Gallery or Camera) into a local application cache file
     * and proceed with image classification and APIs enrichment in background.
     */
    fun startFoodScan(imageUri: Uri) {
        _scanUiState.value = ScanUiState.Loading
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                
                // Copy Uri content to local cache file
                val cacheDir = context.cacheDir
                val file = File(cacheDir, "scan_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }

                if (!file.exists() || file.length() == 0L) {
                    _scanUiState.value = ScanUiState.Error("Gagal memproses gambar")
                    return@launch
                }

                val localPath = file.absolutePath
                _currentImagePath.value = localPath

                // 1. Run local classifier in a background Thread (IO)
                val classificationResult = classifier.classifyImage(localPath)
                Log.d("FoodViewModel", "ML Classified dish: ${classificationResult.label}")

                // 2. Fetch recipe from MealDB and nutrition from Gemini, combining them
                val fullyScannedFood = repository.processFoodScan(
                    predictedName = classificationResult.label,
                    confidence = classificationResult.confidence,
                    imagePath = localPath
                )

                // 3. Save into local Database
                val insertedId = repository.insertFood(fullyScannedFood)
                val finalFoodWithId = fullyScannedFood.copy(id = insertedId.toInt())

                _scanUiState.value = ScanUiState.Success(finalFoodWithId)
            } catch (e: Exception) {
                Log.e("FoodViewModel", "Error scanning food: ${e.message}", e)
                _scanUiState.value = ScanUiState.Error("Gagal mengidentifikasi makanan: ${e.message}")
            }
        }
    }

    /**
     * Delete an item from database history
     */
    fun deleteHistoryItem(id: Int) {
        viewModelScope.launch {
            repository.deleteHistoryItem(id)
        }
    }

    /**
     * Clear all database scanned history
     */
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
