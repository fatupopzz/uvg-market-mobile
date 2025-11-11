package com.example.uvgmarket.core.constants

object ValidationConstants {
    const val MIN_PASSWORD_LENGTH = 8
    const val MIN_USERNAME_LENGTH = 3
    const val MAX_USERNAME_LENGTH = 20

    val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
}