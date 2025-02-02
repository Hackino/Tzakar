package com.senior25.tzakar.ui.presentation.components.fields

import androidx.compose.runtime.Composable
import com.senior25.tzakar.helper.validator.EmailValidator.isValidEmail
import com.senior25.tzakar.helper.validator.PasswordValidator
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.invalid_email_address
import tzakar_reminder.composeapp.generated.resources.password_not_matching
import tzakar_reminder.composeapp.generated.resources.password_not_validated


@Composable
fun isEmailValid(text:String):Pair<Boolean,String>{
    return if (text.isBlank() || text.isEmpty()){
        Pair(false,""/* context.getString(com.base.commons.R.string.invalid_email_address)*/)
    }else if (!isValidEmail(text)) {
        Pair(false, stringResource(Res.string.invalid_email_address))
    }else{
        Pair(true,"")
    }
}

@Composable
fun isNameValid( text:String):Pair<Boolean,String>{
    return if (text.isBlank() || text.isEmpty()) {
        Pair(false,"" /*context.getString(com.base.commons.R.string.invalid)*/)
    }else{
        Pair(true,"")
    }
}

@Composable
fun isPasswordValid(text:String):Pair<Boolean,String>{
    return if (text.isBlank() || text.isEmpty()) {
        Pair(false, "")
    }else if (!PasswordValidator.isValidPassword(password = text)){
        Pair(false, stringResource(Res.string.password_not_validated))
    } else{
        Pair(true,"")
    }
}

@Composable
fun isConfirmPasswordValid(text:String,password:String?):Pair<Boolean,String>{
    return if (text.isBlank() || text.isEmpty()) {
        Pair(false,"")
    }else if (password != text){
        Pair(false, stringResource(Res.string.password_not_matching))
    } else{
        Pair(true,"")
    }
}


