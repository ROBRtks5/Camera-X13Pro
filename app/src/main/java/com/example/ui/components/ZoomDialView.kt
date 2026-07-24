package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CameraObsidian
import com.example.ui.theme.CameraSurfaceBorder
import com.example.ui.theme.CameraSurfaceDark
import com.example.ui.theme.LeicaRed

@Composable
fun ZoomDialView(
    currentZoom: Float,
    onZoomSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFineSlider by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val presetZooms = listOf(
        1.0f to "1x",
        3.2f to "3.2x\n75mm",
        5.0f to "5x\nMacro",
        10.0f to "10x",
        30.0f to "30x",
        70.0f to "70x"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showFineSlider) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(CameraObsidian.copy(alpha = 0.85f))
                    .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("%.1fx", currentZoom),
                    color = LeicaRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Slider(
                    value = currentZoom,
                    onValueChange = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onZoomSelected(it)
                    },
                    valueRange = 1.0f..70.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = LeicaRed,
                        activeTrackColor = LeicaRed,
                        inactiveTrackColor = CameraSurfaceDark
                    ),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "ГОТОВО",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showFineSlider = false
                        }
                        .padding(start = 8.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(CameraObsidian.copy(alpha = 0.8f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            presetZooms.forEach { (zoom, label) ->
                val isSelected = kotlin.math.abs(currentZoom - zoom) < 0.2f
                val animScale by animateFloatAsState(if (isSelected) 1.15f else 1.0f, label = "zoom_scale")

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(if (label.contains("\n")) 46.dp else 40.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) LeicaRed else CameraSurfaceDark.copy(alpha = 0.7f)
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 0.dp,
                            color = if (isSelected) Color.White else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onZoomSelected(zoom)
                            if (isSelected) showFineSlider = !showFineSlider
                        }
                ) {
                    Text(
                        text = label,
                        fontSize = if (label.contains("\n")) 9.sp else 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                        lineHeight = 11.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
