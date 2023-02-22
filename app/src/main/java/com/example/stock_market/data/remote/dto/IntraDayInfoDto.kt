package com.example.stock_market.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IntraDayInfoDto(
    val timestamp: String,
    val close: Double
)