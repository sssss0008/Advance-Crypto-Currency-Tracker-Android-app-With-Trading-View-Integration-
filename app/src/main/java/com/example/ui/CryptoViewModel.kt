package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CryptoRepository
import com.example.data.local.CryptoDatabase
import com.example.data.model.CryptoCategory
import com.example.data.model.CryptoCoin
import com.example.data.model.MarketOverviewMetrics
import com.example.data.model.ScreenerColumn
import com.example.data.model.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val title: String) {
    CHART("Charts"),
    SCREENER("Screener"),
    ADVANCED_SCREENER("Advanced Screener"),
    HEATMAP("Heatmap"),
    MARKETS("Markets"),
    WATCHLIST("Watchlist"),
    DICTIONARY("Dictionary"),
    CALCULATORS("Calculators"),
    EDUCATION("Education"),
    AI_HUB("AI Hub")
}

data class CryptoUiState(
    val coins: List<CryptoCoin> = emptyList(),
    val metrics: MarketOverviewMetrics = MarketOverviewMetrics(),
    val currentTab: AppTab = AppTab.CHART,
    val selectedCoin: CryptoCoin? = null,
    val isDarkTheme: Boolean = true,
    // Toggles between Native and TradingView Embed - default to TradingView widgets
    val isTvOverviewMode: Boolean = true,
    val isTvHeatmapMode: Boolean = true,
    val isTvScreenerMode: Boolean = true,
    val isTvTickerMode: Boolean = true,
    val isTvChartMode: Boolean = true,
    // Filters & Sorting
    val searchQuery: String = "",
    val selectedCategory: CryptoCategory = CryptoCategory.ALL,
    val selectedSortOption: SortOption = SortOption.RANK,
    val selectedScreenerColumn: ScreenerColumn = ScreenerColumn.OVERVIEW,
    val selectedDetailTimeframe: String = "24H",
    val selectedChartSymbol: String = "BITSTAMP:BTCUSD",
    val isChartFullscreen: Boolean = false,
    val aiPrompt: String? = null
)

class CryptoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CryptoRepository

    private val _uiState = MutableStateFlow(CryptoUiState())
    val uiState: StateFlow<CryptoUiState> = _uiState.asStateFlow()

    init {
        val db = CryptoDatabase.getDatabase(application)
        repository = CryptoRepository(db.watchlistDao(), viewModelScope)

        viewModelScope.launch {
            combine(repository.coinsWithFavorites, repository.metrics) { coins, metrics ->
                Pair(coins, metrics)
            }.collect { (coins, metrics) ->
                _uiState.update { current ->
                    val updatedSelected = current.selectedCoin?.let { sel ->
                        coins.find { it.symbol == sel.symbol } ?: sel
                    }
                    current.copy(
                        coins = coins,
                        metrics = metrics,
                        selectedCoin = updatedSelected
                    )
                }
            }
        }
    }

    fun setTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun selectCoin(coin: CryptoCoin?) {
        _uiState.update { it.copy(selectedCoin = coin) }
    }

    fun toggleFavorite(coin: CryptoCoin) {
        viewModelScope.launch {
            repository.toggleFavorite(coin)
        }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun toggleTvOverviewMode() {
        _uiState.update { it.copy(isTvOverviewMode = !it.isTvOverviewMode) }
    }

    fun toggleTvHeatmapMode() {
        _uiState.update { it.copy(isTvHeatmapMode = !it.isTvHeatmapMode) }
    }

    fun toggleTvScreenerMode() {
        _uiState.update { it.copy(isTvScreenerMode = !it.isTvScreenerMode) }
    }

    fun toggleTvTickerMode() {
        _uiState.update { it.copy(isTvTickerMode = !it.isTvTickerMode) }
    }

    fun toggleTvChartMode() {
        _uiState.update { it.copy(isTvChartMode = !it.isTvChartMode) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedCategory(category: CryptoCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSelectedSortOption(sortOption: SortOption) {
        _uiState.update { it.copy(selectedSortOption = sortOption) }
    }

    fun setSelectedScreenerColumn(column: ScreenerColumn) {
        _uiState.update { it.copy(selectedScreenerColumn = column) }
    }

    fun setSelectedDetailTimeframe(timeframe: String) {
        _uiState.update { it.copy(selectedDetailTimeframe = timeframe) }
    }

    fun openChart(symbol: String) {
        val cleaned = com.example.ui.tradingview.cleanSymbolFormat(symbol)
        val targetCoin = _uiState.value.coins.firstOrNull {
            it.tvSymbol.equals(cleaned, ignoreCase = true) ||
            it.tvSymbol.equals(symbol, ignoreCase = true) ||
            it.symbol.equals(cleaned, ignoreCase = true) ||
            it.symbol.equals(symbol, ignoreCase = true) ||
            cleaned.contains(it.symbol, ignoreCase = true) ||
            symbol.contains(it.symbol, ignoreCase = true)
        }
        _uiState.update {
            it.copy(
                selectedChartSymbol = targetCoin?.tvSymbol ?: cleaned,
                currentTab = AppTab.CHART,
                selectedCoin = null
            )
        }
    }

    fun openChartForCoin(coin: CryptoCoin) {
        _uiState.update {
            it.copy(
                selectedChartSymbol = coin.tvSymbol,
                currentTab = AppTab.CHART,
                selectedCoin = null
            )
        }
    }

    fun setChartSymbol(symbol: String) {
        _uiState.update { it.copy(selectedChartSymbol = symbol) }
    }

    fun setChartFullscreen(fullscreen: Boolean) {
        _uiState.update { it.copy(isChartFullscreen = fullscreen) }
    }

    fun toggleChartFullscreen() {
        _uiState.update { it.copy(isChartFullscreen = !it.isChartFullscreen) }
    }

    fun openAiWithPrompt(prompt: String) {
        _uiState.update {
            it.copy(
                currentTab = AppTab.AI_HUB,
                aiPrompt = prompt,
                selectedCoin = null
            )
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CryptoViewModel(application) as T
        }
    }
}
