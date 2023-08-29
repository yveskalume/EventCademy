package com.yveskalume.eventcademy.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract

fun Context.addToPhoneCalendar(
    eventName: String,
    eventLocation: String,
    eventDescription: String,
    eventStartDate: Long?,
    eventEndDate: Long?,
    eventUrl: String,
    eventTimezone: String
) {
    val link = if (eventUrl.isNotBlank()) "\n\nLien : $eventUrl" else ""

    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.Events.TITLE, eventName)
        .putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation)
        .putExtra(CalendarContract.Events.DESCRIPTION, eventDescription + link)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStartDate)
        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEndDate)
        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        .putExtra(Intent.EXTRA_EMAIL, eventUrl)
        .putExtra(CalendarContract.Events.EVENT_TIMEZONE, eventTimezone)

    startActivity(intent)
}

fun Context.sendEmailIntent(email: String) {
    val emailIntent = Intent(
        Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", email, null
        )
    )
    startActivity(Intent.createChooser(emailIntent, "Nous contacter"))
}