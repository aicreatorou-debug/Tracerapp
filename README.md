# Tracer Overlay

A minimal, ad-free floating reference-image tracing assistant for Android.

## What's included (as requested, broken down by file)
- `app/build.gradle.kts` — app-level Gradle config, 32-bit ABI targeting (`armeabi-v7a`, `x86`)
- `app/src/main/AndroidManifest.xml` — permissions + foreground service declaration
- `app/src/main/java/com/tracer/overlay/MainActivity.kt` — permission requests + Photo Picker
- `app/src/main/java/com/tracer/overlay/OverlayService.kt` — dual-window overlay, dual-mode touch system
- `app/src/main/res/layout/activity_main.xml` — main screen UI
- `app/src/main/res/layout/overlay_control_panel.xml` — floating bubble + mini control panel
- `app/src/main/res/drawable/*`, `app/src/main/res/values/*` — supporting resources
- `app/proguard-rules.pro` — placeholder

## Not included (standard Android Studio scaffold, not part of the ask)
This is a drop-in `app/` module. To build it you'll also need, at the project
root (Android Studio generates these automatically for a new project):
- `settings.gradle.kts` (with `include(":app")` and the usual `pluginManagement`/`dependencyResolutionManagement` repo blocks for Google + Maven Central)
- root `build.gradle.kts` (just the `plugins {}` block declaring the AGP + Kotlin plugin versions used above)
- `gradle/wrapper/*`, `gradlew`, `gradlew.bat`
- Launcher icons under `res/mipmap-*` (`@mipmap/ic_launcher`) — any standard adaptive icon works

## Architecture notes worth knowing before you build on this
- **Two overlay windows, not one.** The traced image and the control bubble
  are separate `WindowManager` windows on purpose. If they were one window,
  applying `FLAG_NOT_TOUCHABLE` for GHOST/trace mode would also block taps on
  the bubble — leaving no way to switch back to MOVE mode.
- **Default mode is MOVE**, so the user places/scales the image before
  flipping into GHOST/trace mode.
- **High-contrast "edge mode"** is a `ColorMatrix` contrast/desaturation
  boost, not true Sobel/Canny edge detection — it's a fast, dependency-free
  approximation appropriate for a "lightweight, no heavy libraries" app.
  Swapping in real edge detection later would mean processing the `Bitmap`
  pixels directly (e.g. RenderScript's successor or a small custom
  convolution) rather than a `ColorFilter`.
- **Position lock** freezes drag/scale by early-returning in the touch
  listener, and `onConfigurationChanged` is a no-op while locked so a
  rotation event can't silently shift `x`/`y`.
- `foregroundServiceType="specialUse"` targets API 34 behavior; the
  `PROPERTY_SPECIAL_USE_FGS_SUBTYPE` manifest property is required alongside it.
  
