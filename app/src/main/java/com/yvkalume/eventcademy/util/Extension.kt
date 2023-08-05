package com.yvkalume.eventcademy.util

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