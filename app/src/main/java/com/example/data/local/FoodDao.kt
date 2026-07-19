package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ScannedFood
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM scanned_foods ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<ScannedFood>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: ScannedFood): Long

    @Query("DELETE FROM scanned_foods WHERE id = :id")
    suspend fun deleteHistoryItem(id: Int)

    @Query("DELETE FROM scanned_foods")
    suspend fun clearHistory()
}
