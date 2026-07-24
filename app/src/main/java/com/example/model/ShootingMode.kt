package com.example.model

enum class ShootingMode(
    val titleRu: String,
    val descriptionRu: String,
    val defaultFocalLength: String,
    val defaultZoom: Float,
    val sensorName: String
) {
    MAIN_1INCH_RAW(
        titleRu = "ГЛАВНЫЙ 1\" IMX989",
        descriptionRu = "1-дюймовый сенсор Sony IMX989 23mm: чистый RAW без шумоподавления и искусственной резкости",
        defaultFocalLength = "23mm f/1.9 (Sony IMX989)",
        defaultZoom = 1.0f,
        sensorName = "Sony IMX989 1-Inch"
    ),
    PORTRAIT_75MM(
        titleRu = "ПОРТРЕТ 75мм",
        descriptionRu = "Телеобъектив Samsung JN1 75mm f/2.0: естественные пропорции без сглаживания кожи",
        defaultFocalLength = "75mm f/2.0 (Samsung JN1)",
        defaultZoom = 3.2f,
        sensorName = "Samsung JN1 Telephoto"
    ),
    TELEMACRO(
        titleRu = "ТЕЛЕМАКРО 10см",
        descriptionRu = "Плавающая группа линз: микроскопическое макро от 10 см с оптической четкостью",
        defaultFocalLength = "75mm Floating Lens",
        defaultZoom = 5.0f,
        sensorName = "Samsung JN1 Floating Lens"
    ),
    PRO_RAW_DNG(
        titleRu = "PRO RAW / DNG",
        descriptionRu = "Camera2 API на Android 16: ручной выдержка, ISO, RAW 14-bit без обработки",
        defaultFocalLength = "23mm / 75mm Manual",
        defaultZoom = 3.2f,
        sensorName = "Dual Sensor Raw"
    ),
    NIGHT_RAW_OIS(
        titleRu = "НОЧНОЙ RAW OIS",
        descriptionRu = "Аппаратная OIS стабилизация без 'пластикового' размытия ночного шума",
        defaultFocalLength = "23mm / 75mm Night OIS",
        defaultZoom = 3.2f,
        sensorName = "Dual OIS Sensor"
    )
}

enum class SensorProcessingMode(
    val titleRu: String,
    val descriptionRu: String
) {
    PURE_RAW_NO_PROCESSING("Чистый RAW (Без шумодава)", "Отключение шумоподавления и искусственной резкости для эффекта пленочного зерна"),
    UNPROCESSED_DNG("14-bit DNG Unprocessed", "Нативный сенсорный поток Android 16 Camera2 без сглаживания"),
    LEICA_AUTHENTIC_NATURAL("Leica Authentic Natural", "Оптический контраст Leica без бьюти-фильтров и размытия деталей")
}

enum class PortraitLensStyle(
    val titleRu: String,
    val focalMm: Int,
    val styleDesc: String
) {
    MAIN_23MM("23mm Sony IMX989 1-Inch", 23, "Объемный кадр 1-дюймовой матрицы"),
    DOCU_35MM("35mm Leica Document", 35, "Контрастный классический портрет Leica"),
    PETZVAL_50MM("50mm Swirly Bokeh", 50, "Закрученное размытие фона Petzval"),
    LEICA_75MM("75mm Floating Lens Master", 75, "Естественные пропорции лица и чистая текстура")
}

enum class LeicaColorProfile(
    val titleRu: String,
    val codeName: String
) {
    AUTHENTIC("Leica Authentic (Raw Grain)", "RAW_GRAIN"),
    NATURAL("Sony/Leica Pure Sensor", "PURE_SENSOR"),
    MONOCHROME("Leica Monochrom High-Contrast", "MONO_RAW")
}

enum class GridOverlayType(
    val titleRu: String
) {
    NONE("Без сетки"),
    RULE_OF_THIRDS("Сетка 3х3"),
    GOLDEN_RATIO("Золотое сечение"),
    FIBONACCI_SPIRAL("Спираль Фибоначчи"),
    CROSSHAIR("Центральный крест + Гироскоп")
}
