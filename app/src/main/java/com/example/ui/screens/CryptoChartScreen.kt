package com.example.ui.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCoin
import com.example.ui.components.CryptoCoinVectorBadge
import com.example.ui.components.CryptoInteractiveChart
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.GlassmorphicIconButton
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoRed
import com.example.ui.tradingview.TradingViewHtml
import com.example.ui.tradingview.TradingViewWidgetView
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CryptoChartScreen(
    symbol: String,
    coins: List<CryptoCoin> = emptyList(),
    isDarkTheme: Boolean = true,
    isFullscreen: Boolean = false,
    onToggleFullscreen: () -> Unit = {},
    modifier: Modifier = Modifier,
    onSelectSymbol: ((String) -> Unit)? = null
) {
    var refreshKey by remember { mutableIntStateOf(0) }
    var isTvMode by remember { mutableStateOf(true) }
    var isHeaderVisible by remember { mutableStateOf(true) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Clean and validate the incoming symbol first
    val cleanedSymbol = remember(symbol) {
        com.example.ui.tradingview.cleanSymbolFormat(symbol)
    }

    // Find the corresponding coin data if available
    val coin = remember(symbol, cleanedSymbol, coins) {
        coins.firstOrNull { it.tvSymbol.equals(cleanedSymbol, ignoreCase = true) }
            ?: coins.firstOrNull { it.tvSymbol.equals(symbol, ignoreCase = true) }
            ?: coins.firstOrNull { it.symbol.equals(cleanedSymbol, ignoreCase = true) }
            ?: coins.firstOrNull { it.symbol.equals(symbol, ignoreCase = true) }
            ?: coins.firstOrNull {
                val base = it.symbol.uppercase()
                cleanedSymbol.endsWith(":$base") ||
                cleanedSymbol.endsWith(":$base" + "USDT") ||
                cleanedSymbol.endsWith(":$base" + "USD") ||
                cleanedSymbol == "${base}USDT" ||
                cleanedSymbol == "${base}USD"
            }
    }

    // CRITICAL: The effective chart symbol must ALWAYS reflect the selected symbol!
    // Never force fallback to Bitcoin unless incoming symbol is completely blank.
    val effectiveSymbol = if (cleanedSymbol.isNotBlank()) {
        cleanedSymbol
    } else if (symbol.isNotBlank()) {
        symbol
    } else {
        coin?.tvSymbol ?: "BITSTAMP:BTCUSD"
    }

    // If in fullscreen, back press exits fullscreen mode
    BackHandler(enabled = isFullscreen) {
        onToggleFullscreen()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("crypto_chart_screen")
    ) {
        // Top Header Bar - PLACED DIRECTLY ABOVE THE CHART (NOT OVERLAPPING THE CHART)
        AnimatedVisibility(
            visible = isHeaderVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("chart_top_header_bar"),
                shape = RoundedCornerShape(12.dp),
                surfaceAlpha = 0.80f,
                borderAlpha = 0.20f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Clickable coin info with search badge
                    val displaySymbol = coin?.symbol ?: symbol.substringAfter(":").replace("USDT", "").replace("USD", "").ifBlank { symbol }
                    val displayName = coin?.name ?: displaySymbol
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showSearchDialog = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .testTag("chart_coin_picker_btn")
                    ) {
                        CryptoCoinVectorBadge(symbol = displaySymbol, size = 26.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = displayName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search coin",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            if (coin != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = coin.formattedPrice,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    val isPositive = coin.change24h >= 0
                                    Text(
                                        text = (if (isPositive) "+" else "") + String.format(Locale.US, "%.2f%%", coin.change24h),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPositive) CryptoGreen else CryptoRed
                                    )
                                }
                            } else {
                                Text(
                                    text = effectiveSymbol,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Right: Control actions with Glassmorphic micro-buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Quick Search Dialog button
                        GlassmorphicIconButton(
                            onClick = { showSearchDialog = true },
                            size = 34.dp,
                            modifier = Modifier.testTag("chart_search_dialog_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search and Change Coin",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Toggle between TradingView and Native Chart
                        GlassmorphicIconButton(
                            onClick = { isTvMode = !isTvMode },
                            size = 34.dp,
                            tintGlow = if (isTvMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.testTag("chart_mode_toggle_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = if (isTvMode) "Switch to Native Chart" else "Switch to TradingView Chart",
                                tint = if (isTvMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        if (isTvMode) {
                            GlassmorphicIconButton(
                                onClick = { refreshKey++ },
                                size = 34.dp,
                                modifier = Modifier.testTag("chart_refresh_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh Chart",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        GlassmorphicIconButton(
                            onClick = onToggleFullscreen,
                            size = 34.dp,
                            modifier = Modifier.testTag("chart_fullscreen_btn")
                        ) {
                            Icon(
                                imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                contentDescription = if (isFullscreen) "Exit Fullscreen" else "Fullscreen Mode",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Hide/Collapse Header Button (gives maximum space if user desires)
                        GlassmorphicIconButton(
                            onClick = { isHeaderVisible = false },
                            size = 34.dp,
                            modifier = Modifier.testTag("chart_hide_header_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Hide Header Bar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Floating Unhide pill ONLY if header was hidden by user
        if (!isHeaderVisible) {
            Surface(
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp)
                    .clickable { isHeaderVisible = true },
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Show Header Bar",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Show Bar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Chart Content Area (occupies full remaining height, completely unobstructed)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("chart_content_area")
        ) {
            if (isTvMode) {
                TradingViewWidgetView(
                    htmlContent = remember(effectiveSymbol, isDarkTheme, refreshKey) {
                        TradingViewHtml.getAdvancedChartHtml(
                            symbol = effectiveSymbol,
                            isDark = isDarkTheme
                        )
                    },
                    onFallbackNative = { isTvMode = false },
                    onSymbolSelected = onSelectSymbol,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                NativeChartFullView(
                    coin = coin,
                    symbol = effectiveSymbol,
                    isDarkTheme = isDarkTheme,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Quick Coin Search Dialog
    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = {
                showSearchDialog = false
                searchQuery = ""
            },
            title = {
                Text("Search & Select Coin Chart", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search BTC, ETH, SOL...", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val filteredCoins = remember(searchQuery, coins) {
                        if (searchQuery.isBlank()) coins
                        else coins.filter {
                            it.name.contains(searchQuery, ignoreCase = true) ||
                            it.symbol.contains(searchQuery, ignoreCase = true) ||
                            it.tvSymbol.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredCoins) { c ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectSymbol?.invoke(c.tvSymbol)
                                        showSearchDialog = false
                                        searchQuery = ""
                                    },
                                shape = RoundedCornerShape(8.dp),
                                color = if (c.symbol == coin?.symbol) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CryptoCoinVectorBadge(symbol = c.symbol, size = 20.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(c.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text(c.symbol, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(c.formattedPrice, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            c.formattedChange24h,
                                            fontSize = 11.sp,
                                            color = if (c.change24h >= 0) CryptoGreen else CryptoRed,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (searchQuery.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val cleaned = com.example.ui.tradingview.cleanSymbolFormat(searchQuery)
                                    onSelectSymbol?.invoke(cleaned)
                                    showSearchDialog = false
                                    searchQuery = ""
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Load Chart for \"${searchQuery.uppercase()}\"",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showSearchDialog = false
                    searchQuery = ""
                }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun NativeChartFullView(
    coin: CryptoCoin?,
    symbol: String,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableStateOf("24H") }
    var scrubbedPrice by remember { mutableStateOf<Float?>(null) }
    val priceFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    val rawPoints = coin?.sparklinePoints ?: listOf(100f, 102f, 98f, 105f, 108f, 104f, 110f)
    val points = remember(rawPoints, selectedTimeframe) {
        when (selectedTimeframe) {
            "1H" -> rawPoints.takeLast(6)
            "24H" -> rawPoints
            "7D" -> rawPoints.mapIndexed { idx, p -> p * (1f + (idx % 3 - 1) * 0.02f) }
            else -> rawPoints
        }
    }

    val displayPrice = scrubbedPrice?.let { priceFormatter.format(it) }
        ?: coin?.formattedPrice
        ?: "$0.00"

    val isPositive = (coin?.change24h ?: 0.0) >= 0

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Coin Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = coin?.name ?: symbol,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = coin?.symbol ?: symbol,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = displayPrice,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (coin != null) {
                    Text(
                        text = coin.formattedChange24h,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isPositive) CryptoGreen else CryptoRed
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Timeframe selector
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("1H", "24H", "7D", "1M", "1Y").forEach { tf ->
                FilterChip(
                    selected = selectedTimeframe == tf,
                    onClick = { selectedTimeframe = tf },
                    label = { Text(tf, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Full Interactive Canvas Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CryptoInteractiveChart(
                points = points,
                isPositive = isPositive,
                modifier = Modifier.fillMaxSize(),
                onPriceSelected = { scrubbedPrice = it }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Stats Footer
        if (coin != null) {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                accentGlow = if (isPositive) CryptoGreen else CryptoRed,
                borderAlpha = 0.25f,
                surfaceAlpha = 0.75f
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("24h High", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(String.format("$%,.2f", coin.high24h), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("24h Low", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(String.format("$%,.2f", coin.low24h), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Volume", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(coin.formattedVolume24h, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
