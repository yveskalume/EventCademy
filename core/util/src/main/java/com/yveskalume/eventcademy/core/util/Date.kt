package com.yveskalume.eventcademy.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val Date.dayOfMonth: String
    get() = SimpleDateFormat("dd", Locale.FRENCH).format(this)

val Date.monthName: String
    get() = SimpleDateFormat("MMM", Locale.FRENCH).format(this)

val Date.readableDate: String
    get() = SimpleDateFormat("dd MMM yyyy", Locale.FRENCH).format(this)

val Date.readableDateWithDayName: String
    get() = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.FRENCH).format(this)

val Date.DateAndTime: String
    get() = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.FRENCH).format(this)

val Date.hoursAndMins: String
    get() = SimpleDateFormat("HH:mm", Locale.FRENCH).format(this)

val Date?.isPast: Boolean
    get() = this?.before(Date()) ?: false

val Date?.isFuture: Boolean
    get() = this?.after(Date()) ?: false

fun Date.getCountDown(): String {
    val currentDate = Date()
    val durationMillis = this.time - currentDate.time

    val days = durationMillis / (24 * 60 * 60 * 1000)
    val hours = durationMillis % (24 * 60 * 60 * 1000) / (60 * 60 * 1000)
    val minutes = durationMillis % (60 * 60 * 1000) / (60 * 1000)

    val daysString = if (days > 0) "$days jours, " else ""
    val hoursString = if (hours > 0) "$hours heures, " else ""
    val minutesString = "$minutes minutes."

    if (days == 0L && hours == 0L && minutes <= 3L) return "C'est maintenant"
    return "C'est dans $daysString$hoursString$minutesString"
}