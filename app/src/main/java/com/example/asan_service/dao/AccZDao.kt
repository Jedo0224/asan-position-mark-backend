package com.example.asan_service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.asan_service.entity.AccXEntity
import com.example.asan_service.entity.AccYEntity
import com.example.asan_service.entity.AccZEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccZDao {
    @Query("SELECT * FROM acczentity WHERE watchId = :watchId ORDER BY id ASC LIMIT 60")
    fun getOldestData(watchId: String): Flow<List<AccZEntity>>

    @Insert
    fun insertData(data: AccZEntity)

    @Query("DELETE FROM acczentity")
    fun deleteAllData()
}