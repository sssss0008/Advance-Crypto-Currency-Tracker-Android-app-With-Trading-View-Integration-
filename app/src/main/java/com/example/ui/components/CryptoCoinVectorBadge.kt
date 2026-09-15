package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Clean mathematical vector badge for cryptocurrencies.
 * Uses 100% Android Vector paths and Canvas drawing - no raster or AI images.
 */
@Composable
fun CryptoCoinVectorBadge(
    symbol: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    val cleanSymbol = symbol.uppercase().trim()

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = this.size.minDimension
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = canvasSize / 2f

            when (cleanSymbol) {
                "BTC" -> drawBitcoinBadge(center, radius)
                "ETH" -> drawEthereumBadge(center, radius)
                "SOL" -> drawSolanaBadge(center, radius)
                "BNB" -> drawBnbBadge(center, radius)
                "XRP" -> drawXrpBadge(center, radius)
                "ADA" -> drawCardanoBadge(center, radius)
                "DOGE" -> drawDogecoinBadge(center, radius)
                "DOT" -> drawPolkadotBadge(center, radius)
                "AVAX" -> drawAvalancheBadge(center, radius)
                "LINK" -> drawChainlinkBadge(center, radius)
                "POL", "MATIC" -> drawPolygonBadge(center, radius)
                "SUI" -> drawSuiBadge(center, radius)
                "NEAR" -> drawNearBadge(center, radius)
                "ATOM" -> drawCosmosBadge(center, radius)
                "USDT", "USDT.D", "USD" -> drawTetherBadge(center, radius)
                "ZEC" -> drawZcashBadge(center, radius)
                else -> drawGenericCryptoBadge(cleanSymbol, center, radius)
            }
        }

        // For generic coins that fall into fallback, display monogram text on top
        if (cleanSymbol !in listOf("BTC", "ETH", "SOL", "BNB", "XRP", "ADA", "DOGE", "DOT", "AVAX", "LINK", "POL", "MATIC", "SUI", "NEAR", "ATOM", "USDT", "USDT.D", "USD", "ZEC")) {
            val textSize = (size.value * 0.36f).sp
            Text(
                text = cleanSymbol.take(3),
                fontSize = textSize,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

// 1. Bitcoin (BTC) Vector
private fun DrawScope.drawBitcoinBadge(center: Offset, radius: Float) {
    // Base Gold Circle
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFB300), Color(0xFFF7931A), Color(0xFFD47A00)),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
    // Subtle inner border
    drawCircle(
        color = Color(0x40FFFFFF),
        radius = radius - 1.5f,
        center = center,
        style = Stroke(width = 1.5f)
    )

    // Vector 'B' with double vertical lines
    val s = radius * 0.58f
    val bColor = Color.White

    // Vertical spine
    val spineLeft = center.x - s * 0.38f
    val spineTop = center.y - s * 0.65f
    val spineBottom = center.y + s * 0.65f

    // Two top & bottom through-cut tick marks
    val tickStroke = s * 0.12f
    val tickX1 = center.x - s * 0.12f
    val tickX2 = center.x + s * 0.16f
    drawLine(bColor, Offset(tickX1, center.y - s * 0.85f), Offset(tickX1, center.y + s * 0.85f), strokeWidth = tickStroke, cap = StrokeCap.Round)
    drawLine(bColor, Offset(tickX2, center.y - s * 0.85f), Offset(tickX2, center.y + s * 0.85f), strokeWidth = tickStroke, cap = StrokeCap.Round)

    // Main B shape
    val path = Path().apply {
        moveTo(spineLeft, spineTop)
        lineTo(center.x + s * 0.08f, spineTop)
        cubicTo(
            center.x + s * 0.52f, spineTop,
            center.x + s * 0.52f, center.y - s * 0.02f,
            center.x + s * 0.12f, center.y - s * 0.02f
        )
        cubicTo(
            center.x + s * 0.62f, center.y - s * 0.02f,
            center.x + s * 0.62f, spineBottom,
            center.x + s * 0.12f, spineBottom
        )
        lineTo(spineLeft, spineBottom)
        close()
    }
    drawPath(path, bColor, style = Fill)

    // Cutouts inside B loops
    val cutoutColor = Color(0xFFF7931A)
    // Top inner cutout
    val topHole = Path().apply {
        moveTo(spineLeft + s * 0.18f, spineTop + s * 0.14f)
        lineTo(center.x + s * 0.08f, spineTop + s * 0.14f)
        cubicTo(
            center.x + s * 0.28f, spineTop + s * 0.14f,
            center.x + s * 0.28f, center.y - s * 0.12f,
            center.x + s * 0.08f, center.y - s * 0.12f
        )
        lineTo(spineLeft + s * 0.18f, center.y - s * 0.12f)
        close()
    }
    drawPath(topHole, cutoutColor, style = Fill)

    // Bottom inner cutout
    val bottomHole = Path().apply {
        moveTo(spineLeft + s * 0.18f, center.y + s * 0.08f)
        lineTo(center.x + s * 0.12f, center.y + s * 0.08f)
        cubicTo(
            center.x + s * 0.34f, center.y + s * 0.08f,
            center.x + s * 0.34f, spineBottom - s * 0.14f,
            center.x + s * 0.12f, spineBottom - s * 0.14f
        )
        lineTo(spineLeft + s * 0.18f, spineBottom - s * 0.14f)
        close()
    }
    drawPath(bottomHole, cutoutColor, style = Fill)
}

