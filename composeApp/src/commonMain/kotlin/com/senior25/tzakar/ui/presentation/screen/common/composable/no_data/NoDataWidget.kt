package com.senior25.tzakar.ui.presentation.screen.common.composable.no_data

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH3
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.no_data_available

@Composable
fun NoDataWidget(
    modifier: Modifier,
    title:String?= null,
    description:String?= null,
) {
    Box(modifier = modifier){
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp).align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = "logo",
                modifier = Modifier.size(80.dp),
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title?:stringResource(Res.string.no_data_available),
                style = fontH3,
                color = MyColors.colorDarkBlue,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center

            )

            description?.let{
                Text(
                    text =it,
                    fontSize = 17.sp,
                    color = MyColors.colorDarkBlue,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }


            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

