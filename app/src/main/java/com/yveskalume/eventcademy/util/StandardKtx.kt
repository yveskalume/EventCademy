package com.yveskalume.eventcademy.util

fun String.capitalize() = replaceFirstChar { it.uppercase() }

fun String.isValidUrl() = matches(Regex("^(http|https)://.*$"))