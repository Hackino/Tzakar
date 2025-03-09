package com.senior25.tzakar.ui.presentation.dialog.base


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.button.OutlinedCustomButton
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphS

@Composable
fun BaseDialog(
    title: String? = null,
    description: String? = null,
    okButton: String? = null,
    cancelButton: String? = null,
    icon: Int? = null,
    dismiss: Boolean = true,
    onDismiss:()->Unit? = {  },
    onConfirm:(()->Unit)? = null,
    onCancel:(()->Unit)? = null,
    okButtonColor:Color = MyColors.colorPurple,
) {
    Dialog(
        properties = DialogProperties(
//            decorFitsSystemWindows = true,
            dismissOnClickOutside = dismiss,
            usePlatformDefaultWidth = true,
            dismissOnBackPress = dismiss,
        ),
        onDismissRequest = { onDismiss.invoke() },
        content ={
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp)
                    .border(1.dp, MyColors.colorLightGrey,RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                title?.let {
                    Text(
                        text = title,
                        color =  MyColors.colorDarkBlue,
                        style = fontLink,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    )
                }

                description?.let {
                    Text(
                        text = description,
                        color = MyColors.colorLightDarkBlue,
                        style = fontParagraphS,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    okButton?.let {
                        CustomButton(
                            modifier = Modifier.weight(1f),
                            text = okButton,
                            buttonColor = okButtonColor,
                            onClick = { onDismiss?.invoke();onConfirm?.invoke() }
                        )
                    }
                    cancelButton?.let {
                        OutlinedCustomButton(
                            modifier = Modifier.weight(1f),
                            text = cancelButton,
                            onClick = {
                                onDismiss?.invoke()
                                onCancel?.invoke()
                            },
                        )
                    }
                }
            }
        }
    )
}