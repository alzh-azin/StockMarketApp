package com.example.stock_market.data.mapper

import com.example.stock_market.data.remote.dto.IntraDayInfoDto
import com.example.stock_market.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntraDayInfoDto.toIntraDayInfo() : IntraDayInfo{
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern , Locale.getDefault())
    val localDataTime = LocalDateTime.parse(timestamp , formatter)
    return IntraDayInfo(
        localDataTime , close
    )
}