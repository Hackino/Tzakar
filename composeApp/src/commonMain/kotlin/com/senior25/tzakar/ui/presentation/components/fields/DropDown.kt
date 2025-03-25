package com.senior25.tzakar.ui.presentation.components.fields

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senior25.tzakar.platform_specific.ui.getScreenHeight
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphS
import org.jetbrains.compose.resources.painterResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_arrow_down
import tzakar_reminder.composeapp.generated.resources.ic_arrow_forward_ios

@Composable
fun <T>  DropDownField(
    selectedItem: T?,
    displayValue: (T?) -> String,
    label:String? = null,
    startIcon:Painter? = null,
    endIcon: Painter? = null,
    onItemSelected: (T) -> Unit,
    isMandatory: Boolean? = null,
    placeHolder:String? = null,
    items:List<T>? = null,
    startIconClick:(()->Unit)? = null
    ){
    val selected by remember(selectedItem) { mutableStateOf(selectedItem) }

    Column  (modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        label?.let {
            Text(
                text = label,
                color = MyColors.colorDarkBlue,
                textAlign = TextAlign.Start,
                style = fontLink.copy(fontSize = 14.sp),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        DropdownMenuField(
            endIcon = endIcon ?: painterResource(Res.drawable.ic_arrow_down),
            selectedItem = selectedItem,
            items = items ?: emptyList(),
            startIcon = startIcon,
            onItemSelected = onItemSelected,
            displayValue = displayValue,
            showPlaceholder = true,
            isMandatory = isMandatory == true,
            placeholder = placeHolder ?: "",
            startIconClick =startIconClick
        )
    }
}


@Composable
fun <T> DropdownMenuField(
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    displayValue: (T) -> String,
    placeholder: String = "Select an option",
    showPlaceholder: Boolean = false,
    isMandatory: Boolean = true,
    startIcon: Painter?  = null,
    endIcon:Painter  = painterResource( Res.drawable.ic_arrow_forward_ios),
    startIconClick:(()->Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val list by remember(items) { mutableStateOf(items) }

    val selected by remember(selectedItem) { mutableStateOf(selectedItem) }

    val density = LocalDensity.current
    val screenHeight = getScreenHeight()
    var dropdownOffset by remember { mutableStateOf(0.dp) }


    val displayItems =  list.filter { it != selected }.ifEmpty { null }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            enabled = displayItems != null,
            onClick = {
                if (displayItems != null){
                    expanded = !expanded
                    dropdownOffset = calculateDropdownOffset(density, screenHeight)
                }
            },
            border = BorderStroke(1.dp,if (selected == null && isMandatory) MyColors.colorRed  else if (expanded) MyColors.colorLightDarkBlue else  MyColors.colorLightGrey.copy(alpha = 0.8f)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            shape = RoundedCornerShape(60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                startIcon?.let {
                    Icon(
                        painter =it,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(enabled = startIconClick!= null){
                                startIconClick?.invoke()
                            }
                            .align(Alignment.CenterVertically),
                        tint = MyColors.colorDarkBlue
                    )
                }

                (selected?.let { displayValue(it)  })?.ifEmpty { null }?.let {
                    Text(
                        modifier = Modifier.weight(1f),
                        text =it,
                        color =  MyColors.colorLightDarkBlue,
                        fontSize = 14.sp,
                        maxLines = 2,
                        style = fontParagraphS.copy(
                            fontSize = 14.sp,
                            lineHeight = TextUnit.Unspecified,
                        )
                    )
                }?:run {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = placeholder,
                        maxLines = 2,
                        style = fontParagraphS.copy(
                            fontSize = 14.sp,
                            color =  MyColors.colorLightDarkBlue.copy(alpha =   0.8f),
                            lineHeight = TextUnit.Unspecified,
                        )
                    )
                }

                Icon(
                    painter =endIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    tint = MyColors.colorDarkBlue
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = dropdownOffset),
            modifier = Modifier.fillMaxWidth().background(Color.White)
        ) {
            list.forEach { item ->
                androidx.compose.material.DropdownMenuItem(onClick = {
                    if (item !=  selected) onItemSelected(item)
                    expanded = false
                }) { Text(text = displayValue(item)) }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}



fun calculateDropdownOffset(density: Density, screenHeight: Dp): Dp {
    return with(density) {
        val availableSpaceBelow = screenHeight.toPx() / 2
        val dropdownHeight = 200.dp.toPx()
        val offset = if (availableSpaceBelow < dropdownHeight) -dropdownHeight.dp else 0.dp
        offset
    }
}