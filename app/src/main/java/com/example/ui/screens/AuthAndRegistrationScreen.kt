package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.L10nKey
import com.example.data.model.SafarLocalization
import com.example.data.remote.TelegramBotService
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassInput
import com.example.ui.components.GlassNoticeBanner
import com.example.ui.components.GlassNoticeTone
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SafarGoTheme
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.theme.WarningAmberDarkText
import com.example.ui.theme.WarningAmberText
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun onGoogleSignInClicked(
    context: Context,
    credentialManager: CredentialManager,
    onAuthSuccess: () -> Unit,
    onAuthError: (String) -> Unit,
    scope: CoroutineScope,
    onAuthCancelled: () -> Unit = {},
    language: AppLanguage = AppLanguage.UZ
) {
    val clientId = try {
        context.getString(R.string.default_web_client_id)
    } catch (_: Exception) {
        onAuthError(SafarLocalization.text(language, L10nKey.AUTH_ERR_MISSING_GOOGLE_CONFIG))
        return
    }
    val activity = context.findActivity()
    if (activity == null) {
        onAuthError(SafarLocalization.text(language, L10nKey.AUTH_ERR_NO_ACTIVE_ACTIVITY))
        return
    }

    val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
    val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

    scope.launch {
        try {
            val result = credentialManager.getCredential(activity, request)
            val credential = result.credential
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                Firebase.auth.signInWithCredential(authCredential).await()
                onAuthSuccess()
            } else {
                onAuthError(SafarLocalization.text(language, L10nKey.AUTH_ERR_CREDENTIAL_FAILED))
            }
        } catch (e: GetCredentialCancellationException) {
            Log.w("Auth", "Google Sign-In cancelled by user")
            onAuthCancelled()
        } catch (e: Exception) {
            Log.e("Auth", "Google Sign-In failed", e)
            onAuthError(
                e.localizedMessage
                    ?: SafarLocalization.text(language, L10nKey.AUTH_ERR_SIGNIN_GENERIC)
            )
        }
    }
}

fun signOutSafarGo(
    credentialManager: CredentialManager,
    scope: CoroutineScope,
    onSignOutComplete: () -> Unit = {}
) {
    scope.launch {
        withContext(NonCancellable) {
            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
            } catch (e: Exception) {
                Log.w("Auth", "Failed to clear credential state: ${e.message}")
            } finally {
                Firebase.auth.signOut()
                onSignOutComplete()
            }
        }
    }
}

enum class AuthChannelTab {
    GOOGLE, LOGIN_PASSWORD, PHONE, TELEGRAM
}

internal enum class LoginPasswordSubMode {
    SIGN_IN, CREATE_ACCOUNT, FORGOT_PASSWORD
}

