package com.github.pwoicik.uekschedule.common.util

fun String.toUppercase() = replaceFirstChar(Char::uppercase)

inline fun String?.ifBlankOrNull(defaultValue: () -> String): String =
    this?.ifBlank(defaultValue) ?: defaultValue()
