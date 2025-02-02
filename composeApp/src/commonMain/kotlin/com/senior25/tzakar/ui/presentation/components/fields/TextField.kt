package com.senior25.tzakar.ui.presentation.components.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphS
import com.senior25.tzakar.ui.theme.fontTab
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_password
import tzakar_reminder.composeapp.generated.resources.enter_username
import tzakar_reminder.composeapp.generated.resources.ic_eye_off
import tzakar_reminder.composeapp.generated.resources.ic_eye_on

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeHolder: String? = stringResource(Res.string.email_address),
    value:String? = null,
    onValueChange: (String) -> Unit = {},
    isInputValid:(Boolean)->Unit = {},
    imeAction: ImeAction? = null,
    focusRequester:FocusRequester? = null,
    onKeyPressed:()->Unit = {},
    leadingIcon: Painter? = null
) {
    BaseTextField(
        value = value,
        modifier = modifier,
        placeHolder = placeHolder,
        keyboardType = KeyboardType.Email,
        onValueChange =onValueChange,
        validate = {
            val isValid = isEmailValid(it)
            isInputValid(isValid.first)
            isValid
        },
        label = label,
        imeAction =  imeAction,
        focusRequester = focusRequester,
        onKeyPressed = onKeyPressed,
        leadingIcon = leadingIcon
    )
}

@Composable
fun userNameField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeHolder: String? = stringResource(Res.string.enter_username),
    value:String? = null,
    onValueChange: (String) -> Unit = {},
    isInputValid:(Boolean)->Unit = {},
    imeAction: ImeAction? = null,
    focusRequester:FocusRequester? = null,
    onKeyPressed:()->Unit = {},
    leadingIcon: Painter? = null
) {
    BaseTextField(
        modifier =modifier,
        value = value,
        placeHolder = placeHolder,
        keyboardType = KeyboardType.Text,
        onValueChange =onValueChange,
        validate = {
            val isValid = isNameValid(it)
            isInputValid(isValid.first)
            isValid
        },
        label = label,
        imeAction =  imeAction,
        focusRequester = focusRequester,
        onKeyPressed = onKeyPressed,
        leadingIcon = leadingIcon
    )
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeHolder: String? = stringResource(Res.string.enter_password),
    value:String? = null,
    onValueChange: (String) -> Unit = {},
    isInputValid:(Boolean)->Unit = {},
    imeAction: ImeAction? = null,
    focusRequester:FocusRequester? = null,
    onKeyPressed:()->Unit = {},
    leadingIcon: Painter?=  null,
    trailingIcon: Painter?=  null


) {
    BaseTextField(
        modifier = modifier,
        value = value,
        placeHolder = placeHolder,
        keyboardType = KeyboardType.Password,
        onValueChange =onValueChange,
        validate = {
            val isValid = isPasswordValid(it)
            isInputValid(isValid.first)
            isValid
        },
        label = label,
        imeAction =  imeAction,
        focusRequester = focusRequester,
        onKeyPressed = onKeyPressed,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon
    )
}

@Composable
fun ConfirmPasswordField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeHolder: String? = stringResource(Res.string.enter_password),
    value:String? = null,
    onValueChange: (String) -> Unit = {},
    isInputValid:(Boolean)->Unit = {},
    imeAction: ImeAction? = null,
    focusRequester:FocusRequester? = null,
    onKeyPressed:()->Unit = {},
    password:String? = null,
    leadingIcon: Painter?=  null,
    trailingIcon: Painter?=  null
) {
    BaseTextField(
        modifier = modifier,
        value = value,
        placeHolder = placeHolder,
        keyboardType = KeyboardType.Password,
        onValueChange =onValueChange,
        validate = {
            val isValid =  isConfirmPasswordValid(it,password)
            isInputValid(isValid.first)
            isValid
        },
        label = label,
        imeAction =  imeAction,
        focusRequester = focusRequester,
        onKeyPressed = onKeyPressed,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon
    )
}


