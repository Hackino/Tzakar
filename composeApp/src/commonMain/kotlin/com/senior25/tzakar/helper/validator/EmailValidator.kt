package com.senior25.tzakar.helper.validator

object EmailValidator {

    private val emailRegex = Regex(
        pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
    )

    fun isValidEmail(target: CharSequence?): Boolean {
        return target != null && emailRegex.matches(target)
    }
}