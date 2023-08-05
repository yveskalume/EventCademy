package com.yveskalume.eventcademy.app.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yveskalume.eventcademy.R
import com.yveskalume.eventcademy.ui.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class EventNotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters
) :
    CoroutineWorker(appContext, workerParams) {

    private val eventUid = inputData.getString("eventUid")
    private val eventName = inputData.getString("eventName")
    private val eventCountDown = inputData.getString("eventCountDown")

    override suspend fun doWork(): Result {
        return try {
            sendNotification(eventUid!!, eventName!!, eventCountDown!!)
            Result.success()
        } catch (e: Exception) {
            Log.e("EventNotificationWorker", "Error sending notification", e)
            Result.retry()
        }
    }

    private fun sendNotification(eventUid: String, eventName: String, eventCountDown: String) {

        val eventDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "https://eventcademy.app/event/$eventUid".toUri(),
            appContext,
            MainActivity::class.java
        )

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(appContext).run {
            addNextIntentWithParentStack(eventDetailIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }


        val channelId = appContext.getString(R.string.default_notification_channel_id)
        val builder = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(R.drawable.eventcademy_notification_icon)
            .setContentTitle("$eventName c'est pour bientôt !")
            .setContentText(eventCountDown)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(appContext)) {
            if (ActivityCompat.checkSelfPermission(
                    appContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

}