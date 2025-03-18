package com.senior25.tzakar.ui.presentation.screen.main.notification_history.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.ui.presentation.components.separator.Separator
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH3
import com.senior25.tzakar.ui.theme.fontParagraphM

@Composable
fun NotificationCardWidget(
    modifier: Modifier,
    item: NotificationModel?,
    border:Shape = RoundedCornerShape(8.dp),
    addSpacer:Boolean = true,
    interaction: NotificationItemInteraction? = null
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(border)
            .background(MyColors.colorWhite)
            .padding(2.dp)
            .clickable { interaction?.onNotificationClick(item) }
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = item?.title?:"",
                        style = fontH3,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = item?.body?:"",
                        style = fontParagraphM,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Box(modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp)) {
            if (addSpacer) Separator()
        }
    }
}

interface NotificationItemInteraction{
    fun onNotificationClick(data: NotificationModel?)
}