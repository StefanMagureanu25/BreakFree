package com.example.breakfree.data

data class ActivitySuggestion(
    val activity: String,
    val type: String,
    val participants: Int,
    val price: Float,
    val link: String?,
    val key: String,
    val accessibility: Float
) 