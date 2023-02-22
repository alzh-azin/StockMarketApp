package com.example.stock_market.presentation.info

import com.example.stock_market.domain.model.CompanyInfo
import com.example.stock_market.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfo: List<IntraDayInfo>? = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