// 2. Ethereum (ETH) Vector
private fun DrawScope.drawEthereumBadge(center: Offset, radius: Float) {
    // Background Dark Slate/Indigo
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF7A93FF), Color(0xFF627EEA), Color(0xFF455BB5)),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.65f

    // Top Upper Diamond Facet (Left side lighter)
    val topDiamondLeft = Path().apply {
        moveTo(center.x, center.y - s)
        lineTo(center.x - s * 0.55f, center.y)
        lineTo(center.x, center.y + s * 0.25f)
        close()
    }
    drawPath(topDiamondLeft, Color(0xCCFFFFFF), style = Fill)

    // Top Upper Diamond Facet (Right side highlight)
    val topDiamondRight = Path().apply {
        moveTo(center.x, center.y - s)
        lineTo(center.x + s * 0.55f, center.y)
        lineTo(center.x, center.y + s * 0.25f)
        close()
    }
    drawPath(topDiamondRight, Color.White, style = Fill)

    // Bottom Lower Diamond Facet (Left side)
    val bottomDiamondLeft = Path().apply {
        moveTo(center.x, center.y + s * 0.40f)
        lineTo(center.x - s * 0.55f, center.y + s * 0.12f)
        lineTo(center.x, center.y + s)
        close()
    }
    drawPath(bottomDiamondLeft, Color(0xCCFFFFFF), style = Fill)

    // Bottom Lower Diamond Facet (Right side)
    val bottomDiamondRight = Path().apply {
        moveTo(center.x, center.y + s * 0.40f)
        lineTo(center.x + s * 0.55f, center.y + s * 0.12f)
        lineTo(center.x, center.y + s)
        close()
    }
    drawPath(bottomDiamondRight, Color.White, style = Fill)
}

