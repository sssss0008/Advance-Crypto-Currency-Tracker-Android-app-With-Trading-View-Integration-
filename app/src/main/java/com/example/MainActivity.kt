package com.example

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.CryptoViewModel
import com.example.ui.about.AboutUsScreen
import com.example.ui.components.AppDrawerContent
import com.example.ui.components.CryptoDetailModal
import com.example.ui.components.GlassmorphicIconButton
import com.example.ui.components.TickerTapeBar
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.splash.SplashScreen
import com.example.ui.screens.CryptoAdvancedScreenerScreen
import com.example.ui.screens.CryptoAiHubScreen
import com.example.ui.screens.CryptoCalculatorsScreen
import com.example.ui.screens.CryptoChartScreen
import com.example.ui.screens.CryptoDictionaryScreen
import com.example.ui.screens.CryptoEducationScreen
import com.example.ui.screens.CryptoHeatmapScreen
import com.example.ui.screens.CryptoScreenerScreen
import com.example.ui.screens.MarketOverviewScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.CryptoAccentCyan
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoPrimary
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: CryptoViewModel by viewModels {
        CryptoViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CryptoApplication.cleanCorruptedWebViewCache(this)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            MyApplicationTheme(darkTheme = uiState.isDarkTheme) {
                when {
                    uiState.showSplash -> {
                        SplashScreen(
                            onSplashFinished = { viewModel.finishSplash() }
                        )
                    }
                    uiState.showOnboarding -> {
                        OnboardingScreen(
                            onFinishOnboarding = { viewModel.finishOnboarding() }
                        )
                    }
                    else -> {
                        CryptoApp(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoApp(
    viewModel: CryptoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isChartTab = uiState.currentTab == AppTab.CHART
    val hideBars = isChartTab && (uiState.isChartFullscreen || isLandscape)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        // Disable swipe/drag gesture so drawer opens EXCLUSIVELY by tapping the navigation menu icon button
        gesturesEnabled = false,
        drawerContent = {
            AppDrawerContent(
                currentTab = uiState.currentTab,
                coins = uiState.coins,
                isDarkTheme = uiState.isDarkTheme,
                onTabSelected = { tab ->
                    viewModel.setTab(tab)
                },
                onCoinSelected = { coin ->
                    viewModel.selectCoin(coin)
                },
                onToggleTheme = {
                    viewModel.toggleTheme()
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                },
                onOpenOnboarding = {
                    viewModel.reopenOnboarding()
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("crypto_app_scaffold"),
            topBar = {
                if (!hideBars) {
                    TopAppBar(
                        navigationIcon = {
                            Box(modifier = Modifier.padding(start = 8.dp, end = 4.dp)) {
                                GlassmorphicIconButton(
                                    onClick = {
                                        scope.launch {
                                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                        }
                                    },
                                    size = 40.dp,
                                    tintGlow = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.testTag("open_drawer_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Open Navigation Drawer",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        },
                        title = {
                            val headerTitle = when (uiState.currentTab) {
                                AppTab.CHART -> "TradingView Charts"
                                AppTab.SCREENER -> "Crypto Screener"
                                AppTab.ADVANCED_SCREENER -> "Advanced Screener"
                                AppTab.HEATMAP -> "Visual Heatmap"
                                AppTab.MARKETS -> "Crypto Markets"
                                AppTab.WATCHLIST -> "My Watchlist"
                                AppTab.DICTIONARY -> "Crypto Dictionary"
                                AppTab.CALCULATORS -> "Crypto Calculators"
                                AppTab.EDUCATION -> "Learning Portal"
                                AppTab.AI_HUB -> "AI Hub"
                                AppTab.ABOUT -> "About Us"
                            }

                            val headerSubtitle = when (uiState.currentTab) {
                                AppTab.CHART -> "Live Interactive Technical Charts"
                                AppTab.SCREENER -> "TradingView Crypto Screener"
                                AppTab.ADVANCED_SCREENER -> "Multi-Metric Screener & Overview"
                                AppTab.HEATMAP -> "Top Market Cap Coins Treemap"
                                AppTab.MARKETS -> "Top Movers & 24h Volume"
                                AppTab.WATCHLIST -> "${uiState.coins.count { it.isFavorite }} Starred Assets"
                                AppTab.DICTIONARY -> "1,000+ Words & Practical Examples"
                                AppTab.CALCULATORS -> "7 Precision Tools & Interactive Keypad"
                                AppTab.EDUCATION -> "Academy Modules & Practice Quizzes"
                                AppTab.AI_HUB -> "Gemini AI Trading Assistant"
                                AppTab.ABOUT -> "Awiskar Acharya • Privacy • Free Project"
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (uiState.currentTab) {
                                                AppTab.ADVANCED_SCREENER -> CryptoAccentCyan
                                                AppTab.WATCHLIST -> CryptoAccentGold
                                                AppTab.CALCULATORS -> CryptoAccentGold
                                                AppTab.DICTIONARY -> CryptoAccentCyan
                                                AppTab.EDUCATION -> CryptoGreen
                                                AppTab.ABOUT -> CryptoAccentCyan
                                                else -> MaterialTheme.colorScheme.primary
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (uiState.currentTab) {
                                             AppTab.CHART -> Icons.Default.CandlestickChart
                                             AppTab.SCREENER -> Icons.Default.Tune
                                             AppTab.ADVANCED_SCREENER -> Icons.Default.FilterAlt
                                             AppTab.HEATMAP -> Icons.Default.GridView
                                             AppTab.MARKETS -> Icons.Default.CurrencyExchange
                                             AppTab.WATCHLIST -> Icons.Default.Star
                                             AppTab.CALCULATORS -> Icons.Default.Calculate
                                             AppTab.DICTIONARY -> Icons.AutoMirrored.Filled.MenuBook
                                             AppTab.EDUCATION -> Icons.Default.School
                                             AppTab.AI_HUB -> Icons.Default.AutoAwesome
                                             AppTab.ABOUT -> Icons.Default.Info
                                        },
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = headerTitle,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = headerSubtitle,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        actions = {
                            Box(modifier = Modifier.padding(end = 8.dp)) {
                                GlassmorphicIconButton(
                                    onClick = { viewModel.toggleTheme() },
                                    size = 40.dp,
                                    tintGlow = if (uiState.isDarkTheme) CryptoAccentGold else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.testTag("theme_toggle_btn")
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme",
                                        tint = if (uiState.isDarkTheme) CryptoAccentGold else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            },
            bottomBar = {
                if (!hideBars) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.CHART,
                            onClick = { viewModel.setTab(AppTab.CHART) },
                            icon = { Icon(Icons.Default.CandlestickChart, contentDescription = "Charts") },
                            label = { Text("Charts", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("nav_tab_chart"),
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.MARKETS,
                            onClick = { viewModel.setTab(AppTab.MARKETS) },
                            icon = { Icon(Icons.Default.CurrencyExchange, contentDescription = "Markets") },
                            label = { Text("Markets", fontSize = 10.sp) },
                            modifier = Modifier.testTag("nav_tab_markets"),
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.CALCULATORS,
                            onClick = { viewModel.setTab(AppTab.CALCULATORS) },
                            icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculators") },
                            label = { Text("Calculator", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            modifier = Modifier.testTag("nav_tab_calculators"),
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CryptoAccentGold,
                                selectedTextColor = CryptoAccentGold,
                                indicatorColor = CryptoAccentGold.copy(alpha = 0.2f)
                            )
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.DICTIONARY,
                            onClick = { viewModel.setTab(AppTab.DICTIONARY) },
                            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Dictionary") },
                            label = { Text("Dictionary", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            modifier = Modifier.testTag("nav_tab_dictionary"),
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CryptoAccentCyan,
                                selectedTextColor = CryptoAccentCyan,
                                indicatorColor = CryptoAccentCyan.copy(alpha = 0.2f)
                            )
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppTab.EDUCATION,
                            onClick = { viewModel.setTab(AppTab.EDUCATION) },
                            icon = { Icon(Icons.Default.School, contentDescription = "Learning Portal") },
                            label = { Text("Learning", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            modifier = Modifier.testTag("nav_tab_education"),
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CryptoGreen,
                                selectedTextColor = CryptoGreen,
                                indicatorColor = CryptoGreen.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Persistent Real-time Ticker Tape Bar (hidden in Chart tab for edge-to-edge chart space)
                if (!isChartTab) {
                    TickerTapeBar(
                        coins = uiState.coins,
                        isTvMode = uiState.isTvTickerMode,
                        isDarkTheme = uiState.isDarkTheme,
                        onToggleTvMode = { viewModel.toggleTvTickerMode() },
                        onCoinClick = { coin -> viewModel.openChartForCoin(coin) },
                        onSelectSymbol = { symbol -> viewModel.openChart(symbol) }
                    )

                    // Quick Access Hub Bar
                    QuickAccessHubBar(
                        currentTab = uiState.currentTab,
                        watchlistCount = uiState.coins.count { it.isFavorite },
                        onTabSelected = { viewModel.setTab(it) }
                    )
                }

                // Animated Screen Switching
                AnimatedContent(
                    targetState = uiState.currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_transition",
                    modifier = Modifier.weight(1f)
                ) { tab ->
                    when (tab) {
                        AppTab.MARKETS -> {
                            MarketOverviewScreen(
                                coins = uiState.coins,
                                metrics = uiState.metrics,
                                isTvMode = uiState.isTvOverviewMode,
                                isDarkTheme = uiState.isDarkTheme,
                                onToggleTvMode = { viewModel.toggleTvOverviewMode() },
                                onCoinClick = { coin -> viewModel.openChartForCoin(coin) },
                                onToggleFavorite = { coin -> viewModel.toggleFavorite(coin) },
                                onSelectSymbol = { symbol -> viewModel.openChart(symbol) }
                            )
                        }
                        AppTab.WATCHLIST -> {
                            WatchlistScreen(
                                coins = uiState.coins,
                                onCoinClick = { coin -> viewModel.openChartForCoin(coin) },
                                onChartClick = { coin -> viewModel.openChartForCoin(coin) },
                                onToggleFavorite = { coin -> viewModel.toggleFavorite(coin) },
                                onExploreCoins = { viewModel.setTab(AppTab.MARKETS) }
                            )
                        }
                        AppTab.DICTIONARY -> {
                            CryptoDictionaryScreen(
                                onAskAiAboutTerm = { term ->
                                    viewModel.openAiWithPrompt("Provide an in-depth quantitative and practical crypto breakdown of '$term' including real-world trade setups and tokenomic implications.")
                                }
                            )
                        }
                        AppTab.CALCULATORS -> {
                            CryptoCalculatorsScreen(coins = uiState.coins)
                        }
                        AppTab.EDUCATION -> {
                            CryptoEducationScreen()
                        }
                        AppTab.AI_HUB -> {
                            CryptoAiHubScreen(
                                coins = uiState.coins,
                                initialPrompt = uiState.aiPrompt
                            )
                        }
                        AppTab.HEATMAP -> {
                            CryptoHeatmapScreen(
                                coins = uiState.coins,
                                isTvMode = uiState.isTvHeatmapMode,
                                isDarkTheme = uiState.isDarkTheme,
                                onToggleTvMode = { viewModel.toggleTvHeatmapMode() },
                                onCoinClick = { coin -> viewModel.openChartForCoin(coin) },
                                onSelectSymbol = { symbol -> viewModel.openChart(symbol) }
                            )
                        }
                        AppTab.SCREENER -> {
                            CryptoScreenerScreen(
                                coins = uiState.coins,
                                isTvMode = uiState.isTvScreenerMode,
                                isDarkTheme = uiState.isDarkTheme,
                                searchQuery = uiState.searchQuery,
                                selectedCategory = uiState.selectedCategory,
                                selectedSortOption = uiState.selectedSortOption,
                                selectedColumn = uiState.selectedScreenerColumn,
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                onCategoryChange = { viewModel.setSelectedCategory(it) },
                                onSortOptionChange = { viewModel.setSelectedSortOption(it) },
                                onColumnChange = { viewModel.setSelectedScreenerColumn(it) },
                                onToggleTvMode = { viewModel.toggleTvScreenerMode() },
                                onCoinClick = { coin -> viewModel.openChartForCoin(coin) },
                                onToggleFavorite = { coin -> viewModel.toggleFavorite(coin) },
                                onSelectSymbol = { symbol -> viewModel.openChart(symbol) }
                            )
                        }
                        AppTab.ADVANCED_SCREENER -> {
                            CryptoAdvancedScreenerScreen(
                                isDarkTheme = uiState.isDarkTheme,
                                onSelectSymbol = { symbol -> viewModel.openChart(symbol) }
                            )
                        }
                        AppTab.CHART -> {
                            CryptoChartScreen(
                                symbol = uiState.selectedChartSymbol,
                                coins = uiState.coins,
                                isDarkTheme = uiState.isDarkTheme,
                                isFullscreen = uiState.isChartFullscreen,
                                onToggleFullscreen = { viewModel.toggleChartFullscreen() },
                                onSelectSymbol = { symbol -> viewModel.openChart(symbol) }
                            )
                        }
                        AppTab.ABOUT -> {
                            AboutUsScreen(
                                onReplayOnboarding = { viewModel.reopenOnboarding() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Detail & Interactive Chart Modal
    if (uiState.selectedCoin != null) {
        CryptoDetailModal(
            coin = uiState.selectedCoin,
            isDarkTheme = uiState.isDarkTheme,
            onDismiss = { viewModel.selectCoin(null) },
            onToggleFavorite = { coin -> viewModel.toggleFavorite(coin) },
            onOpenFullChart = { symbol -> viewModel.openChart(symbol) }
        )
    }
}

private data class QuickHubItem(
    val tab: AppTab,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val badge: String? = null
)

@Composable
fun QuickAccessHubBar(
    currentTab: AppTab,
    watchlistCount: Int,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember(watchlistCount) {
        listOf(
            QuickHubItem(AppTab.CHART, "Charts", Icons.Default.CandlestickChart),
            QuickHubItem(AppTab.MARKETS, "Markets", Icons.Default.CurrencyExchange),
            QuickHubItem(AppTab.CALCULATORS, "Calculator", Icons.Default.Calculate, "7"),
            QuickHubItem(AppTab.DICTIONARY, "Dictionary", Icons.AutoMirrored.Filled.MenuBook, "1k+"),
            QuickHubItem(AppTab.EDUCATION, "Learning", Icons.Default.School, "Academy"),
            QuickHubItem(AppTab.SCREENER, "Screener", Icons.Default.Tune),
            QuickHubItem(AppTab.ADVANCED_SCREENER, "Adv Screener", Icons.Default.FilterAlt),
            QuickHubItem(AppTab.HEATMAP, "Heatmap", Icons.Default.GridView),
            QuickHubItem(AppTab.WATCHLIST, "Watchlist", Icons.Default.Star, if (watchlistCount > 0) "$watchlistCount" else null),
            QuickHubItem(AppTab.AI_HUB, "AI Hub", Icons.Default.AutoAwesome, "AI"),
            QuickHubItem(AppTab.ABOUT, "About Us", Icons.Default.Info, "Free")
        )
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth()
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items, key = { it.tab.name }) { item ->
                val isSelected = currentTab == item.tab
                Surface(
                    color = if (isSelected) {
                        when (item.tab) {
                            AppTab.CALCULATORS -> CryptoAccentGold.copy(alpha = 0.2f)
                            AppTab.DICTIONARY -> CryptoAccentCyan.copy(alpha = 0.2f)
                            AppTab.EDUCATION -> CryptoGreen.copy(alpha = 0.2f)
                            AppTab.WATCHLIST -> CryptoAccentGold.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.primaryContainer
                        }
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    },
                    shape = RoundedCornerShape(20.dp),
                    border = if (isSelected) {
                        BorderStroke(
                            1.dp,
                            when (item.tab) {
                                AppTab.CALCULATORS -> CryptoAccentGold
                                AppTab.DICTIONARY -> CryptoAccentCyan
                                AppTab.EDUCATION -> CryptoGreen
                                AppTab.WATCHLIST -> CryptoAccentGold
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                    } else null,
                    modifier = Modifier
                        .clickable { onTabSelected(item.tab) }
                        .testTag("quick_tab_${item.tab.name.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (isSelected) {
                                when (item.tab) {
                                    AppTab.CALCULATORS -> CryptoAccentGold
                                    AppTab.DICTIONARY -> CryptoAccentCyan
                                    AppTab.EDUCATION -> CryptoGreen
                                    AppTab.WATCHLIST -> CryptoAccentGold
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                        if (item.badge != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (isSelected) {
                                    when (item.tab) {
                                        AppTab.CALCULATORS -> CryptoAccentGold
                                        AppTab.DICTIONARY -> CryptoAccentCyan
                                        AppTab.EDUCATION -> CryptoGreen
                                        AppTab.WATCHLIST -> CryptoAccentGold
                                        else -> MaterialTheme.colorScheme.primary
                                    }
                                } else {
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                },
                                shape = CircleShape
                            ) {
                                Text(
                                    text = item.badge,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
