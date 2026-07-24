package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Portrait
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CameraAccentGold
import com.example.ui.theme.CameraObsidian
import com.example.ui.theme.CameraSurfaceBorder
import com.example.ui.theme.CameraSurfaceDark
import com.example.ui.theme.FocusPeakingGreen
import com.example.ui.theme.LeicaRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterGuideScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Гид по телеобъективу 75мм",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CameraSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LeicaRed, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LeicaRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraEnhance,
                                contentDescription = "Leica 75mm",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Xiaomi 13 Pro • Leica 75mm",
                                color = CameraAccentGold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Плавающая линза & Суперобъектив",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "В отличие от обычных смартфонов, Xiaomi 13 Pro оснащен уникальным телеобъективом 75мм с плавающей группой линз. Линзы смещаются внутри объектива, позволяя сфокусироваться как на портретах от 1.5м, так и на супер-макро с расстояния всего 10 см!",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            // Telemacro Section
            GuideSectionCard(
                title = "1. Режим 'Телемакро' (Фокус от 10 см)",
                icon = Icons.Default.CenterFocusStrong,
                accentColor = FocusPeakingGreen,
                description = "Плавающая линза разделена на две группы. При приближении к объекту задняя группа линз смещается вперед, обеспечивая микроскопическую резкость.",
                tips = listOf(
                    "Держите смартфон на расстоянии 10-15 см от объекта.",
                    "Включите 'Focus Peaking' в приложении — зеленые контуры подскажут идеальную зону резкости.",
                    "Используйте зум 5x Macro для максимального увеличения узоров, капель воды или деталей часов."
                ),
                imageRes = R.drawable.img_telemacro_sample_1784850526558
            )

            // Portrait 75mm Section
            GuideSectionCard(
                title = "2. Портрет 75мм (Естественные пропорции)",
                icon = Icons.Default.Portrait,
                accentColor = CameraAccentGold,
                description = "Фокусное расстояние 75мм — золотой стандарт классической портретной фотографии. Оно устраняет искажения лица, характерные для широкоугольных камер.",
                tips = listOf(
                    "Выберите стиль '75mm Leica Master' для естественного сжатия перспективы и мягкого боке.",
                    "Оптимальное расстояние до модели — 1.5 - 2.5 метра.",
                    "Попробуйте стиль '50mm Petzval' для создания художественного закрученного размытия фона."
                ),
                imageRes = R.drawable.img_tele_portrait_1784850515502
            )

            // Night Tele & OIS
            GuideSectionCard(
                title = "3. Ночной теле-режим и OIS",
                icon = Icons.Default.NightlightRound,
                accentColor = Color(0xFF81D4FA),
                description = "Оптическая стабилизация OIS компенсирует дрожание рук при съемке удаленных объектов ночью.",
                tips = listOf(
                    "Для съемки луны или ночного города зафиксируйте экспозицию EV на -1.0.",
                    "В ручном режиме PRO выберите выдержку 1/15s и ISO 400 - 800.",
                    "Включите таймер 2с перед нажатием спуска, чтобы исключить толчок пальца."
                ),
                imageRes = null
            )

            // Sports & Action
            GuideSectionCard(
                title = "4. Спорт и Сверхбыстрая съемка",
                icon = Icons.Default.Speed,
                accentColor = LeicaRed,
                description = "Телеобъектив 75мм в сочетании со спускной выдержкой 1/2000s замораживает капли воды, движения спортсменов или бег животных.",
                tips = listOf(
                    "Включите режим 'Спорт' для автоматической блокировки короткой выдержки.",
                    "Нажмите и удерживайте кнопку затвора для серийной съемки."
                ),
                imageRes = null
            )
        }
    }
}

@Composable
private fun GuideSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    description: String,
    tips: List<String>,
    imageRes: Int?
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CameraSurfaceDark),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CameraSurfaceBorder, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            if (imageRes != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Советы съемки:",
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            tips.forEach { tip ->
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = "• ", color = accentColor, fontSize = 11.sp)
                    Text(
                        text = tip,
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