// 3. Solana (SOL) Vector
private fun DrawScope.drawSolanaBadge(center: Offset, radius: Float) {
    // Background deep cyber black
    drawCircle(color = Color(0xFF10141E), radius = radius, center = center)
    drawCircle(
        brush = Brush.sweepGradient(
            listOf(Color(0xFF9945FF), Color(0xFF14F195), Color(0xFF00C2FF), Color(0xFF9945FF)),
            center = center
        ),
        radius = radius - 1f,
        center = center,
        style = Stroke(width = 1.5f)
    )

    val w = radius * 0.95f
    val h = radius * 0.22f
    val slant = radius * 0.20f

    // Top speed bar (left-pointing gradient)
    val topBar = Path().apply {
        moveTo(center.x - w / 2f + slant, center.y - radius * 0.42f)
        lineTo(center.x + w / 2f, center.y - radius * 0.42f)
        lineTo(center.x + w / 2f - slant, center.y - radius * 0.42f + h)
        lineTo(center.x - w / 2f, center.y - radius * 0.42f + h)
        close()
    }
    drawPath(topBar, Brush.horizontalGradient(listOf(Color(0xFF9945FF), Color(0xFF14F195))), style = Fill)

    // Middle speed bar (right-pointing gradient)
    val midBar = Path().apply {
        moveTo(center.x - w / 2f, center.y - h / 2f)
        lineTo(center.x + w / 2f - slant, center.y - h / 2f)
        lineTo(center.x + w / 2f, center.y + h / 2f)
        lineTo(center.x - w / 2f + slant, center.y + h / 2f)
        close()
    }
    drawPath(midBar, Brush.horizontalGradient(listOf(Color(0xFF9945FF), Color(0xFF14F195))), style = Fill)

    // Bottom speed bar
    val botBar = Path().apply {
        moveTo(center.x - w / 2f + slant, center.y + radius * 0.42f - h)
        lineTo(center.x + w / 2f, center.y + radius * 0.42f - h)
        lineTo(center.x + w / 2f - slant, center.y + radius * 0.42f)
        lineTo(center.x - w / 2f, center.y + radius * 0.42f)
        close()
    }
    drawPath(botBar, Brush.horizontalGradient(listOf(Color(0xFF9945FF), Color(0xFF14F195))), style = Fill)
}

// 4. BNB Chain Vector
private fun DrawScope.drawBnbBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFFF3BA2F), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.28f
    val dark = Color(0xFF1E2026)

    // Center diamond
    val centerDiamond = Path().apply {
        moveTo(center.x, center.y - s)
        lineTo(center.x + s, center.y)
        lineTo(center.x, center.y + s)
        lineTo(center.x - s, center.y)
        close()
    }
    drawPath(centerDiamond, dark, style = Fill)

    // Top chevron
    val d = s * 1.55f
    val topChevron = Path().apply {
        moveTo(center.x, center.y - d - s * 0.6f)
        lineTo(center.x + s * 0.8f, center.y - d + s * 0.2f)
        lineTo(center.x + s * 0.5f, center.y - d + s * 0.5f)
        lineTo(center.x, center.y - d)
        lineTo(center.x - s * 0.5f, center.y - d + s * 0.5f)
        lineTo(center.x - s * 0.8f, center.y - d + s * 0.2f)
        close()
    }
    drawPath(topChevron, dark, style = Fill)

    // Bottom chevron
    val botChevron = Path().apply {
        moveTo(center.x, center.y + d + s * 0.6f)
        lineTo(center.x + s * 0.8f, center.y + d - s * 0.2f)
        lineTo(center.x + s * 0.5f, center.y + d - s * 0.5f)
        lineTo(center.x, center.y + d)
        lineTo(center.x - s * 0.5f, center.y + d - s * 0.5f)
        lineTo(center.x - s * 0.8f, center.y + d - s * 0.2f)
        close()
    }
    drawPath(botChevron, dark, style = Fill)

    // Left chevron
    val leftChevron = Path().apply {
        moveTo(center.x - d - s * 0.6f, center.y)
        lineTo(center.x - d + s * 0.2f, center.y + s * 0.8f)
        lineTo(center.x - d + s * 0.5f, center.y + s * 0.5f)
        lineTo(center.x - d, center.y)
        lineTo(center.x - d + s * 0.5f, center.y - s * 0.5f)
        lineTo(center.x - d + s * 0.2f, center.y - s * 0.8f)
        close()
    }
    drawPath(leftChevron, dark, style = Fill)

    // Right chevron
    val rightChevron = Path().apply {
        moveTo(center.x + d + s * 0.6f, center.y)
        lineTo(center.x + d - s * 0.2f, center.y + s * 0.8f)
        lineTo(center.x + d - s * 0.5f, center.y + s * 0.5f)
        lineTo(center.x + d, center.y)
        lineTo(center.x + d - s * 0.5f, center.y - s * 0.5f)
        lineTo(center.x + d - s * 0.2f, center.y - s * 0.8f)
        close()
    }
    drawPath(rightChevron, dark, style = Fill)
}

