package com.senior25.tzakar.ui.presentation.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senior25.tzakar.ui.presentation.components.debounce.debounceClick
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontLink


@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick:()->Unit = {},
    isEnabled:Boolean? = true,
    text:String? = null,
    startIcon:Painter? = null,
    endIcon:Painter? = null,
    keepIconColor:Boolean? = null,

){
    Row(
        modifier.alpha(if (isEnabled==true)1f else 0.7f)
            .clip(CircleShape)
            .background(MyColors.colorPurple)
            .let {
                return@let if (isEnabled == true) { it.debounceClick { onClick()} } else it
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
            startIcon?.let {
                Icon(
                    painter =  it,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (isEnabled==true)
                        MyColors.colorWhite
                    else MyColors.colorWhite.copy(alpha = 0.5f)
                )
            }

            text?.let {
                Spacer(Modifier.width(4.dp))
                Text(
                    text = it,
                    style = fontLink,
                    fontSize = 16.sp,
                    color = if (isEnabled==true)
                        MyColors.colorWhite
                    else MyColors.colorWhite.copy(alpha = 0.5f)
                )
                Spacer(Modifier.width(4.dp))
            }

            endIcon?.let {
                Icon(
                    painter = endIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint =
                    if (keepIconColor == true) {
                        Color.Unspecified
                    } else if (isEnabled==true)
                        MyColors.colorWhite
                    else MyColors.colorWhite.copy(alpha = 0.5f)
                )
            }
        }
    }

@Composable
fun  OutlinedCustomButton(
    modifier: Modifier = Modifier,
    onClick:()->Unit = {},
    isEnabled:Boolean? = true,
    text:String? = null,
    startIcon:Painter? = null,
    endIcon:Painter? = null,
    keepIconColor:Boolean? = null,
    tint:Color = MyColors.colorLightDarkBlue,
    textColor:Color = MyColors.colorLightDarkBlue

){
    Row(
        modifier.alpha(if (isEnabled==true)1f else 0.7f)
            .clip(CircleShape)
            .border(1.dp, tint, CircleShape)
            .let {
                return@let if (isEnabled == true) { it.debounceClick { onClick()} } else it
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        startIcon?.let {
            Icon(
                painter = it,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (keepIconColor == true) {
                    Color.Unspecified
                } else MyColors.colorDarkBlue

            )
        }
        text?.let {
            Spacer(Modifier.width(4.dp))
            Text(text = it, style = fontLink, color =  textColor)
            Spacer(Modifier.width(4.dp))
        }
        endIcon?.let {
            Icon(
                painter =endIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (keepIconColor == true) Color.Unspecified else MyColors.colorDarkBlue

            )
        }
    }
}

