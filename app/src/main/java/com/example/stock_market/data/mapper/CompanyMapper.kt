package com.example.stock_market.data.mapper

import com.example.stock_market.data.local.CompanyListingEntity
import com.example.stock_market.data.remote.dto.CompanyInfoDto
import com.example.stock_market.domain.model.CompanyInfo
import com.example.stock_market.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing() = CompanyListing(
    name = name,
    symbol = symbol,
    exchange = exchange
)

fun CompanyListing.toCompanyListingEntity() = CompanyListingEntity(
    name = name,
    symbol = symbol,
    exchange = exchange
)

fun CompanyInfoDto.toCompanyInfo() = CompanyInfo(
    symbol = symbol ?: "",
    description = description ?: "",
    name = name ?: "",
    country = country ?: "",
    industry = industry ?: ""

)