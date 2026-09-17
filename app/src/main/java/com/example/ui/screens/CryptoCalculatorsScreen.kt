package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CryptoCoin
import com.example.ui.components.CryptoCoinVectorBadge
import com.example.ui.theme.CryptoAccentCyan
import com.example.ui.theme.CryptoAccentGold
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoPrimary
import com.example.ui.theme.CryptoRed
import kotlin.math.pow
import kotlin.math.sqrt

enum class CalculatorType(val displayName: String, val icon: ImageVector) {
    KEYPAD("Keypad & Converter", Icons.Default.Calculate),
    PNL("Profit / Loss & ROI", Icons.Default.TrendingUp),
    DCA("DCA Simulator", Icons.Default.Savings),
    STAKING("Staking Yield", Icons.Default.AccountBalance),
    LEVERAGE("Futures & Liquidation", Icons.Default.Bolt),
    IMPERMANENT_LOSS("Impermanent Loss", Icons.Default.SwapHoriz),
    MINING("Mining Economics", Icons.Default.PrecisionManufacturing)
}

@Composable
fun CryptoCalculatorsScreen(
    coins: List<CryptoCoin> = emptyList()
) {
    var selectedCalc by remember { mutableStateOf(CalculatorType.KEYPAD) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("crypto_calculators_screen")
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Crypto Calculators",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Interactive Keypad & 6 Financial Simulators",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                color = CryptoAccentGold.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = CryptoAccentGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Real-Time Rates",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CryptoAccentGold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Calculator Selector Carousel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(CalculatorType.entries) { calc ->
                val isSelected = calc == selectedCalc
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCalc = calc },
                    leadingIcon = {
                        Icon(
                            imageVector = calc.icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            text = calc.displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selected Calculator Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when (selectedCalc) {
                CalculatorType.KEYPAD -> CryptoKeypadCalculator(coins = coins)
                CalculatorType.PNL -> PnlCalculatorView(coins = coins)
                CalculatorType.DCA -> DcaCalculatorView(coins = coins)
                CalculatorType.STAKING -> StakingCalculatorView(coins = coins)
                CalculatorType.LEVERAGE -> LeverageCalculatorView(coins = coins)
                CalculatorType.IMPERMANENT_LOSS -> ImpermanentLossCalculatorView()
                CalculatorType.MINING -> MiningCalculatorView()
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------
// PNL CALCULATOR WITH LIVE COIN SELECTION & TARGET MULTIPLIERS
// -------------------------------------------------------------
@Composable
private fun PnlCalculatorView(coins: List<CryptoCoin>) {
    val defaultBtc = coins.find { it.symbol == "BTC" }
    var buyPriceText by remember(defaultBtc) {
        mutableStateOf(if (defaultBtc != null) String.format("%.0f", defaultBtc.priceUsd) else "65000")
    }
    var sellPriceText by remember(defaultBtc) {
        mutableStateOf(if (defaultBtc != null) String.format("%.0f", defaultBtc.priceUsd * 1.15) else "75000")
    }
    var investmentText by remember { mutableStateOf("1000") }
    var feePercentText by remember { mutableStateOf("0.1") }

    val buyPrice = buyPriceText.toDoubleOrNull() ?: 0.0
    val sellPrice = sellPriceText.toDoubleOrNull() ?: 0.0
    val investment = investmentText.toDoubleOrNull() ?: 0.0
    val feePercent = feePercentText.toDoubleOrNull() ?: 0.0

    val coinAmount = if (buyPrice > 0) investment / buyPrice else 0.0
    val totalExitBeforeFee = coinAmount * sellPrice
    val buyFee = investment * (feePercent / 100.0)
    val sellFee = totalExitBeforeFee * (feePercent / 100.0)
    val totalFees = buyFee + sellFee
    val netExitValue = (totalExitBeforeFee - sellFee).coerceAtLeast(0.0)
    val netProfit = if (investment > 0) (netExitValue - investment) else 0.0
    val roiPercent = if (investment > 0) (netProfit / investment) * 100.0 else 0.0
    val breakEvenPrice = if (buyPrice > 0) buyPrice * (1 + (feePercent / 100.0) * 2) else 0.0

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Coin presets
        if (coins.isNotEmpty()) {
            Text("QUICK FILL COIN PRICE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(coins.take(8)) { coin ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable {
                            buyPriceText = String.format("%.2f", coin.priceUsd)
                            sellPriceText = String.format("%.2f", coin.priceUsd * 1.25)
                        }
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            CryptoCoinVectorBadge(symbol = coin.symbol, size = 14.dp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${coin.symbol} $${String.format("%,.0f", coin.priceUsd)}", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        CalcInputField(label = "Buy Price ($)", value = buyPriceText, onValueChange = { buyPriceText = it })

        // Quick Exit Target Multiplier Buttons
        Text("SET TARGET EXIT PRICE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val targets = listOf(
                "+5%" to 1.05,
                "+10%" to 1.10,
                "+25%" to 1.25,
                "+50%" to 1.50,
                "+100% (2x)" to 2.0,
                "+400% (5x)" to 5.0,
                "+900% (10x)" to 10.0,
                "-10% Stop" to 0.90
            )
            items(targets) { (label, mult) ->
                Surface(
                    color = if (mult >= 1.0) CryptoGreen.copy(alpha = 0.15f) else CryptoRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                        val base = buyPriceText.toDoubleOrNull() ?: 60000.0
                        sellPriceText = String.format("%.2f", base * mult)
                    }
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (mult >= 1.0) CryptoGreen else CryptoRed,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        CalcInputField(label = "Sell / Target Price ($)", value = sellPriceText, onValueChange = { sellPriceText = it })

        // Quick Investment Chips
        Text("INVESTMENT AMOUNT ($)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(listOf("100", "250", "500", "1000", "2500", "5000", "10000")) { amt ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { investmentText = amt }
                ) {
                    Text("$$amt", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
        }

        CalcInputField(label = "Total Investment ($)", value = investmentText, onValueChange = { investmentText = it })
        CalcInputField(label = "Trading Fee per Trade (%)", value = feePercentText, onValueChange = { feePercentText = it })

        Spacer(modifier = Modifier.height(4.dp))

        // Results Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Calculation Results", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                ResultRow(
                    label = "Net Profit / Loss",
                    value = String.format("%+$,.2f", netProfit),
                    valueColor = if (netProfit >= 0) CryptoGreen else CryptoRed,
                    isBold = true
                )

                ResultRow(
                    label = "Return on Investment (ROI)",
                    value = String.format("%+.2f%%", roiPercent),
                    valueColor = if (roiPercent >= 0) CryptoGreen else CryptoRed,
                    isBold = true
                )

                ResultRow(label = "Total Exit Payout", value = String.format("$,.2f", netExitValue))
                ResultRow(label = "Estimated Coins Acquired", value = String.format("%.6f", coinAmount))
                ResultRow(label = "Total Exchange Fees Paid", value = String.format("$,.2f", totalFees))
                ResultRow(label = "Break-Even Price", value = String.format("$,.2f", breakEvenPrice))
            }
        }
    }
}

// -------------------------------------------------------------
// DCA SIMULATOR WITH FREQUENCIES & COIN INTEGRATION
// -------------------------------------------------------------
@Composable
private fun DcaCalculatorView(coins: List<CryptoCoin>) {
    var recurringAmountText by remember { mutableStateOf("100") }
    var frequencyDays by remember { mutableIntStateOf(7) } // 7 = weekly, 30 = monthly, 14 = biweekly
    var durationMonthsText by remember { mutableStateOf("12") }
    var expectedAnnualGrowthText by remember { mutableStateOf("35") }

    val recurringAmount = recurringAmountText.toDoubleOrNull() ?: 0.0
    val durationMonths = durationMonthsText.toIntOrNull() ?: 12
    val expectedAnnualGrowth = expectedAnnualGrowthText.toDoubleOrNull() ?: 35.0

    val intervals = when (frequencyDays) {
        1 -> durationMonths * 30
        7 -> (durationMonths * 4.33).toInt()
        14 -> (durationMonths * 2.16).toInt()
        else -> durationMonths
    }
    val totalInvested = recurringAmount * intervals
    val monthlyRate = (1 + expectedAnnualGrowth / 100.0).pow(1.0 / 12.0) - 1.0

    var portfolioValue = 0.0
    val periodRate = when (frequencyDays) {
        1 -> monthlyRate / 30.0
        7 -> monthlyRate / 4.33
        14 -> monthlyRate / 2.16
        else -> monthlyRate
    }
    for (i in 1..intervals) {
        portfolioValue = (portfolioValue + recurringAmount) * (1 + periodRate)
    }

    val profit = portfolioValue - totalInvested
    val roi = if (totalInvested > 0) (profit / totalInvested) * 100.0 else 0.0

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("PURCHASE FREQUENCY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val freqs = listOf("Daily" to 1, "Weekly" to 7, "Bi-Weekly" to 14, "Monthly" to 30)
            for ((label, days) in freqs) {
                FilterChip(
                    selected = frequencyDays == days,
                    onClick = { frequencyDays = days },
                    label = { Text(label, fontSize = 11.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick amount chips
        Text("RECURRING AMOUNT ($)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(listOf("25", "50", "100", "200", "500", "1000")) { amt ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { recurringAmountText = amt }
                ) {
                    Text("$$amt", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                }
            }
        }

        CalcInputField(label = "Recurring Investment Amount ($)", value = recurringAmountText, onValueChange = { recurringAmountText = it })

        // Duration Chips
        Text("PLAN DURATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val durations = listOf("3 Mo" to "3", "6 Mo" to "6", "1 Year" to "12", "2 Years" to "24", "3 Years" to "36", "5 Years" to "60")
            items(durations) { (label, mo) ->
                Surface(
                    color = if (durationMonthsText == mo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { durationMonthsText = mo }
                ) {
                    Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                }
            }
        }

        CalcInputField(label = "Duration in Months", value = durationMonthsText, onValueChange = { durationMonthsText = it })
        CalcInputField(label = "Projected Annual Asset Return (%)", value = expectedAnnualGrowthText, onValueChange = { expectedAnnualGrowthText = it })

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("DCA Simulation Results", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                ResultRow(label = "Total Cash Invested", value = String.format("$,.2f", totalInvested))
                ResultRow(label = "Estimated Portfolio Value", value = String.format("$,.2f", portfolioValue), valueColor = CryptoAccentCyan, isBold = true)
                ResultRow(label = "Projected Net Profit", value = String.format("%+$,.2f", profit), valueColor = CryptoGreen, isBold = true)
                ResultRow(label = "Estimated Total ROI", value = String.format("%+.2f%%", roi), valueColor = CryptoGreen)
                ResultRow(label = "Total Purchase Intervals", value = "$intervals purchases")
            }
        }
    }
}

// -------------------------------------------------------------
// STAKING YIELD & APY CALCULATOR
// -------------------------------------------------------------
@Composable
private fun StakingCalculatorView(coins: List<CryptoCoin>) {
    var stakeAmountText by remember { mutableStateOf("10") }
    var apyPercentText by remember { mutableStateOf("7.2") }
    var tokenPriceText by remember { mutableStateOf("145") }
    var durationMonthsText by remember { mutableStateOf("12") }

    val stakeAmount = stakeAmountText.toDoubleOrNull() ?: 0.0
    val apy = apyPercentText.toDoubleOrNull() ?: 0.0
    val tokenPrice = tokenPriceText.toDoubleOrNull() ?: 0.0
    val durationMonths = durationMonthsText.toDoubleOrNull() ?: 12.0

    val years = durationMonths / 12.0
    val compoundPeriodsPerYear = 365.0 // Daily compounding
    val finalTokens = stakeAmount * (1 + (apy / 100.0) / compoundPeriodsPerYear).pow(compoundPeriodsPerYear * years)
    val tokensEarned = finalTokens - stakeAmount
    val initialUsd = stakeAmount * tokenPrice
    val finalUsd = finalTokens * tokenPrice
    val rewardsUsd = tokensEarned * tokenPrice

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Staking preset assets
        Text("SELECT PROOF-OF-STAKE COIN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val stakingCoins = listOf(
                Triple("ETH", 3.5, coins.find { it.symbol == "ETH" }?.priceUsd ?: 3500.0),
                Triple("SOL", 7.2, coins.find { it.symbol == "SOL" }?.priceUsd ?: 145.0),
                Triple("DOT", 11.8, coins.find { it.symbol == "DOT" }?.priceUsd ?: 7.5),
                Triple("ATOM", 14.5, coins.find { it.symbol == "ATOM" }?.priceUsd ?: 8.8),
                Triple("ADA", 3.0, coins.find { it.symbol == "ADA" }?.priceUsd ?: 0.45),
                Triple("SUI", 6.5, coins.find { it.symbol == "SUI" }?.priceUsd ?: 1.85)
            )
            items(stakingCoins) { (sym, defaultApy, price) ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                        apyPercentText = defaultApy.toString()
                        tokenPriceText = String.format("%.2f", price)
                    }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                        CryptoCoinVectorBadge(symbol = sym, size = 14.dp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$sym ($defaultApy% APY)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        CalcInputField(label = "Staked Token Amount", value = stakeAmountText, onValueChange = { stakeAmountText = it })
        CalcInputField(label = "Staking APY (%)", value = apyPercentText, onValueChange = { apyPercentText = it })
        CalcInputField(label = "Token Price USD ($)", value = tokenPriceText, onValueChange = { tokenPriceText = it })
        CalcInputField(label = "Staking Duration (Months)", value = durationMonthsText, onValueChange = { durationMonthsText = it })

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Staking Yield Forecast (Daily Compounding)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                ResultRow(label = "Staking Rewards Earned", value = String.format("%.4f Tokens", tokensEarned), valueColor = CryptoGreen, isBold = true)
                ResultRow(label = "Rewards Value in USD", value = String.format("$,.2f", rewardsUsd), valueColor = CryptoGreen, isBold = true)
                ResultRow(label = "Total Ending Token Balance", value = String.format("%.4f Tokens", finalTokens))
                ResultRow(label = "Total Ending USD Value", value = String.format("$,.2f", finalUsd))
                ResultRow(label = "Initial Deposit Value", value = String.format("$,.2f", initialUsd))
            }
        }
    }
}

// -------------------------------------------------------------
// FUTURES & LEVERAGE LIQUIDATION CALCULATOR
// -------------------------------------------------------------
@Composable
private fun LeverageCalculatorView(coins: List<CryptoCoin>) {
    var isLong by remember { mutableStateOf(true) }
    val defaultBtc = coins.find { it.symbol == "BTC" }
    var entryPriceText by remember(defaultBtc) {
        mutableStateOf(if (defaultBtc != null) String.format("%.0f", defaultBtc.priceUsd) else "65000")
    }
    var positionSizeText by remember { mutableStateOf("5000") }
    var leverageFloat by remember { mutableFloatStateOf(10f) }

    val entryPrice = entryPriceText.toDoubleOrNull() ?: 65000.0
    val positionSize = positionSizeText.toDoubleOrNull() ?: 5000.0
    val leverage = leverageFloat.toInt().coerceAtLeast(1)

    val initialMargin = if (leverage > 0) positionSize / leverage else 0.0
    val maintenanceMarginPercent = 0.5

    val liquidationPrice = if (isLong) {
        entryPrice * (1.0 - (1.0 / leverage) + (maintenanceMarginPercent / 100.0))
    } else {
        entryPrice * (1.0 + (1.0 / leverage) - (maintenanceMarginPercent / 100.0))
    }

    val distanceToLiqPercent = if (entryPrice > 0) {
        kotlin.math.abs((liquidationPrice - entryPrice) / entryPrice) * 100.0
    } else 0.0

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = isLong,
                onClick = { isLong = true },
                label = { Text("Long (Buy / Bull)") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = !isLong,
                onClick = { isLong = false },
                label = { Text("Short (Sell / Bear)") },
                modifier = Modifier.weight(1f)
            )
        }

        CalcInputField(label = "Entry Price ($)", value = entryPriceText, onValueChange = { entryPriceText = it })
        CalcInputField(label = "Position Notional Size ($)", value = positionSizeText, onValueChange = { positionSizeText = it })

        // Leverage Quick Presets
        Text("LEVERAGE MULTIPLIER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val levs = listOf(2f, 5f, 10f, 20f, 25f, 50f, 75f, 100f)
            items(levs) { lev ->
                Surface(
                    color = if (leverageFloat == lev) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { leverageFloat = lev }
                ) {
                    Text("${lev.toInt()}x", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
                }
            }
        }

        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Leverage: ${leverage}x", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = if (leverage > 25) "⚠️ High Risk" else "Moderate",
                    fontSize = 12.sp,
                    color = if (leverage > 25) CryptoRed else CryptoAccentGold
                )
            }
            Slider(
                value = leverageFloat,
                onValueChange = { leverageFloat = it },
                valueRange = 1f..100f,
                steps = 98
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Futures Position Risk Analysis", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                ResultRow(label = "Estimated Liquidation Price", value = String.format("$,.2f", liquidationPrice), valueColor = CryptoRed, isBold = true)
                ResultRow(label = "Margin Distance to Liquidation", value = String.format("%.2f%%", distanceToLiqPercent), valueColor = if (distanceToLiqPercent < 5) CryptoRed else CryptoAccentGold, isBold = true)
                ResultRow(label = "Required Initial Margin (Equity)", value = String.format("$,.2f", initialMargin))
                ResultRow(label = "ROE on +5% Price Movement", value = String.format("%+.1f%%", 5.0 * leverage), valueColor = CryptoGreen)
                ResultRow(label = "ROE on -5% Price Movement", value = String.format("%-.1f%%", -5.0 * leverage), valueColor = CryptoRed)
            }
        }
    }
}

// -------------------------------------------------------------
// IMPERMANENT LOSS CALCULATOR
// -------------------------------------------------------------
@Composable
private fun ImpermanentLossCalculatorView() {
    var priceChangeAFloat by remember { mutableFloatStateOf(50f) }
    var priceChangeBFloat by remember { mutableFloatStateOf(0f) }

    val ratioA = 1.0 + (priceChangeAFloat / 100.0)
    val ratioB = 1.0 + (priceChangeBFloat / 100.0)
    val priceRatio = ratioA / ratioB

    val k = priceRatio
    val ilFraction = if (k > 0) (2.0 * sqrt(k) / (1.0 + k)) - 1.0 else 0.0
    val ilPercent = ilFraction * 100.0

    val initialDeposit = 10000.0
    val holdValue = initialDeposit * ((ratioA + ratioB) / 2.0)
    val lpValue = holdValue * (1.0 + ilFraction)
    val dollarDifference = holdValue - lpValue

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Quick preset scenarios
        Text("PRESET SCENARIOS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val scenarios = listOf(
                "A +50%, B Flat" to (50f to 0f),
                "A +100%, B Flat" to (100f to 0f),
                "A +200%, B Flat" to (200f to 0f),
                "Both +50%" to (50f to 50f),
                "Market Crash -50%" to (-50f to -50f),
                "Reset (0%)" to (0f to 0f)
            )
            items(scenarios) { (label, pair) ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                        priceChangeAFloat = pair.first
                        priceChangeBFloat = pair.second
                    }
                ) {
                    Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
        }

        Column {
            Text("Token A Price Change: ${priceChangeAFloat.toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Slider(value = priceChangeAFloat, onValueChange = { priceChangeAFloat = it }, valueRange = -90f..400f)
        }

        Column {
            Text("Token B Price Change: ${priceChangeBFloat.toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Slider(value = priceChangeBFloat, onValueChange = { priceChangeBFloat = it }, valueRange = -90f..400f)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Automated Market Maker (AMM) Analysis", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                ResultRow(label = "Impermanent Loss", value = String.format("%.2f%%", ilPercent), valueColor = CryptoRed, isBold = true)
                ResultRow(label = "HODL Portfolio Value ($10k base)", value = String.format("$,.2f", holdValue))
                ResultRow(label = "Liquidity Pool Value", value = String.format("$,.2f", lpValue))
                ResultRow(label = "Opportunity Cost (IL $ Loss)", value = String.format("$,.2f", dollarDifference), valueColor = CryptoRed)
            }
        }
    }
}

