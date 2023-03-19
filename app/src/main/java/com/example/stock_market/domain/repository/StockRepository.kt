package com.example.stock_market.domain.repository

import com.example.stock_market.domain.model.CompanyInfo
import com.example.stock_market.domain.model.CompanyListing
import com.example.stock_market.domain.model.IntraDayInfo
import kotlinx.coroutines.flow.Flow
import com.example.stock_market.util.Result

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote : Boolean,
        query : String
    ) : Flow<Result<List<CompanyListing>>>

    suspend fun getIntraDayInfo(
        symbol : String
    ) : Result<List<IntraDayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ) : Result<CompanyInfo>
}