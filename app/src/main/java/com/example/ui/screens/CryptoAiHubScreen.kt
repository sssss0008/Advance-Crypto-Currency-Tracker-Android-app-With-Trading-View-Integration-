package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.GeminiCryptoService
import com.example.data.model.CryptoCoin
import com.example.ui.components.CryptoCoinVectorBadge
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.CryptoAccentCyan
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoPrimary
import com.example.ui.theme.CryptoRed
import kotlinx.coroutines.launch

data class QuickAiPromptItem(
    val label: String,
    val prompt: String,
    val icon: ImageVector
)

@Composable
fun CryptoAiHubScreen(
    coins: List<CryptoCoin>,
    initialPrompt: String? = null
) {
    var queryText by remember { mutableStateOf(initialPrompt ?: "") }
    var aiOutput by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val isLiveKey = remember { GeminiCryptoService.isApiKeyConfigured }

    val defaultQuickPrompts = remember {
        listOf(
            QuickAiPromptItem(
                label = "Market Sentiment Radar",
                prompt = "Provide a real-time crypto market sentiment radar, macro regimes, and BTC dominance outlook.",
                icon = Icons.Default.Public
            ),
            QuickAiPromptItem(
                label = "Bitcoin Halving Cycle",
                prompt = "Analyze Bitcoin's current market structure relative to previous post-halving bull cycles.",
                icon = Icons.Default.CurrencyBitcoin
            ),
            QuickAiPromptItem(
                label = "Ethereum L2 Scaling & Gas",
                prompt = "Evaluate Ethereum Layer 2 adoption, blob economics, and ETH supply burn dynamics.",
                icon = Icons.Default.Layers
            ),
            QuickAiPromptItem(
                label = "Solana Throughput & Catalysts",
                prompt = "Break down Solana DEX volume, Firedancer validator upgrade, and ecosystem momentum.",
                icon = Icons.Default.Speed
            ),
            QuickAiPromptItem(
                label = "Risk & Liquidation Zones",
                prompt = "What are the biggest macro risks and liquidation cascade zones in the crypto market right now?",
                icon = Icons.Default.Warning
            ),
            QuickAiPromptItem(
                label = "DeFi Yield & Staking Regimes",
                prompt = "Compare real yields across Ethereum staking, Solana staking, and top lending protocols.",
                icon = Icons.Default.AccountBalance
            )
        )
    }

    LaunchedEffect(initialPrompt) {
        if (!initialPrompt.isNullOrBlank()) {
            queryText = initialPrompt
            isLoading = true
            scope.launch {
                aiOutput = GeminiCryptoService.queryGemini(initialPrompt)
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("crypto_ai_hub_screen")
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AI Market Intelligence",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Powered by Gemini 3.5 Flash",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                color = if (isLiveKey) CryptoGreen.copy(alpha = 0.15f) else CryptoAccentGold.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (isLiveKey) CryptoGreen else CryptoAccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isLiveKey) "Gemini Live" else "Smart AI Ready",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isLiveKey) CryptoGreen else CryptoAccentGold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Preset Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(defaultQuickPrompts) { item ->
                GlassmorphicCard(
                    shape = RoundedCornerShape(12.dp),
                    surfaceAlpha = 0.60f,
                    borderAlpha = 0.20f,
                    accentGlow = MaterialTheme.colorScheme.primary,
                    onClick = {
                        queryText = item.prompt
                        isLoading = true
                        scope.launch {
                            aiOutput = GeminiCryptoService.queryGemini(item.prompt)
                            isLoading = false
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Query Bar
        OutlinedTextField(
            value = queryText,
            onValueChange = { queryText = it },
            placeholder = { Text("Ask Gemini about coins, technical levels, DeFi...") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ai_prompt_input"),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (queryText.isNotBlank()) {
                            isLoading = true
                            scope.launch {
                                aiOutput = GeminiCryptoService.queryGemini(queryText)
                                isLoading = false
                            }
                        }
                    },
                    enabled = queryText.isNotBlank() && !isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (queryText.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Analyzing on-chain metrics & market structure...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (aiOutput != null) {
                GlassmorphicCard(
                    shape = RoundedCornerShape(18.dp),
                    surfaceAlpha = 0.85f,
                    borderAlpha = 0.35f,
                    accentGlow = CryptoAccentCyan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_response_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI Quantitative Synthesis",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (queryText.isNotBlank()) {
                                        isLoading = true
                                        scope.launch {
                                            aiOutput = GeminiCryptoService.queryGemini(queryText)
                                            isLoading = false
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Regenerate", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = aiOutput!!,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = CryptoAccentGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Educational analysis only. Crypto markets carry high risk. DYOR.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            } else {
                // Empty state with coin quick analysis buttons
                GlassmorphicCard(
                    shape = RoundedCornerShape(20.dp),
                    surfaceAlpha = 0.55f,
                    borderAlpha = 0.20f,
                    accentGlow = CryptoPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Instant 1-Tap Coin Analysis",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Select any asset below to trigger instant AI technical & fundamental evaluation:",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                                coins.take(3).forEach { coin ->
                                    val isPositive = coin.change24h >= 0
                                    GlassmorphicCard(
                                        shape = RoundedCornerShape(12.dp),
                                        surfaceAlpha = 0.70f,
                                        borderAlpha = 0.20f,
                                        accentGlow = if (isPositive) CryptoGreen else CryptoRed,
                                        onClick = {
                                            queryText = "Analyze ${coin.name} (${coin.symbol})"
                                            isLoading = true
                                            scope.launch {
                                                aiOutput = GeminiCryptoService.analyzeCoin(coin)
                                                isLoading = false
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 8.dp, vertical = 10.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CryptoCoinVectorBadge(symbol = coin.symbol, size = 26.dp)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = coin.symbol,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "$${String.format("%.2f", coin.priceUsd)}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isPositive) CryptoGreen else CryptoRed
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
}
