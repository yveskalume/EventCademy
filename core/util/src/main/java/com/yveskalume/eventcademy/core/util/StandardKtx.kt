package com.yveskalume.eventcademy.core.util

fun String.capitalize() = replaceFirstChar { it.uppercase() }

fun String.isValidUrl() = matches(Regex("^(http|https)://.*$"))