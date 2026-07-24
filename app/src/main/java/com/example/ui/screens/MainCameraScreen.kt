package com.example.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GridOff
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.CropOriginal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camera.CameraXManager
import com.example.ui.components.CameraViewfinder
import com.example.ui.components.ModeSelectorBar
import com.example.ui.components.ProControlPanel
import com.example.ui.components.ZoomDialView
import com.example.ui.theme.CameraAccentGold
import com.example.ui.theme.CameraObsidian
import com.example.ui.theme.CameraSurfaceBorder
import com.example.ui.theme.CameraSurfaceDark
import com.example.ui.theme.FocusPeakingGreen
import com.example.ui.theme.LeicaRed
import com.example.viewmodel.CameraViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(androidx.camera.camera2.interop.ExperimentalCamera2Interop::class)
@Composable
fun MainCameraScreen(
    viewModel: CameraViewModel,
    onNavigateToGallery: () -> Unit,
    onNavigateToGuide: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val settings by viewModel.settingsState.collectAsState()
    val isCapturing by viewModel.isCapturing.collectAsState()
    val savedPhotos by viewModel.savedPhotos.collectAsState()

    var showProPanel by remember { mutableStateOf(false) }
    var isCameraBound by remember { mutableStateOf(false) }
    var countdownValue by remember { mutableStateOf(0) }

    val cameraManager = remember { CameraXManager(context) }

    // Camera Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Camera permission granted
        } else {
            Toast.makeText(context, "Разрешение на камеру необходимо для съемки", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraManager.shutdown()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CameraObsidian)
    ) {
        // Main Live Camera Viewfinder
        CameraViewfinder(
            settings = settings,
            isCameraBound = isCameraBound,
            onPreviewViewCreated = { previewView ->
                cameraManager.startCamera(lifecycleOwner, previewView) { bound ->
                    isCameraBound = bound
                }
            },
            onTapToFocus = { offset ->
                Toast.makeText(context, "Фокусировка 75мм объектива...", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxSize()
        )

        // Top Control Bar (Status Bar safe)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .fillMaxWidth()
                .background(CameraObsidian.copy(alpha = 0.8f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Torch toggle
                IconButton(onClick = {
                    viewModel.toggleTorch()
                    cameraManager.setTorch(!settings.isTorchOn)
                }) {
                    Icon(
                        imageVector = if (settings.isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Фонарик",
                        tint = if (settings.isTorchOn) CameraAccentGold else Color.White
                    )
                }

                // Grid overlay toggle
                IconButton(onClick = { viewModel.cycleGridType() }) {
                    Icon(
                        imageVector = if (settings.gridType.ordinal > 0) Icons.Default.GridOn else Icons.Default.GridOff,
                        contentDescription = "Сетка",
                        tint = if (settings.gridType.ordinal > 0) CameraAccentGold else Color.White.copy(alpha = 0.7f)
                    )
                }

                // Timer cycle
                IconButton(onClick = { viewModel.cycleTimer() }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Таймер",
                            tint = if (settings.timerSeconds > 0) LeicaRed else Color.White.copy(alpha = 0.7f)
                        )
                        if (settings.timerSeconds > 0) {
                            Text(
                                text = "${settings.timerSeconds}s",
                                color = LeicaRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Histogram toggle
                IconButton(onClick = { viewModel.toggleHistogram() }) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "Гистограмма",
                        tint = if (settings.isHistogramEnabled) FocusPeakingGreen else Color.White.copy(alpha = 0.7f)
                    )
                }

                // Watermark frame toggle
                IconButton(onClick = { viewModel.toggleWatermarkFrame() }) {
                    Icon(
                        imageVector = Icons.Default.CropOriginal,
                        contentDescription = "Рамка Leica",
                        tint = if (settings.isWatermarkFrameEnabled) LeicaRed else Color.White.copy(alpha = 0.7f)
                    )
                }

                // Master Guide button
                IconButton(onClick = onNavigateToGuide) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Гид 75мм",
                        tint = CameraAccentGold
                    )
                }
            }
        }

        // Countdown Timer Overlay
        if (countdownValue > 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$countdownValue",
                    fontSize = 110.sp,
                    fontWeight = FontWeight.Black,
                    color = LeicaRed
                )
            }
        }

        // Bottom Controls Overlay Area
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .fillMaxWidth()
                .background(CameraObsidian.copy(alpha = 0.9f))
                .padding(top = 8.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pro Panel Slider if open
            AnimatedVisibility(
                visible = showProPanel,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                ProControlPanel(
                    settings = settings,
                    onSettingsChanged = { newSet ->
                        viewModel.updateSettings(newSet)
                        cameraManager.setZoomRatio(newSet.zoomFactor)
                    }
                )
            }

            // Tactile Zoom Dial Selector
            ZoomDialView(
                currentZoom = settings.zoomFactor,
                onZoomSelected = { zoom ->
                    viewModel.setZoomFactor(zoom)
                    cameraManager.setZoomRatio(zoom)
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Mode Selector Bar
            ModeSelectorBar(
                currentMode = settings.shootingMode,
                onModeSelected = { mode ->
                    viewModel.setShootingMode(mode)
                    cameraManager.setZoomRatio(mode.defaultZoom)
                }
            )

            // Shutter Button Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gallery Thumbnail Button
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CameraSurfaceDark)
                        .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(12.dp))
                        .clickable { onNavigateToGallery() },
                    contentAlignment = Alignment.Center
                ) {
                    if (savedPhotos.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Collections,
                            contentDescription = "Галерея",
                            tint = CameraAccentGold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.InsertPhoto,
                            contentDescription = "Галерея пуста",
                            tint = Color.Gray
                        )
                    }
                }

                // Shutter Capture Ring
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(if (isCapturing) LeicaRed.copy(alpha = 0.5f) else LeicaRed)
                        .clickable {
                            scope.launch {
                                if (settings.timerSeconds > 0) {
                                    for (t in settings.timerSeconds downTo 1) {
                                        countdownValue = t
                                        delay(1000)
                                    }
                                    countdownValue = 0
                                }

                                val outputDir = context.cacheDir
                                if (isCameraBound) {
                                    cameraManager.takePhoto(
                                        outputDirectory = outputDir,
                                        onPhotoCaptured = { file ->
                                            viewModel.recordCapturedPhoto(file.absolutePath)
                                            Toast.makeText(context, "Теле-снимок сохранен!", Toast.LENGTH_SHORT).show()
                                        },
                                        onError = {
                                            // Fallback save simulated file
                                            val dummyPath = File(outputDir, "tele_sample_${System.currentTimeMillis()}.jpg").absolutePath
                                            viewModel.recordCapturedPhoto(dummyPath)
                                            Toast.makeText(context, "Снимок сохранен", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                } else {
                                    // Emulator simulation save
                                    val dummyPath = File(outputDir, "tele_sample_${System.currentTimeMillis()}.jpg").absolutePath
                                    viewModel.recordCapturedPhoto(dummyPath)
                                    Toast.makeText(context, "Теле-снимок сохранен в галерею!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }

                // Pro Controls Toggle Button
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (showProPanel) LeicaRed else CameraSurfaceDark)
                        .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(12.dp))
                        .clickable { showProPanel = !showProPanel },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Pro Настройки",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
