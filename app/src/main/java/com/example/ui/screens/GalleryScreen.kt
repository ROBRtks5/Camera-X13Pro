package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoSizeSelectLarge
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.PhotoEntity
import com.example.ui.components.LeicaFrameView
import com.example.ui.theme.CameraAccentGold
import com.example.ui.theme.CameraObsidian
import com.example.ui.theme.CameraSurfaceBorder
import com.example.ui.theme.CameraSurfaceDark
import com.example.ui.theme.FocusPeakingGreen
import com.example.ui.theme.LeicaRed
import com.example.viewmodel.CameraViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit
) {
    val photos by viewModel.savedPhotos.collectAsState()
    var selectedPhoto by remember { mutableStateOf<PhotoEntity?>(null) }
    var showCropComparison by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Галерея TeleCam Pro (${photos.size})",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CameraObsidian
                )
            )
        },
        containerColor = CameraObsidian
    ) { innerPadding ->
        if (selectedPhoto != null) {
            // Detailed Inspector View
            PhotoDetailView(
                photo = selectedPhoto!!,
                onClose = { selectedPhoto = null },
                onDelete = {
                    viewModel.deletePhoto(selectedPhoto!!.id)
                    selectedPhoto = null
                },
                onToggleFavorite = {
                    viewModel.toggleFavorite(selectedPhoto!!)
                    selectedPhoto = selectedPhoto!!.copy(isFavorite = !selectedPhoto!!.isFavorite)
                },
                onSaveNote = { note ->
                    viewModel.updatePhotoNote(selectedPhoto!!, note)
                },
                onToggleCropCompare = {
                    showCropComparison = !showCropComparison
                },
                showCropComparison = showCropComparison,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            // Photo Grid View
            if (photos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Пусто",
                            tint = CameraAccentGold,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Снимков пока нет",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Перейдите в режим кадра и сделайте фото 75мм телеобъективом",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(photos) { photo ->
                        PhotoGridItem(
                            photo = photo,
                            onClick = { selectedPhoto = photo }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoGridItem(
    photo: PhotoEntity,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CameraSurfaceDark),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
            ) {
                val imageFile = File(photo.filePath)
                if (imageFile.exists()) {
                    AsyncImage(
                        model = imageFile,
                        contentDescription = photo.modeName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback sample image
                    val fallbackRes = if (photo.modeName.contains("МАКРО")) {
                        R.drawable.img_telemacro_sample_1784850526558
                    } else {
                        R.drawable.img_tele_portrait_1784850515502
                    }
                    Image(
                        painter = painterResource(id = fallbackRes),
                        contentDescription = photo.modeName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Mode Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(LeicaRed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = photo.modeName,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (photo.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Избранное",
                        tint = LeicaRed,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(18.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "${photo.focalLengthDesc} • ${String.format("%.1fx", photo.zoomFactor)}",
                    color = CameraAccentGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(photo.timestamp)),
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun PhotoDetailView(
    photo: PhotoEntity,
    onClose: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit,
    onSaveNote: (String) -> Unit,
    onToggleCropCompare: () -> Unit,
    showCropComparison: Boolean,
    modifier: Modifier = Modifier
) {
    var noteText by remember(photo.note) { mutableStateOf(photo.note) }
    var isEditingNote by remember { mutableStateOf(false) }

    val dateStr = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(Date(photo.timestamp))

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Image View with Leica Footer Frame
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CameraSurfaceDark),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(16.dp))
        ) {
            Column {
                val imageFile = File(photo.filePath)
                if (showCropComparison) {
                    // 1x Digital Crop vs 3.2x Native Telephoto Comparison
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(1.dp, Color.Red)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_tele_portrait_1784850515502),
                                contentDescription = "1x Crop",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                "1x Цифровой зум\n(Размыто)",
                                color = Color.Red,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .background(Color.Black.copy(alpha = 0.7f))
                                    .padding(4.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(1.dp, FocusPeakingGreen)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_telemacro_sample_1784850526558),
                                contentDescription = "3.2x Tele",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                "3.2x Оптический Tele\n(100% Четкость)",
                                color = FocusPeakingGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .background(Color.Black.copy(alpha = 0.7f))
                                    .padding(4.dp)
                            )
                        }
                    }
                } else if (imageFile.exists()) {
                    AsyncImage(
                        model = imageFile,
                        contentDescription = photo.modeName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    )
                } else {
                    val fallbackRes = if (photo.modeName.contains("МАКРО")) {
                        R.drawable.img_telemacro_sample_1784850526558
                    } else {
                        R.drawable.img_tele_portrait_1784850515502
                    }
                    Image(
                        painter = painterResource(id = fallbackRes),
                        contentDescription = photo.modeName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    )
                }

                // Leica Watermark Frame
                if (photo.isFramed) {
                    LeicaFrameView(
                        modeTitle = photo.modeName,
                        focalLengthDesc = photo.focalLengthDesc,
                        iso = photo.iso,
                        shutter = photo.shutterSpeed,
                        ev = photo.ev,
                        timestampStr = dateStr
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Toolbar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onToggleCropCompare,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showCropComparison) LeicaRed else CameraSurfaceDark
                )
            ) {
                Icon(imageVector = Icons.Default.PhotoSizeSelectLarge, contentDescription = "Сравнить")
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (showCropComparison) "Обычный вид" else "Сравнить Кроп")
            }

            Row {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (photo.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Избранное",
                        tint = LeicaRed
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // EXIF Inspector Table
        Card(
            colors = CardDefaults.cardColors(containerColor = CameraSurfaceDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = "EXIF Метаданные (Xiaomi 13 Pro Telephoto)",
                    color = CameraAccentGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExifRow("Камера / Сенсор:", "Xiaomi 13 Pro 75mm Floating Lens")
                ExifRow("Режим съемки:", photo.modeName)
                ExifRow("Фокусное расстояние:", "${photo.focalLengthDesc} (Эквивалент)")
                ExifRow("Оптический зум:", String.format("%.1fx", photo.zoomFactor))
                ExifRow("Дистанция фокусировки:", "${photo.focusDistanceCm.toInt()} см")
                ExifRow("Выдержка / ISO:", "${photo.shutterSpeed} • ISO ${photo.iso}")
                ExifRow("Экспокоррекция EV:", String.format("%+.1f EV", photo.ev))
                ExifRow("Цветовой профиль:", photo.leicaProfileName)
                ExifRow("Дата и время:", dateStr)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Note Editor
        Card(
            colors = CardDefaults.cardColors(containerColor = CameraSurfaceDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Заметка фотографа",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { isEditingNote = !isEditingNote }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Правка", tint = CameraAccentGold)
                    }
                }

                if (isEditingNote) {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LeicaRed,
                            unfocusedBorderColor = CameraSurfaceBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            onSaveNote(noteText)
                            isEditingNote = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LeicaRed),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp)
                    ) {
                        Text("Сохранить")
                    }
                } else {
                    Text(
                        text = if (noteText.isEmpty()) "Нажмите иконку карандаша, чтобы добавить заметку к снимку" else noteText,
                        color = if (noteText.isEmpty()) Color.Gray else Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ExifRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 11.sp)
        Text(text = value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}
