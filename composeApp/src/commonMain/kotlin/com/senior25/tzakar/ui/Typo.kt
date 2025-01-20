package com.senior25.tzakar.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.gilroy_black
import org.jetbrains.compose.resources.Font
import tzakar_reminder.composeapp.generated.resources.Gilroy_Bold
import tzakar_reminder.composeapp.generated.resources.Gilroy_Light
import tzakar_reminder.composeapp.generated.resources.Gilroy_Medium
import tzakar_reminder.composeapp.generated.resources.Gilroy_Regular
import tzakar_reminder.composeapp.generated.resources.Gilroy_SemiBold

@Composable fun FontBlack() = FontFamily(Font(Res.font.gilroy_black))
@Composable fun FontBold() = FontFamily(Font(Res.font.Gilroy_Bold))
@Composable fun FontSemiBold() = FontFamily(Font(Res.font.Gilroy_SemiBold))
@Composable fun FontMedium() = FontFamily(Font(Res.font.Gilroy_Medium))
@Composable fun FontRegular() = FontFamily(Font(Res.font.Gilroy_Regular))
@Composable fun FontLight() = FontFamily(Font(Res.font.Gilroy_Light))

val fontH1
    @Composable
    get() = TextStyle(fontSize = 24.sp, fontFamily = FontBlack())

val fontH2
    @Composable
    get() = TextStyle(fontSize = 21.sp, fontFamily = FontBold())

val fontH3
    @Composable
    get() = TextStyle(fontSize = 18.sp, fontFamily = FontBold())

val fontHighlight
    @Composable
    get() = TextStyle(fontSize = 17.sp, fontFamily = FontSemiBold())

val fontLink
    @Composable
    get() = TextStyle(fontSize = 16.sp, fontFamily = FontBold())

val fontParagraphL
    @Composable
    get() = TextStyle(fontSize = 16.sp, fontFamily = FontRegular())

val fontParagraphM
    @Composable
    get() = TextStyle(fontSize = 15.sp, fontFamily = FontRegular())

val fontParagraphS
    @Composable
    get() = TextStyle(fontSize = 12.sp, fontFamily = FontSemiBold())

val fontTab
    @Composable
    get() = TextStyle(fontSize = 16.sp, fontFamily = FontRegular())

