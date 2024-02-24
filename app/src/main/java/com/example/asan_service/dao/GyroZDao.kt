package com.example.asan_service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.asan_service.entity.GyroZEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GyroZDao {
    @Query("SELECT * FROM gyrozentity WHERE watchId = :watchId ORDER BY id ASC LIMIT 60")
    fun getOldestData(watchId: String): Flow<List<GyroZEntity>>

    @Insert
    fun insertData(data: GyroZEntity)

    @Query("DELETE FROM GyroZEntity")
    fun deleteAllData()
}