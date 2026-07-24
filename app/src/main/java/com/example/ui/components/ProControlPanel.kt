package com.example.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ShutterSpeed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CameraSettingsState
import com.example.model.FocusPeakingColor
import com.example.model.LeicaColorProfile
import com.example.model.PortraitLensStyle
import com.example.model.ShootingMode
import com.example.ui.theme.CameraAccentGold
import com.example.ui.theme.CameraObsidian
import com.example.ui.theme.CameraSurfaceBorder
import com.example.ui.theme.CameraSurfaceDark
import com.example.ui.theme.FocusPeakingGreen
import com.example.ui.theme.LeicaRed

@Composable
fun ProControlPanel(
    settings: CameraSettingsState,
    onSettingsChanged: (CameraSettingsState) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf("RAW_ENGINE") } // "RAW_ENGINE", "FOCUS", "EXPOSURE", "LEICA"

    val shutterValues = listOf("AUTO", "1/8000s", "1/2000s", "1/1000s", "1/500s", "1/250s", "1/60s", "1/15s", "1s", "5s")
    val isoValues = listOf(0, 50, 100, 200, 400, 800, 1600, 3200)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(CameraObsidian.copy(alpha = 0.95f))
            .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(12.dp)
    ) {
        // Tab Selector Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlTabChip(
                title = "1\" & 75mm RAW",
                isSelected = activeTab == "RAW_ENGINE",
                icon = Icons.Default.Tune,
                onClick = { activeTab = "RAW_ENGINE" }
            )
            ControlTabChip(
                title = "ФОКУС / МАКРО",
                isSelected = activeTab == "FOCUS",
                icon = Icons.Default.CenterFocusStrong,
                onClick = { activeTab = "FOCUS" }
            )
            ControlTabChip(
                title = "ЭКСПОЗТИЦИЯ",
                isSelected = activeTab == "EXPOSURE",
                icon = Icons.Default.ShutterSpeed,
                onClick = { activeTab = "EXPOSURE" }
            )
            ControlTabChip(
                title = "LEICA ЦВЕТ",
                isSelected = activeTab == "LEICA",
                icon = Icons.Default.ColorLens,
                onClick = { activeTab = "LEICA" }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (activeTab) {
            "RAW_ENGINE" -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Оптимизация под Xiaomi 13 Pro (Android 16)",
                        color = CameraAccentGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Шумоподавление и бьюти-обработка:",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (settings.isDisableDenoise) "ОТКЛЮЧЕНО (Нативная текстура/зерно)" else "ВКЛЮЧЕНО",
                                color = if (settings.isDisableDenoise) FocusPeakingGreen else Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (settings.isDisableDenoise) LeicaRed else CameraSurfaceDark)
                                .clickable {
                                    onSettingsChanged(settings.copy(isDisableDenoise = !settings.isDisableDenoise))
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (settings.isDisableDenoise) "ЧИСТЫЙ RAW" else "СОФТ",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Захват RAW / DNG 14-bit:",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (settings.isRawMode) "ВКЛ (Без сжатия Camera2)" else "ВЫКЛ (JPEG)",
                                color = if (settings.isRawMode) CameraAccentGold else Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (settings.isRawMode) CameraAccentGold else CameraSurfaceDark)
                                .clickable {
                                    onSettingsChanged(settings.copy(isRawMode = !settings.isRawMode))
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (settings.isRawMode) "14-bit DNG" else "JPEG",
                                color = if (settings.isRawMode) Color.Black else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            "FOCUS" -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (settings.focusDistanceCm <= 20f)
                                "Дистанция фокуса: ${settings.focusDistanceCm.toInt()} см (СУПЕР-МАКРО)"
                            else
                                "Дистанция фокуса: ${String.format("%.1f", settings.focusDistanceCm / 100f)} м",
                            color = if (settings.focusDistanceCm <= 20f) FocusPeakingGreen else CameraAccentGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Focus Peaking: ",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                            Text(
                                text = if (settings.isFocusPeakingEnabled) "ВКЛ" else "ВЫКЛ",
                                color = if (settings.isFocusPeakingEnabled) FocusPeakingGreen else Color.Gray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        onSettingsChanged(settings.copy(isFocusPeakingEnabled = !settings.isFocusPeakingEnabled))
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Slider(
                        value = settings.focusDistanceCm,
                        onValueChange = {
                            onSettingsChanged(settings.copy(focusDistanceCm = it, isAutoDistance = false))
                        },
                        valueRange = 10f..300f,
                        colors = SliderDefaults.colors(
                            thumbColor = if (settings.focusDistanceCm <= 20f) FocusPeakingGreen else LeicaRed,
                            activeTrackColor = if (settings.focusDistanceCm <= 20f) FocusPeakingGreen else LeicaRed
                        )
                    )

                    // Color picker for Focus Peaking
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Цвет пикинга: ", color = Color.Gray, fontSize = 11.sp)
                        FocusPeakingColor.values().forEach { fpColor ->
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(fpColor.hexColor))
                                    .border(
                                        width = if (settings.focusPeakingColor == fpColor) 2.dp else 0.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        onSettingsChanged(settings.copy(focusPeakingColor = fpColor))
                                    }
                            )
                        }
                    }
                }
            }

            "EXPOSURE" -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ISO: " + if (settings.isAutoIso) "AUTO (${settings.isoValue})" else "${settings.isoValue}",
                        color = CameraAccentGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        items(isoValues) { iso ->
                            val isSelected = (iso == 0 && settings.isAutoIso) || (!settings.isAutoIso && settings.isoValue == iso)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) LeicaRed else CameraSurfaceDark)
                                    .clickable {
                                        if (iso == 0) {
                                            onSettingsChanged(settings.copy(isAutoIso = true))
                                        } else {
                                            onSettingsChanged(settings.copy(isAutoIso = false, isoValue = iso))
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (iso == 0) "AUTO" else "$iso",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Экспозиция EV: " + String.format("%+.1f EV", settings.evCompensation),
                        color = Color.White,
                        fontSize = 11.sp
                    )
                    Slider(
                        value = settings.evCompensation,
                        onValueChange = { onSettingsChanged(settings.copy(evCompensation = it)) },
                        valueRange = -3.0f..3.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = CameraAccentGold,
                            activeTrackColor = CameraAccentGold
                        )
                    )
                }
            }

            "LEICA" -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Профиль цвета Leica:",
                        color = CameraAccentGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LeicaColorProfile.values().forEach { profile ->
                            val isSelected = settings.leicaProfile == profile
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) LeicaRed else CameraSurfaceDark)
                                    .border(
                                        width = if (isSelected) 1.dp else 0.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        onSettingsChanged(settings.copy(leicaProfile = profile))
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profile.titleRu,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            "PORTRAIT" -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Мастер-объектив Leica Portrait:",
                        color = CameraAccentGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        items(PortraitLensStyle.values()) { style ->
                            val isSelected = settings.portraitStyle == style
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) LeicaRed else CameraSurfaceDark)
                                    .clickable {
                                        onSettingsChanged(settings.copy(portraitStyle = style))
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = style.titleRu,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = style.styleDesc,
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 9.sp
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

@Composable
private fun ControlTabChip(
    title: String,
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) LeicaRed else CameraSurfaceDark)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.height(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                color = if (isSelected) Color.White else Color.Gray,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
