package com.example.data

import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistEntity
import com.example.data.model.CryptoCategory
import com.example.data.model.CryptoCoin
import com.example.data.model.MarketOverviewMetrics
import com.example.data.model.TechnicalRating
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class CryptoRepository(
    private val watchlistDao: WatchlistDao,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {

    private val _coins = MutableStateFlow<List<CryptoCoin>>(getInitialCoins())
    val coins: StateFlow<List<CryptoCoin>> = _coins.asStateFlow()

    private val _metrics = MutableStateFlow(MarketOverviewMetrics())
    val metrics: StateFlow<MarketOverviewMetrics> = _metrics.asStateFlow()

    val watchlist: Flow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()

    // Coins with favorite state combined
    val coinsWithFavorites: Flow<List<CryptoCoin>> = combine(coins, watchlist) { coinList, favoriteList ->
        val favoriteSymbols = favoriteList.map { it.symbol }.toSet()
        coinList.map { coin ->
            coin.copy(isFavorite = favoriteSymbols.contains(coin.symbol))
        }
    }

    init {
        startLivePriceSimulation()
    }

    private fun startLivePriceSimulation() {
        externalScope.launch {
            while (isActive) {
                delay(3500) // Update every 3.5 seconds
                val current = _coins.value
                val countToUpdate = Random.nextInt(2, 5)
                val updatedCoins = current.toMutableList()

                repeat(countToUpdate) {
                    val index = Random.nextInt(current.size)
                    val coin = updatedCoins[index]
                    // Skip USDT.D for random jumps or keep subtle
                    val percentDelta = (Random.nextDouble(-0.35, 0.45)) / 100.0
                    val newPrice = (coin.priceUsd * (1.0 + percentDelta)).coerceAtLeast(0.000001)
                    val newChange = coin.change24h + (percentDelta * 100.0)

                    val isUp = percentDelta > 0

                    updatedCoins[index] = coin.copy(
                        priceUsd = newPrice,
                        change24h = newChange,
                        isFlashUp = isUp,
                        isFlashDown = !isUp
                    )
                }

                _coins.value = updatedCoins

                // Clear flash effect after brief flash
                delay(600)
                _coins.value = _coins.value.map {
                    if (it.isFlashUp || it.isFlashDown) it.copy(isFlashUp = false, isFlashDown = false) else it
                }
            }
        }
    }

    suspend fun toggleFavorite(coin: CryptoCoin) {
        if (coin.isFavorite) {
            watchlistDao.removeFromWatchlist(coin.symbol)
        } else {
            watchlistDao.addToWatchlist(
                WatchlistEntity(
                    symbol = coin.symbol,
                    name = coin.name
                )
            )
        }
    }

    private fun getInitialCoins(): List<CryptoCoin> {
        return listOf(
            CryptoCoin(
                id = "bitcoin",
                symbol = "BTC",
                name = "Bitcoin",
                tvSymbol = "BITSTAMP:BTCUSD",
                priceUsd = 67840.50,
                change24h = 3.42,
                change1h = 0.28,
                change7d = 5.84,
                volume24h = 34_820_000_000.0,
                marketCap = 1_338_000_000_000.0,
                high24h = 68450.00,
                low24h = 65200.00,
                circulatingSupply = "19.75M BTC",
                allTimeHigh = 73750.00,
                rank = 1,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(64500f, 64900f, 65200f, 65800f, 66300f, 65900f, 67200f, 67840f),
                technicalRating = TechnicalRating.STRONG_BUY,
                rsi14 = 62.4
            ),
            CryptoCoin(
                id = "ethereum",
                symbol = "ETH",
                name = "Ethereum",
                tvSymbol = "BITSTAMP:ETHUSD",
                priceUsd = 2640.80,
                change24h = 2.85,
                change1h = 0.15,
                change7d = 4.12,
                volume24h = 16_450_000_000.0,
                marketCap = 317_800_000_000.0,
                high24h = 2695.00,
                low24h = 2540.00,
                circulatingSupply = "120.4M ETH",
                allTimeHigh = 4891.00,
                rank = 2,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(2520f, 2550f, 2580f, 2540f, 2610f, 2600f, 2640f),
                technicalRating = TechnicalRating.BUY,
                rsi14 = 58.1
            ),
            CryptoCoin(
                id = "solana",
                symbol = "SOL",
                name = "Solana",
                tvSymbol = "BINANCE:SOLUSDT",
                priceUsd = 158.45,
                change24h = 6.78,
                change1h = 0.82,
                change7d = 12.45,
                volume24h = 4_890_000_000.0,
                marketCap = 74_200_000_000.0,
                high24h = 162.10,
                low24h = 146.50,
                circulatingSupply = "468.2M SOL",
                allTimeHigh = 260.06,
                rank = 3,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(144f, 146f, 150f, 148f, 153f, 156f, 158.45f),
                technicalRating = TechnicalRating.STRONG_BUY,
                rsi14 = 66.8
            ),
            CryptoCoin(
                id = "binancecoin",
                symbol = "BNB",
                name = "BNB",
                tvSymbol = "BINANCE:BNBUSDT",
                priceUsd = 592.30,
                change24h = 1.45,
                change1h = 0.05,
                change7d = 3.10,
                volume24h = 1_120_000_000.0,
                marketCap = 86_500_000_000.0,
                high24h = 598.00,
                low24h = 582.00,
                circulatingSupply = "146.0M BNB",
                allTimeHigh = 720.67,
                rank = 4,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(580f, 584f, 588f, 586f, 590f, 592.3f),
                technicalRating = TechnicalRating.BUY,
                rsi14 = 54.2
            ),
            CryptoCoin(
                id = "ripple",
                symbol = "XRP",
                name = "XRP",
                tvSymbol = "BINANCE:XRPUSDT",
                priceUsd = 0.5840,
                change24h = -0.92,
                change1h = -0.10,
                change7d = 2.15,
                volume24h = 1_350_000_000.0,
                marketCap = 33_100_000_000.0,
                high24h = 0.6010,
                low24h = 0.5750,
                circulatingSupply = "56.6B XRP",
                allTimeHigh = 3.84,
                rank = 5,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(0.57f, 0.59f, 0.58f, 0.60f, 0.59f, 0.584f),
                technicalRating = TechnicalRating.NEUTRAL,
                rsi14 = 49.3
            ),
            CryptoCoin(
                id = "usdt_dominance",
                symbol = "USDT.D",
                name = "Tether Dominance",
                tvSymbol = "CRYPTOCAP:USDT.D",
                priceUsd = 4.42,
                change24h = -1.65,
                change1h = -0.05,
                change7d = -3.20,
                volume24h = 75_000_000_000.0,
                marketCap = 118_400_000_000.0,
                high24h = 4.58,
                low24h = 4.38,
                circulatingSupply = "118.4B USDT",
                allTimeHigh = 9.54,
                rank = 6,
                category = CryptoCategory.METRICS,
                sparklinePoints = listOf(4.6f, 4.55f, 4.5f, 4.48f, 4.42f),
                technicalRating = TechnicalRating.SELL,
                rsi14 = 38.5
            ),
            CryptoCoin(
                id = "dogecoin",
                symbol = "DOGE",
                name = "Dogecoin",
                tvSymbol = "BINANCE:DOGEUSDT",
                priceUsd = 0.1284,
                change24h = 5.62,
                change1h = 0.45,
                change7d = 8.90,
                volume24h = 1_280_000_000.0,
                marketCap = 18_700_000_000.0,
                high24h = 0.1340,
                low24h = 0.1190,
                circulatingSupply = "146.1B DOGE",
                allTimeHigh = 0.737,
                rank = 7,
                category = CryptoCategory.MEME,
                sparklinePoints = listOf(0.118f, 0.120f, 0.123f, 0.125f, 0.1284f),
                technicalRating = TechnicalRating.BUY,
                rsi14 = 63.2
            ),
            CryptoCoin(
                id = "avalanche-2",
                symbol = "AVAX",
                name = "Avalanche",
                tvSymbol = "BINANCE:AVAXUSDT",
                priceUsd = 28.95,
                change24h = 4.25,
                change1h = 0.32,
                change7d = 9.80,
                volume24h = 520_000_000.0,
                marketCap = 11_700_000_000.0,
                high24h = 29.80,
                low24h = 27.20,
                circulatingSupply = "404.5M AVAX",
                allTimeHigh = 146.22,
                rank = 8,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(26.5f, 27.2f, 27.8f, 28.1f, 28.95f),
                technicalRating = TechnicalRating.BUY,
                rsi14 = 60.5
            ),
            CryptoCoin(
                id = "near",
                symbol = "NEAR",
                name = "NEAR Protocol",
                tvSymbol = "BINANCE:NEARUSDT",
                priceUsd = 5.48,
                change24h = 8.45,
                change1h = 1.15,
                change7d = 18.20,
                volume24h = 680_000_000.0,
                marketCap = 6_650_000_000.0,
                high24h = 5.65,
                low24h = 4.95,
                circulatingSupply = "1.21B NEAR",
                allTimeHigh = 20.42,
                rank = 9,
                category = CryptoCategory.AI,
                sparklinePoints = listOf(4.8f, 5.0f, 5.15f, 5.3f, 5.48f),
                technicalRating = TechnicalRating.STRONG_BUY,
                rsi14 = 71.0
            ),
            CryptoCoin(
                id = "zcash",
                symbol = "ZEC",
                name = "Zcash",
                tvSymbol = "BINANCE:ZECUSDT",
                priceUsd = 36.40,
                change24h = -2.15,
                change1h = -0.30,
                change7d = 1.45,
                volume24h = 85_000_000.0,
                marketCap = 590_000_000.0,
                high24h = 38.20,
                low24h = 35.80,
                circulatingSupply = "16.3M ZEC",
                allTimeHigh = 5941.00,
                rank = 10,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(37.5f, 38.0f, 37.2f, 36.8f, 36.4f),
                technicalRating = TechnicalRating.NEUTRAL,
                rsi14 = 46.2
            ),
            CryptoCoin(
                id = "sui",
                symbol = "SUI",
                name = "Sui",
                tvSymbol = "BINANCE:SUIUSDT",
                priceUsd = 2.12,
                change24h = 11.85,
                change1h = 1.45,
                change7d = 34.60,
                volume24h = 1_420_000_000.0,
                marketCap = 5_850_000_000.0,
                high24h = 2.25,
                low24h = 1.84,
                circulatingSupply = "2.76B SUI",
                allTimeHigh = 2.36,
                rank = 11,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(1.75f, 1.88f, 1.95f, 2.05f, 2.12f),
                technicalRating = TechnicalRating.STRONG_BUY,
                rsi14 = 78.4
            ),
            CryptoCoin(
                id = "bittensor",
                symbol = "TAO",
                name = "Bittensor",
                tvSymbol = "BINANCE:TAOUSDT",
                priceUsd = 582.50,
                change24h = 7.15,
                change1h = 0.65,
                change7d = 22.40,
                volume24h = 310_000_000.0,
                marketCap = 4_290_000_000.0,
                high24h = 605.00,
                low24h = 535.00,
                circulatingSupply = "7.38M TAO",
                allTimeHigh = 774.86,
                rank = 12,
                category = CryptoCategory.AI,
                sparklinePoints = listOf(520f, 545f, 560f, 575f, 582.5f),
                technicalRating = TechnicalRating.STRONG_BUY,
                rsi14 = 69.2
            ),
            CryptoCoin(
                id = "chainlink",
                symbol = "LINK",
                name = "Chainlink",
                tvSymbol = "BINANCE:LINKUSDT",
                priceUsd = 12.15,
                change24h = 3.65,
                change1h = 0.20,
                change7d = 7.40,
                volume24h = 440_000_000.0,
                marketCap = 7_380_000_000.0,
                high24h = 12.45,
                low24h = 11.60,
                circulatingSupply = "608.1M LINK",
                allTimeHigh = 52.88,
                rank = 13,
                category = CryptoCategory.INFRASTRUCTURE,
                sparklinePoints = listOf(11.4f, 11.7f, 11.9f, 12.0f, 12.15f),
                technicalRating = TechnicalRating.BUY,
                rsi14 = 59.8
            ),
            CryptoCoin(
                id = "cardano",
                symbol = "ADA",
                name = "Cardano",
                tvSymbol = "BINANCE:ADAUSDT",
                priceUsd = 0.362,
                change24h = 1.82,
                change1h = 0.12,
                change7d = 4.20,
                volume24h = 310_000_000.0,
                marketCap = 12_980_000_000.0,
                high24h = 0.372,
                low24h = 0.351,
                circulatingSupply = "35.7B ADA",
                allTimeHigh = 3.10,
                rank = 14,
                category = CryptoCategory.LAYER1,
                sparklinePoints = listOf(0.35f, 0.355f, 0.358f, 0.362f),
                technicalRating = TechnicalRating.NEUTRAL,
                rsi14 = 51.4
            ),
            CryptoCoin(
                id = "pepe",
                symbol = "PEPE",
                name = "Pepe",
                tvSymbol = "BINANCE:PEPEUSDT",
                priceUsd = 0.0000108,
                change24h = 9.40,
                change1h = 1.20,
                change7d = 16.50,
                volume24h = 1_180_000_000.0,
                marketCap = 4_540_000_000.0,
                high24h = 0.0000115,
                low24h = 0.0000096,
                circulatingSupply = "420.6T PEPE",
                allTimeHigh = 0.0000171,
                rank = 15,
                category = CryptoCategory.MEME,
                sparklinePoints = listOf(0.0000094f, 0.0000101f, 0.0000104f, 0.0000108f),
                technicalRating = TechnicalRating.STRONG_BUY,
                rsi14 = 72.1
            ),
            CryptoCoin(
                id = "render-token",
                symbol = "RENDER",
                name = "Render",
                tvSymbol = "BINANCE:RENDERUSDT",
                priceUsd = 6.42,
                change24h = 4.80,
                change1h = 0.35,
                change7d = 11.20,
                volume24h = 240_000_000.0,
                marketCap = 3_320_000_000.0,
                high24h = 6.65,
                low24h = 6.05,
                circulatingSupply = "518.2M RENDER",
                allTimeHigh = 13.60,
                rank = 16,
                category = CryptoCategory.AI,
                sparklinePoints = listOf(5.9f, 6.1f, 6.25f, 6.42f),
                technicalRating = TechnicalRating.BUY,
                rsi14 = 61.3
            ),
            CryptoCoin(
                id = "injective",
                symbol = "INJ",
                name = "Injective",
                tvSymbol = "BINANCE:INJUSDT",
                priceUsd = 22.40,
                change24h = -3.40,
                change1h = -0.40,
                change7d = 2.10,
                volume24h = 160_000_000.0,
                marketCap = 2_180_000_000.0,
                high24h = 23.80,
                low24h = 21.90,
                circulatingSupply = "97.4M INJ",
                allTimeHigh = 52.75,
                rank = 17,
                category = CryptoCategory.DEFI,
                sparklinePoints = listOf(23.5f, 23.8f, 23.0f, 22.4f),
                technicalRating = TechnicalRating.NEUTRAL,
                rsi14 = 44.8
            ),
            CryptoCoin(
                id = "shiba-inu",
                symbol = "SHIB",
                name = "Shiba Inu",
                tvSymbol = "BINANCE:SHIBUSDT",
                priceUsd = 0.0000188,
                change24h = 3.15,
                change1h = 0.10,
                change7d = 5.60,
                volume24h = 480_000_000.0,
                marketCap = 11_080_000_000.0,
                high24h = 0.0000194,
                low24h = 0.0000180,
                circulatingSupply = "589.3T SHIB",
                allTimeHigh = 0.0000884,
                rank = 18,
                category = CryptoCategory.MEME,
                sparklinePoints = listOf(0.000018f, 0.0000184f, 0.0000188f),
                technicalRating = TechnicalRating.BUY,
                rsi14 = 55.4
            )
        )
    }
}
