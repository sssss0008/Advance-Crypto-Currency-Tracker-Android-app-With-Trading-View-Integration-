package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modern Glassmorphic Container with subtle gradient border, semi-transparent frosted backdrop,
 * and dynamic hover / press micro-interaction scaling and illumination.
 */
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: (() -> Unit)? = null,
    borderAlpha: Float = 0.28f,
    surfaceAlpha: Float = 0.65f,
    accentGlow: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth hover / press scale animation
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.985f
            isHovered -> 1.015f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    val currentBorderAlpha by animateFloatAsState(
        targetValue = if (isHovered) (borderAlpha + 0.35f).coerceAtMost(0.9f) else borderAlpha,
        animationSpec = tween(250),
        label = "borderGlow"
    )

    val baseSurface = MaterialTheme.colorScheme.surface
    val highlightColor = MaterialTheme.colorScheme.primary

    // Dynamic glass gradient brush
    val glassBrush = Brush.linearGradient(
        colors = listOf(
            baseSurface.copy(alpha = (surfaceAlpha + if (isHovered) 0.12f else 0f).coerceAtMost(0.95f)),
            baseSurface.copy(alpha = surfaceAlpha * 0.75f)
        )
    )

    val borderBrush = Brush.linearGradient(
        colors = listOf(
            (accentGlow ?: highlightColor).copy(alpha = currentBorderAlpha),
            Color.White.copy(alpha = currentBorderAlpha * 0.4f),
            baseSurface.copy(alpha = 0.1f)
        )
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(brush = glassBrush, shape = shape)
            .border(
                width = if (isHovered) 1.2.dp else 1.dp,
                brush = borderBrush,
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null, // Custom scale and glow handles feedback
                        onClick = onClick
                    )
                } else Modifier
            ),
        content = content
    )
}

/**
 * Interactive Glassmorphic Icon / Action Button with hover lighting and press animation.
 */
@Composable
fun GlassmorphicIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    size: Dp = 44.dp,
    tintGlow: Color = MaterialTheme.colorScheme.primary,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.92f
            isHovered -> 1.08f
            else -> 1f
        },
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "btnScale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.45f
            isHovered -> 0.35f
            else -> 0.15f
        },
        animationSpec = tween(200),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        tintGlow.copy(alpha = glowAlpha),
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    )
                ),
                shape = shape
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        tintGlow.copy(alpha = (glowAlpha + 0.3f).coerceAtMost(0.9f)),
                        Color.White.copy(alpha = 0.15f)
                    )
                ),
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = androidx.compose.ui.Alignment.Center,
        content = content
    )
}

/**
 * Premium Glassmorphic Coin Card featuring soft gradient reflection, glowing borders based on
 * 24h market performance (neon green or neon red), and interactive hover & press micro-animations.
 */
@Composable
fun GlassmorphicCoinCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPositive: Boolean = true,
    isFlashing: Boolean = false,
    flashColor: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.98f
            isHovered -> 1.015f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "coinCardScale"
    )

    val baseBorderColor = if (isPositive) Color(0xFF00E676) else Color(0xFFFF5252)
    val borderAlpha by animateFloatAsState(
        targetValue = when {
            isHovered -> 0.65f
            isPressed -> 0.50f
            else -> 0.20f
        },
        animationSpec = tween(220),
        label = "borderGlow"
    )

    val surfaceColor = MaterialTheme.colorScheme.surface
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    val cardBrush = Brush.linearGradient(
        colors = if (isFlashing) {
            listOf(
                flashColor.copy(alpha = 0.35f),
                surfaceVariant.copy(alpha = 0.85f)
            )
        } else {
            listOf(
                surfaceVariant.copy(alpha = if (isHovered) 0.90f else 0.70f),
                surfaceColor.copy(alpha = if (isHovered) 0.80f else 0.60f)
            )
        }
    )

    val borderBrush = Brush.linearGradient(
        colors = listOf(
            baseBorderColor.copy(alpha = borderAlpha),
            Color.White.copy(alpha = if (isHovered) 0.40f else 0.12f),
            baseBorderColor.copy(alpha = borderAlpha * 0.4f)
        )
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(brush = cardBrush, shape = shape)
            .border(
                width = if (isHovered) 1.5.dp else 1.dp,
                brush = borderBrush,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        content = content
    )
}