// 5. Ripple (XRP) Vector
private fun DrawScope.drawXrpBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFF23292F), radius = radius, center = center)
    drawCircle(color = Color(0x30FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.60f
    val strokeW = s * 0.22f

    // Top X Wing
    val topWing = Path().apply {
        moveTo(center.x - s * 0.8f, center.y - s * 0.6f)
        cubicTo(
            center.x - s * 0.4f, center.y - s * 0.6f,
            center.x - s * 0.2f, center.y - s * 0.1f,
            center.x, center.y - s * 0.1f
        )
        cubicTo(
            center.x + s * 0.2f, center.y - s * 0.1f,
            center.x + s * 0.4f, center.y - s * 0.6f,
            center.x + s * 0.8f, center.y - s * 0.6f
        )
    }
    drawPath(topWing, Color(0xFF00AAE4), style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

    // Bottom X Wing
    val botWing = Path().apply {
        moveTo(center.x - s * 0.8f, center.y + s * 0.6f)
        cubicTo(
            center.x - s * 0.4f, center.y + s * 0.6f,
            center.x - s * 0.2f, center.y + s * 0.1f,
            center.x, center.y + s * 0.1f
        )
        cubicTo(
            center.x + s * 0.2f, center.y + s * 0.1f,
            center.x + s * 0.4f, center.y + s * 0.6f,
            center.x + s * 0.8f, center.y + s * 0.6f
        )
    }
    drawPath(botWing, Color.White, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

// 6. Cardano (ADA) Vector
private fun DrawScope.drawCardanoBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFF0033AD), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    // Center dot
    drawCircle(color = Color.White, radius = radius * 0.18f, center = center)

    // Inner orbital dots
    val innerR = radius * 0.42f
    for (i in 0 until 6) {
        val angle = Math.toRadians((i * 60.0))
        val dotCenter = Offset(
            (center.x + innerR * cos(angle)).toFloat(),
            (center.y + innerR * sin(angle)).toFloat()
        )
        drawCircle(color = Color(0xEEFFFFFF), radius = radius * 0.08f, center = dotCenter)
    }

    // Outer orbital dots
    val outerR = radius * 0.72f
    for (i in 0 until 12) {
        val angle = Math.toRadians((i * 30.0) + 15.0)
        val dotCenter = Offset(
            (center.x + outerR * cos(angle)).toFloat(),
            (center.y + outerR * sin(angle)).toFloat()
        )
        drawCircle(color = Color(0xAAFFFFFF), radius = radius * 0.05f, center = dotCenter)
    }
}

// 7. Dogecoin (DOGE) Vector
private fun DrawScope.drawDogecoinBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFFC2A633), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.55f
    val dColor = Color.White
    val spineLeft = center.x - s * 0.35f
    val top = center.y - s * 0.7f
    val bottom = center.y + s * 0.7f

    // D letter shape
    val path = Path().apply {
        moveTo(spineLeft, top)
        lineTo(center.x + s * 0.1f, top)
        cubicTo(
            center.x + s * 0.7f, top,
            center.x + s * 0.7f, bottom,
            center.x + s * 0.1f, bottom
        )
        lineTo(spineLeft, bottom)
        close()
    }
    drawPath(path, dColor, style = Fill)

    // Inner Cutout
    val cutout = Path().apply {
        moveTo(spineLeft + s * 0.22f, top + s * 0.20f)
        lineTo(center.x + s * 0.08f, top + s * 0.20f)
        cubicTo(
            center.x + s * 0.45f, top + s * 0.20f,
            center.x + s * 0.45f, bottom - s * 0.20f,
            center.x + s * 0.08f, bottom - s * 0.20f
        )
        lineTo(spineLeft + s * 0.22f, bottom - s * 0.20f)
        close()
    }
    drawPath(cutout, Color(0xFFC2A633), style = Fill)

    // Horizontal cross-bar
    drawLine(
        color = Color(0xFFC2A633),
        start = Offset(spineLeft - s * 0.15f, center.y),
        end = Offset(spineLeft + s * 0.35f, center.y),
        strokeWidth = s * 0.18f,
        cap = StrokeCap.Round
    )
}

