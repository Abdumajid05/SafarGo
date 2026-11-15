package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.data.model.AdminSystemConfigDoc
import com.example.data.model.AppLanguage
import com.example.data.model.BookingDoc
import com.example.data.model.BookingStatusTag
import com.example.data.model.L10nKey
import com.example.data.model.SafarLocalization
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
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.theme.WarningAmberDarkText
import com.example.ui.theme.WarningAmberText
import com.example.ui.viewmodel.AdminPanelUiState

internal fun openTelegramBotUrl(context: Context, botUsername: String?): Boolean {
    val cleanUser = botUsername.orEmpty()
        .trim()
        .removePrefix("@")
        .ifBlank { "SafarGoUzBot" }
    return runCatching {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://t.me/$cleanUser?start=safargo_auth")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        true
    }.getOrDefault(false)
}

@Composable
fun AdminConsoleSection(
    profile: UserProfileDoc,
    bookings: List<BookingDoc>,
    adminConfig: AdminSystemConfigDoc?,
    adminState: AdminPanelUiState,
    onRefreshAdminClaim: () -> Unit,
    onSaveAdminConfig: (telegramHandle: String, webhookConfigured: Boolean, maintenanceNotice: String) -> Unit,
    modifier: Modifier = Modifier,
    language: AppLanguage = AppLanguage.UZ
) {
    val context = LocalContext.current
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val warningColor = if (isDark) WarningAmberDarkText else WarningAmberText
    val botService = remember { TelegramBotService() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when {
            adminState.isCheckingClaim -> {
                GlassCard(
                    shape = RoundedCornerShape(GlassTokens.CardRadius),
                    elevation = GlassTokens.DefaultElevation
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Text(
                            text = SafarLocalization.text(language, L10nKey.ADMIN_CHECKING_CLAIM),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            !adminState.isAdminClaimVerified -> {
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(IndigoPrimary.copy(alpha = 0.14f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.ADMIN_PANEL_TITLE),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.ADMIN_SERVER_REQUIRED),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = warningColor
                                )
                            }
                        }

                        Text(
                            text = SafarLocalization.text(language, L10nKey.ADMIN_SECURITY_DESC),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        GlassNoticeBanner(
                            text = SafarLocalization.text(language, L10nKey.ADMIN_SETUP_HINT),
                            tone = GlassNoticeTone.WARNING
                        )

                        adminState.lastActionMessage?.let { msg ->
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        GlassSecondaryButton(
                            onClick = onRefreshAdminClaim,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_refresh_claim_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(SafarLocalization.text(language, L10nKey.ADMIN_RECHECK_CLAIM))
                        }
                    }
                }
            }

            else -> {
                GlassIndigoHeroBox(shape = RoundedCornerShape(GlassTokens.CardRadius)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = SkyBlueAccent
                                )
                                Column {
                                    Text(
                                        text = SafarLocalization.text(language, L10nKey.ADMIN_CONSOLE_HEADER),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.VerifiedUser,
                                            contentDescription = null,
                                            tint = SuccessEmerald,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = SafarLocalization.text(language, L10nKey.ADMIN_CLAIM_ACTIVE),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SkyBlueAccent
                                        )
                                    }
                                }
                            }
                            IconButton(onClick = onRefreshAdminClaim) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = SafarLocalization.text(
                                        language,
                                        L10nKey.ADMIN_REFRESH_TOKEN_CD
                                    ),
                                    tint = Color.White
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            AdminMetricChip(
                                label = SafarLocalization.text(language, L10nKey.ADMIN_METRIC_TOTAL),
                                value = SafarLocalization.text(
                                    language,
                                    L10nKey.ADMIN_METRIC_TOTAL_VAL,
                                    bookings.size
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            AdminMetricChip(
                                label = SafarLocalization.text(language, L10nKey.ADMIN_METRIC_CHECKIN),
                                value = SafarLocalization.text(
                                    language,
                                    L10nKey.ADMIN_METRIC_CHECKIN_VAL,
                                    bookings.count { it.statusTag == BookingStatusTag.CHECKED_IN }
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            AdminMetricChip(
                                label = SafarLocalization.text(language, L10nKey.ADMIN_METRIC_TELEGRAM),
                                value = if (profile.telegramChatId != 0L) {
                                    SafarLocalization.text(language, L10nKey.ADMIN_TG_CONNECTED)
                                } else {
                                    SafarLocalization.text(language, L10nKey.ADMIN_TG_PENDING)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Server-guarded /adminConfig card & Telegram setup status
                var handleInput by remember(adminConfig?.telegramBotHandle) {
                    mutableStateOf(adminConfig?.telegramBotHandle ?: botService.publicConfig.botHandle)
                }
                var webhookFlag by remember(adminConfig?.telegramWebhookConfigured) {
                    mutableStateOf(adminConfig?.telegramWebhookConfigured ?: false)
                }
                var noticeInput by remember(adminConfig?.maintenanceNotice) {
                    mutableStateOf(adminConfig?.maintenanceNotice ?: "")
                }

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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = SafarLocalization.text(
                                        language,
                                        L10nKey.ADMIN_TG_INTEGRATION_TITLE
                                    ),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            GlassSurface(
                                shape = CircleShape,
                                subtle = true,
                                elevation = 0.dp
                            ) {
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.ADMIN_TG_SETUP_REQUIRED),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = warningColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        GlassNoticeBanner(
                            text = SafarLocalization.text(language, L10nKey.ADMIN_TG_SECURITY_NOTE),
                            tone = GlassNoticeTone.INFO
                        )

                        GlassInput(
                            value = handleInput,
                            onValueChange = { handleInput = it },
                            label = SafarLocalization.text(language, L10nKey.ADMIN_TG_HANDLE_LABEL),
                            textStyle = TabularCodeStyle,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.testTag("admin_bot_handle_input")
                        )

                        GlassInput(
                            value = noticeInput,
                            onValueChange = { noticeInput = it },
                            label = SafarLocalization.text(language, L10nKey.ADMIN_NOTICE_LABEL),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier.testTag("admin_maintenance_input")
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { webhookFlag = !webhookFlag }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = SafarLocalization.text(language, L10nKey.ADMIN_WEBHOOK_SWITCH),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            )
                            Switch(
                                checked = webhookFlag,
                                onCheckedChange = { webhookFlag = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = IndigoPrimary
                                )
                            )
                        }

                        adminState.lastActionMessage?.let { msg ->
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GlassSecondaryButton(
                                onClick = { openTelegramBotUrl(context, handleInput) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    SafarLocalization.text(language, L10nKey.ADMIN_OPEN_BOT),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }

                            GlassButton(
                                text = SafarLocalization.text(language, L10nKey.SAVE_LABEL),
                                isLoading = adminState.isSavingConfig,
                                enabled = !adminState.isSavingConfig,
                                onClick = { onSaveAdminConfig(handleInput, webhookFlag, noticeInput) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_save_config_button")
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMetricChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.24f))
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
