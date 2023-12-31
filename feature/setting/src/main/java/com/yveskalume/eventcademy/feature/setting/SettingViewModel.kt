package com.yveskalume.eventcademy.feature.setting

import androidx.lifecycle.ViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    // FIXME: Get the contact from remote config
    fun getDeveloperContactLink(): String {
        return remoteConfig.getString("developer_contact_link")
    }

}