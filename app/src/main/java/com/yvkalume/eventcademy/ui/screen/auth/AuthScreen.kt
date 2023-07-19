package com.yvkalume.eventcademy.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yvkalume.eventcademy.R
import com.yvkalume.eventcademy.ui.navigation.Destination
import com.yvkalume.eventcademy.util.ThemePreview
import com.yvkalume.eventcademy.util.navigate
import kiwi.orbit.compose.ui.OrbitTheme
import kiwi.orbit.compose.ui.controls.ButtonSecondary
import kiwi.orbit.compose.ui.controls.Icon
import kiwi.orbit.compose.ui.controls.Text

@Composable
fun AuthScreen(
    navController: NavController
) {
    Column {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Connectez-vous", style = OrbitTheme.typography.title2)
            Text(
                text = "Sauvergardez vos information et les evenement que aux-quels vous aimeriez " +
                        "participer afin d'y acceder plus rapidement.",
                style = OrbitTheme.typography.bodySmall
            )

            ButtonSecondary(
                onClick = {
                    navController.navigate(Destination.HomeScreen)
                }, modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(text = "Se connecter avec Google")
            }

            ButtonSecondary(
                onClick = {
                    navController.navigate(Destination.HomeScreen)
                }, modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(text = "Se connecter avec Google")
            }
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    ThemePreview {
        AuthScreen(navController = rememberNavController())
    }
}