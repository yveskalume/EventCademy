package com.yveskalume.eventcademy.core.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun PartyAnimation(
    modifier: Modifier = Modifier,
) {
    KonfettiView(
        modifier = modifier,
        parties = listOf(
            Party(
                speed = 0f,
                timeToLive = 5000,
                maxSpeed = 30f,
                damping = 0.9f,
                angle = Angle.TOP,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 1000, TimeUnit.MILLISECONDS).max(500),
                position = Position.Relative(0.5, 0.3)
            )
        )
    )
}