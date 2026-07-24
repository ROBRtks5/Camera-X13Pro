package com.example.ui.components

import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.filled.RawOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.R
import com.example.model.CameraSettingsState
import com.example.model.GridOverlayType
import com.example.model.ShootingMode
import com.example.ui.theme.CameraAccentGold
import com.example.ui.theme.CameraObsidian
import com.example.ui.theme.CameraSurfaceDark
import com.example.ui.theme.FocusPeakingGreen
import com.example.ui.theme.LeicaRed
import kotlinx.coroutines.delay

@Composable
fun CameraViewfinder(
    settings: CameraSettingsState,
    isCameraBound: Boolean,
    onPreviewViewCreated: (PreviewView) -> Unit,
    onTapToFocus: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    var tapOffset by remember { mutableStateOf<Offset?>(null) }

    LaunchedEffect(tapOffset) {
        if (tapOffset != null) {
            delay(1500)
            tapOffset = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CameraObsidian)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    tapOffset = offset
                    onTapToFocus(offset)
                }
            }
    ) {
        if (isCameraBound) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        onPreviewViewCreated(this)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // High-fidelity camera simulation canvas for emulator / before camera start
            SimulatedTelephotoFeed(settings = settings)
        }

        // Grid Overlays
        GridOverlay(gridType = settings.gridType)

        // Focus Peaking Lines Canvas
        FocusPeakingOverlay(
            isEnabled = settings.isFocusPeakingEnabled,
            color = settings.focusPeakingColor,
            focusDistanceCm = settings.focusDistanceCm,
            isTelemacro = settings.shootingMode == ShootingMode.TELEMACRO
        )

        // Tap-to-Focus Reticle Ring
        tapOffset?.let { pos ->
            Box(
                modifier = Modifier
                    .offset { IntOffset(pos.x.toInt() - 36, pos.y.toInt() - 36) }
                    .size(72.dp)
                    .border(1.5.dp, FocusPeakingGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(FocusPeakingGreen)
                )
            }
        }

        // Live Telephoto Metadata Header Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(CameraObsidian.copy(alpha = 0.75f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(LeicaRed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = settings.leicaProfile.codeName,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${settings.shootingMode.defaultFocalLength} f/2.0 OIS",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (settings.shootingMode == ShootingMode.TELEMACRO) {
                    Text(
                        text = "10cm MACRO",
                        color = FocusPeakingGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(FocusPeakingGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Text(
                    text = "${settings.shutterSpeedSec} • ISO ${if (settings.isAutoIso) "A" else settings.isoValue}",
                    color = CameraAccentGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Live Histogram view floating at top right
        if (settings.isHistogramEnabled) {
            LiveHistogramView(
                iso = settings.isoValue,
                ev = settings.evCompensation,
                isNightMode = settings.shootingMode == ShootingMode.NIGHT_RAW_OIS,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 45.dp, end = 12.dp)
            )
        }
    }
}

@Composable
private fun GridOverlay(gridType: GridOverlayType) {
    if (gridType == GridOverlayType.NONE) return

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        when (gridType) {
            GridOverlayType.RULE_OF_THIRDS -> {
                val gridColor = Color.White.copy(alpha = 0.35f)
                drawLine(gridColor, Offset(w / 3f, 0f), Offset(w / 3f, h), strokeWidth = 1f)
                drawLine(gridColor, Offset(2 * w / 3f, 0f), Offset(2 * w / 3f, h), strokeWidth = 1f)
                drawLine(gridColor, Offset(0f, h / 3f), Offset(w, h / 3f), strokeWidth = 1f)
                drawLine(gridColor, Offset(0f, 2 * h / 3f), Offset(w, 2 * h / 3f), strokeWidth = 1f)
            }

            GridOverlayType.GOLDEN_RATIO -> {
                val gridColor = Color.White.copy(alpha = 0.4f)
                val x1 = w * 0.382f
                val x2 = w * 0.618f
                val y1 = h * 0.382f
                val y2 = h * 0.618f
                drawLine(gridColor, Offset(x1, 0f), Offset(x1, h), strokeWidth = 1.2f)
                drawLine(gridColor, Offset(x2, 0f), Offset(x2, h), strokeWidth = 1.2f)
                drawLine(gridColor, Offset(0f, y1), Offset(w, y1), strokeWidth = 1.2f)
                drawLine(gridColor, Offset(0f, y2), Offset(w, y2), strokeWidth = 1.2f)
            }

            GridOverlayType.CROSSHAIR -> {
                val gridColor = FocusPeakingGreen.copy(alpha = 0.6f)
                val cx = w / 2f
                val cy = h / 2f
                drawLine(gridColor, Offset(cx - 30f, cy), Offset(cx + 30f, cy), strokeWidth = 2f)
                drawLine(gridColor, Offset(cx, cy - 30f), Offset(cx, cy + 30f), strokeWidth = 2f)
                drawCircle(gridColor, radius = 40f, center = Offset(cx, cy), style = Stroke(width = 1.5f))
            }

            GridOverlayType.FIBONACCI_SPIRAL -> {
                val gridColor = CameraAccentGold.copy(alpha = 0.5f)
                val path = Path()
                path.moveTo(0f, 0f)
                path.quadraticTo(w * 0.618f, 0f, w * 0.618f, h * 0.618f)
                path.quadraticTo(w * 0.618f, h, w, h)
                drawPath(path, color = gridColor, style = Stroke(width = 1.8f))
            }

            else -> {}
        }
    }
}

@Composable
private fun SimulatedTelephotoFeed(settings: CameraSettingsState) {
    val sampleResId = if (settings.shootingMode == ShootingMode.TELEMACRO) {
        R.drawable.img_telemacro_sample_1784850526558
    } else {
        R.drawable.img_tele_portrait_1784850515502
    }

    Image(
        painter = painterResource(id = sampleResId),
        contentDescription = "Simulated Viewfinder",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}
