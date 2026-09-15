package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCoin
import com.example.ui.components.CryptoCoinVectorBadge
import com.example.ui.theme.CryptoAccentCyan
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoPrimary

data class KeypadCoinOption(
    val symbol: String,
    val name: String,
    val priceUsd: Double
)

@Composable
fun CryptoKeypadCalculator(
    coins: List<CryptoCoin>,
    modifier: Modifier = Modifier
) {
    val defaultOptions = listOf(
        KeypadCoinOption("BTC", "Bitcoin", coins.find { it.symbol == "BTC" }?.priceUsd ?: 68450.0),
        KeypadCoinOption("ETH", "Ethereum", coins.find { it.symbol == "ETH" }?.priceUsd ?: 3540.0),
        KeypadCoinOption("SOL", "Solana", coins.find { it.symbol == "SOL" }?.priceUsd ?: 148.0),
        KeypadCoinOption("BNB", "BNB", coins.find { it.symbol == "BNB" }?.priceUsd ?: 590.0),
        KeypadCoinOption("XRP", "XRP", coins.find { it.symbol == "XRP" }?.priceUsd ?: 0.58),
        KeypadCoinOption("DOGE", "Dogecoin", coins.find { it.symbol == "DOGE" }?.priceUsd ?: 0.12),
        KeypadCoinOption("ADA", "Cardano", coins.find { it.symbol == "ADA" }?.priceUsd ?: 0.46),
        KeypadCoinOption("AVAX", "Avalanche", coins.find { it.symbol == "AVAX" }?.priceUsd ?: 27.5),
        KeypadCoinOption("SUI", "Sui", coins.find { it.symbol == "SUI" }?.priceUsd ?: 1.85),
        KeypadCoinOption("LINK", "Chainlink", coins.find { it.symbol == "LINK" }?.priceUsd ?: 12.4)
    )

    val options = if (coins.isNotEmpty()) {
        coins.map { KeypadCoinOption(it.symbol, it.name, it.priceUsd) }
    } else {
        defaultOptions
    }

    var selectedCoin by remember(coins) { mutableStateOf(options.first()) }
    var isCryptoToUsd by remember { mutableStateOf(true) }
    var expression by remember { mutableStateOf("1") }

    // Evaluate expression safely
    fun evaluateExpression(expr: String): Double {
        return try {
            val sanitized = expr.replace("×", "*").replace("÷", "/")
            val tokens = sanitized.split("(?<=[-+*/])|(?=[-+*/])".toRegex()).map { it.trim() }.filter { it.isNotEmpty() }
            if (tokens.isEmpty()) return 0.0

            var result = tokens[0].toDoubleOrNull() ?: 0.0
            var i = 1
            while (i < tokens.size - 1) {
                val op = tokens[i]
                val nextVal = tokens[i + 1].toDoubleOrNull() ?: 0.0
                when (op) {
                    "+" -> result += nextVal
                    "-" -> result -= nextVal
                    "*" -> result *= nextVal
                    "/" -> if (nextVal != 0.0) result /= nextVal
                }
                i += 2
            }
            result
        } catch (_: Exception) {
            0.0
        }
    }

    val evaluatedInput = remember(expression) { evaluateExpression(expression) }
    val coinPrice = selectedCoin.priceUsd.coerceAtLeast(0.0000001)

    // Calculation results
    val (cryptoAmount, usdAmount) = if (isCryptoToUsd) {
        evaluatedInput to (evaluatedInput * coinPrice)
    } else {
        (evaluatedInput / coinPrice) to evaluatedInput
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("crypto_keypad_calculator")
    ) {
        // Coin Selector Carousel
        Text(
            text = "SELECT ASSET FOR LIVE PRICING",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(options.take(12)) { coin ->
                val isSelected = coin.symbol == selectedCoin.symbol
                Surface(
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                    modifier = Modifier
                        .clickable { selectedCoin = coin }
                        .testTag("keypad_coin_${coin.symbol.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CryptoCoinVectorBadge(symbol = coin.symbol, size = 18.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = coin.symbol,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$${String.format("%,.0f", coin.priceUsd)}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Dual Converter Display Screen
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Top Row: Primary Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isCryptoToUsd) {
                            CryptoCoinVectorBadge(symbol = selectedCoin.symbol, size = 26.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedCoin.symbol,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Surface(
                                color = CryptoAccentGold.copy(alpha = 0.2f),
                                shape = CircleShape,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("$", fontWeight = FontWeight.Bold, color = CryptoAccentGold, fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("USD", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Large Expression display
                    Text(
                        text = if (expression.isEmpty()) "0" else expression,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Swap Direction Button Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { isCryptoToUsd = !isCryptoToUsd }
                            .testTag("keypad_swap_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap conversion",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isCryptoToUsd) "Crypto → USD" else "USD → Crypto",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Row: Converted Result
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isCryptoToUsd) {
                            Surface(
                                color = CryptoGreen.copy(alpha = 0.2f),
                                shape = CircleShape,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("$", fontWeight = FontWeight.Bold, color = CryptoGreen, fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("USD", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        } else {
                            CryptoCoinVectorBadge(symbol = selectedCoin.symbol, size = 26.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedCoin.symbol,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Converted value
                    val formattedResult = if (isCryptoToUsd) {
                        String.format("$%,.2f", usdAmount)
                    } else {
                        String.format("%,.6f %s", cryptoAmount, selectedCoin.symbol)
                    }

                    Text(
                        text = formattedResult,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = CryptoGreen,
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Multi-currency / Sats secondary breakdown row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "€${String.format("%,.2f", usdAmount * 0.92)}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "£${String.format("%,.2f", usdAmount * 0.79)}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "¥${String.format("%,.0f", usdAmount * 155.0)}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (selectedCoin.symbol == "BTC") {
                        Text(
                            text = "${String.format("%,.0f", cryptoAmount * 100000000.0)} Sats",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CryptoAccentGold
                        )
                    } else {
                        Text(
                            text = "Rate: $${String.format("%,.2f", coinPrice)}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Preset Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val presets = if (isCryptoToUsd) {
                listOf("0.1", "0.25", "0.5", "1", "2.5", "5", "10")
            } else {
                listOf("100", "250", "500", "1000", "2500", "5000", "10000")
            }
            items(presets) { preset ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { expression = preset }
                ) {
                    Text(
                        text = if (isCryptoToUsd) "$preset ${selectedCoin.symbol}" else "$$preset",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive 4x5 Keypad Grid
        val keypadButtons = listOf(
            listOf("C", "÷", "×", "⌫"),
            listOf("7", "8", "9", "−"),
            listOf("4", "5", "6", "+"),
            listOf("1", "2", "3", "%"),
            listOf("00", "0", ".", "=")
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            for (row in keypadButtons) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (btn in row) {
                        val isOperator = btn in listOf("÷", "×", "−", "+", "=")
                        val isSpecial = btn in listOf("C", "⌫", "%")

                        val btnColor = when {
                            btn == "=" -> MaterialTheme.colorScheme.primary
                            isOperator -> MaterialTheme.colorScheme.primaryContainer
                            isSpecial -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surface
                        }

                        val textColor = when {
                            btn == "=" -> MaterialTheme.colorScheme.onPrimary
                            isOperator -> MaterialTheme.colorScheme.onPrimaryContainer
                            isSpecial -> MaterialTheme.colorScheme.onSurfaceVariant
                            else -> MaterialTheme.colorScheme.onSurface
                        }

                        Surface(
                            color = btnColor,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    when (btn) {
                                        "C" -> expression = "0"
                                        "⌫" -> {
                                            expression = if (expression.length <= 1) "0" else expression.dropLast(1)
                                        }
                                        "=" -> {
                                            val evaluated = evaluateExpression(expression)
                                            expression = if (evaluated % 1.0 == 0.0) {
                                                evaluated.toLong().toString()
                                            } else {
                                                String.format("%.4f", evaluated).trimEnd('0').trimEnd('.')
                                            }
                                        }
                                        "%" -> {
                                            val evaluated = evaluateExpression(expression)
                                            val pct = evaluated / 100.0
                                            expression = String.format("%.6f", pct).trimEnd('0').trimEnd('.')
                                        }
                                        "+", "−", "×", "÷" -> {
                                            if (expression.isNotEmpty() && expression.last() !in listOf('+', '-', '×', '÷', '*')) {
                                                expression += btn
                                            }
                                        }
                                        "." -> {
                                            if (!expression.endsWith(".")) {
                                                expression += "."
                                            }
                                        }
                                        else -> {
                                            if (expression == "0") {
                                                expression = btn
                                            } else {
                                                expression += btn
                                            }
                                        }
                                    }
                                }
                                .testTag("keypad_btn_$btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (btn == "⌫") {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                                        contentDescription = "Backspace",
                                        tint = textColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(
                                        text = btn,
                                        fontSize = if (btn.length > 1) 16.sp else 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
