package com.yveskalume.eventcademy.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import com.yveskalume.eventcademy.core.domain.model.Event

fun Context.addToPhoneCalendar(event: Event) {
    val link = if (event.eventUrl.isNotBlank()) "\n\nLien : ${event.eventUrl}" else ""

    val intent = Intent(Intent.ACTION_INSERT)
        .setData(CalendarContract.Events.CONTENT_URI)
        .putExtra(CalendarContract.Events.TITLE, event.name)
        .putExtra(CalendarContract.Events.EVENT_LOCATION, event.location)
        .putExtra(CalendarContract.Events.DESCRIPTION, event.description + link)
        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.startDate?.time)
        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.endDate?.time)
        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        .putExtra(Intent.EXTRA_EMAIL, event.eventUrl)
        .putExtra(CalendarContract.Events.EVENT_TIMEZONE, event.timezone)

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