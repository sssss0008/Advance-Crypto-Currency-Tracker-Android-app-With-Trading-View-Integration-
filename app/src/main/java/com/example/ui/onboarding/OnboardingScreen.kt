package com.example.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CryptoAccentCyan
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val title: String,
    val subtitle: String,
    val description: String,
    val features: List<String>,
    val graphicType: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        OnboardingPageData(
            title = "Real-Time Market Screener",
            subtitle = "Live Multi-Asset Intelligence",
            description = "Track hundreds of cryptocurrencies with real-time updates. Analyze top gainers, losers, market cap leaders, 24-hour volume, and interactive visual heatmaps with zero latency.",
            features = listOf(
                "Filter by Gainers, Losers, Market Cap & Volume",
                "Color-coded visual heatmaps with market capitalization sizing",
                "Instant search across Bitcoin, Ethereum, Solana, and 500+ coins"
            ),
            graphicType = 1
        ),
        OnboardingPageData(
            title = "TradingView Charts",
            subtitle = "Pro-Grade Technical Indicators",
            description = "Explore full interactive candlestick charts powered by TradingView. Switch seamlessly across multiple timeframes, apply RSI, MACD, Moving Averages, and draw trend lines with precision.",
            features = listOf(
                "Real-time candlestick charts with full crosshair inspection",
                "Multiple timeframes from 1-minute scalping to weekly macro trends",
                "Fluid landscape and portrait fullscreen charting experiences"
            ),
            graphicType = 2
        ),
        OnboardingPageData(
            title = "Zero Tracking & Smart Tools",
            subtitle = "100% Private, Free & Offline-Ready",
            description = "We value your freedom and security. Your financial activity is never tracked, stored, or sold. Enjoy 7 built-in offline calculators, a 1,000+ word crypto encyclopedia, and complete privacy.",
            features = listOf(
                "100% Client-side privacy: Zero analytics, telemetry, or user tracking",
                "7 Offline financial tools: DCA, Staking APY, Profit/Loss, and Fees",
                "Completely Free Community Project: Open for developer contribution"
            ),
            graphicType = 3
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF070D18),
                        Color(0xFF0B1426),
                        Color(0xFF030712)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("onboarding_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar with App Branding & Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CryptoAccentCyan)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CRYPTO SCREENER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CryptoAccentCyan,
                        letterSpacing = 1.sp
                    )
                }

                TextButton(
                    onClick = onFinishOnboarding,
                    modifier = Modifier.testTag("onboarding_skip_button")
                ) {
                    Text(
                        text = "Skip",
                        color = Color.White.copy(alpha = 0.65f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIndex ->
                val page = pages[pageIndex]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Graphic Illustration
                    when (page.graphicType) {
                        1 -> ScreenerOnboardingGraphic(modifier = Modifier.testTag("onboarding_graphic_1"))
                        2 -> ChartsOnboardingGraphic(modifier = Modifier.testTag("onboarding_graphic_2"))
                        else -> PrivacyAndToolsOnboardingGraphic(modifier = Modifier.testTag("onboarding_graphic_3"))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Subtitle Badge
                    Surface(
                        color = when (pageIndex) {
                            0 -> CryptoAccentCyan.copy(alpha = 0.15f)
                            1 -> Color(0xFF6366F1).copy(alpha = 0.2f)
                            else -> CryptoGreen.copy(alpha = 0.15f)
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(
                            1.dp,
                            when (pageIndex) {
                                0 -> CryptoAccentCyan.copy(alpha = 0.4f)
                                1 -> Color(0xFF6366F1).copy(alpha = 0.5f)
                                else -> CryptoGreen.copy(alpha = 0.5f)
                            }
                        )
                    ) {
                        Text(
                            text = page.subtitle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (pageIndex) {
                                0 -> CryptoAccentCyan
                                1 -> Color(0xFF818CF8)
                                else -> CryptoGreen
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Page Title
                    Text(
                        text = page.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Description
                    Text(
                        text = page.description,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Feature highlights list
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        page.features.forEach { feature ->
                            Surface(
                                color = Color(0xFF101827).copy(alpha = 0.8f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.07f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(CryptoGreen.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = CryptoGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = feature,
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Bottom Navigation Area: Page Indicators & Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dot Indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (isSelected) 24.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) CryptoAccentCyan else Color.White.copy(alpha = 0.25f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Navigation Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pagerState.currentPage > 0) {
                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("onboarding_back_button")
                        ) {
                            Text(
                                text = "Back",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    val isLastPage = pagerState.currentPage == pages.size - 1
                    Button(
                        onClick = {
                            if (isLastPage) {
                                onFinishOnboarding()
                            } else {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLastPage) CryptoGreen else CryptoAccentCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(if (pagerState.currentPage > 0) 1.5f else 1f)
                            .height(50.dp)
                            .testTag(if (isLastPage) "onboarding_get_started_button" else "onboarding_next_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (isLastPage) "Get Started" else "Next",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
