package com.senior25.tzakar.helper.validator

object PasswordValidator {

    private val passwordRegex = Regex(
        pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@_#&()–[{}]:;',?/*~$^+=<>]).{6,}$"
    )

    fun isValidPassword(password: String?): Boolean {
        return password != null && passwordRegex.matches(password)
    }

    fun advancedPasswordValidatorSuccessive(password: String?): Boolean {
        if (password == null) return false

        for (x in 0..7) {
            if (password.contains("$x${x + 1}${x + 2}")) {
                return false
            }
        }

        for (x in 2..9) {
            if (password.contains("$x${x - 1}${x - 2}")) {
                return false
            }
        }
        return true
    }

    fun advancedPasswordValidatorIdentical(password: String?): Boolean {
        if (password == null) return false

        for (x in 0..9) {
            if (password.contains("$x$x$x")) {
                return false
            }
        }
        return true
    }
}