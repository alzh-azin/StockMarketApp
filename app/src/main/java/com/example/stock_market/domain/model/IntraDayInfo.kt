package com.example.stock_market.domain.model

import java.time.LocalDateTime

data class IntraDayInfo(
    val date: LocalDateTime,
    val close: Double
)