// 8. Polkadot (DOT) Vector
private fun DrawScope.drawPolkadotBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFFE6007A), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    // Main Dot
    drawCircle(color = Color.White, radius = radius * 0.32f, center = center)

    // Surrounding Satellite Dots
    val orbitR = radius * 0.65f
    val angles = listOf(30.0, 90.0, 150.0, 210.0, 270.0, 330.0)
    for (a in angles) {
        val rad = Math.toRadians(a)
        val p = Offset((center.x + orbitR * cos(rad)).toFloat(), (center.y + orbitR * sin(rad)).toFloat())
        drawCircle(color = Color.White, radius = radius * 0.09f, center = p)
    }
}

// 9. Avalanche (AVAX) Vector
private fun DrawScope.drawAvalancheBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFFE84142), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.65f

    // Peak A (Left/Main peak)
    val mainPeak = Path().apply {
        moveTo(center.x, center.y - s)
        lineTo(center.x + s * 0.75f, center.y + s * 0.75f)
        lineTo(center.x + s * 0.38f, center.y + s * 0.75f)
        lineTo(center.x, center.y - s * 0.15f)
        lineTo(center.x - s * 0.38f, center.y + s * 0.75f)
        lineTo(center.x - s * 0.75f, center.y + s * 0.75f)
        close()
    }
    drawPath(mainPeak, Color.White, style = Fill)
}

// 10. Chainlink (LINK) Vector
private fun DrawScope.drawChainlinkBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFF375BD2), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.60f
    val strokeW = s * 0.28f

    // Hexagon loop
    val hex = Path().apply {
        val angles = (0..5).map { it * 60.0 + 30.0 }
        val startRad = Math.toRadians(angles[0])
        moveTo((center.x + s * cos(startRad)).toFloat(), (center.y + s * sin(startRad)).toFloat())
        for (i in 1..5) {
            val rad = Math.toRadians(angles[i])
            lineTo((center.x + s * cos(rad)).toFloat(), (center.y + s * sin(rad)).toFloat())
        }
        close()
    }
    drawPath(hex, Color.White, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

// 11. Polygon (POL/MATIC) Vector
private fun DrawScope.drawPolygonBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFF8247E5), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.55f
    val strokeW = s * 0.25f

    // Infinity geometric links
    val p1 = Path().apply {
        moveTo(center.x - s * 0.5f, center.y - s * 0.3f)
        lineTo(center.x, center.y + s * 0.3f)
        lineTo(center.x + s * 0.5f, center.y - s * 0.3f)
    }
    drawPath(p1, Color.White, style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

    val p2 = Path().apply {
        moveTo(center.x - s * 0.5f, center.y + s * 0.3f)
        lineTo(center.x, center.y - s * 0.3f)
        lineTo(center.x + s * 0.5f, center.y + s * 0.3f)
    }
    drawPath(p2, Color(0xCCFFFFFF), style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

// 12. Sui (SUI) Vector
private fun DrawScope.drawSuiBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFF2A82E4), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.65f
    // Water drop wave vector
    val drop = Path().apply {
        moveTo(center.x, center.y - s)
        cubicTo(center.x + s * 0.65f, center.y - s * 0.1f, center.x + s * 0.65f, center.y + s * 0.5f, center.x, center.y + s * 0.8f)
        cubicTo(center.x - s * 0.65f, center.y + s * 0.5f, center.x - s * 0.65f, center.y - s * 0.1f, center.x, center.y - s)
        close()
    }
    drawPath(drop, Color.White, style = Fill)
}

// 13. Near Protocol (NEAR) Vector
private fun DrawScope.drawNearBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFF00EC97), radius = radius, center = center)
    drawCircle(color = Color(0x40000000), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.55f
    val dark = Color(0xFF111111)

    // Modern ribbon N
    val nPath = Path().apply {
        moveTo(center.x - s * 0.55f, center.y + s * 0.7f)
        lineTo(center.x - s * 0.55f, center.y - s * 0.7f)
        lineTo(center.x - s * 0.22f, center.y - s * 0.7f)
        lineTo(center.x + s * 0.28f, center.y + s * 0.25f)
        lineTo(center.x + s * 0.28f, center.y - s * 0.7f)
        lineTo(center.x + s * 0.58f, center.y - s * 0.7f)
        lineTo(center.x + s * 0.58f, center.y + s * 0.7f)
        lineTo(center.x + s * 0.25f, center.y + s * 0.7f)
        lineTo(center.x - s * 0.25f, center.y - s * 0.25f)
        lineTo(center.x - s * 0.25f, center.y + s * 0.7f)
        close()
    }
    drawPath(nPath, dark, style = Fill)
}

