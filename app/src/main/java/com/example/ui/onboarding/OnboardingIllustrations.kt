package com.example.ui.onboarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CryptoAccentCyan
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoRed

/**
 * High-fidelity vector illustration for Onboarding Page 1:
 * Real-time Multi-Asset Screener & Visual Heatmaps.
 */
@Composable
fun ScreenerOnboardingGraphic(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1424),
                        Color(0xFF070B14)
                    )
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        CryptoAccentCyan.copy(alpha = pulseAlpha),
                        Color(0xFF6366F1).copy(alpha = 0.3f),
                        CryptoGreen.copy(alpha = 0.2f)
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .padding(14.dp)
    ) {
        // Glowing background chart canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background subtle grid
            val gridStep = 32f
            var y = 0f
            while (y < h) {
                drawLine(
                    color = Color.White.copy(alpha = 0.04f),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f
                )
                y += gridStep
            }

            // Upward glowing trend curve
            val path = Path().apply {
                moveTo(0f, h * 0.85f)
                cubicTo(w * 0.25f, h * 0.8f, w * 0.4f, h * 0.5f, w * 0.65f, h * 0.55f)
                cubicTo(w * 0.8f, h * 0.6f, w * 0.9f, h * 0.25f, w, h * 0.15f)
            }
            drawPath(
                path = path,
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF00E5FF).copy(alpha = 0.3f), Color(0xFF10B981).copy(alpha = 0.9f))
                ),
                style = Stroke(width = 3.5f, cap = StrokeCap.Round)
            )
        }

        // Overlay Interactive Screener UI Mockup
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CryptoGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE SCREENER STREAM",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CryptoGreen,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    color = CryptoAccentCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, CryptoAccentCyan.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "500+ ASSETS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CryptoAccentCyan,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Screener Row 1 - Bitcoin
            ScreenerItemMockup(
                rank = "1",
                symbol = "BTC",
                name = "Bitcoin",
                price = "$67,820",
                change = "+4.2%",
                isPositive = true,
                badgeColor = Color(0xFFF59E0B)
            )

            // Screener Row 2 - Ethereum
            ScreenerItemMockup(
                rank = "2",
                symbol = "ETH",
                name = "Ethereum",
                price = "$3,540",
                change = "+7.8%",
                isPositive = true,
                badgeColor = Color(0xFF6366F1)
            )

            // Screener Row 3 - Solana
            ScreenerItemMockup(
                rank = "3",
                symbol = "SOL",
                name = "Solana",
                price = "$178.40",
                change = "+14.6%",
                isPositive = true,
                badgeColor = Color(0xFF14F195)
            )
        }
    }
}

