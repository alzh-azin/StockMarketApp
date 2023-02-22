package com.example.stock_market.data.repository

import com.example.stock_market.data.csv.CSVParser
import com.example.stock_market.data.csv.CompanyListingsParser
import com.example.stock_market.data.local.StockDao
import com.example.stock_market.data.mapper.toCompanyInfo
import com.example.stock_market.data.mapper.toCompanyListing
import com.example.stock_market.data.mapper.toCompanyListingEntity
import com.example.stock_market.data.remote.StockApi
import com.example.stock_market.domain.model.CompanyInfo
import com.example.stock_market.domain.model.CompanyListing
import com.example.stock_market.domain.model.IntraDayInfo
import com.example.stock_market.domain.repository.StockRepository
import com.example.stock_market.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val dao: StockDao,
    val api: StockApi,
    val companyListingsParser: CSVParser<CompanyListing>,
    val intraDayParser: CSVParser<IntraDayInfo>,
) :
    StockRepository {
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Result<List<CompanyListing>>> = flow {
        emit(Result.Loading(true))

        val localListings = dao.searchCompanyListing(query)
        emit(
            Result.Success(localListings.map {
                it.toCompanyListing()
            })
        )

        val isDbEmpty = localListings.isEmpty() && query.isBlank()
        val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
        if (shouldJustLoadFromCache) {
            emit(Result.Loading(false))
            return@flow
        }

        val remoteListing = try {
            val response = api.getList()
            companyListingsParser.parse(response.byteStream())
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Result.Error(message = "Couldn't load data"))
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Result.Error(message = "Couldn't load data"))
            null
        }

        remoteListing?.let { list ->
            dao.clearCompanyListing()
            dao.insertCompanyListing(list.map {
                it.toCompanyListingEntity()
            })
            emit(Result.Success(dao.searchCompanyListing("").map {
                it.toCompanyListing()
            }))
            emit(Result.Loading(false))
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Result<List<IntraDayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val result = intraDayParser.parse(response.byteStream())
            Result.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Result.Error(message = "Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.Error(message = "Couldn't load data")
        }

    }

    override suspend fun getCompanyInfo(symbol: String): Result<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
           Result.Success(result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Result.Error(message = "Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.Error(message = "Couldn't load data")
        }

    }
}