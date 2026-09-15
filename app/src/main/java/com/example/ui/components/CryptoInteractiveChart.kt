package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CryptoGreen
import com.example.ui.theme.CryptoRed

@Composable
fun CryptoInteractiveChart(
    points: List<Float>,
    isPositive: Boolean,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(220.dp),
    onPriceSelected: ((Float?) -> Unit)? = null
) {
    if (points.size < 2) return

    val lineColor = if (isPositive) CryptoGreen else CryptoRed
    var touchX by remember { mutableStateOf<Float?>(null) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    val min = points.minOrNull() ?: 0f
    val max = points.maxOrNull() ?: 1f
    val range = if (max - min == 0f) 1f else max - min

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(points) {
                    detectTapGestures(
                        onPress = { offset ->
                            val stepX = size.width.toFloat() / (points.size - 1)
                            val idx = (offset.x / stepX).toInt().coerceIn(0, points.size - 1)
                            touchX = idx * stepX
                            selectedIndex = idx
                            onPriceSelected?.invoke(points[idx])
                            tryAwaitRelease()
                            touchX = null
                            selectedIndex = null
                            onPriceSelected?.invoke(null)
                        }
                    )
                }
                .pointerInput(points) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val stepX = size.width.toFloat() / (points.size - 1)
                            val idx = (offset.x / stepX).toInt().coerceIn(0, points.size - 1)
                            touchX = idx * stepX
                            selectedIndex = idx
                            onPriceSelected?.invoke(points[idx])
                        },
                        onDragEnd = {
                            touchX = null
                            selectedIndex = null
                            onPriceSelected?.invoke(null)
                        },
                        onDragCancel = {
                            touchX = null
                            selectedIndex = null
                            onPriceSelected?.invoke(null)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val stepX = size.width.toFloat() / (points.size - 1)
                            val idx = (change.position.x / stepX).toInt().coerceIn(0, points.size - 1)
                            touchX = idx * stepX
                            selectedIndex = idx
                            onPriceSelected?.invoke(points[idx])
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val topPadding = 16.dp.toPx()
                val bottomPadding = 16.dp.toPx()
                val usableHeight = height - topPadding - bottomPadding

                // Horizontal dashed grid lines
                val gridColor = Color.Gray.copy(alpha = 0.2f)
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                // High line
                drawLine(
                    color = gridColor,
                    start = Offset(0f, topPadding),
                    end = Offset(width, topPadding),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashEffect
                )

                // Mid line
                drawLine(
                    color = gridColor,
                    start = Offset(0f, topPadding + usableHeight / 2),
                    end = Offset(width, topPadding + usableHeight / 2),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashEffect
                )

                // Low line
                drawLine(
                    color = gridColor,
                    start = Offset(0f, height - bottomPadding),
                    end = Offset(width, height - bottomPadding),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashEffect
                )

                val stepX = width / (points.size - 1)
                val path = Path()
                val fillPath = Path()

                points.forEachIndexed { index, value ->
                    val x = index * stepX
                    val normalizedY = 1f - ((value - min) / range)
                    val y = topPadding + (normalizedY * usableHeight)

                    if (index == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, height)
                        fillPath.lineTo(x, y)
                    } else {
                        val prevX = (index - 1) * stepX
                        val prevNormalizedY = 1f - ((points[index - 1] - min) / range)
                        val prevY = topPadding + (prevNormalizedY * usableHeight)

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

                // Draw Gradient Fill
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lineColor.copy(alpha = 0.35f),
                            lineColor.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )

                // Draw Smooth Line
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(
                        width = 2.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                // Crosshair touch indicator
                touchX?.let { currentX ->
                    val idx = selectedIndex ?: 0
                    val value = points[idx]
                    val normalizedY = 1f - ((value - min) / range)
                    val currentY = topPadding + (normalizedY * usableHeight)

                    // Vertical crosshair line
                    drawLine(
                        color = Color.White.copy(alpha = 0.6f),
                        start = Offset(currentX, 0f),
                        end = Offset(currentX, height),
                        strokeWidth = 1.2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )

                    // Glowing point node
                    drawCircle(
                        color = lineColor.copy(alpha = 0.3f),
                        radius = 8.dp.toPx(),
                        center = Offset(currentX, currentY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 4.dp.toPx(),
                        center = Offset(currentX, currentY)
                    )
                    drawCircle(
                        color = lineColor,
                        radius = 2.5.dp.toPx(),
                        center = Offset(currentX, currentY)
                    )
                }
            }
        }

        // Min and Max Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Low: ${String.format("$%,.2f", min)}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "High: ${String.format("$%,.2f", max)}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
