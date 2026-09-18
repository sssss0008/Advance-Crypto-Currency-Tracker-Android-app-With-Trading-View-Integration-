package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import com.example.data.model.CryptoCategory
import com.example.data.model.CryptoCoin
import com.example.data.model.ScreenerColumn
import com.example.data.model.SortOption
import com.example.data.model.TechnicalRating
import com.example.ui.components.CryptoCoinVectorBadge
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoGreenBg
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoRedBg
import com.example.ui.tradingview.TradingViewHtml
import com.example.ui.tradingview.TradingViewWidgetView

@Composable
fun CryptoScreenerScreen(
    coins: List<CryptoCoin>,
    isTvMode: Boolean,
    isDarkTheme: Boolean,
    searchQuery: String,
    selectedCategory: CryptoCategory,
    selectedSortOption: SortOption,
    selectedColumn: ScreenerColumn,
    onSearchQueryChange: (String) -> Unit,
    onCategoryChange: (CryptoCategory) -> Unit,
    onSortOptionChange: (SortOption) -> Unit,
    onColumnChange: (ScreenerColumn) -> Unit,
    onToggleTvMode: () -> Unit,
    onCoinClick: (CryptoCoin) -> Unit,
    onToggleFavorite: (CryptoCoin) -> Unit,
    modifier: Modifier = Modifier,
    onSelectSymbol: ((String) -> Unit)? = null
) {
    var isSortMenuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("crypto_screener_screen")
    ) {
        // Toolbar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Crypto Screener",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isTvMode) "TradingView Screener Widget" else "Filter & Screen by Valuation & Technicals",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onToggleTvMode,
                        modifier = Modifier.testTag("toggle_screener_widget_btn")
                    ) {
                        Icon(
                            imageVector = if (isTvMode) Icons.Default.SwapHoriz else Icons.Default.Code,
                            contentDescription = "Switch Screener Mode",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (!isTvMode) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("screener_search_input"),
                        placeholder = { Text("Search coin or symbol (e.g. BTC, Solana)...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(20.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        if (isTvMode) {
            TradingViewWidgetView(
                htmlContent = TradingViewHtml.getScreenerHtml(isDark = isDarkTheme),
                onFallbackNative = onToggleTvMode,
                onSymbolSelected = onSelectSymbol,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Native Screener Content
            // Screener Columns Tabs: Overview, Performance, Valuation, Technicals
            ScrollableTabRow(
                selectedTabIndex = selectedColumn.ordinal,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                ScreenerColumn.values().forEach { col ->
                    Tab(
                        selected = selectedColumn == col,
                        onClick = { onColumnChange(col) },
                        text = {
                            Text(
                                text = col.displayName,
                                fontWeight = if (selectedColumn == col) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            // Filters & Sort Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chips
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CryptoCategory.values().forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { onCategoryChange(cat) },
                            label = { Text(cat.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Sort Button
                Box {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { isSortMenuOpen = true }
                            .testTag("sort_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Sort, contentDescription = "Sort", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sort", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }

                    DropdownMenu(
                        expanded = isSortMenuOpen,
                        onDismissRequest = { isSortMenuOpen = false }
                    ) {
                        SortOption.values().forEach { sort ->
                            DropdownMenuItem(
                                text = { Text(sort.displayName, fontSize = 13.sp) },
                                onClick = {
                                    onSortOptionChange(sort)
                                    isSortMenuOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // Filter & Sort Logic
            val filteredCoins = remember(coins, searchQuery, selectedCategory, selectedSortOption) {
                var list = coins

                if (searchQuery.isNotBlank()) {
                    val query = searchQuery.trim().lowercase()
                    list = list.filter {
                        it.name.lowercase().contains(query) || it.symbol.lowercase().contains(query)
                    }
                }

                if (selectedCategory != CryptoCategory.ALL) {
                    list = list.filter { it.category == selectedCategory }
                }

                when (selectedSortOption) {
                    SortOption.RANK -> list.sortedBy { it.rank }
                    SortOption.MARKET_CAP_DESC -> list.sortedByDescending { it.marketCap }
                    SortOption.CHANGE_DESC -> list.sortedByDescending { it.change24h }
                    SortOption.CHANGE_ASC -> list.sortedBy { it.change24h }
                    SortOption.PRICE_DESC -> list.sortedByDescending { it.priceUsd }
                    SortOption.VOLUME_DESC -> list.sortedByDescending { it.volume24h }
                }
            }

            // Screener Table Header
            ScreenerTableHeader(column = selectedColumn)

            // Coin Rows
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(
                    items = filteredCoins,
                    key = { it.symbol }
                ) { coin ->
                    ScreenerCoinRow(
                        coin = coin,
                        column = selectedColumn,
                        onClick = { onCoinClick(coin) },
                        onToggleFavorite = { onToggleFavorite(coin) }
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenerTableHeader(column: ScreenerColumn) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "# Symbol",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1.6f)
            )

            when (column) {
                ScreenerColumn.OVERVIEW -> {
                    Text(
                        text = "Price",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "24h %",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(0.9f)
                    )
                    Text(
                        text = "Market Cap",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.1f)
                    )
                }
                ScreenerColumn.PERFORMANCE -> {
                    Text(
                        text = "1h",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "24h",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "7d",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.0f)
                    )
                }
                ScreenerColumn.VALUATION -> {
                    Text(
                        text = "Market Cap",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.1f)
                    )
                    Text(
                        text = "Volume",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "ATH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(0.9f)
                    )
                }
                ScreenerColumn.TECHNICALS -> {
                    Text(
                        text = "RSI (14)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = "Consensus Rating",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(2.0f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp))
        }
    }
}

@Composable
fun ScreenerCoinRow(
    coin: CryptoCoin,
    column: ScreenerColumn,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val isPositive = coin.change24h >= 0
    GlassmorphicCard(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        surfaceAlpha = 0.50f,
        borderAlpha = 0.15f,
        accentGlow = if (isPositive) CryptoGreen else CryptoRed,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .testTag("screener_row_${coin.symbol}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Symbol & Rank
            Row(
                modifier = Modifier.weight(1.6f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${coin.rank}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(18.dp)
                )
                CryptoCoinVectorBadge(symbol = coin.symbol, size = 22.dp)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = coin.symbol,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = coin.name,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Columns based on active tab
            when (column) {
                ScreenerColumn.OVERVIEW -> {
                    Text(
                        text = coin.formattedPrice,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = coin.formattedChange24h,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (coin.change24h >= 0) CryptoGreen else CryptoRed,
                        modifier = Modifier.weight(0.9f)
                    )
                    Text(
                        text = coin.formattedMarketCap,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.1f)
                    )
                }
                ScreenerColumn.PERFORMANCE -> {
                    Text(
                        text = String.format("%+.2f%%", coin.change1h),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (coin.change1h >= 0) CryptoGreen else CryptoRed,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = coin.formattedChange24h,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (coin.change24h >= 0) CryptoGreen else CryptoRed,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = String.format("%+.2f%%", coin.change7d),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (coin.change7d >= 0) CryptoGreen else CryptoRed,
                        modifier = Modifier.weight(1.0f)
                    )
                }
                ScreenerColumn.VALUATION -> {
                    Text(
                        text = coin.formattedMarketCap,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1.1f)
                    )
                    Text(
                        text = coin.formattedVolume24h,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        text = String.format("$%,.2f", coin.allTimeHigh),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(0.9f)
                    )
                }
                ScreenerColumn.TECHNICALS -> {
                    Text(
                        text = String.format("%.1f", coin.rsi14),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            coin.rsi14 >= 70 -> CryptoGreen
                            coin.rsi14 <= 30 -> CryptoRed
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.weight(1.0f)
                    )
                    Box(
                        modifier = Modifier
                            .weight(2.0f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (coin.technicalRating) {
                                    TechnicalRating.STRONG_BUY -> CryptoGreen.copy(alpha = 0.2f)
                                    TechnicalRating.BUY -> CryptoGreen.copy(alpha = 0.12f)
                                    TechnicalRating.NEUTRAL -> Color.Gray.copy(alpha = 0.15f)
                                    TechnicalRating.SELL -> CryptoRed.copy(alpha = 0.12f)
                                    TechnicalRating.STRONG_SELL -> CryptoRed.copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = coin.technicalRating.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (coin.technicalRating) {
                                TechnicalRating.STRONG_BUY, TechnicalRating.BUY -> CryptoGreen
                                TechnicalRating.NEUTRAL -> MaterialTheme.colorScheme.onSurfaceVariant
                                TechnicalRating.SELL, TechnicalRating.STRONG_SELL -> CryptoRed
                            }
                        )
                    }
                }
            }

            // Favorite toggle
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .size(32.dp)
                    .testTag("star_screener_${coin.symbol}")
            ) {
                Icon(
                    imageVector = if (coin.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = if (coin.isFavorite) CryptoAccentGold else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
