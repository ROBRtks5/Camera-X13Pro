package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CameraObsidian
import com.example.ui.theme.CameraSurfaceBorder
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun LiveHistogramView(
    iso: Int,
    ev: Float,
    isNightMode: Boolean,
    modifier: Modifier = Modifier
) {
    val samplePoints = remember(iso, ev, isNightMode) {
        val list = FloatArray(32)
        val rng = Random((iso + (ev * 10).toInt()).hashCode())
        val peakIndex = if (isNightMode) 8 else 18 + (ev * 3).toInt().coerceIn(-10, 10)
        
        for (i in 0..31) {
            val distFromPeak = kotlin.math.abs(i - peakIndex)
            val baseVal = (1f - (distFromPeak / 16f).coerceIn(0f, 1f))
            list[i] = (baseVal * 0.8f + rng.nextFloat() * 0.2f).coerceIn(0.05f, 0.95f)
        }
        list
    }

    Box(
        modifier = modifier
            .width(110.dp)
            .height(55.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(CameraObsidian.copy(alpha = 0.75f))
            .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(6.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val step = w / (samplePoints.size - 1)

            // Grid lines
            drawLine(
                color = Color.White.copy(alpha = 0.15f),
                start = Offset(w * 0.33f, 0f),
                end = Offset(w * 0.33f, h)
            )
            drawLine(
                color = Color.White.copy(alpha = 0.15f),
                start = Offset(w * 0.66f, 0f),
                end = Offset(w * 0.66f, h)
            )

            // Red channel path
            val pathR = Path()
            pathR.moveTo(0f, h)
            for (i in samplePoints.indices) {
                val x = i * step
                val valR = (samplePoints[i] * 0.9f).coerceIn(0f, 1f)
                val y = h - (valR * h)
                pathR.lineTo(x, y)
            }
            pathR.lineTo(w, h)
            pathR.close()
            drawPath(pathR, color = Color.Red.copy(alpha = 0.25f))

            // White luminance curve
            val pathL = Path()
            pathL.moveTo(0f, h - (samplePoints[0] * h))
            for (i in 1 until samplePoints.size) {
                val x = i * step
                val y = h - (samplePoints[i] * h)
                pathL.lineTo(x, y)
            }
            drawPath(
                pathL,
                color = Color.White.copy(alpha = 0.85f),
                style = Stroke(width = 1.5f)
            )
        }
    }
}
