package com.senior25.tzakar
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import com.senior25.tzakar.di.initializeKoin
import com.senior25.tzakar.di.permissionsModuleShared
import com.senior25.tzakar.platform_specific.map.NativeViewFactory
import com.senior25.tzakar.ui.presentation.app.App

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> { error("No shit") }

fun MainViewController(
    nativeViewFactory:NativeViewFactory
) = ComposeUIViewController(configure = { initializeKoin(config = {
    modules(permissionsModuleShared)

}) }) {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) { App() }
}