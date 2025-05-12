@file:Suppress("DEPRECATION")

package com.yveskalume.eventcademy.feature.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.yveskalume.eventcademy.core.designsystem.components.LinkedText
import com.yveskalume.eventcademy.core.designsystem.components.withLink
import com.yveskalume.eventcademy.core.designsystem.theme.ThemePreview
import com.yveskalume.eventcademy.core.designsystem.R.drawable as Drawables

@Composable
fun AuthRoute(
    viewModel: AuthViewModel = hiltViewModel(),
    webClientIdToken: String,
    onConnectSuccess: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        (context as? Activity)?.finish()
    }

    AuthScreen(
        uiState = uiState,
        webClientIdToken = webClientIdToken,
        onConnectWithGoogle = {
            viewModel.signInWithCredential(it)
            onConnectSuccess()
        },
        startAuthFlow = { viewModel.startAuthFlow() })
}

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    webClientIdToken: String,
    onConnectWithGoogle: (credential: AuthCredential) -> Unit,
    startAuthFlow: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val privacyPolicyLink = stringResource(id = R.string.link_privacy_policy)
    val termsOfUseLink = stringResource(id = R.string.link_terms_of_use)

    val oneTapClient = remember {
        Identity.getSignInClient(context)
    }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                onConnectWithGoogle(credential)
            } catch (e: ApiException) {
                Log.e("AuthScreen", "signInResult:failed", e)
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    val oneTapLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                val signInCredential = oneTapClient.getSignInCredentialFromIntent(it.data)
                val credential = GoogleAuthProvider
                    .getCredential(signInCredential.googleIdToken!!, null)
                onConnectWithGoogle(credential)
            } catch (e: ApiException) {
                Log.e("AuthScreen", "signInResult:failed", e)
            }
        }

    LaunchedEffect(Unit) {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientIdToken)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(context as Activity) { result ->
                val intentSenderRequest =
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()

                try {
                    oneTapLauncher.launch(intentSenderRequest)
                } catch (e: Exception) {
                    // Attempting to launch an unregistered ActivityResultLauncher
                    /*this catch avoid the app to crash after sign out (when the user is redirected to this screen again)*/
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = Drawables.eventcademy_app_icon),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "EventCademy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Apprenez & Réseautez !",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "Participez à des événements qui laisseront une empreinte durable dans votre parcours personnel et professionnel.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                AnimatedContent(targetState = uiState, label = "") {
                    when (it) {
                        AuthUiState.Idle -> ConnectWithGoogleButton(
                            onClick = {
                                startAuthFlow()
                                val gso =
                                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(webClientIdToken)
                                        .requestEmail()
                                        .build()
                                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                googleSignInClient.revokeAccess()

                                launcher.launch(googleSignInClient.signInIntent)
                            }
                        )

                        AuthUiState.Loading -> {
                            CircularProgressIndicator()
                        }

                        else -> {}
                    }
                }

                LinkedText(
                    style = MaterialTheme.typography.labelSmall,
                    text = buildAnnotatedString {
                        append("En vous connectant, vous acceptez nos ")
                        withLink(url = termsOfUseLink) {
                            append("Conditions générales d'utilisation")
                        }
                        append(" et notre ")
                        withLink(url = privacyPolicyLink) {
                            append("Politique de confidentialité")
                        }
                    },
                    onClick = { url -> uriHandler.openUri(url) }
                )
            }
        }
    }
}


@Composable
fun ConnectWithGoogleButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = Color.White,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Text(text = "Se connecter avec Google", fontWeight = FontWeight.SemiBold)
            // in order to center the content
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                tint = Color.Transparent,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    ThemePreview {
        AuthScreen(
            uiState = AuthUiState.Idle,
            onConnectWithGoogle = {},
            webClientIdToken = "",
            startAuthFlow = {})
    }
}