// 14. Cosmos (ATOM) Vector
private fun DrawScope.drawCosmosBadge(center: Offset, radius: Float) {
    drawCircle(color = Color(0xFF2E3148), radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    // Center nucleus
    drawCircle(color = Color.White, radius = radius * 0.18f, center = center)

    val strokeW = radius * 0.08f
    // Orbit 1 (Horizontal ellipse)
    drawOval(
        color = Color(0xBBFFFFFF),
        topLeft = Offset(center.x - radius * 0.7f, center.y - radius * 0.25f),
        size = Size(radius * 1.4f, radius * 0.5f),
        style = Stroke(width = strokeW)
    )
}

// 15. Tether (USDT / USDT.D) Vector
private fun DrawScope.drawTetherBadge(center: Offset, radius: Float) {
    val tetherColor = Color(0xFF26A17B)
    drawCircle(color = tetherColor, radius = radius, center = center)
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.55f
    // Tether Ring
    drawOval(
        color = Color.White,
        topLeft = Offset(center.x - s * 0.65f, center.y - s * 0.15f),
        size = Size(s * 1.3f, s * 0.45f),
        style = Stroke(width = s * 0.16f)
    )

    // Tether T Bar (horizontal)
    drawLine(
        color = Color.White,
        start = Offset(center.x - s * 0.55f, center.y - s * 0.5f),
        end = Offset(center.x + s * 0.55f, center.y - s * 0.5f),
        strokeWidth = s * 0.2f,
        cap = StrokeCap.Round
    )

    // Tether Stem (vertical)
    drawLine(
        color = Color.White,
        start = Offset(center.x, center.y - s * 0.5f),
        end = Offset(center.x, center.y + s * 0.6f),
        strokeWidth = s * 0.2f,
        cap = StrokeCap.Round
    )
}

// 16. Zcash (ZEC) Vector
private fun DrawScope.drawZcashBadge(center: Offset, radius: Float) {
    val zcashGold = Color(0xFFF4B240)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFD54F), zcashGold, Color(0xFFC7851A)),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
    drawCircle(color = Color(0x40FFFFFF), radius = radius - 1.5f, center = center, style = Stroke(width = 1.5f))

    val s = radius * 0.52f
    val strokeW = s * 0.22f

    // Vertical through-cut bar
    drawLine(
        color = Color.White,
        start = Offset(center.x, center.y - s * 0.85f),
        end = Offset(center.x, center.y + s * 0.85f),
        strokeWidth = strokeW * 0.75f,
        cap = StrokeCap.Round
    )

    // Bold Z path
    val zPath = Path().apply {
        moveTo(center.x - s * 0.55f, center.y - s * 0.52f)
        lineTo(center.x + s * 0.55f, center.y - s * 0.52f)
        lineTo(center.x - s * 0.45f, center.y + s * 0.52f)
        lineTo(center.x + s * 0.55f, center.y + s * 0.52f)
    }
    drawPath(
        path = zPath,
        color = Color.White,
        style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

// 17. Generic Monogram Badge for any other cryptocurrency
private fun DrawScope.drawGenericCryptoBadge(symbol: String, center: Offset, radius: Float) {
    // Deterministic pleasant color from symbol string
    val hash = symbol.fold(0) { acc, c -> (acc * 31 + c.code) and 0x7FFFFFFF }
    val hues = listOf(
        Color(0xFF3F51B5), Color(0xFF009688), Color(0xFF673AB7),
        Color(0xFFFF5722), Color(0xFF00BCD4), Color(0xFF4CAF50),
        Color(0xFFE91E63), Color(0xFF607D8B), Color(0xFF795548)
    )
    val baseColor = hues[hash % hues.size]

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(baseColor, baseColor.copy(alpha = 0.75f)),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
    // Geometric inner ring
    drawCircle(
        color = Color(0x40FFFFFF),
        radius = radius - 1.5f,
        center = center,
        style = Stroke(width = 1.5f)
    )
}
