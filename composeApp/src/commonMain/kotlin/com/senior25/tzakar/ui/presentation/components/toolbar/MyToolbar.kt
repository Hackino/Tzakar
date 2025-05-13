package com.senior25.tzakar.ui.presentation.components.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.ui.presentation.components.debounce.rememberDebounceClick
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.painterResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_back

@Composable
fun MyTopAppBar(

    title:String,
    showBack:Boolean? = true ,
    centerTitle:Boolean = true,
    interaction : BackPressInteraction? = null
) {

    val debouncedOnClick = rememberDebounceClick {  interaction?.onBackPress()  }

    TopAppBar(
        modifier = Modifier.background(MyColors.colorPurple).statusBarsPadding(),
        title = {
            Text(
                title,
                textAlign = if (centerTitle) TextAlign.Center else TextAlign.Start,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        },
        elevation = 0.dp,
        navigationIcon = if (showBack == true){
            {
                IconButton(
                    modifier = Modifier
                        .size(30.dp)
                        .fillMaxHeight(),
                    onClick = debouncedOnClick
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        } else null,
        actions = {
            IconButton(
                modifier = Modifier
                    .size(30.dp)
                    .fillMaxHeight(),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.Transparent
                )
            }
        },
        backgroundColor = MyColors.colorPurple
//        contentColor = TopAppBar}.centerAlignedTopAppBarColors(
//            containerColor =MyColors.colorPurple,
//            titleContentColor = Color.White,
//            navigationIconContentColor = Color.White
//        )
    )
}
interface BackPressInteraction {

    fun onBackPress()
}

