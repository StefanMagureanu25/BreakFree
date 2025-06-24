package com.example.breakfree.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addictions")
data class Addiction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dateStarted: Long
) 