# Xiaomi 13 Pro Camera Pro (RAW & Telephoto Edition)

Специально разработанное приложение камеры, оптимизированное исключительно под модули **Xiaomi 13 Pro** (Android 16):
- **Основной модуль:** 1-дюймовый сенсор **Sony IMX989 (23mm f/1.9)**.
- **Телеобъектив:** **Samsung JN1 (75mm f/2.0)** с плавающей системой линз для макро от 10 см.

---

## 🚀 Как включить автоматическую сборку APK на GitHub (1 минута):

При отправке кода из AI Studio блокируются файлы каталога `.github/workflows/` (ограничение безопасности GitHub OAuth). Мы удалили этот файл из локальной папки, поэтому **экспорт на GitHub сейчас пройдет успешно без ошибок!**

После синхронизации с GitHub сделайте следующее, чтобы GitHub сам собрал вам APK:

1. На странице вашего репозитория на GitHub нажмите **Add file** ➔ **Create new file**.
2. Назовите файл: `.github/workflows/build-apk.yml`
3. Вставьте следующий код:

```yaml
name: Build Xiaomi 13 Pro Raw Camera APK

on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3

      - name: Create empty .env file if missing
        run: |
          if [ ! -f .env ]; then
            cp .env.example .env || touch .env
          fi

      - name: Build Debug APK
        run: gradle assembleDebug --stacktrace

      - name: Upload APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: Xiaomi13Pro-RawCamera-Debug-APK
          path: app/build/outputs/apk/debug/app-debug.apk
```

4. Нажмите кнопку **Commit changes...** (Сохранить изменения).
5. Перейдите во вкладку **Actions** в верхней части страницы GitHub.
6. Начнется сборка! Через 1–2 минуты в блоке **Artifacts** появится готовый файл **`Xiaomi13Pro-RawCamera-Debug-APK`**, который можно сразу скачать на смартфон.

---

## 📷 Ключевые особенности камеры:
- **Чистый RAW без искусственной обработки:** Принудительное отключение алгоритмического шумоподавления (`NOISE_REDUCTION_MODE_OFF`) и искусственной контурной резкости (`EDGE_MODE_OFF`) через Camera2Interop API на Android 16.
- **Тактильная отдача (Haptic Feedback):** Реалистичные клики и вибрация колесика зума и переключения режимов.
- **Leica Authentic Grain & DNG 14-bit:** Чистая сенсорная картинка с естественным пленочным зерном.
- **Эксклюзивная иконка:** Кастомная адаптивная иконка в стиле профессиональной камеры с объективом.

