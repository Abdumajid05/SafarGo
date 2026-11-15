package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.data.model.AppLanguage
import com.example.data.model.L10nKey
import com.example.data.model.SafarCatalog
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
import com.example.ui.components.PaymentSelectorTile
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SafarGoTheme
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.theme.WarningAmberDarkText
import com.example.ui.theme.WarningAmberText

@Composable
fun RegistrationProfileScreen(
    language: AppLanguage = AppLanguage.UZ,
    initialName: String,
    initialPhone: String = "",
    initialTelegramHandle: String = "",
    initialTelegramNotif: Boolean = false,
    isSubmitting: Boolean,
    errorMessage: String?,
    onSubmitRegistration: (
        fullName: String,
        phone: String,
        passportSeries: String,
        paymentId: String,
        telegramUsername: String,
        telegramNotificationsEnabled: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }
    val botHandle = remember { TelegramBotService().publicConfig.botHandle }
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val warningText = if (isDark) WarningAmberDarkText else WarningAmberText

    var fullName by remember(initialName) { mutableStateOf(initialName) }
    var phone by remember(initialPhone) { mutableStateOf(initialPhone.ifBlank { "+998 " }) }
    var passportSeries by remember { mutableStateOf("") }
    var telegramUsername by remember(initialTelegramHandle) { mutableStateOf(initialTelegramHandle) }
    var telegramNotifEnabled by remember(initialTelegramNotif) { mutableStateOf(initialTelegramNotif) }
    var selectedPaymentId by remember { mutableStateOf("uzcard") }
    var localValidationError by remember { mutableStateOf<String?>(null) }
    var botOpenedNote by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = !isSubmitting) {
        signOutSafarGo(credentialManager, scope)
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
                    .widthIn(max = 600.dp)
                    .imePadding()
                    .testTag("registration_list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Clean Top Header + Cancel Button
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = SafarLocalization.text(language, L10nKey.REG_TITLE),
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = { signOutSafarGo(credentialManager, scope) },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("registration_cancel_signout_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = SafarLocalization.text(language, L10nKey.REG_CANCEL))
                            }
                        }
                        Text(
                            text = SafarLocalization.text(language, L10nKey.REG_SUBTITLE),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 2. Personal & Passport Section
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
                                text = SafarLocalization.text(language, L10nKey.REG_PERSONAL_SECTION),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            GlassInput(
                                value = fullName,
                                onValueChange = { fullName = it; localValidationError = null },
                                label = SafarLocalization.text(language, L10nKey.REG_FULLNAME_LABEL),
                                placeholder = SafarLocalization.text(language, L10nKey.REG_FULLNAME_PLACEHOLDER),
                                leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier.testTag("reg_fullname_input")
                            )

                            GlassInput(
                                value = passportSeries,
                                onValueChange = { passportSeries = it; localValidationError = null },
                                label = SafarLocalization.text(language, L10nKey.REG_PASSPORT_LABEL),
                                placeholder = SafarLocalization.text(language, L10nKey.REG_PASSPORT_PLACEHOLDER),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Characters,
                                    imeAction = ImeAction.Next
                                ),
                                textStyle = TabularCodeStyle,
                                modifier = Modifier.testTag("reg_passport_input")
                            )
                        }
                    }
                }

                // 3. Phone Number Section
                item {
                    GlassCard(
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        elevation = GlassTokens.DefaultElevation
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = SafarLocalization.text(language, L10nKey.REG_PHONE_SECTION),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            GlassInput(
                                value = phone,
                                onValueChange = { phone = it; localValidationError = null },
                                label = SafarLocalization.text(language, L10nKey.REG_PHONE_LABEL),
                                placeholder = SafarLocalization.text(language, L10nKey.AUTH_PHONE_PLACEHOLDER),
                                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Next
                                ),
                                textStyle = TabularCodeStyle,
                                modifier = Modifier.testTag("reg_phone_input")
                            )

                            UzbekOperatorPrefixChips(
                                onSelectPrefix = { prefix ->
                                    phone = "$prefix "
                                    localValidationError = null
                                }
                            )
                        }
                    }
                }

                // 4. Optional Telegram Section
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
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Telegram ($botHandle)",
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
                                            L10nKey.REG_TELEGRAM_OPTIONAL_BADGE
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = warningText,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            GlassNoticeBanner(
                                text = SafarLocalization.text(language, L10nKey.REG_TELEGRAM_NOTICE),
                                tone = GlassNoticeTone.WARNING
                            )

                            GlassInput(
                                value = telegramUsername,
                                onValueChange = {
                                    telegramUsername = it
                                    localValidationError = null
                                },
                                label = SafarLocalization.text(language, L10nKey.REG_TELEGRAM_LABEL),
                                placeholder = SafarLocalization.text(
                                    language,
                                    L10nKey.AUTH_TELEGRAM_USERNAME_PLACEHOLDER
                                ),
                                leadingIcon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Ascii,
                                    imeAction = ImeAction.Done
                                ),
                                textStyle = TabularCodeStyle,
                                modifier = Modifier.testTag("reg_telegram_input")
                            )

                            GlassSecondaryButton(
                                onClick = {
                                    val opened = openSafarGoTelegramBot(context)
                                    botOpenedNote = if (opened) {
                                        SafarLocalization.text(
                                            language,
                                            L10nKey.AUTH_INFO_BOT_OPENED,
                                            botHandle
                                        )
                                    } else {
                                        SafarLocalization.text(
                                            language,
                                            L10nKey.AUTH_ERR_BOT_NOT_FOUND
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_open_telegram_bot_button")
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

                            botOpenedNote?.let { note ->
                                Text(
                                    text = note,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { telegramNotifEnabled = !telegramNotifEnabled }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.REG_TELEGRAM_NOTIF_LABEL),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 10.dp)
                                )
                                Switch(
                                    checked = telegramNotifEnabled,
                                    onCheckedChange = { telegramNotifEnabled = it },
                                    modifier = Modifier.testTag("reg_telegram_notif_switch"),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = IndigoPrimary
                                    )
                                )
                            }
                        }
                    }
                }

                // 5. Preferred Payment Rail (Demo)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = SafarLocalization.text(language, L10nKey.REG_PAYMENT_SECTION),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        SafarCatalog.paymentRails.take(4).forEach { rail ->
                            PaymentSelectorTile(
                                rail = rail,
                                selected = selectedPaymentId == rail.id,
                                onSelect = { selectedPaymentId = rail.id },
                                language = language
                            )
                        }
                    }
                }

                val displayError = localValidationError ?: errorMessage
                if (displayError != null) {
                    item {
                        GlassNoticeBanner(
                            text = displayError,
                            tone = GlassNoticeTone.ERROR,
                            modifier = Modifier.testTag("registration_error_banner")
                        )
                    }
                }

                item {
                    GlassButton(
                        text = if (isSubmitting) {
                            SafarLocalization.text(language, L10nKey.REG_SAVING)
                        } else {
                            SafarLocalization.text(language, L10nKey.REG_SUBMIT_CTA)
                        },
                        enabled = !isSubmitting,
                        isLoading = isSubmitting,
                        onClick = {
                            val cleanTelegram = cleanTelegramUsername(telegramUsername)
                            when {
                                fullName.trim().length < 2 -> {
                                    localValidationError = SafarLocalization.text(language, L10nKey.REG_ERR_NAME)
                                }
                                !isValidUzPhone(phone) -> {
                                    localValidationError = SafarLocalization.text(language, L10nKey.REG_ERR_PHONE)
                                }
                                passportSeries.trim().length < 5 -> {
                                    localValidationError = SafarLocalization.text(language, L10nKey.REG_ERR_PASSPORT)
                                }
                                cleanTelegram.isNotBlank() && !isValidTelegramUsername(telegramUsername) -> {
                                    localValidationError = SafarLocalization.text(language, L10nKey.REG_ERR_TELEGRAM)
                                }
                                else -> {
                                    val normalizedHandle = if (cleanTelegram.isBlank()) "" else "@$cleanTelegram"
                                    onSubmitRegistration(
                                        fullName.trim(),
                                        phone.trim(),
                                        passportSeries.trim().uppercase(),
                                        selectedPaymentId,
                                        normalizedHandle,
                                        telegramNotifEnabled && normalizedHandle.isNotBlank()
                                    )
                                }
                            }
                        },
                        modifier = Modifier.testTag("complete_registration_button")
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Registration Profile (Light)")
@Composable
private fun RegistrationProfilePreview() {
    SafarGoTheme(darkTheme = false) {
        RegistrationProfileScreen(
            language = AppLanguage.UZ,
            initialName = "Farrux Alimov",
            initialPhone = "+998 90 123 45 67",
            isSubmitting = false,
            errorMessage = null,
            onSubmitRegistration = { _, _, _, _, _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "Registration Profile (Dark)")
@Composable
private fun RegistrationProfileDarkPreview() {
    SafarGoTheme(darkTheme = true) {
        RegistrationProfileScreen(
            language = AppLanguage.UZ,
            initialName = "Farrux Alimov",
            initialPhone = "+998 90 123 45 67",
            isSubmitting = false,
            errorMessage = null,
            onSubmitRegistration = { _, _, _, _, _, _ -> }
        )
    }
}
