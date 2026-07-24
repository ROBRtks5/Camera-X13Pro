package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.PhotoEntity
import com.example.data.PhotoRepository
import com.example.model.CameraSettingsState
import com.example.model.GridOverlayType
import com.example.model.ShootingMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PhotoRepository

    init {
        val database = AppDatabase.getInstance(application)
        repository = PhotoRepository(database.photoDao())
    }

    val savedPhotos: StateFlow<List<PhotoEntity>> = repository.allPhotos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _settingsState = MutableStateFlow(CameraSettingsState())
    val settingsState: StateFlow<CameraSettingsState> = _settingsState.asStateFlow()

    private val _isCapturing = MutableStateFlow(false)
    val isCapturing: StateFlow<Boolean> = _isCapturing.asStateFlow()

    private val _lastCapturedPhoto = MutableStateFlow<PhotoEntity?>(null)
    val lastCapturedPhoto: StateFlow<PhotoEntity?> = _lastCapturedPhoto.asStateFlow()

    fun updateSettings(newSettings: CameraSettingsState) {
        _settingsState.value = newSettings
    }

    fun setShootingMode(mode: ShootingMode) {
        _settingsState.update { current ->
            current.copy(
                shootingMode = mode,
                zoomFactor = mode.defaultZoom,
                focusDistanceCm = if (mode == ShootingMode.TELEMACRO) 15f else 100f
            )
        }
    }

    fun setZoomFactor(zoom: Float) {
        _settingsState.update { it.copy(zoomFactor = zoom) }
    }

    fun cycleGridType() {
        _settingsState.update { current ->
            val modes = GridOverlayType.values()
            val nextIndex = (current.gridType.ordinal + 1) % modes.size
            current.copy(gridType = modes[nextIndex])
        }
    }

    fun toggleTorch() {
        _settingsState.update { it.copy(isTorchOn = !it.isTorchOn) }
    }

    fun toggleHistogram() {
        _settingsState.update { it.copy(isHistogramEnabled = !it.isHistogramEnabled) }
    }

    fun toggleWatermarkFrame() {
        _settingsState.update { it.copy(isWatermarkFrameEnabled = !it.isWatermarkFrameEnabled) }
    }

    fun cycleTimer() {
        _settingsState.update { current ->
            val nextTimer = when (current.timerSeconds) {
                0 -> 2
                2 -> 5
                5 -> 10
                else -> 0
            }
            current.copy(timerSeconds = nextTimer)
        }
    }

    fun recordCapturedPhoto(filePath: String) {
        viewModelScope.launch {
            _isCapturing.value = true
            val currentSettings = _settingsState.value

            val entity = PhotoEntity(
                filePath = filePath,
                timestamp = System.currentTimeMillis(),
                modeName = currentSettings.shootingMode.titleRu,
                focalLengthDesc = currentSettings.shootingMode.defaultFocalLength,
                zoomFactor = currentSettings.zoomFactor,
                iso = currentSettings.isoValue,
                shutterSpeed = currentSettings.shutterSpeedSec,
                ev = currentSettings.evCompensation,
                focusDistanceCm = currentSettings.focusDistanceCm,
                leicaProfileName = currentSettings.leicaProfile.titleRu,
                isFramed = currentSettings.isWatermarkFrameEnabled
            )

            val id = repository.insert(entity)
            val savedEntity = entity.copy(id = id)
            _lastCapturedPhoto.value = savedEntity
            _isCapturing.value = false
        }
    }

    fun deletePhoto(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun toggleFavorite(photo: PhotoEntity) {
        viewModelScope.launch {
            repository.update(photo.copy(isFavorite = !photo.isFavorite))
        }
    }

    fun updatePhotoNote(photo: PhotoEntity, note: String) {
        viewModelScope.launch {
            repository.update(photo.copy(note = note))
        }
    }
}
