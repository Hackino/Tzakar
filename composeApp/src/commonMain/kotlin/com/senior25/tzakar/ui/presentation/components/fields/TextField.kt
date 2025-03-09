package com.senior25.tzakar.ui.presentation.components.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senior25.tzakar.ktx.ifEmpty
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphS
import com.senior25.tzakar.ui.theme.fontTab
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.ui.timepicker.WheelTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_password
import tzakar_reminder.composeapp.generated.resources.enter_username
import tzakar_reminder.composeapp.generated.resources.ic_eye_off
import tzakar_reminder.composeapp.generated.resources.ic_eye_on
import tzakar_reminder.composeapp.generated.resources.invalid_time

@Composable
fun normalTextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeHolder: String? = stringResource(Res.string.enter_username),
    value:String? = null,
    onValueChange: (String) -> Unit = {},
    isInputValid:(Boolean)->Unit = {},
    imeAction: ImeAction? = null,
    focusRequester:FocusRequester? = null,
    onKeyPressed:()->Unit = {},
    leadingIcon: Painter? = null,
    validate:Boolean? = true,
    isMandatory: Boolean? = true
) {
    BaseTextField(
        modifier =modifier,
        value = value,
        placeHolder = placeHolder,
        keyboardType = KeyboardType.Text,
        onValueChange =onValueChange,
        validate = {
            if (validate == true) {
                val isValid = isNameValid(it)
                isInputValid(isValid.first)
                isValid
            }else{
                Pair(true,"")
            }
        },
        isMandatory = isMandatory,
        label = label,
        imeAction =  imeAction,
        focusRequester = focusRequester,
        onKeyPressed = onKeyPressed,
        leadingIcon = leadingIcon
    )
}
@Composable
fun DateField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeHolder: String? = stringResource(Res.string.enter_username),
    value:String? = null,
    onValueChange: (String) -> Unit = {},
    isInputValid:(Boolean)->Unit = {},
    imeAction: ImeAction? = null,
    focusRequester:FocusRequester? = null,
    onKeyPressed:()->Unit = {},
    leadingIcon: Painter? = null,
    validate:Boolean? = true
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember(value) { mutableStateOf(value) }

    BaseTextField(
        modifier =modifier,
        value = selectedDate,
        placeHolder = placeHolder,
        keyboardType = KeyboardType.Text,
        onValueChange =onValueChange,
        validate = {
            if (validate == true) {
                val isValid = if (selectedDate?.ifEmpty { null } == null){
                    Pair(false,"")
                }else{
                    val inputDate = LocalDate.parse(it)
                    val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                    Pair(inputDate >= today,"")
                }
                isInputValid(isValid.first)
                print("hackinoooooo is input valid date in function  $isValid")
                isValid
            }else{
                print("hackinoooooo not validating")
                Pair(true,"")
            }
        },
        label = label,
        imeAction =  imeAction,
        focusRequester = focusRequester,
        onKeyPressed = onKeyPressed,
        leadingIcon = leadingIcon,
        onFieldClick = { showDatePicker  =true },
        isValidationEnabled = true,
        isEnabled = false
    )

    if (showDatePicker) {
        WheelDatePickerView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp, bottom = 26.dp),
            minDate = LocalDate.now(),
            showDatePicker = showDatePicker,
            title = label?:"",
            doneLabel = "Done",

            titleStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MyColors.colorDarkBlue
            ),
            doneLabelStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color = MyColors.colorRed
            ),
            dateTextColor = MyColors.colorDarkBlue,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                borderColor = MyColors.colorLightGrey,
            ),
            rowCount = 5,
            height = 180.dp,
            dateTextStyle = TextStyle(
                fontWeight = FontWeight(600),
            ),
            dragHandle = {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp).width(50.dp).clip(CircleShape),
                    thickness = 4.dp,
                    color = Color(0xFFE8E4E4)
                )
            },
            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
            dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
            onDoneClick = {
                selectedDate = it.toString()
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@Composable
fun TimeField(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeHolder: String? = stringResource(Res.string.enter_username),
    value:String? = null,
    onValueChange: (String) -> Unit = {},
    isInputValid:(Boolean)->Unit = {},
    imeAction: ImeAction? = null,
    focusRequester:FocusRequester? = null,
    onKeyPressed:()->Unit = {},
    leadingIcon: Painter? = null,
    validate:Boolean? = true,
    validateDateBefore:(()->Boolean)? = null,
    selectedDate:String? = null
) {

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedTime by remember(value) { mutableStateOf(value) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val inputDate by remember(selectedDate) { mutableStateOf(selectedDate?.ifEmpty { null }?.let { LocalDate.parse(it) }) }

    println("selected date in time $inputDate")
    println("today date $today")
    println("is equal ${inputDate == today}")

    BaseTextField(
        modifier =modifier,
        value = selectedTime,
        placeHolder = placeHolder,
        keyboardType = KeyboardType.Text,
        onValueChange =onValueChange,
        validate = {
            val isValid =    if (validate == true) {
                if (inputDate == today) {
                    val inputTime = selectedTime?.ifEmpty { null }?.let { LocalTime.parse(it) }
                    println("input time $inputTime")
                    println("selected time $selectedTime")
                    if (inputTime == null){
                        Pair(false,"")
                    }else if (inputTime<LocalTime.now()){
                        Pair(false, stringResource(Res.string.invalid_time))
                    }else{
                        Pair(true,"")
                    }

                } else {
                    println("not today")

                    Pair(true,"")
                }
            }else{
                Pair(true,"")
            }
            println("startinggg")
            println(selectedTime)
            println(selectedTime?.ifEmpty { null }?.let { LocalTime.parse(it) })

            println(LocalTime.now())

            isInputValid(isValid.first)
            isValid

        },
        label = label,
        imeAction =  imeAction,
        focusRequester = focusRequester,
        onKeyPressed = onKeyPressed,
        leadingIcon = leadingIcon,
        onFieldClick = {
            if (validateDateBefore?.invoke()==false){
                return@BaseTextField
            }
            showDatePicker  =true
        },
        isEnabled = false,
        isValidationEnabled = true
    )
    if (showDatePicker) {
        WheelTimePickerView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp, bottom = 26.dp),
            showTimePicker = showDatePicker,
            title = label?:"",
            doneLabel = "Done",
            minTime = if (inputDate == today) {
                LocalTime.now()
            } else {
                LocalTime(0, 0)
            },
            titleStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MyColors.colorDarkBlue
            ),
            doneLabelStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color = MyColors.colorRed
            ),
            textColor = MyColors.colorDarkBlue,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                borderColor = MyColors.colorLightGrey,
            ),
            rowCount = 5,
            height = 180.dp,
            textStyle = TextStyle(
                fontWeight = FontWeight(600),
            ),
            dragHandle = {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp).width(50.dp).clip(CircleShape),
                    thickness = 4.dp,
                    color = Color(0xFFE8E4E4)
                )
            },
            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
            dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
            onDoneClick = {
                selectedTime = it.toString()
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

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
    leadingIcon: Painter? = null,
    isEnabled:Boolean? = true
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
        leadingIcon = leadingIcon,
        isEnabled=isEnabled,
        isValidationEnabled =isEnabled

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
    trailingIcon: Painter?=  null,
    isEnabled:Boolean? = true,
    fullValidation:Boolean? = true
) {
    BaseTextField(
        modifier = modifier,
        value = value,
        placeHolder = placeHolder,
        keyboardType = KeyboardType.Password,
        onValueChange =onValueChange,
        validate = {
            val isValid = isPasswordValid(it,fullValidation =fullValidation)
            isInputValid(isValid.first)
            isValid
        },
        label = label,
        imeAction =  imeAction,
        focusRequester = focusRequester,
        onKeyPressed = onKeyPressed,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        isEnabled=isEnabled,
        isValidationEnabled = isEnabled
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

@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    value:String? = null,
    label:String? = null,
    placeHolder:String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    validate:@Composable ( (String) -> Pair<Boolean, String> )? = null,
    onValueChange: (String) -> Unit = {},
    isMandatory: Boolean? = true,
    imeAction: ImeAction? = null,
    focusRequester: FocusRequester? = null,
    onKeyPressed: () -> Unit = {},
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    isEnabled: Boolean? = true,
    isValidationEnabled: Boolean? = true,
    onFieldClick: ( () -> Unit )?= null
) {
    var content by remember(value) { mutableStateOf(value?:"") }
    var error by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // State to toggle password visibility

    Column(modifier ) {
        label?.let {
            Text(
                textAlign = TextAlign.Start,
                text = it,
                color = MyColors.colorDarkBlue,
                style = fontLink.copy(fontSize = 14.sp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            Modifier.clickable(enabled = onFieldClick != null) {
                onFieldClick?.invoke()
            }.clip(RoundedCornerShape(24.dp))

        ){
            OutlinedTextField(
                shape = RoundedCornerShape(24.dp),
                maxLines = 1,
                textStyle = fontParagraphS.copy(
                    fontSize = 14.sp,
                    lineHeight = TextUnit.Unspecified,
                ),
                enabled = isEnabled ==true,
                readOnly = isEnabled !=true,
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = MyColors.colorLightDarkBlue,
                    textColor =  MyColors.colorLightDarkBlue.copy(alpha = if (isEnabled == true) 1f else 0.8f),
                    backgroundColor = Color.Transparent,
                    errorIndicatorColor = MyColors.colorRed,
                    focusedIndicatorColor = if (isEnabled == true) MyColors.colorLightDarkBlue else MyColors.colorLightGrey.copy(0.8f),
                    unfocusedIndicatorColor = MyColors.colorLightGrey.copy(alpha = if (isEnabled == true) 1f else 0.8f)
                ),

                leadingIcon = leadingIcon?.let{{
                    Icon(
                        painter = leadingIcon,
                        contentDescription = "",
                        tint = MyColors.colorLightDarkBlue.copy(alpha = if (isEnabled == true) 1f else 0.8f),
                        modifier = Modifier.size(24.dp)
                    )
                }
                },
                value = content,
                onValueChange = {
                    val text = it
                    if (keyboardType == KeyboardType.Phone || keyboardType == KeyboardType.Number ){
                        if (text.all { it.isDigit() }) content = text
                    }else{
                        content = text
                    }
                },
                placeholder = {
                    Text(
                        textAlign = TextAlign.Start,
                        text = makeMandatoryText(placeHolder,isMandatory),
                        color = MyColors.colorLightGrey,
                        style = fontTab,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier.fillMaxWidth().let {modifier->
                    focusRequester?.let { modifier.focusRequester(it) }?:   modifier
                }.heightIn(min = 10.dp) ,
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
                isError = if (isValidationEnabled == true){
                    println("hackino     validation   ")
                    isError(
                        validate = validate,
                        onValueChange = onValueChange,
                        content = content,
                        error = { error = it }
                    )
                }else false
            )
        }
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
fun makeMandatoryText(text:String?,isMandatory: Boolean? =true): AnnotatedString {
    return text?.ifEmpty { null }?.let {
        buildAnnotatedString {
            append(text)
            if (isMandatory ==true) {
                withStyle(style = SpanStyle(color = MyColors.colorRed)) {
                    append(" *")
                }
            }
        }
    }?:buildAnnotatedString {}
}



