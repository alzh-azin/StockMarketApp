package com.example.stock_market.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class CompanyInfo(
    val symbol: String?,
    val description: String?,
    val name: String?,
    val country: String?,
    val industry: String?,
)
