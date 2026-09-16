package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCoin
import com.example.ui.AppTab
import com.example.ui.theme.CryptoAccentCyan
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoPrimary
import com.example.ui.theme.CryptoRed

data class DrawerMenuItem(
    val tab: AppTab,
    val title: String,
    val icon: ImageVector,
    val badge: String? = null,
    val badgeColor: Color? = null
)

@Composable
fun AppDrawerContent(
    currentTab: AppTab,
    coins: List<CryptoCoin>,
    isDarkTheme: Boolean,
    onTabSelected: (AppTab) -> Unit,
    onCoinSelected: (CryptoCoin) -> Unit,
    onToggleTheme: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val favoriteCoins = remember(coins) { coins.filter { it.isFavorite } }
    val btc = remember(coins) { coins.firstOrNull { it.symbol == "BTC" } }
    val eth = remember(coins) { coins.firstOrNull { it.symbol == "ETH" } }

    ModalDrawerSheet(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
            .testTag("app_navigation_drawer"),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Drawer Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CryptoCoinVectorBadge(symbol = "BTC", size = 36.dp)

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "CryptoPro",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Quant & Terminal",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onCloseDrawer) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Drawer",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real-time BTC / ETH mini ticker card
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    btc?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("BTC: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("$${String.format("%,.0f", it.priceUsd)}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text(
                                " (${String.format("%+.1f%%", it.change24h)})",
                                fontSize = 10.sp,
                                color = if (it.change24h >= 0) CryptoGreen else CryptoRed
                            )
                        }
                    }

                    eth?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("ETH: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("$${String.format("%,.0f", it.priceUsd)}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SECTION: TRADINGVIEW TOOLS & WIDGETS
            Text(
                text = "TRADINGVIEW TOOLS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )

            // Advanced Screener (Requested TradingView Screener Embed)
            NavigationDrawerItem(
                label = { Text("Advanced Screener", fontWeight = FontWeight.SemiBold) },
                selected = currentTab == AppTab.ADVANCED_SCREENER,
                onClick = {
                    onTabSelected(AppTab.ADVANCED_SCREENER)
                    onCloseDrawer()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = null,
                        tint = CryptoAccentCyan
                    )
                },
                badge = {
                    Surface(
                        color = CryptoAccentCyan.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "TV Screener",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CryptoAccentCyan,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.testTag("drawer_item_advanced_screener")
            )

            // TradingView Charts
            NavigationDrawerItem(
                label = { Text("TradingView Charts", fontWeight = FontWeight.SemiBold) },
                selected = currentTab == AppTab.CHART,
                onClick = {
                    onTabSelected(AppTab.CHART)
                    onCloseDrawer()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CandlestickChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                badge = {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Interactive",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.testTag("drawer_item_charts")
            )

            // Crypto Screener
            NavigationDrawerItem(
                label = { Text("Crypto Screener") },
                selected = currentTab == AppTab.SCREENER,
                onClick = {
                    onTabSelected(AppTab.SCREENER)
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Default.FilterAlt, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("drawer_item_screener")
            )

            // Visual Heatmap
            NavigationDrawerItem(
                label = { Text("Visual Heatmap") },
                selected = currentTab == AppTab.HEATMAP,
                onClick = {
                    onTabSelected(AppTab.HEATMAP)
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Default.GridView, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("drawer_item_heatmap")
            )

            // Market Overview
            NavigationDrawerItem(
                label = { Text("Market Movers & Overview") },
                selected = currentTab == AppTab.MARKETS,
                onClick = {
                    onTabSelected(AppTab.MARKETS)
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Default.CurrencyExchange, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("drawer_item_markets")
            )

            // Watchlist
            NavigationDrawerItem(
                label = { Text("My Watchlist") },
                selected = currentTab == AppTab.WATCHLIST,
                onClick = {
                    onTabSelected(AppTab.WATCHLIST)
                    onCloseDrawer()
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = CryptoAccentGold
                    )
                },
                badge = {
                    if (favoriteCoins.isNotEmpty()) {
                        Surface(
                            color = CryptoAccentGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "${favoriteCoins.size} Starred",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CryptoAccentGold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("drawer_item_watchlist")
            )

            // Watchlist peek row if favorites exist
            if (favoriteCoins.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "WATCHLIST QUICK ACCESS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                ) {
                    items(favoriteCoins) { coin ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable {
                                onCoinSelected(coin)
                                onCloseDrawer()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CryptoCoinVectorBadge(symbol = coin.symbol, size = 18.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = coin.symbol,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = String.format("%+.1f%%", coin.change24h),
                                        fontSize = 10.sp,
                                        color = if (coin.change24h >= 0) CryptoGreen else CryptoRed
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(10.dp))

            // Footer with Dark / Light Theme Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggleTheme)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Toggle Theme",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isDarkTheme) "Dark Mode" else "Light Mode",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Switch",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
