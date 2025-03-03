package com.senior25.tzakar.platform_specific.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIScreen
import kotlinx.cinterop.CValue
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGSize

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth(): Dp = LocalWindowInfo.current.containerSize.width.pxToPoint().dp


@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenHeight(): Dp = LocalWindowInfo.current.containerSize.height.pxToPoint().dp

fun Int.pxToPoint(): Double = this.toDouble() / UIScreen.mainScreen.scale

