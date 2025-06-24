package com.example.breakfree.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface AddictionDao {
    @Insert
    suspend fun insert(addiction: Addiction)

    @Delete
    suspend fun delete(addiction: Addiction)

    @Query("SELECT * FROM addictions ORDER BY dateStarted DESC")
    suspend fun getAll(): List<Addiction>

    @Query("SELECT * FROM addictions WHERE id = :id")
    suspend fun getById(id: Int): Addiction?
} 