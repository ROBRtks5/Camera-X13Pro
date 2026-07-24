package com.example.model

data class CameraSettingsState(
    val shootingMode: ShootingMode = ShootingMode.MAIN_1INCH_RAW,
    val zoomFactor: Float = 1.0f, // 1.0f (Sony IMX989 23mm), 3.2f (75mm Tele), 5.0f (Telemacro)
    val processingMode: SensorProcessingMode = SensorProcessingMode.PURE_RAW_NO_PROCESSING,
    val isFocusPeakingEnabled: Boolean = true,
    val focusPeakingColor: FocusPeakingColor = FocusPeakingColor.GREEN,
    val focusDistanceCm: Float = 100f, // 10cm to 500cm (5m+)
    val isAutoDistance: Boolean = true,
    val isoValue: Int = 100, // 50 to 3200 (Sony IMX989 Native ISO 50)
    val isAutoIso: Boolean = true,
    val shutterSpeedSec: String = "1/250", // "1/8000", "1/2000", "1/500", "1/250", "1/60", "1s", "5s"
    val isAutoShutter: Boolean = true,
    val evCompensation: Float = 0f, // -3.0 to +3.0
    val whiteBalanceKelvin: Int = 5500, // 2000K to 8000K
    val isAutoWb: Boolean = true,
    val portraitStyle: PortraitLensStyle = PortraitLensStyle.MAIN_23MM,
    val simulatedAperture: Float = 1.9f, // f/1.9 native Sony IMX989 / f/2.0 JN1 Tele
    val leicaProfile: LeicaColorProfile = LeicaColorProfile.AUTHENTIC,
    val gridType: GridOverlayType = GridOverlayType.RULE_OF_THIRDS,
    val isHistogramEnabled: Boolean = true,
    val isZebraEnabled: Boolean = false,
    val isTorchOn: Boolean = false,
    val isRawMode: Boolean = true, // Default to RAW for uncompressed camera look
    val isDisableDenoise: Boolean = true, // Bypasses noise reduction software algorithms
    val isWatermarkFrameEnabled: Boolean = true,
    val timerSeconds: Int = 0, // 0, 2, 5, 10
    val aspectRatio: String = "4:3" // "4:3", "16:9", "1:1", "3:2"
)

enum class FocusPeakingColor(val hexColor: Long, val nameRu: String) {
    GREEN(0xFF00FF66, "Зеленый"),
    RED(0xFFFF3366, "Красный"),
    YELLOW(0xFFFFD600, "Желтый"),
    CYAN(0xFF00E5FF, "Голубой")
}
