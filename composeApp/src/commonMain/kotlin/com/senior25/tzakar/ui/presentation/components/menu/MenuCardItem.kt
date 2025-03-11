package com.senior25.tzakar.ui.presentation.components.menu


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.senior25.tzakar.data.local.model.menu.MenuModel
import com.senior25.tzakar.platform_specific.ui.spToDp
import com.senior25.tzakar.ui.presentation.components.image.LoadMediaImage
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontParagraphS
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_warning

@Composable
fun MenuCardItem(
    modifier: Modifier,
    card: MenuModel?,
    selected:Boolean? = false,
    onClick:(MenuModel?)->Unit
) {

    println(card.toString() +" "+ selected.toString())
    val lineHeight = spToDp(sp = 12.sp)
    val twoLineHeight = lineHeight * 2

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                BorderStroke(
                    1.dp, if (selected== true) MyColors.colorDarkBlue else  MyColors.colorLightGrey
                ),
                RoundedCornerShape(8.dp)
            )
            .background(Color.White)
            .clickable { onClick(card) }
            .padding(horizontal = 2.dp, vertical = 12.dp),
    ) {
        LoadMediaImage(
            modifier =Modifier.size(48.dp),
            url = card?.icon,
            default = card?.iconRes?:Res.drawable.ic_warning,
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .height(twoLineHeight)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = card?.title ?: "",
                color = MyColors.colorDarkBlue ,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                style = fontParagraphS,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