// -------------------------------------------------------------
// MINING PROFITABILITY CALCULATOR
// -------------------------------------------------------------
@Composable
private fun MiningCalculatorView() {
    var hashrateThText by remember { mutableStateOf("120") }
    var powerWattsText by remember { mutableStateOf("3300") }
    var electricityCostKwhText by remember { mutableStateOf("0.06") }
    var dailyRewardUsdText by remember { mutableStateOf("14.50") }

    val hashrate = hashrateThText.toDoubleOrNull() ?: 120.0
    val powerWatts = powerWattsText.toDoubleOrNull() ?: 3300.0
    val costKwh = electricityCostKwhText.toDoubleOrNull() ?: 0.06
    val dailyRevenue = dailyRewardUsdText.toDoubleOrNull() ?: 14.50

    val dailyKwh = (powerWatts * 24.0) / 1000.0
    val dailyPowerCost = dailyKwh * costKwh
    val dailyNetProfit = dailyRevenue - dailyPowerCost
    val monthlyNetProfit = dailyNetProfit * 30.5
    val annualNetProfit = dailyNetProfit * 365.0

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Hardware Presets
        Text("HARDWARE PRESETS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val rigs = listOf(
                Triple("Antminer S21", "200", "3500"),
                Triple("Antminer S19 Pro", "110", "3250"),
                Triple("WhatsMiner M50", "118", "3300")
            )
            items(rigs) { (name, hr, pwr) ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable {
                        hashrateThText = hr
                        powerWattsText = pwr
                    }
                ) {
                    Text("$name ($hr TH/s)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
        }

        CalcInputField(label = "Hashrate (TH/s)", value = hashrateThText, onValueChange = { hashrateThText = it })
        CalcInputField(label = "Power Consumption (Watts)", value = powerWattsText, onValueChange = { powerWattsText = it })
        CalcInputField(label = "Electricity Cost ($ / kWh)", value = electricityCostKwhText, onValueChange = { electricityCostKwhText = it })
        CalcInputField(label = "Estimated Daily Gross Reward ($)", value = dailyRewardUsdText, onValueChange = { dailyRewardUsdText = it })

        Spacer(modifier = Modifier.height(4.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Mining Economics Output", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                ResultRow(label = "Daily Power Expense", value = String.format("$,.2f", dailyPowerCost), valueColor = CryptoRed)
                ResultRow(label = "Daily Gross Revenue", value = String.format("$,.2f", dailyRevenue))
                ResultRow(label = "Daily Net Profit", value = String.format("%+$,.2f", dailyNetProfit), valueColor = if (dailyNetProfit >= 0) CryptoGreen else CryptoRed, isBold = true)
                ResultRow(label = "Estimated Monthly Net Profit", value = String.format("%+$,.2f", monthlyNetProfit), valueColor = if (monthlyNetProfit >= 0) CryptoGreen else CryptoRed, isBold = true)
                ResultRow(label = "Estimated Annual Net Profit", value = String.format("%+$,.2f", annualNetProfit), valueColor = if (annualNetProfit >= 0) CryptoGreen else CryptoRed)
            }
        }
    }
}

// -------------------------------------------------------------
// HELPER COMPOSABLES
// -------------------------------------------------------------
@Composable
private fun CalcInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear field",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

@Composable
private fun ResultRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold,
            color = valueColor
        )
    }
}
