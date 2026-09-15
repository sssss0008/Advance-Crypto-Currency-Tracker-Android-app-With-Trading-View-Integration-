package com.example.data.model

enum class TechnicalRating(val label: String) {
    STRONG_BUY("Strong Buy"),
    BUY("Buy"),
    NEUTRAL("Neutral"),
    SELL("Sell"),
    STRONG_SELL("Strong Sell")
}

enum class CryptoCategory(val displayName: String) {
    ALL("All"),
    LAYER1("Layer 1"),
    DEFI("DeFi"),
    AI("AI & Big Data"),
    MEME("Meme"),
    INFRASTRUCTURE("Infra"),
    METRICS("Dominance")
}

enum class ScreenerColumn(val displayName: String) {
    OVERVIEW("Overview"),
    PERFORMANCE("Performance"),
    VALUATION("Valuation"),
    TECHNICALS("Technicals")
}

enum class SortOption(val displayName: String) {
    RANK("Rank"),
    MARKET_CAP_DESC("Market Cap (High-Low)"),
    CHANGE_DESC("24h Gainers (%)"),
    CHANGE_ASC("24h Losers (%)"),
    PRICE_DESC("Price (High-Low)"),
    VOLUME_DESC("24h Volume")
}

data class CryptoCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val tvSymbol: String,
    val priceUsd: Double,
    val change24h: Double,
    val change1h: Double,
    val change7d: Double,
    val volume24h: Double,
    val marketCap: Double,
    val high24h: Double,
    val low24h: Double,
    val circulatingSupply: String,
    val allTimeHigh: Double,
    val rank: Int,
    val category: CryptoCategory,
    val sparklinePoints: List<Float>,
    val technicalRating: TechnicalRating,
    val rsi14: Double,
    val isFavorite: Boolean = false,
    val isFlashUp: Boolean = false,
    val isFlashDown: Boolean = false
) {
    val formattedPrice: String
        get() = when {
            symbol == "USDT.D" -> String.format("%.2f%%", priceUsd)
            priceUsd >= 1000 -> String.format("$%,.2f", priceUsd)
            priceUsd >= 1 -> String.format("$%.2f", priceUsd)
            priceUsd >= 0.01 -> String.format("$%.4f", priceUsd)
            else -> String.format("$%.6f", priceUsd)
        }

    val formattedChange24h: String
        get() = if (change24h >= 0) String.format("+%.2f%%", change24h) else String.format("%.2f%%", change24h)

    val formattedMarketCap: String
        get() = formatLargeNumber(marketCap)

    val formattedVolume24h: String
        get() = formatLargeNumber(volume24h)

    private fun formatLargeNumber(number: Double): String {
        return when {
            number >= 1_000_000_000_000.0 -> String.format("$%.2fT", number / 1_000_000_000_000.0)
            number >= 1_000_000_000.0 -> String.format("$%.2fB", number / 1_000_000_000.0)
            number >= 1_000_000.0 -> String.format("$%.2fM", number / 1_000_000.0)
            number >= 1_000.0 -> String.format("$%.1fK", number / 1_000.0)
            else -> String.format("$%.0f", number)
        }
    }
}

data class MarketOverviewMetrics(
    val totalMarketCap: Double = 2_680_000_000_000.0,
    val volume24h: Double = 98_450_000_000.0,
    val btcDominance: Double = 56.8,
    val usdtDominance: Double = 4.42,
    val ethDominance: Double = 14.1,
    val marketCapChange24h: Double = 2.45,
    val fearGreedIndex: Int = 68,
    val fearGreedLabel: String = "Greed"
) {
    val formattedTotalMarketCap: String
        get() = String.format("$%.2fT", totalMarketCap / 1_000_000_000_000.0)

    val formattedVolume24h: String
        get() = String.format("$%.2fB", volume24h / 1_000_000_000.0)
}
