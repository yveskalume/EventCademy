package com.yvkalume.eventcademy.app.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.yvkalume.eventcademy.data.entity.Event
import com.yvkalume.eventcademy.util.getCountDown
import java.util.concurrent.TimeUnit

fun Context.scheduleEventNotification(event: Event) {
    // create work request 30 minutes before event
    val workRequest30minBefore = OneTimeWorkRequestBuilder<EventNotificationWorker>()
        .setInputData(
            workDataOf(
                "eventUid" to event.uid,
                "eventName" to event.name,
                "eventCountDown" to event.startDate?.getCountDown()
            )
        )
        .setInitialDelay(event.startDate!!.time - 30 * 60 * 1000, TimeUnit.MILLISECONDS)
        .addTag(event.uid)
        .setBackoffCriteria(
            BackoffPolicy.LINEAR,
            10000,
            TimeUnit.MILLISECONDS
        ).build()

    WorkManager
        .getInstance(this)
        .enqueue(workRequest30minBefore)

    // create work request 5 minutes before event
    val workRequest5minBefore = OneTimeWorkRequestBuilder<EventNotificationWorker>()
        .setInputData(
            workDataOf(
                "eventUid" to event.uid,
                "eventName" to event.name,
                "eventCountDown" to event.startDate.getCountDown()
            )
        )
        .setInitialDelay(event.startDate.time - 5 * 60 * 1000, TimeUnit.MILLISECONDS)
        .addTag(event.uid)
        .setBackoffCriteria(
            BackoffPolicy.LINEAR,
            10000,
            TimeUnit.MILLISECONDS
        ).build()

    WorkManager
        .getInstance(this)
        .enqueue(workRequest5minBefore)

    // create work request 1 minute before event
    val workRequest1minBefore = OneTimeWorkRequestBuilder<EventNotificationWorker>()
        .setInputData(
            workDataOf(
                "eventUid" to event.uid,
                "eventName" to event.name,
                "eventCountDown" to event.startDate.getCountDown()
            )
        )
        .setInitialDelay(event.startDate.time - 1 * 60 * 1000, TimeUnit.MILLISECONDS)
        .addTag(event.uid)
        .setBackoffCriteria(
            BackoffPolicy.LINEAR,
            10000,
            TimeUnit.MILLISECONDS
        ).build()

    WorkManager
        .getInstance(this)
        .enqueue(workRequest1minBefore)
}