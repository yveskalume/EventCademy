package com.yveskalume.eventcademy.feature.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ContactSupport
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yveskalume.eventcademy.core.designsystem.theme.ThemePreview
import com.yveskalume.eventcademy.core.util.sendEmailIntent

@Composable
fun SettingRoute(
    onBackClick: () -> Unit,
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val privacyLink = stringResource(id = R.string.link_privacy_policy)
    val conditionLink = stringResource(id = R.string.link_terms_of_use)
    val context = LocalContext.current

    SettingScreen(
        onBackClick = onBackClick,
        darkMode = darkMode,
        onDarkModeChange = onDarkModeChange,
        onContactClick = {
            context.sendEmailIntent("eventcademy@gmail.com")
        },
        onConditionClick = {
            uriHandler.openUri(conditionLink)
        },
        onPrivacyClick = {
            uriHandler.openUri(privacyLink)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onContactClick: () -> Unit,
    onConditionClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(text = "Paramètres")
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Card {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .height(48.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                imageVector = Icons.Rounded.DarkMode,
                                contentDescription = null
                            )
                            Text(
                                text = "Mode sombre",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Switch(
                            checked = darkMode,
                            onCheckedChange = onDarkModeChange
                        )
                    }
                }
            }

            item {
                Card(onClick = onContactClick) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .height(48.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Nous contacter",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ContactSupport,
                            contentDescription = null,
                        )
                    }
                }
            }

            item {
                Card(onClick = onConditionClick) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .height(48.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Conditions d'utilisation",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                        )
                    }
                }
            }

            item {
                Card(onClick = onPrivacyClick) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .height(48.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Politique de confidentialité",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    ThemePreview {
        SettingScreen(
            onBackClick = {},
            darkMode = false,
            onDarkModeChange = {},
            onContactClick = {},
            onConditionClick = {},
            onPrivacyClick = {},
        )
    }
}