internal fun openSafarGoTelegramBot(context: Context): Boolean {
    return runCatching {
        val botUrl = TelegramBotService().publicConfig.publicStartUrl
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(botUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        true
    }.getOrDefault(false)
}

internal fun cleanTelegramUsername(raw: String): String {
    return raw.trim()
        .removePrefix("https://t.me/")
        .removePrefix("t.me/")
        .removePrefix("@")
        .replace(" ", "")
}

internal fun isValidTelegramUsername(raw: String): Boolean {
    val clean = cleanTelegramUsername(raw)
    return clean.length in 3..32 && clean.all { it.isLetterOrDigit() || it == '_' }
}

internal fun isValidUzPhone(raw: String): Boolean {
    val digits = raw.filter { it.isDigit() }
    return digits.length in 9..15
}

private fun isValidEmailLogin(raw: String): Boolean {
    val trimmed = raw.trim()
    val atIdx = trimmed.indexOf('@')
    val dotIdx = trimmed.lastIndexOf('.')
    return trimmed.length >= 5 && atIdx > 0 && dotIdx > atIdx + 1 && dotIdx < trimmed.length - 1
}

@Composable
fun AuthGateScreen(
    language: AppLanguage,
    initialPhone: String = "",
    initialTelegramHandle: String = "",
    initialTelegramNotif: Boolean = false,
    onSavePendingPhone: (String) -> Unit = {},
    onSavePendingTelegram: (String, Boolean) -> Unit = { _, _ -> },
    onCycleLanguage: () -> Unit,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    initialTab: AuthChannelTab = AuthChannelTab.GOOGLE
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }
    val botHandle = remember { TelegramBotService().publicConfig.botHandle }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var infoBanner by remember { mutableStateOf<String?>(null) }

    var selectedTab by remember(initialTab) { mutableStateOf(initialTab) }
    var loginSubMode by remember { mutableStateOf(LoginPasswordSubMode.SIGN_IN) }
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var loginConfirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var phoneInput by remember(initialPhone) {
        mutableStateOf(initialPhone.ifBlank { "+998 " })
    }
    var telegramInput by remember(initialTelegramHandle) {
        mutableStateOf(initialTelegramHandle)
    }
    var telegramNotif by remember(initialTelegramNotif) {
        mutableStateOf(initialTelegramNotif)
    }

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val warningText = if (isDark) WarningAmberDarkText else WarningAmberText

    BackHandler(
        enabled = selectedTab != AuthChannelTab.GOOGLE ||
            (selectedTab == AuthChannelTab.LOGIN_PASSWORD && loginSubMode == LoginPasswordSubMode.FORGOT_PASSWORD)
    ) {
        errorMessage = null
        infoBanner = null
        if (selectedTab == AuthChannelTab.LOGIN_PASSWORD && loginSubMode == LoginPasswordSubMode.FORGOT_PASSWORD) {
            loginSubMode = LoginPasswordSubMode.SIGN_IN
        } else {
            selectedTab = AuthChannelTab.GOOGLE
        }
    }

    val submitLoginForm: () -> Unit = {
        focusManager.clearFocus()
        errorMessage = null
        infoBanner = null
        when (loginSubMode) {
            LoginPasswordSubMode.SIGN_IN -> {
                when {
                    !isValidEmailLogin(loginEmail) -> {
                        errorMessage = SafarLocalization.text(language, L10nKey.AUTH_ERR_INVALID_EMAIL_SIGNIN)
                    }
                    loginPassword.length < 6 -> {
                        errorMessage = SafarLocalization.text(language, L10nKey.AUTH_ERR_SHORT_PASSWORD)
                    }
                    else -> {
                        infoBanner = SafarLocalization.text(language, L10nKey.AUTH_INFO_SIGNIN_UNCONFIGURED)
                    }
                }
            }

            LoginPasswordSubMode.CREATE_ACCOUNT -> {
                when {
                    !isValidEmailLogin(loginEmail) -> {
                        errorMessage = SafarLocalization.text(language, L10nKey.AUTH_ERR_INVALID_EMAIL_SIGNUP)
                    }
                    loginPassword.length < 6 -> {
                        errorMessage = SafarLocalization.text(language, L10nKey.AUTH_ERR_SHORT_PASSWORD)
                    }
                    loginConfirmPassword != loginPassword -> {
                        errorMessage = SafarLocalization.text(language, L10nKey.AUTH_ERR_PASSWORD_MISMATCH)
                    }
                    else -> {
                        infoBanner = SafarLocalization.text(language, L10nKey.AUTH_INFO_SIGNUP_UNCONFIGURED)
                    }
                }
            }

            LoginPasswordSubMode.FORGOT_PASSWORD -> {
                if (!isValidEmailLogin(loginEmail)) {
                    errorMessage = SafarLocalization.text(language, L10nKey.AUTH_ERR_INVALID_EMAIL_RESET)
                } else {
                    infoBanner = SafarLocalization.text(language, L10nKey.AUTH_INFO_RESET_UNCONFIGURED)
                }
            }
        }
    }

    SafarGoGlassBackdrop(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 560.dp)
                    .imePadding()
                    .testTag("auth_list"),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // 1. Glass Brand Header + Language Switcher
                item {
                    GlassSurface(
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        elevation = GlassTokens.DefaultElevation
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(IndigoPrimary, SkyBlueAccent.copy(alpha = 0.85f))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Flight,
                                        contentDescription = SafarLocalization.text(language, L10nKey.CD_SAFARGO_LOGO),
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.APP_NAME),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            GlassSurface(
                                onClick = onCycleLanguage,
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("auth_language_toggle"),
                                shape = CircleShape,
                                subtle = true,
                                elevation = 0.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Translate,
                                        contentDescription = SafarLocalization.text(language, L10nKey.CD_SWITCH_LANGUAGE),
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = language.shortLabel,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. Concise Welcome Title & Subtitle
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = SafarLocalization.text(language, L10nKey.AUTH_TITLE),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = SafarLocalization.text(language, L10nKey.AUTH_SUBTITLE),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 3. Glassmorphic Auth Card with 4-Channel Selector
                item {
                    GlassCard(
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        elevation = GlassTokens.DefaultElevation
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.AUTH_CHOOSE_METHOD),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                // 2x2 Channel Grid
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        AuthMethodPill(
                                            label = SafarLocalization.text(language, L10nKey.AUTH_METHOD_GOOGLE),
                                            icon = Icons.Filled.AccountCircle,
                                            selected = selectedTab == AuthChannelTab.GOOGLE,
                                            onClick = {
                                                selectedTab = AuthChannelTab.GOOGLE
                                                errorMessage = null
                                                infoBanner = null
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("auth_tab_google")
                                        )
                                        AuthMethodPill(
                                            label = SafarLocalization.text(language, L10nKey.AUTH_METHOD_LOGIN_PASS),
                                            icon = Icons.Filled.Key,
                                            selected = selectedTab == AuthChannelTab.LOGIN_PASSWORD,
                                            onClick = {
                                                selectedTab = AuthChannelTab.LOGIN_PASSWORD
                                                errorMessage = null
                                                infoBanner = null
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("auth_tab_login_password")
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        AuthMethodPill(
                                            label = SafarLocalization.text(language, L10nKey.AUTH_METHOD_PHONE),
                                            icon = Icons.Filled.Phone,
                                            selected = selectedTab == AuthChannelTab.PHONE,
                                            onClick = {
                                                selectedTab = AuthChannelTab.PHONE
                                                errorMessage = null
                                                infoBanner = null
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("auth_tab_phone")
                                        )
                                        AuthMethodPill(
                                            label = SafarLocalization.text(language, L10nKey.AUTH_METHOD_TELEGRAM),
                                            icon = Icons.AutoMirrored.Filled.Send,
                                            selected = selectedTab == AuthChannelTab.TELEGRAM,
                                            onClick = {
                                                selectedTab = AuthChannelTab.TELEGRAM
                                                errorMessage = null
                                                infoBanner = null
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("auth_tab_telegram")
                                        )
                                    }
                                }
                            }

                            when (selectedTab) {
                                AuthChannelTab.GOOGLE -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.VerifiedUser,
                                                contentDescription = null,
                                                tint = SuccessEmerald,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = SafarLocalization.text(language, L10nKey.AUTH_GOOGLE_HINT),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        GlassButton(
                                            text = SafarLocalization.text(language, L10nKey.AUTH_GOOGLE_CTA),
                                            isLoading = isLoading,
                                            enabled = !isLoading,
                                            onClick = {
                                                errorMessage = null
                                                infoBanner = null
                                                isLoading = true
                                                onGoogleSignInClicked(
                                                    context = context,
                                                    credentialManager = credentialManager,
                                                    onAuthSuccess = {
                                                        isLoading = false
                                                        onAuthSuccess()
                                                    },
                                                    onAuthError = { msg ->
                                                        isLoading = false
                                                        errorMessage = msg
                                                    },
                                                    scope = scope,
                                                    onAuthCancelled = { isLoading = false },
                                                    language = language
                                                )
                                            },
                                            modifier = Modifier.testTag("google_sign_in_button")
                                        )
                                    }
                                }

                                AuthChannelTab.LOGIN_PASSWORD -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        if (loginSubMode != LoginPasswordSubMode.FORGOT_PASSWORD) {
                                            GlassSurface(
                                                shape = RoundedCornerShape(GlassTokens.ControlRadius),
                                                subtle = true,
                                                elevation = 0.dp
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(4.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    SubModeSegment(
                                                        label = SafarLocalization.text(language, L10nKey.AUTH_SIGN_IN),
                                                        selected = loginSubMode == LoginPasswordSubMode.SIGN_IN,
                                                        onClick = {
                                                            loginSubMode = LoginPasswordSubMode.SIGN_IN
                                                            errorMessage = null
                                                            infoBanner = null
                                                        },
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .testTag("login_mode_signin")
                                                    )
                                                    SubModeSegment(
                                                        label = SafarLocalization.text(language, L10nKey.AUTH_CREATE_ACCOUNT),
                                                        selected = loginSubMode == LoginPasswordSubMode.CREATE_ACCOUNT,
                                                        onClick = {
                                                            loginSubMode = LoginPasswordSubMode.CREATE_ACCOUNT
                                                            errorMessage = null
                                                            infoBanner = null
                                                        },
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .testTag("login_mode_signup")
                                                    )
                                                }
                                            }
                                        } else {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                IconButton(
                                                    onClick = {
                                                        loginSubMode = LoginPasswordSubMode.SIGN_IN
                                                        errorMessage = null
                                                        infoBanner = null
                                                    },
                                                    modifier = Modifier
                                                        .minimumInteractiveComponentSize()
                                                        .testTag("auth_forgot_back_button")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                        contentDescription = SafarLocalization.text(
                                                            language,
                                                            L10nKey.AUTH_FORGOT_BACK_CD
                                                        )
                                                    )
                                                }
                                                Text(
                                                    text = SafarLocalization.text(language, L10nKey.AUTH_FORGOT_PASSWORD),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        GlassNoticeBanner(
                                            text = SafarLocalization.text(language, L10nKey.AUTH_LOGIN_UNCONFIGURED_NOTICE),
                                            tone = GlassNoticeTone.WARNING
                                        )

                                        GlassInput(
                                            value = loginEmail,
                                            onValueChange = {
                                                loginEmail = it
                                                errorMessage = null
                                                infoBanner = null
                                            },
                                            label = SafarLocalization.text(language, L10nKey.AUTH_EMAIL_LABEL),
                                            placeholder = SafarLocalization.text(language, L10nKey.AUTH_EMAIL_PLACEHOLDER),
                                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Email,
                                                imeAction = if (loginSubMode == LoginPasswordSubMode.FORGOT_PASSWORD) {
                                                    ImeAction.Done
                                                } else {
                                                    ImeAction.Next
                                                }
                                            ),
                                            keyboardActions = KeyboardActions(
                                                onDone = {
                                                    if (loginSubMode == LoginPasswordSubMode.FORGOT_PASSWORD) {
                                                        submitLoginForm()
                                                    }
                                                }
                                            ),
                                            modifier = Modifier.testTag("auth_login_email_input")
                                        )

                                        if (loginSubMode != LoginPasswordSubMode.FORGOT_PASSWORD) {
                                            GlassInput(
                                                value = loginPassword,
                                                onValueChange = {
                                                    loginPassword = it
                                                    errorMessage = null
                                                    infoBanner = null
                                                },
                                                label = SafarLocalization.text(language, L10nKey.AUTH_PASSWORD_LABEL),
                                                placeholder = SafarLocalization.text(language, L10nKey.AUTH_PASSWORD_PLACEHOLDER),
                                                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                                                trailingIcon = {
                                                    IconButton(
                                                        onClick = { passwordVisible = !passwordVisible },
                                                        modifier = Modifier
                                                            .minimumInteractiveComponentSize()
                                                            .testTag("auth_login_password_toggle")
                                                    ) {
                                                        Icon(
                                                            imageVector = if (passwordVisible) {
                                                                Icons.Filled.VisibilityOff
                                                            } else {
                                                                Icons.Filled.Visibility
                                                            },
                                                            contentDescription = SafarLocalization.text(
                                                                language,
                                                                if (passwordVisible) {
                                                                    L10nKey.AUTH_HIDE_PASSWORD_CD
                                                                } else {
                                                                    L10nKey.AUTH_SHOW_PASSWORD_CD
                                                                }
                                                            )
                                                        )
                                                    }
                                                },
                                                visualTransformation = if (passwordVisible) {
                                                    VisualTransformation.None
                                                } else {
                                                    PasswordVisualTransformation()
                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Password,
                                                    imeAction = if (loginSubMode == LoginPasswordSubMode.CREATE_ACCOUNT) {
                                                        ImeAction.Next
                                                    } else {
                                                        ImeAction.Done
                                                    }
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = {
                                                        if (loginSubMode == LoginPasswordSubMode.SIGN_IN) {
                                                            submitLoginForm()
                                                        }
                                                    }
                                                ),
                                                modifier = Modifier.testTag("auth_login_password_input")
                                            )
                                        }

                                        if (loginSubMode == LoginPasswordSubMode.CREATE_ACCOUNT) {
                                            GlassInput(
                                                value = loginConfirmPassword,
                                                onValueChange = {
                                                    loginConfirmPassword = it
                                                    errorMessage = null
                                                    infoBanner = null
                                                },
                                                label = SafarLocalization.text(language, L10nKey.AUTH_CONFIRM_PASSWORD_LABEL),
                                                placeholder = SafarLocalization.text(language, L10nKey.AUTH_CONFIRM_PASSWORD_PLACEHOLDER),
                                                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                                                trailingIcon = {
                                                    IconButton(
                                                        onClick = { confirmPasswordVisible = !confirmPasswordVisible },
                                                        modifier = Modifier
                                                            .minimumInteractiveComponentSize()
                                                            .testTag("auth_login_confirm_password_toggle")
                                                    ) {
                                                        Icon(
                                                            imageVector = if (confirmPasswordVisible) {
                                                                Icons.Filled.VisibilityOff
                                                            } else {
                                                                Icons.Filled.Visibility
                                                            },
                                                            contentDescription = SafarLocalization.text(
                                                                language,
                                                                if (confirmPasswordVisible) {
                                                                    L10nKey.AUTH_HIDE_PASSWORD_CD
                                                                } else {
                                                                    L10nKey.AUTH_SHOW_PASSWORD_CD
                                                                }
                                                            )
                                                        )
                                                    }
                                                },
                                                visualTransformation = if (confirmPasswordVisible) {
                                                    VisualTransformation.None
                                                } else {
                                                    PasswordVisualTransformation()
                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Password,
                                                    imeAction = ImeAction.Done
                                                ),
                                                keyboardActions = KeyboardActions(
                                                    onDone = { submitLoginForm() }
                                                ),
                                                modifier = Modifier.testTag("auth_login_confirm_password_input")
                                            )
                                        }

                                        if (loginSubMode == LoginPasswordSubMode.SIGN_IN) {
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                TextButton(
                                                    onClick = {
                                                        loginSubMode = LoginPasswordSubMode.FORGOT_PASSWORD
                                                        errorMessage = null
                                                        infoBanner = null
                                                    },
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .minimumInteractiveComponentSize()
                                                        .testTag("auth_forgot_password_button")
                                                ) {
                                                    Text(
                                                        text = SafarLocalization.text(language, L10nKey.AUTH_FORGOT_PASSWORD),
                                                        style = MaterialTheme.typography.labelLarge,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                        }

                                        GlassButton(
                                            text = when (loginSubMode) {
                                                LoginPasswordSubMode.SIGN_IN ->
                                                    SafarLocalization.text(language, L10nKey.AUTH_SIGN_IN)
                                                LoginPasswordSubMode.CREATE_ACCOUNT ->
                                                    SafarLocalization.text(language, L10nKey.AUTH_CREATE_ACCOUNT)
                                                LoginPasswordSubMode.FORGOT_PASSWORD ->
                                                    SafarLocalization.text(language, L10nKey.AUTH_SEND_RESET_CTA)
                                            },
                                            onClick = submitLoginForm,
                                            modifier = Modifier.testTag("auth_login_submit_button")
                                        )
                                    }
                                }

                                AuthChannelTab.PHONE -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        GlassNoticeBanner(
                                            text = SafarLocalization.text(language, L10nKey.AUTH_PHONE_UNCONFIGURED_NOTICE),
                                            tone = GlassNoticeTone.WARNING
                                        )

                                        GlassInput(
                                            value = phoneInput,
                                            onValueChange = {
                                                phoneInput = it
                                                errorMessage = null
                                                infoBanner = null
                                            },
                                            label = SafarLocalization.text(language, L10nKey.AUTH_PHONE_LABEL),
                                            placeholder = SafarLocalization.text(language, L10nKey.AUTH_PHONE_PLACEHOLDER),
                                            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Phone,
                                                imeAction = ImeAction.Done
                                            ),
                                            textStyle = TabularCodeStyle,
                                            modifier = Modifier.testTag("auth_phone_input")
                                        )

                                        UzbekOperatorPrefixChips(
                                            onSelectPrefix = { prefix ->
                                                phoneInput = "$prefix "
                                                errorMessage = null
                                                infoBanner = null
                                            }
                                        )

                                        GlassButton(
                                            text = SafarLocalization.text(language, L10nKey.AUTH_PHONE_SAVE_CTA),
                                            onClick = {
                                                focusManager.clearFocus()
                                                errorMessage = null
                                                infoBanner = null
                                                if (!isValidUzPhone(phoneInput)) {
                                                    errorMessage = SafarLocalization.text(
                                                        language,
                                                        L10nKey.AUTH_ERR_INVALID_PHONE
                                                    )
                                                } else {
                                                    onSavePendingPhone(phoneInput.trim())
                                                    infoBanner = SafarLocalization.text(
                                                        language,
                                                        L10nKey.AUTH_INFO_PHONE_SAVED
                                                    )
                                                }
                                            },
                                            modifier = Modifier.testTag("auth_phone_save_button")
                                        )
                                    }
                                }

                                AuthChannelTab.TELEGRAM -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = botHandle,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            GlassSurface(
                                                shape = CircleShape,
                                                subtle = true,
                                                elevation = 0.dp
                                            ) {
                                                Text(
                                                    text = SafarLocalization.text(
                                                        language,
                                                        L10nKey.AUTH_TELEGRAM_UNCONFIGURED_BADGE
                                                    ),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = warningText,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                )
                                            }
                                        }

                                        GlassNoticeBanner(
                                            text = SafarLocalization.text(
                                                language,
                                                L10nKey.AUTH_TELEGRAM_UNCONFIGURED_NOTICE
                                            ),
                                            tone = GlassNoticeTone.WARNING
                                        )

                                        GlassInput(
                                            value = telegramInput,
                                            onValueChange = {
                                                telegramInput = it
                                                errorMessage = null
                                                infoBanner = null
                                            },
                                            label = SafarLocalization.text(language, L10nKey.AUTH_TELEGRAM_USERNAME_LABEL),
                                            placeholder = SafarLocalization.text(language, L10nKey.AUTH_TELEGRAM_USERNAME_PLACEHOLDER),
                                            leadingIcon = {
                                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                                            },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Ascii,
                                                imeAction = ImeAction.Done
                                            ),
                                            textStyle = TabularCodeStyle,
                                            modifier = Modifier.testTag("auth_telegram_input")
                                        )

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable { telegramNotif = !telegramNotif }
                                                .padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(end = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.NotificationsActive,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = SafarLocalization.text(
                                                        language,
                                                        L10nKey.AUTH_TELEGRAM_NOTIF_PREF
                                                    ),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            Switch(
                                                checked = telegramNotif,
                                                onCheckedChange = { telegramNotif = it },
                                                modifier = Modifier.testTag("auth_telegram_notif_switch"),
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = Color.White,
                                                    checkedTrackColor = IndigoPrimary
                                                )
                                            )
                                        }

                                        GlassButton(
                                            text = SafarLocalization.text(language, L10nKey.AUTH_TELEGRAM_SAVE_CTA),
                                            onClick = {
                                                focusManager.clearFocus()
                                                errorMessage = null
                                                infoBanner = null
                                                if (!isValidTelegramUsername(telegramInput)) {
                                                    errorMessage = SafarLocalization.text(
                                                        language,
                                                        L10nKey.AUTH_ERR_INVALID_TELEGRAM
                                                    )
                                                } else {
                                                    val normalized = "@${cleanTelegramUsername(telegramInput)}"
                                                    onSavePendingTelegram(normalized, telegramNotif)
                                                    infoBanner = SafarLocalization.text(
                                                        language,
                                                        L10nKey.AUTH_INFO_TELEGRAM_SAVED,
                                                        normalized
                                                    )
                                                }
                                            },
                                            modifier = Modifier.testTag("auth_telegram_save_button")
                                        )

                                        GlassSecondaryButton(
                                            onClick = {
                                                val opened = openSafarGoTelegramBot(context)
                                                if (opened) {
                                                    infoBanner = SafarLocalization.text(
                                                        language,
                                                        L10nKey.AUTH_INFO_BOT_OPENED,
                                                        botHandle
                                                    )
                                                } else {
                                                    errorMessage = SafarLocalization.text(
                                                        language,
                                                        L10nKey.AUTH_ERR_BOT_NOT_FOUND
                                                    )
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("open_telegram_bot_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = SafarLocalization.text(
                                                    language,
                                                    L10nKey.AUTH_OPEN_BOT_CTA,
                                                    botHandle
                                                ),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }

                            AnimatedVisibility(visible = infoBanner != null) {
                                infoBanner?.let { msg ->
                                    GlassNoticeBanner(
                                        text = msg,
                                        tone = GlassNoticeTone.INFO,
                                        modifier = Modifier.testTag("auth_info_banner")
                                    )
                                }
                            }

                            if (errorMessage != null) {
                                GlassNoticeBanner(
                                    text = errorMessage!!,
                                    tone = GlassNoticeTone.ERROR,
                                    modifier = Modifier.testTag("auth_error_banner")
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthMethodPill(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        onClick = onClick,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .heightIn(min = 48.dp),
        shape = RoundedCornerShape(GlassTokens.ControlRadius),
        isSelected = selected,
        subtle = !selected,
        elevation = if (selected) GlassTokens.DefaultElevation else 0.dp,
        containerColor = if (selected) IndigoPrimary else null,
        border = if (selected) {
            BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.65f))
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color.White else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SubModeSegment(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        onClick = onClick,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .heightIn(min = 44.dp),
        shape = RoundedCornerShape(10.dp),
        isSelected = selected,
        subtle = !selected,
        elevation = if (selected) 2.dp else 0.dp,
        containerColor = if (selected) null else Color.Transparent,
        border = if (selected) null else BorderStroke(0.dp, Color.Transparent)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun UzbekOperatorPrefixChips(
    onSelectPrefix: (String) -> Unit
) {
    val prefixes = listOf("+998 90", "+998 91", "+998 93", "+998 94", "+998 97", "+998 99")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        prefixes.forEach { prefix ->
            GlassSurface(
                onClick = { onSelectPrefix(prefix) },
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .heightIn(min = 40.dp),
                shape = CircleShape,
                subtle = true,
                elevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = prefix,
                        style = TabularCodeStyle,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Auth - Google (Light)")
@Composable
private fun AuthGateGooglePreview() {
    SafarGoTheme(darkTheme = false) {
        AuthGateScreen(
            language = AppLanguage.UZ,
            initialTab = AuthChannelTab.GOOGLE,
            onCycleLanguage = {},
            onAuthSuccess = {}
        )
    }
}

@Preview(showBackground = true, name = "Auth - Login/Parol (Dark)")
@Composable
private fun AuthGateLoginPasswordDarkPreview() {
    SafarGoTheme(darkTheme = true) {
        AuthGateScreen(
            language = AppLanguage.UZ,
            initialTab = AuthChannelTab.LOGIN_PASSWORD,
            onCycleLanguage = {},
            onAuthSuccess = {}
        )
    }
}

@Preview(showBackground = true, name = "Auth - Telefon")
@Composable
private fun AuthGatePhonePreview() {
    SafarGoTheme(darkTheme = false) {
        AuthGateScreen(
            language = AppLanguage.UZ,
            initialTab = AuthChannelTab.PHONE,
            onCycleLanguage = {},
            onAuthSuccess = {}
        )
    }
}

@Preview(showBackground = true, name = "Auth - Telegram")
@Composable
private fun AuthGateTelegramPreview() {
    SafarGoTheme(darkTheme = false) {
        AuthGateScreen(
            language = AppLanguage.UZ,
            initialTab = AuthChannelTab.TELEGRAM,
            onCycleLanguage = {},
            onAuthSuccess = {}
        )
    }
}
