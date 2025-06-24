package com.example.breakfree.data

import retrofit2.http.GET

interface QuoteApiService {
    @GET("api/random")
    suspend fun getRandomQuote(): List<Quote>
} 