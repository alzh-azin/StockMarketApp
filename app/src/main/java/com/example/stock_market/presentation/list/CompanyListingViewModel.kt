package com.example.stock_market.presentation.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock_market.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.stock_market.util.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@HiltViewModel
class CompanyListingViewModel @Inject constructor(private val repository: StockRepository) :
    ViewModel() {

    var state by mutableStateOf(CompanyListingsState())

    private var searchJob : Job? = null

    init {
        getCompanyListings(fetchFromRemote = true , query = "")
    }

    fun onEvent(event: CompanyListingsEvent) {
        when (event) {
            is CompanyListingsEvent.refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }

            is CompanyListingsEvent.onSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyListings(fetchFromRemote, query).collect { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { state = state.copy(companies = it) }
                    }

                    is Result.Error -> {

                    }

                    is Result.Loading -> {
                        state = state.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

}