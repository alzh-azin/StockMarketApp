package com.example.stock_market.presentation.info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock_market.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.stock_market.util.Result

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)

            val companyInfo = async { repository.getCompanyInfo(symbol) }
            val intraDayInfo = async { repository.getIntraDayInfo(symbol) }

            when (val result = companyInfo.await()) {
                is Result.Success -> {
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        company = null
                    )
                }
                else -> {
                }
            }

            when (val result = intraDayInfo.await()) {
                is Result.Success -> {
                    state = state.copy(
                        stockInfo = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        stockInfo = null
                    )
                }
                else -> {
                }
            }

        }
    }

}