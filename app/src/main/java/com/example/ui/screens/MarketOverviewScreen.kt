package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCoin
import com.example.data.model.MarketOverviewMetrics
import com.example.ui.components.CryptoCoinVectorBadge
import com.example.ui.components.SparklineChart
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoGreenBg
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoRedBg
import com.example.ui.tradingview.TradingViewHtml
import com.example.ui.tradingview.TradingViewWidgetView

enum class MoversCategory(val title: String) {
    ALL("Market Movers"),
    GAINERS("Top Gainers"),
    LOSERS("Top Losers"),
    VOLUME("Most Active")
}

@Composable
fun MarketOverviewScreen(
    coins: List<CryptoCoin>,
    metrics: MarketOverviewMetrics,
    isTvMode: Boolean,
    isDarkTheme: Boolean,
    onToggleTvMode: () -> Unit,
    onCoinClick: (CryptoCoin) -> Unit,
    onToggleFavorite: (CryptoCoin) -> Unit,
    modifier: Modifier = Modifier,
    onSelectSymbol: ((String) -> Unit)? = null
) {
    var selectedMoversCategory by remember { mutableStateOf(MoversCategory.ALL) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("market_overview_screen")
    ) {
        // Header Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Market Movers",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isTvMode) "TradingView TV Market Overview" else "Real-time Crypto Market Activity",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onToggleTvMode,
                    modifier = Modifier.testTag("toggle_overview_widget_btn")
                ) {
                    Icon(
                        imageVector = if (isTvMode) Icons.Default.SwapHoriz else Icons.Default.Code,
                        contentDescription = "Switch Overview Mode",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (isTvMode) {
            // TradingView Market Movers Web Component Embed
            TradingViewWidgetView(
                htmlContent = TradingViewHtml.getMarketOverviewHtml(isDark = isDarkTheme),
                onFallbackNative = onToggleTvMode,
                onSymbolSelected = onSelectSymbol,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Native High-Performance Layout
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Global Crypto Macro Statistics Carousel
                item {
                    MarketMetricsBanner(
                        metrics = metrics,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Filter Category Chips
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MoversCategory.values().forEach { category ->
                            FilterChip(
                                selected = selectedMoversCategory == category,
                                onClick = { selectedMoversCategory = category },
                                leadingIcon = {
                                    when (category) {
                                        MoversCategory.ALL -> Icon(Icons.Default.LocalFireDepartment, contentDescription = null, modifier = Modifier.size(16.dp))
                                        MoversCategory.GAINERS -> Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp), tint = CryptoGreen)
                                        MoversCategory.LOSERS -> Icon(Icons.Default.TrendingDown, contentDescription = null, modifier = Modifier.size(16.dp), tint = CryptoRed)
                                        MoversCategory.VOLUME -> Icon(Icons.Default.LocalFireDepartment, contentDescription = null, modifier = Modifier.size(16.dp), tint = CryptoAccentGold)
                                    }
                                },
                                label = { Text(category.title, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }

                val displayedCoins = when (selectedMoversCategory) {
                    MoversCategory.ALL -> coins
                    MoversCategory.GAINERS -> coins.filter { it.change24h > 0 }.sortedByDescending { it.change24h }
                    MoversCategory.LOSERS -> coins.filter { it.change24h < 0 }.sortedBy { it.change24h }
                    MoversCategory.VOLUME -> coins.sortedByDescending { it.volume24h }
                }

                items(
                    items = displayedCoins,
                    key = { it.symbol }
                ) { coin ->
                    CryptoCoinCard(
                        coin = coin,
                        onClick = { onCoinClick(coin) },
                        onToggleFavorite = { onToggleFavorite(coin) }
                    )
                }
            }
        }
    }
}

@Composable
fun MarketMetricsBanner(
    metrics: MarketOverviewMetrics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Global Market Summary",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CryptoGreenBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Fear & Greed: ${metrics.fearGreedIndex} (${metrics.fearGreedLabel})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CryptoGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total M.Cap",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = metrics.formattedTotalMarketCap,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "+${metrics.marketCapChange24h}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CryptoGreen
                    )
                }

                Column {
                    Text(
                        text = "24h Volume",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = metrics.formattedVolume24h,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column {
                    Text(
                        text = "BTC Dominance",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${metrics.btcDominance}%",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CryptoAccentGold
                    )
                }

                Column {
                    Text(
                        text = "USDT.D",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${metrics.usdtDominance}%",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoCoinCard(
    coin: CryptoCoin,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPositive = coin.change24h >= 0
    val targetBg = when {
        coin.isFlashUp -> CryptoGreen.copy(alpha = 0.25f)
        coin.isFlashDown -> CryptoRed.copy(alpha = 0.25f)
        else -> MaterialTheme.colorScheme.surface
    }
    val animatedBg by animateColorAsState(
        targetValue = targetBg,
        animationSpec = tween(durationMillis = 350),
        label = "flashBg"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .testTag("coin_card_${coin.symbol}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = animatedBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Text(
                text = "${coin.rank}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(22.dp)
            )

            // Pure Mathematical Vector Badge
            CryptoCoinVectorBadge(
                symbol = coin.symbol,
                size = 36.dp
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Name & Symbol
            Column(modifier = Modifier.weight(1.3f)) {
                Text(
                    text = coin.symbol,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = coin.name,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Sparkline Curve
            SparklineChart(
                points = coin.sparklinePoints,
                isPositive = isPositive,
                modifier = Modifier
                    .width(60.dp)
                    .height(28.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Price & 24h Change
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1.4f)
            ) {
                Text(
                    text = coin.formattedPrice,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isPositive) CryptoGreenBg else CryptoRedBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = coin.formattedChange24h,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isPositive) CryptoGreen else CryptoRed
                    )
                }
            }

            // Watchlist Star Icon
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("star_button_${coin.symbol}")
            ) {
                Icon(
                    imageVector = if (coin.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = if (coin.isFavorite) "Remove from Watchlist" else "Add to Watchlist",
                    tint = if (coin.isFavorite) CryptoAccentGold else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
