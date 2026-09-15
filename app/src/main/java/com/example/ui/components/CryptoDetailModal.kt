package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCoin
import com.example.data.model.TechnicalRating
import com.example.ui.components.CryptoCoinVectorBadge
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoGreenBg
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoRedBg
import com.example.ui.tradingview.TradingViewHtml
import com.example.ui.tradingview.TradingViewWidgetView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailModal(
    coin: CryptoCoin?,
    isDarkTheme: Boolean,
    onDismiss: () -> Unit,
    onToggleFavorite: (CryptoCoin) -> Unit,
    onOpenFullChart: ((String) -> Unit)? = null
) {
    if (coin == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isTvChartMode by remember { mutableStateOf(false) }
    var selectedTimeframe by remember { mutableStateOf("24H") }
    var scrubbedPrice by remember { mutableStateOf<Float?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null,
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .testTag("crypto_detail_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Pure mathematical vector badge
                    CryptoCoinVectorBadge(
                        symbol = coin.symbol,
                        size = 42.dp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = coin.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "#${coin.rank}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "${coin.symbol} • ${coin.tvSymbol}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    onOpenFullChart?.let { openChart ->
                        IconButton(
                            onClick = {
                                onDismiss()
                                openChart(coin.tvSymbol)
                            },
                            modifier = Modifier.testTag("modal_fullscreen_chart_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Open Fullscreen Chart",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(
                        onClick = { onToggleFavorite(coin) },
                        modifier = Modifier.testTag("modal_star_btn")
                    ) {
                        Icon(
                            imageVector = if (coin.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Watchlist",
                            tint = if (coin.isFavorite) CryptoAccentGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            }

            // Price & Change
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                val displayPrice = if (scrubbedPrice != null) {
                    if (scrubbedPrice!! >= 1) String.format("$%,.2f", scrubbedPrice)
                    else String.format("$%.4f", scrubbedPrice)
                } else {
                    coin.formattedPrice
                }

                Text(
                    text = displayPrice,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    val isPositive = coin.change24h >= 0
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isPositive) CryptoGreenBg else CryptoRedBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = coin.formattedChange24h,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPositive) CryptoGreen else CryptoRed
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "24h Volume: ${coin.formattedVolume24h}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chart Mode Selector: Native vs TradingView Pro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timeframe Selector
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("1H", "24H", "7D", "1M", "1Y").forEach { tf ->
                        FilterChip(
                            selected = selectedTimeframe == tf,
                            onClick = { selectedTimeframe = tf },
                            label = { Text(tf, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                // Switch to TradingView Widget
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isTvChartMode) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clickable { isTvChartMode = !isTvChartMode }
                        .testTag("toggle_tv_chart_mode_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = if (isTvChartMode) Icons.Default.ShowChart else Icons.Default.Code,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isTvChartMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isTvChartMode) "Native" else "TradingView Pro",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isTvChartMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chart Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp)
            ) {
                if (isTvChartMode) {
                    TradingViewWidgetView(
                        htmlContent = TradingViewHtml.getSymbolChartHtml(symbol = coin.tvSymbol, isDark = isDarkTheme),
                        onFallbackNative = { isTvChartMode = false },
                        onSymbolSelected = { sym ->
                            onDismiss()
                            onOpenFullChart?.invoke(sym)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    CryptoInteractiveChart(
                        points = coin.sparklinePoints,
                        isPositive = coin.change24h >= 0,
                        modifier = Modifier.fillMaxSize(),
                        onPriceSelected = { scrubbedPrice = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 24h High / Low Progress Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "24h Trading Range",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val low = coin.low24h
                    val high = coin.high24h
                    val current = coin.priceUsd
                    val progress = if (high > low) ((current - low) / (high - low)).toFloat().coerceIn(0f, 1f) else 0.5f

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Low: ${if (low >= 1) String.format("$%,.2f", low) else String.format("$%.4f", low)}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "High: ${if (high >= 1) String.format("$%,.2f", high) else String.format("$%.4f", high)}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Key Statistics Grid
            Text(
                text = "Key Market Statistics",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    StatRow(label = "Market Cap", value = coin.formattedMarketCap)
                    StatDivider()
                    StatRow(label = "24h Trading Volume", value = coin.formattedVolume24h)
                    StatDivider()
                    StatRow(label = "Circulating Supply", value = coin.circulatingSupply)
                    StatDivider()
                    StatRow(
                        label = "All-Time High (ATH)",
                        value = if (coin.allTimeHigh >= 1) String.format("$%,.2f", coin.allTimeHigh) else String.format("$%.4f", coin.allTimeHigh)
                    )
                    StatDivider()
                    StatRow(label = "14-Day RSI", value = String.format("%.1f", coin.rsi14))
                    StatDivider()
                    StatRow(
                        label = "Technical Consensus",
                        value = coin.technicalRating.label,
                        valueColor = when (coin.technicalRating) {
                            TechnicalRating.STRONG_BUY, TechnicalRating.BUY -> CryptoGreen
                            TechnicalRating.NEUTRAL -> MaterialTheme.colorScheme.onSurface
                            TechnicalRating.SELL, TechnicalRating.STRONG_SELL -> CryptoRed
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Composable
fun StatDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.6.dp)
            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    )
}
