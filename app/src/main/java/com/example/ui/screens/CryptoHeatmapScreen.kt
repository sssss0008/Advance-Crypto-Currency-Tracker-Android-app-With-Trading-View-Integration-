package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCategory
import com.example.data.model.CryptoCoin
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoRed
import com.example.ui.tradingview.TradingViewHtml
import com.example.ui.tradingview.TradingViewWidgetView

@Composable
fun CryptoHeatmapScreen(
    coins: List<CryptoCoin>,
    isTvMode: Boolean,
    isDarkTheme: Boolean,
    onToggleTvMode: () -> Unit,
    onCoinClick: (CryptoCoin) -> Unit,
    modifier: Modifier = Modifier,
    onSelectSymbol: ((String) -> Unit)? = null
) {
    var selectedCategory by remember { mutableStateOf(CryptoCategory.ALL) }
    var selectedCoinForPreview by remember { mutableStateOf<CryptoCoin?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("crypto_heatmap_screen")
    ) {
        // Heatmap Toolbar
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
                        text = "Crypto Coins Heatmap",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isTvMode) "TradingView Heatmap Widget" else "Sized by M.Cap • Colored by 24h Change",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleTvMode,
                        modifier = Modifier.testTag("toggle_heatmap_widget_btn")
                    ) {
                        Icon(
                            imageVector = if (isTvMode) Icons.Default.SwapHoriz else Icons.Default.Code,
                            contentDescription = "Switch Heatmap Mode",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        if (isTvMode) {
            // Full TradingView Embed Mode
            TradingViewWidgetView(
                htmlContent = TradingViewHtml.getHeatmapHtml(isDark = isDarkTheme),
                onFallbackNative = onToggleTvMode,
                onSymbolSelected = onSelectSymbol,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Native Interactive Treemap Heatmap
            val filteredCoins = remember(coins, selectedCategory) {
                if (selectedCategory == CryptoCategory.ALL) {
                    coins.filter { it.symbol != "USDT.D" }.take(16)
                } else {
                    coins.filter { it.category == selectedCategory }
                }
            }

            // Category Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    CryptoCategory.ALL,
                    CryptoCategory.LAYER1,
                    CryptoCategory.DEFI,
                    CryptoCategory.AI,
                    CryptoCategory.MEME
                ).forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.displayName, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            // Color legend bar
            HeatmapLegendBar(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))

            // Treemap Layout Container
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                NativeHeatmapGrid(
                    coins = filteredCoins,
                    onCoinClick = { coin ->
                        onCoinClick(coin)
                    }
                )
            }
        }
    }
}

@Composable
fun HeatmapLegendBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFB71C1C))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("-5%", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFE53935))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("-2%", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF334155))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("0%", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF00B050))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("+2%", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF00E676))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("+5%+", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun NativeHeatmapGrid(
    coins: List<CryptoCoin>,
    onCoinClick: (CryptoCoin) -> Unit
) {
    if (coins.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No coins in this category", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    // Adaptive Treemap Block Arrangement
    // Top 2 coins (e.g. BTC, ETH) take dominant prominent top section
    // Next tier coins take mid tier
    // Remaining take bottom tiles
    val btc = coins.firstOrNull { it.symbol == "BTC" } ?: coins.getOrNull(0)
    val eth = coins.firstOrNull { it.symbol == "ETH" } ?: coins.getOrNull(1)
    val remaining = coins.filter { it != btc && it != eth }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Top Tier (BTC & ETH or top 2)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            btc?.let {
                HeatmapTile(
                    coin = it,
                    modifier = Modifier.weight(1.4f),
                    onCoinClick = onCoinClick,
                    isLarge = true
                )
            }
            eth?.let {
                HeatmapTile(
                    coin = it,
                    modifier = Modifier.weight(1.0f),
                    onCoinClick = onCoinClick,
                    isLarge = true
                )
            }
        }

        // Tier 2 (Next 3-4 coins: SOL, BNB, XRP, AVAX)
        if (remaining.isNotEmpty()) {
            val tier2 = remaining.take(3)
            val tier3 = remaining.drop(3)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tier2.forEach { coin ->
                    HeatmapTile(
                        coin = coin,
                        modifier = Modifier.weight(1f),
                        onCoinClick = onCoinClick,
                        isLarge = false
                    )
                }
            }

            // Tier 3 (Remaining small tiles)
            if (tier3.isNotEmpty()) {
                val row1 = tier3.take(4)
                val row2 = tier3.drop(4).take(4)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    row1.forEach { coin ->
                        HeatmapTile(
                            coin = coin,
                            modifier = Modifier.weight(1f),
                            onCoinClick = onCoinClick,
                            isLarge = false
                        )
                    }
                }

                if (row2.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.0f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        row2.forEach { coin ->
                            HeatmapTile(
                                coin = coin,
                                modifier = Modifier.weight(1f),
                                onCoinClick = onCoinClick,
                                isLarge = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeatmapTile(
    coin: CryptoCoin,
    modifier: Modifier = Modifier,
    onCoinClick: (CryptoCoin) -> Unit,
    isLarge: Boolean = false
) {
    val bgColor = getHeatmapColor(coin.change24h)

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(0.8.dp, Color.Black.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
            .clickable { onCoinClick(coin) }
            .padding(6.dp)
            .testTag("heatmap_tile_${coin.symbol}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = coin.symbol,
                fontSize = if (isLarge) 18.sp else 13.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (isLarge) {
                Text(
                    text = coin.name,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1
                )
            }

            Text(
                text = coin.formattedPrice,
                fontSize = if (isLarge) 13.sp else 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.95f),
                maxLines = 1
            )

            Text(
                text = coin.formattedChange24h,
                fontSize = if (isLarge) 13.sp else 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
        }
    }
}

fun getHeatmapColor(change24h: Double): Color {
    return when {
        change24h >= 6.0 -> Color(0xFF00E676)
        change24h >= 3.0 -> Color(0xFF00C853)
        change24h >= 0.5 -> Color(0xFF2E7D32)
        change24h >= -0.5 -> Color(0xFF334155) // Neutral slate
        change24h >= -3.0 -> Color(0xFFC62828)
        change24h >= -6.0 -> Color(0xFFD50000)
        else -> Color(0xFFB71C1C) // Deep dark crimson
    }
}

@Composable
fun HeatmapCoinPreviewCard(
    coin: CryptoCoin,
    onDismiss: () -> Unit,
    onOpenChart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .testTag("heatmap_preview_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${coin.name} (${coin.symbol})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${coin.formattedPrice} • ${coin.formattedChange24h} • Cap: ${coin.formattedMarketCap}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onOpenChart() }
            ) {
                Text(
                    text = "Open Chart",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}
