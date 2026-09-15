package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCoin
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoGreenBg
import com.example.ui.theme.CryptoRed
import com.example.ui.theme.CryptoRedBg
import com.example.ui.tradingview.TradingViewHtml
import com.example.ui.tradingview.TradingViewWidgetView

@Composable
fun TickerTapeBar(
    coins: List<CryptoCoin>,
    isTvMode: Boolean,
    isDarkTheme: Boolean,
    onToggleTvMode: () -> Unit,
    onCoinClick: (CryptoCoin) -> Unit,
    modifier: Modifier = Modifier,
    onSelectSymbol: ((String) -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ticker_tape_bar"),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        if (isTvMode) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                TradingViewWidgetView(
                    htmlContent = TradingViewHtml.getTickerTapeHtml(isDark = isDarkTheme),
                    onFallbackNative = onToggleTvMode,
                    onSymbolSelected = onSelectSymbol,
                    modifier = Modifier.fillMaxWidth()
                )
                IconButton(
                    onClick = onToggleTvMode,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .testTag("toggle_ticker_mode_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch to Native Ticker",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Live Pulse Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 8.dp)
                        .background(
                            color = CryptoGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CryptoGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "LIVE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CryptoGreen
                    )
                }

                // Horizontal Scrollable Symbols Strip
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Filter or prioritize the key prompt symbols: BTC, ETH, SOL, XRP, USDT.D, ZEC, DOGE, NEAR, BNB, AVAX
                    val tapeCoins = coins.take(12)
                    tapeCoins.forEach { coin ->
                        TickerTapeItem(
                            coin = coin,
                            onClick = { onCoinClick(coin) }
                        )
                    }
                }

                // Switch to TV Widget Mode button
                IconButton(
                    onClick = onToggleTvMode,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(36.dp)
                        .testTag("toggle_ticker_mode_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "Switch to TradingView Ticker Widget",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TickerTapeItem(
    coin: CryptoCoin,
    onClick: () -> Unit
) {
    val isPositive = coin.change24h >= 0
    val targetBg = when {
        coin.isFlashUp -> CryptoGreen.copy(alpha = 0.3f)
        coin.isFlashDown -> CryptoRed.copy(alpha = 0.3f)
        else -> Color.Transparent
    }
    val animatedBg by animateColorAsState(
        targetValue = targetBg,
        animationSpec = tween(durationMillis = 350),
        label = "flashBg"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(animatedBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 3.dp)
            .testTag("ticker_item_${coin.symbol}")
    ) {
        // Pure Vector Coin Badge
        CryptoCoinVectorBadge(
            symbol = coin.symbol,
            size = 18.dp
        )

        Spacer(modifier = Modifier.width(6.dp))

        Column {
            Text(
                text = coin.symbol,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = coin.formattedPrice,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (isPositive) CryptoGreenBg else CryptoRedBg)
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Text(
                text = coin.formattedChange24h,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isPositive) CryptoGreen else CryptoRed
            )
        }
    }
}
