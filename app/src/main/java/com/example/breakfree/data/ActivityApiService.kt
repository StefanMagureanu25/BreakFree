package com.example.breakfree.data

import retrofit2.http.GET

interface ActivityApiService {
    @GET("advice")
    suspend fun getAdvice(): AdviceSlipResponse
} 