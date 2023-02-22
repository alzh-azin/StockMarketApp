package com.example.stock_market.presentation.list

import com.example.stock_market.domain.model.CompanyListing

data class CompanyListingsState(
    val companies : List<CompanyListing> = emptyList(),
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val searchQuery : String = ""
)
