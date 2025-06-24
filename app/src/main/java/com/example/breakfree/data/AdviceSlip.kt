package com.example.breakfree.data

data class AdviceSlipResponse(
    val slip: AdviceSlip
)

data class AdviceSlip(
    val id: Int,
    val advice: String
) 