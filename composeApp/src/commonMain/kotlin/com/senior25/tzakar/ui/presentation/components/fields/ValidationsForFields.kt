package com.senior25.tzakar.ui.presentation.components.fields

//import android.content.Context
//import com.base.commons.helpers.validator.EmailValidator.isValidEmail
//import com.base.commons.helpers.validator.PasswordValidator
//
//
//fun isEmailValid(context: Context, text:String):Pair<Boolean,String>{
//    return if (text.isBlank() || text.isEmpty()){
//        Pair(false,""/* context.getString(com.base.commons.R.string.invalid_email_address)*/)
//    }else if (!isValidEmail(text)) {
//        Pair(false, context.getString((com.base.commons.R.string.invalid_email_address)))
//    }else{
//        Pair(true,"")
//    }
//}
//
//fun isNameValid(context: Context, text:String):Pair<Boolean,String>{
//    return if (text.isBlank() || text.isEmpty()) {
//        Pair(false,"" /*context.getString(com.base.commons.R.string.invalid)*/)
//    }else{
//        Pair(true,"")
//    }
//}
//
//fun isPasswordValid(context: Context, text:String):Pair<Boolean,String>{
//    return if (text.isBlank() || text.isEmpty()) {
//        Pair(false, ""/*context.getString(com.base.commons.R.string.invalid)*/)
//    }else if (!PasswordValidator.isValid(password = text)){
//        Pair(false, context.getString(com.base.commons.R.string.password_not_validated))
//    } else{
//        Pair(true,"")
//    }
//}
//
//fun isConfirmPasswordValid(context: Context, text:String,password:String):Pair<Boolean,String>{
//    return if (text.isBlank() || text.isEmpty()) {
//        Pair(false,""/* context.getString(com.base.commons.R.string.invalid_password)*/)
//    }else if (password != text){
//        Pair(false, context.getString(com.base.commons.R.string.password_not_matching))
//    } else{
//        Pair(true,"")
//    }
//}
//
//