//@Composable
//fun BaseTextFieldWithDatePicker(
//    value: String? = null,
//    label: String? = null,
//    placeHolder: String? = null,
//    validate: ((String) -> Pair<Boolean, String>)? = null,
//    onDateSelected: (String) -> Unit = {},
//    leadingIcon: String? = null
//) {
//    var content by remember { mutableStateOf(value ?: "") }
//    var error by remember { mutableStateOf("") }
//
//    // Handle DatePicker state
////    val context = LocalContext.current
////    val calendar = Calendar.getInstance()
////    val year = calendar.get(Calendar.YEAR)
////    val month = calendar.get(Calendar.MONTH)
////    val day = calendar.get(Calendar.DAY_OF_MONTH)
//
////    val datePickerDialog = DatePickerDialog(
////        context, com.base.commons.R.style.datepicker,
////        { _, selectedYear, selectedMonth, selectedDay ->
////            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
////            content = selectedDate
////            onDateSelected(selectedDate)
////        },
////        year, month, day
////    )
//
//    Column {
//        label?.let {
//            Text(
//                textAlign = TextAlign.Start,
//                text = it,
//                color = colorResource(R.color.main_grey),
//                style = a400_14,
//            )
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { datePickerDialog.show() } // Box handles click event
//        ) {
//            OutlinedTextField(
//                value = content,
//                onValueChange = { }, // No direct input allowed, controlled by DatePicker
//                readOnly = true, // Make it read-only
//                placeholder = {
//                    Text(
//                        textAlign = TextAlign.Start,
//                        text = makeMandatoryText(placeHolder),
//                        color = colorResource(R.color.main_grey),
//                        style = a400_14,
//                    )
//                },
//                leadingIcon = {
//                    leadingIcon?.let {
//
//                    }
//                },
//
//                textStyle = fontParagraphM,
//                colors = TextFieldDefaults.colors(
//                    cursorColor = colorResource(R.color.main_dark_blue),
//                    focusedTextColor = colorResource(R.color.main_dark_blue),
//                    unfocusedTextColor = colorResource(R.color.main_dark_blue),
//                    focusedContainerColor = colorResource(com.base.commons.R.color.transparent),
//                    errorContainerColor = colorResource(com.base.commons.R.color.transparent),
//                    unfocusedContainerColor = colorResource(com.base.commons.R.color.transparent),
//                    errorIndicatorColor = colorResource(com.base.commons.R.color.main_red),
//                    focusedIndicatorColor = colorResource(R.color.main_blue),
//                    unfocusedIndicatorColor = colorResource(R.color.main_light_grey)
//                ),
//                modifier = Modifier.fillMaxWidth() // Modifiers moved inside the Box
//            )
//
////            Box(
////                modifier = Modifier
////                    .matchParentSize()
////                    .background(Color.Transparent)
////                    .clickable { datePickerDialog.show()   }
////            )
////            {
////
////            }
//        }
//
//
//        error.ifEmpty { null }?.let {
//            Text(
//                textAlign = TextAlign.Start,
//                text = it,
//                color = MyColors.colorRed,
//                style = a400_14,
//            )
//        }
//    }
//}

@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    value: String? = null,
    label:String? = null,
    placeHolder:String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    validate:@Composable ((String)->Pair<Boolean, String>)? = null,
    onValueChange: (String) -> Unit = {},
    imeAction: ImeAction? = null,
    focusRequester:FocusRequester? = null,
    onKeyPressed:()->Unit = {},
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
) {
    var content by remember(value) { mutableStateOf(value?:"") }
    var error by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // State to toggle password visibility

    Column(modifier) {
        label?.let {
            Text(
                textAlign = TextAlign.Start,
                text = it,
                color = MyColors.colorDarkBlue,
                style = fontLink.copy(fontSize = 14.sp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            shape = RoundedCornerShape(24.dp),
            maxLines = 1,
            textStyle = fontParagraphS.copy(
                fontSize = 14.sp,
                lineHeight = TextUnit.Unspecified,
            ),

            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MyColors.colorLightDarkBlue,
                textColor =  MyColors.colorLightDarkBlue,
                backgroundColor = Color.Transparent,
                errorIndicatorColor = MyColors.colorRed,
                focusedIndicatorColor = MyColors.colorLightDarkBlue,
                unfocusedIndicatorColor =MyColors.colorLightGrey
            ),

            leadingIcon = leadingIcon?.let{{
                Icon(
                    painter = leadingIcon,
                    contentDescription = "",
                    tint = MyColors.colorLightDarkBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
            },
            value = content,
            onValueChange = {
                val text = it.trim()
                if (keyboardType == KeyboardType.Phone || keyboardType == KeyboardType.Number ){
                    if (text.all { it.isDigit() }) content = text
                }else{
                    content = text
                }
            },
            placeholder = {
                Text(
                    textAlign = TextAlign.Start,
                    text = makeMandatoryText(placeHolder),
                    color = MyColors.colorLightGrey,
                    style = fontTab,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = Modifier.fillMaxWidth().let {modifier->
                focusRequester?.let { modifier.focusRequester(it) }?:   modifier
            }.heightIn(min = 10.dp)  ,
            visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction?:ImeAction.Done,
                keyboardType = keyboardType
            ),
            trailingIcon =trailingIcon?.let{ {
                if (keyboardType == KeyboardType.Password) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) Res.drawable.ic_eye_on else Res.drawable.ic_eye_off
                            ),
                            contentDescription = "",
                            tint =MyColors.colorLightDarkBlue
                        )
                    }
                }
            }
            },
            keyboardActions = KeyboardActions(onNext = { onKeyPressed() },onDone = { onKeyPressed() }),
            isError =
            isError(
                validate = validate,
                onValueChange = onValueChange,
                content = content,
                error = { error = it }
            )
        )

        error.ifEmpty { null }?.let {
            Text(
                textAlign = TextAlign.Start,
                text = it,
                color =MyColors.colorRed,
                style = fontParagraphS,
            )
        }
    }
}

@Composable
private fun isError(
    validate:@Composable ((String)->Pair<Boolean, String>)? = null,
    onValueChange: (String) -> Unit = {},
    error:(String)->Unit = {},
    content:String? = null
):Boolean{
    Row {  }
    val isValid = validate?.invoke(content?:"")
    if (isValid == null){ error("");onValueChange(content?:"");return false }
    return if (!isValid.first){
        error(isValid.second)
        onValueChange(content?:"")
        true
    }else {
        error("")
        onValueChange(content?:"")
        false
    }
}

@Composable
@ReadOnlyComposable
fun makeMandatoryText(text:String?): AnnotatedString {
    return text?.ifEmpty { null }?.let {
        buildAnnotatedString {
            append(text)
            withStyle(style = SpanStyle(color = MyColors.colorRed)) {
                append(" *")
            }
        }
    }?:buildAnnotatedString {}
}



