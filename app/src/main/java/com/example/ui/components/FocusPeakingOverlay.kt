package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.FocusPeakingColor
import kotlin.random.Random

@Composable
fun FocusPeakingOverlay(
    isEnabled: Boolean,
    color: FocusPeakingColor,
    focusDistanceCm: Float,
    isTelemacro: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isEnabled) return

    val peakColor = Color(color.hexColor)
    // Generate organic edge noise points around focus plane based on focusDistanceCm
    val randomSeed = remember(focusDistanceCm, isTelemacro) {
        (focusDistanceCm * 100).toInt()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val rng = Random(randomSeed)

        // Focus plane band y position based on focus distance
        val centerY = height * (0.3f + (focusDistanceCm / 100f).coerceIn(0f, 0.4f))
        val bandHeight = if (isTelemacro) height * 0.25f else height * 0.15f

        val strokeWidth = 2.5f

        // Draw edge highlights imitating Camera2/Leica focus peaking
        val path = Path()
        val numLines = if (isTelemacro) 40 else 25

        for (i in 0 until numLines) {
            val startX = rng.nextFloat() * width
            val startY = centerY + (rng.nextFloat() - 0.5f) * bandHeight
            val length = 15f + rng.nextFloat() * 45f
            val angle = rng.nextFloat() * 360f

            val endX = startX + length * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val endY = startY + length * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()

            drawCircle(
                color = peakColor.copy(alpha = 0.85f),
                radius = 2.5f,
                center = Offset(startX, startY)
            )

            drawLine(
                color = peakColor.copy(alpha = 0.65f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = strokeWidth
            )
        }

        // Draw macro focus ring guide if telemacro focus is in 10-20cm range
        if (isTelemacro && focusDistanceCm in 10f..25f) {
            val ringRadius = width * 0.22f
            val center = Offset(width * 0.5f, height * 0.45f)
            drawCircle(
                color = peakColor,
                radius = ringRadius,
                center = center,
                style = Stroke(width = 3f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f))
            )
        }
    }
}
