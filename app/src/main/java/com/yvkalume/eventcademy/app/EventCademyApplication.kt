package com.yvkalume.eventcademy.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EventCademyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}