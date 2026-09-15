package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoRed

@Composable
fun SparklineChart(
    points: List<Float>,
    isPositive: Boolean,
    modifier: Modifier = Modifier
        .width(72.dp)
        .height(32.dp),
    strokeWidth: Dp = 1.8.dp,
    showGradient: Boolean = true
) {
    if (points.size < 2) return

    val lineColor = if (isPositive) CryptoGreen else CryptoRed

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val min = points.minOrNull() ?: 0f
        val max = points.maxOrNull() ?: 1f
        val range = if (max - min == 0f) 1f else max - min

        val stepX = width / (points.size - 1)
        val path = Path()
        val fillPath = Path()

        points.forEachIndexed { index, value ->
            val x = index * stepX
            val normalizedY = 1f - ((value - min) / range)
            val y = (normalizedY * (height - 6.dp.toPx())) + 3.dp.toPx()

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                val prevX = (index - 1) * stepX
                val prevNormalizedY = 1f - ((points[index - 1] - min) / range)
                val prevY = (prevNormalizedY * (height - 6.dp.toPx())) + 3.dp.toPx()

                // Smooth cubic bezier curve
                val controlX1 = prevX + (x - prevX) / 2
                val controlY1 = prevY
                val controlX2 = prevX + (x - prevX) / 2
                val controlY2 = y

                path.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                fillPath.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
            }
        }

        fillPath.lineTo(width, height)
        fillPath.close()

        if (showGradient) {
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.25f),
                        Color.Transparent
                    )
                )
            )
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}
