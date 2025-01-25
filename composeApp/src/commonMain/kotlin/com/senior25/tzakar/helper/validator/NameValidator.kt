package com.senior25.tzakar.helper.validator

object NameValidator {

    private val nameRegex = Regex(pattern = "^[a-zA-ZÀ-ȕ\\s-]*$")

    fun isValidName(name: String?): Boolean {
        return name != null && nameRegex.matches(name)
    }
}