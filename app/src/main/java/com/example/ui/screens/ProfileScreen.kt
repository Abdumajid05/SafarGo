package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.data.local.UserPreferences
import com.example.data.model.AdminSystemConfigDoc
import com.example.data.model.AppLanguage
import com.example.data.model.BookingDoc
import com.example.data.model.CurrencyCode
import com.example.data.model.L10nKey
import com.example.data.model.SafarCatalog
import com.example.data.model.SafarLocalization
import com.example.data.model.ThemePreference
import com.example.data.model.UserProfileDoc
import com.example.data.remote.TelegramBotService
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassIndigoHeroBox
import com.example.ui.components.GlassInput
import com.example.ui.components.GlassNoticeBanner
import com.example.ui.components.GlassNoticeTone
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.PaymentSelectorTile
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.viewmodel.AdminPanelUiState
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun ProfileScreen(
    profile: UserProfileDoc,
    bookings: List<BookingDoc>,
    preferences: UserPreferences,
    adminConfig: AdminSystemConfigDoc?,
    adminState: AdminPanelUiState,
    isSavingProfile: Boolean,
    onSetCurrency: (CurrencyCode) -> Unit,
    onSetLanguage: (AppLanguage) -> Unit,
    onSetTheme: (ThemePreference) -> Unit,
    onSelectPreferredPayment: (String) -> Unit,
    onSaveTraveler: (String, String, String, String, Boolean) -> Unit,
    onRefreshAdminClaim: () -> Unit,
    onSaveAdminConfig: (String, Boolean, String) -> Unit,
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val resolvedBotHandle = adminConfig?.telegramBotHandle?.takeIf { it.isNotBlank() }
        ?: TelegramBotService().publicConfig.botHandle

    var editName by remember(profile.fullName) { mutableStateOf(profile.fullName.ifBlank { preferences.savedPassengerName }) }
    var editPassport by remember(profile.passportSeries) { mutableStateOf(profile.passportSeries.ifBlank { preferences.savedPassportSeries }) }
    var editPhone by remember(profile.phone) { mutableStateOf(profile.phone.ifBlank { preferences.savedPhone }) }
    var editTelegram by remember(profile.telegramUsername) { mutableStateOf(profile.telegramUsername) }
    var editTelegramNotif by remember(profile.telegramNotificationsEnabled) {
        mutableStateOf(profile.telegramNotificationsEnabled)
    }
    var validationError by remember { mutableStateOf<String?>(null) }

    val lang = preferences.language

    val ptsFmt = remember {
        DecimalFormat("#,###", DecimalFormatSymbols(Locale.US).apply { groupingSeparator = ' ' })
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 720.dp)
                .imePadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GlassIndigoHeroBox(shape = RoundedCornerShape(GlassTokens.CardRadius)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = profile.fullName.ifBlank {
                                    SafarLocalization.text(lang, L10nKey.PROFILE_FALLBACK_NAME)
                                },
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (profile.passportSeries.isNotBlank()) {
                                    SafarLocalization.text(
                                        lang,
                                        L10nKey.PROFILE_PASSPORT_VALUE,
                                        profile.passportSeries
                                    )
                                } else {
                                    SafarLocalization.text(lang, L10nKey.PROFILE_PASSPORT_EMPTY)
                                },
                                style = TabularCodeStyle,
                                color = SkyBlueAccent
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = SafarLocalization.text(
                                    lang,
                                    L10nKey.PROFILE_CLUB_SUMMARY,
                                    profile.tierLabel,
                                    ptsFmt.format(profile.clubPoints)
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.92f)
                            )
                            if (profile.telegramUsername.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Telegram: ${profile.telegramUsername} · $resolvedBotHandle",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SkyBlueAccent
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Stars,
                                contentDescription = null,
                                tint = SkyBlueAccent,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            item {
                GlassCard(
                    shape = RoundedCornerShape(GlassTokens.CardRadius),
                    elevation = GlassTokens.DefaultElevation
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = SafarLocalization.text(lang, L10nKey.PROFILE_DISPLAY_SETTINGS),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = SafarLocalization.text(lang, L10nKey.PROFILE_CURRENCY),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CurrencyCode.entries.forEach { code ->
                                val selected = preferences.currency == code
                                GlassSurface(
                                    onClick = { onSetCurrency(code) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .minimumInteractiveComponentSize()
                                        .heightIn(min = 44.dp)
                                        .testTag("pref_currency_${code.name}"),
                                    shape = RoundedCornerShape(10.dp),
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
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = code.name,
                                            style = TabularCodeStyle,
                                            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = SafarLocalization.text(lang, L10nKey.PROFILE_LANGUAGE),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppLanguage.entries.forEach { appLang ->
                                val selected = preferences.language == appLang
                                GlassSurface(
                                    onClick = { onSetLanguage(appLang) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .minimumInteractiveComponentSize()
                                        .heightIn(min = 44.dp)
                                        .testTag("pref_lang_${appLang.name}"),
                                    shape = RoundedCornerShape(10.dp),
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
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = appLang.fullName,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = SafarLocalization.text(lang, L10nKey.PROFILE_THEME),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ThemePreference.entries.forEach { pref ->
                                val selected = preferences.themePreference == pref
                                val prefLabel = when (pref) {
                                    ThemePreference.SYSTEM -> SafarLocalization.text(lang, L10nKey.PROFILE_THEME_SYSTEM)
                                    ThemePreference.LIGHT -> SafarLocalization.text(lang, L10nKey.PROFILE_THEME_LIGHT)
                                    ThemePreference.DARK -> SafarLocalization.text(lang, L10nKey.PROFILE_THEME_DARK)
                                }
                                GlassSurface(
                                    onClick = { onSetTheme(pref) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .minimumInteractiveComponentSize()
                                        .heightIn(min = 44.dp)
                                        .testTag("pref_theme_${pref.name}"),
                                    shape = RoundedCornerShape(10.dp),
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
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = prefLabel,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = SafarLocalization.text(lang, L10nKey.PROFILE_PAYMENT_RAILS),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    SafarCatalog.paymentRails.forEach { rail ->
                        PaymentSelectorTile(
                            rail = rail,
                            selected = profile.preferredPaymentId == rail.id,
                            onSelect = { onSelectPreferredPayment(rail.id) },
                            language = preferences.language
                        )
                    }
                }
            }

            item {
                GlassCard(
                    shape = RoundedCornerShape(GlassTokens.CardRadius),
                    elevation = GlassTokens.DefaultElevation
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Badge,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = SafarLocalization.text(lang, L10nKey.PROFILE_TRAVELER_DOC),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        GlassInput(
                            value = editName,
                            onValueChange = { editName = it; validationError = null },
                            label = SafarLocalization.text(lang, L10nKey.PROFILE_FULLNAME_LABEL),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier.testTag("profile_name_input")
                        )
                        GlassInput(
                            value = editPassport,
                            onValueChange = { editPassport = it; validationError = null },
                            label = SafarLocalization.text(lang, L10nKey.PROFILE_PASSPORT_LABEL),
                            placeholder = SafarLocalization.text(lang, L10nKey.REG_PASSPORT_PLACEHOLDER),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Characters,
                                imeAction = ImeAction.Next
                            ),
                            textStyle = TabularCodeStyle,
                            modifier = Modifier.testTag("profile_passport_input")
                        )
                        GlassInput(
                            value = editPhone,
                            onValueChange = { editPhone = it; validationError = null },
                            label = SafarLocalization.text(lang, L10nKey.PROFILE_PHONE_LABEL),
                            placeholder = SafarLocalization.text(lang, L10nKey.AUTH_PHONE_PLACEHOLDER),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Next
                            ),
                            textStyle = TabularCodeStyle,
                            modifier = Modifier.testTag("profile_phone_input")
                        )
                        GlassInput(
                            value = editTelegram,
                            onValueChange = { editTelegram = it; validationError = null },
                            label = "Telegram @username ($resolvedBotHandle)",
                            placeholder = SafarLocalization.text(
                                lang,
                                L10nKey.AUTH_TELEGRAM_USERNAME_PLACEHOLDER
                            ),
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done
                            ),
                            textStyle = TabularCodeStyle,
                            modifier = Modifier.testTag("profile_telegram_input")
                        )
                        GlassNoticeBanner(
                            text = if (profile.telegramChatId != 0L) {
                                SafarLocalization.text(
                                    lang,
                                    L10nKey.PROFILE_TELEGRAM_VERIFIED,
                                    resolvedBotHandle
                                )
                            } else {
                                SafarLocalization.text(lang, L10nKey.PROFILE_TELEGRAM_NOT_SET)
                            },
                            tone = if (profile.telegramChatId != 0L) {
                                GlassNoticeTone.SUCCESS
                            } else {
                                GlassNoticeTone.WARNING
                            }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { editTelegramNotif = !editTelegramNotif }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = SafarLocalization.text(
                                    lang,
                                    L10nKey.PROFILE_TELEGRAM_PREF_BOT,
                                    resolvedBotHandle
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            )
                            Switch(
                                checked = editTelegramNotif,
                                onCheckedChange = { editTelegramNotif = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = IndigoPrimary
                                )
                            )
                        }
                        GlassSecondaryButton(
                            onClick = { openTelegramBotUrl(context, resolvedBotHandle) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("profile_open_telegram_bot_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = SafarLocalization.text(
                                    lang,
                                    L10nKey.AUTH_OPEN_BOT_CTA,
                                    resolvedBotHandle
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (validationError != null) {
                            GlassNoticeBanner(
                                text = validationError!!,
                                tone = GlassNoticeTone.ERROR
                            )
                        }
                        GlassButton(
                            text = if (isSavingProfile) {
                                SafarLocalization.text(lang, L10nKey.REG_SAVING)
                            } else {
                                SafarLocalization.text(lang, L10nKey.PROFILE_SAVE_DETAILS)
                            },
                            isLoading = isSavingProfile,
                            enabled = !isSavingProfile,
                            onClick = {
                                val cleanTg = cleanTelegramUsername(editTelegram)
                                when {
                                    editName.trim().length < 2 ->
                                        validationError = SafarLocalization.text(lang, L10nKey.REG_ERR_NAME)
                                    editPassport.trim().length < 5 ->
                                        validationError = SafarLocalization.text(lang, L10nKey.REG_ERR_PASSPORT)
                                    editPhone.filter { it.isDigit() }.length < 9 ->
                                        validationError = SafarLocalization.text(lang, L10nKey.REG_ERR_PHONE)
                                    cleanTg.isNotBlank() && !isValidTelegramUsername(editTelegram) ->
                                        validationError = SafarLocalization.text(lang, L10nKey.REG_ERR_TELEGRAM)
                                    else -> {
                                        validationError = null
                                        val normalizedTg = if (cleanTg.isBlank()) "" else "@$cleanTg"
                                        onSaveTraveler(
                                            editName.trim(),
                                            editPassport.trim().uppercase(),
                                            editPhone.trim(),
                                            normalizedTg,
                                            editTelegramNotif && normalizedTg.isNotBlank()
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.testTag("save_profile_button")
                        )
                    }
                }
            }

            item {
                AdminConsoleSection(
                    profile = profile,
                    bookings = bookings,
                    adminConfig = adminConfig,
                    adminState = adminState,
                    onRefreshAdminClaim = onRefreshAdminClaim,
                    onSaveAdminConfig = onSaveAdminConfig,
                    language = lang
                )
            }

            item {
                GlassSecondaryButton(
                    onClick = {
                        signOutSafarGo(
                            credentialManager = credentialManager,
                            scope = scope,
                            onSignOutComplete = onSignedOut
                        )
                    },
                    contentColor = MaterialTheme.colorScheme.error,
                    borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.65f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sign_out_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = SafarLocalization.text(lang, L10nKey.PROFILE_SIGN_OUT))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