@Composable
private fun ScreenerItemMockup(
    rank: String,
    symbol: String,
    name: String,
    price: String,
    change: String,
    isPositive: Boolean,
    badgeColor: Color
) {
    Surface(
        color = Color(0xFF131C31).copy(alpha = 0.85f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = rank,
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(14.dp)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(badgeColor.copy(alpha = 0.25f))
                        .border(1.dp, badgeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = symbol.take(1),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = symbol,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = name,
                        fontSize = 9.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = price,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = if (isPositive) CryptoGreen.copy(alpha = 0.2f) else CryptoRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isPositive) CryptoGreen.copy(alpha = 0.4f) else CryptoRed.copy(alpha = 0.4f)
                    )
                ) {
                    Text(
                        text = change,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPositive) CryptoGreen else CryptoRed,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * High-fidelity vector illustration for Onboarding Page 2:
 * Interactive TradingView Candlestick Charts & Technical Indicators.
 */
@Composable
fun ChartsOnboardingGraphic(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_chart")
    val glowOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0B1120),
                        Color(0xFF030712)
                    )
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        Color(0xFF10B981).copy(alpha = 0.6f),
                        Color(0xFF3B82F6).copy(alpha = 0.4f),
                        Color(0xFF8B5CF6).copy(alpha = 0.2f)
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chart Top Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "BTC/USDT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "4H",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CryptoAccentCyan
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("15m", "1H", "4H", "1D", "EMA").forEach { tf ->
                        Surface(
                            color = if (tf == "4H") Color(0xFF3B82F6).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(4.dp),
                            border = if (tf == "4H") BorderStroke(1.dp, Color(0xFF3B82F6)) else null
                        ) {
                            Text(
                                text = tf,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (tf == "4H") Color.White else Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Candlestick Drawing Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines
                    for (i in 1..4) {
                        val y = h * (i / 5f)
                        drawLine(
                            color = Color.White.copy(alpha = 0.05f),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                    }

                    // Candlestick pattern data: (openFrac, closeFrac, highFrac, lowFrac, isBullish)
                    val candles = listOf(
                        Triple(0.60f, 0.45f, 0.38f) to (0.65f to true),
                        Triple(0.46f, 0.52f, 0.40f) to (0.58f to false),
                        Triple(0.51f, 0.38f, 0.30f) to (0.55f to true),
                        Triple(0.39f, 0.42f, 0.34f) to (0.45f to false),
                        Triple(0.41f, 0.28f, 0.22f) to (0.43f to true),
                        Triple(0.29f, 0.35f, 0.25f) to (0.38f to false),
                        Triple(0.34f, 0.20f, 0.15f) to (0.36f to true),
                        Triple(0.21f, 0.14f, 0.10f) to (0.24f to true),
                        Triple(0.15f, 0.22f, 0.12f) to (0.26f to false),
                        Triple(0.21f, 0.10f, 0.06f) to (0.23f to true)
                    )

                    val candleWidth = w / (candles.size * 1.5f)
                    val spacing = w / candles.size

                    candles.forEachIndexed { i, candle ->
                        val openY = h * candle.first.first
                        val closeY = h * candle.first.second
                        val highY = h * candle.first.third
                        val lowY = h * candle.second.first
                        val isBullish = candle.second.second

                        val candleColor = if (isBullish) Color(0xFF10B981) else Color(0xFFEF4444)
                        val centerX = spacing * i + spacing / 2

                        // Draw Wick
                        drawLine(
                            color = candleColor,
                            start = Offset(centerX, highY),
                            end = Offset(centerX, lowY),
                            strokeWidth = 1.5f
                        )

                        // Draw Candle Body
                        val top = minOf(openY, closeY)
                        val bottom = maxOf(openY, closeY)
                        val candleH = maxOf(bottom - top, 4f)
                        drawRect(
                            color = candleColor,
                            topLeft = Offset(centerX - candleWidth / 2, top),
                            size = Size(candleWidth, candleH)
                        )
                    }

                    // EMA Trend Line
                    val emaPath = Path().apply {
                        moveTo(0f, h * 0.62f)
                        cubicTo(w * 0.3f, h * 0.48f, w * 0.6f, h * 0.32f, w, h * 0.12f)
                    }
                    drawPath(
                        path = emaPath,
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF3B82F6), Color(0xFF00E5FF))
                        ),
                        style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                    )

                    // Crosshair at current price
                    val crosshairY = h * 0.12f
                    drawLine(
                        color = Color(0xFF00E5FF).copy(alpha = 0.6f),
                        start = Offset(0f, crosshairY),
                        end = Offset(w, crosshairY),
                        strokeWidth = 1f,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                    )
                }

                // Price Tag Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp)
                ) {
                    Surface(
                        color = Color(0xFF10B981),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "$68,420.50",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * High-fidelity vector illustration for Onboarding Page 3:
 * Zero Data Tracking & Offline Financial Tools Suite.
 */
@Composable
fun PrivacyAndToolsOnboardingGraphic(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_shield")
    val shieldScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shieldScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0C1929),
                        Color(0xFF060B14)
                    )
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        CryptoAccentGold.copy(alpha = 0.6f),
                        Color(0xFF10B981).copy(alpha = 0.4f),
                        CryptoAccentCyan.copy(alpha = 0.4f)
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Badge header
            Surface(
                color = CryptoGreen.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, CryptoGreen.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = CryptoGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "100% PRIVATE • ZERO DATA TRACKING",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CryptoGreen,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            // Central Shield & Encrypted Vault Symbol
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF10B981).copy(alpha = 0.25f),
                                Color(0xFF00E5FF).copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.linearGradient(
                            listOf(CryptoGreen, CryptoAccentCyan)
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Zero Tracking Shield",
                    tint = CryptoAccentCyan,
                    modifier = Modifier.size(42.dp)
                )
            }

            // 3 Feature Pill Highlights
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    FeaturePill(
                        title = "Zero Telemetry",
                        subtitle = "No tracking or ads",
                        accentColor = CryptoGreen
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    FeaturePill(
                        title = "7 Calculators",
                        subtitle = "Offline DCA & APY",
                        accentColor = CryptoAccentGold
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    FeaturePill(
                        title = "100% Free",
                        subtitle = "Open collaboration",
                        accentColor = CryptoAccentCyan
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(
    title: String,
    subtitle: String,
    accentColor: Color
) {
    Surface(
        color = Color(0xFF121B2F),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}
