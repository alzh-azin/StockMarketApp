package com.example.stock_market.presentation.list

sealed class CompanyListingsEvent{
     object  refresh : CompanyListingsEvent()

    data class onSearchQueryChange(val query : String) : CompanyListingsEvent()
}
