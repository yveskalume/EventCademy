package com.yveskalume.eventcademy.core.domain.model

import androidx.annotation.Keep
import java.util.Date

@Keep
data class Advertisement(
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val destination: String = "",
    val userUid: String = "",
    val published: Boolean = false,
    val type: AdvertisementType = AdvertisementType.INTERNAL_EVENT,
    val featuredUntil: Date? = null,
    val createdAt: Date? = null,
)
