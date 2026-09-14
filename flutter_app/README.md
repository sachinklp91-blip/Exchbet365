# Dual Web - Flutter & Android Application

A modern dual-tab WebView Android application built with Material Design 3.

## Features

- **Dual-Tab Browsing**:
  - **Tab 1 (ExchBet)**: Loads `https://exchbet365.com`
  - **Tab 2 (Crex)**: Loads `https://www.crex.com`
- **Session & Cookie Persistence**: Automatic cookie and session storage preservation using Android WebStorage and CookieManager.
- **Pull-to-Refresh**: Native pull-to-refresh on both tabs to reload content easily.
- **Top Loading Indicator**: Real-time progress bar indicating page loading status.
- **Graceful Error Handling**: Custom offline/network error cards with Retry and Home navigation buttons.
- **Smart Back Navigation**: Back gesture/button navigates backward inside the active WebView history before switching tabs or exiting.
- **Material Design 3**: Modern M3 bottom navigation bar, dynamic styling, and typography.
- **Dark Mode Support**: Seamless light and dark theme adaptation.
- **Tab State Preservation**: Uses `IndexedStack` (Flutter) and z-indexed multi-webview caching (Native Android) so switching tabs does not reload pages or drop active user sessions.

## Project Structure

```
├── app/                             # Native Android Jetpack Compose Project (Runnable in AI Studio Streaming Emulator)
│   ├── src/main/java/com/example/
│   │   ├── MainActivity.kt
│   │   ├── model/WebTab.kt
│   │   ├── ui/MainScreen.kt
│   │   └── ui/components/WebViewContainer.kt
│   └── src/main/AndroidManifest.xml
└── flutter_app/                     # Complete Flutter Source Code
    ├── pubspec.yaml
    ├── lib/
    │   ├── main.dart
    │   ├── models/tab_config.dart
    │   ├── screens/main_screen.dart
    │   ├── theme/app_theme.dart
    │   └── widgets/webview_tab_view.dart
    └── android/app/src/main/AndroidManifest.xml
```

## How to Run the Flutter App

1. Ensure Flutter SDK (>= 3.0.0) is installed.
2. Navigate to the flutter app folder:
   ```bash
   cd flutter_app
   ```
3. Install dependencies:
   ```bash
   flutter pub get
   ```
4. Run on an Android device or emulator:
   ```bash
   flutter run
   ```
