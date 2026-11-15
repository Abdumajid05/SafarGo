package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.data.model.AdminSystemConfigDoc
import com.example.data.model.AppLanguage
import com.example.data.model.L10nKey
import com.example.data.model.SafarLocalization
import com.example.data.model.UserProfileDoc
import com.example.data.remote.TelegramBotService
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassInput
import com.example.ui.components.GlassNoticeBanner
import com.example.ui.components.GlassNoticeTone
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.SuccessEmeraldText
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.theme.WarningAmberDarkText
import com.example.ui.theme.WarningAmberText
import com.example.ui.viewmodel.AdminPanelUiState

enum class LoginMenuTab {
    GOOGLE, PHONE, TELEGRAM, ADMIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginMenuBottomSheet(
    userEmail: String?,
    profile: UserProfileDoc,
    adminConfig: AdminSystemConfigDoc?,
    adminState: AdminPanelUiState,
    isSavingProfile: Boolean,
    onDismiss: () -> Unit,
    onRefreshAdminClaim: () -> Unit,
    onUpdatePhone: (String) -> Unit,
    onOpenAdminConsoleInProfile: () -> Unit,
    onSignedOut: () -> Unit,
    language: AppLanguage = AppLanguage.UZ
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val glassSheetColor = if (isDark) Color(0xF010182E) else Color(0xF2EEF3FF)
    val warningColor = if (isDark) WarningAmberDarkText else WarningAmberText
    val successColor = if (isDark) SuccessEmerald else SuccessEmeraldText

    var selectedTab by remember { mutableStateOf(LoginMenuTab.GOOGLE) }
    var localError by remember { mutableStateOf<String?>(null) }
    var phoneInput by remember(profile.phone) { mutableStateOf(profile.phone) }
    val resolvedBotHandle = adminConfig?.telegramBotHandle?.takeIf { it.isNotBlank() }
        ?: TelegramBotService().publicConfig.botHandle

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = glassSheetColor,
        shape = RoundedCornerShape(topStart = GlassTokens.SheetRadius, topEnd = GlassTokens.SheetRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = SafarLocalization.text(language, L10nKey.MENU_TITLE),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = SafarLocalization.text(language, L10nKey.MENU_SUBTITLE),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                GlassSurface(
                    shape = CircleShape,
                    subtle = true,
                    elevation = 0.dp
                ) {
                    Text(
                        text = if (adminState.isAdminClaimVerified) {
                            SafarLocalization.text(language, L10nKey.MENU_ROLE_ADMIN)
                        } else {
                            SafarLocalization.text(language, L10nKey.MENU_ROLE_TRAVELER)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (adminState.isAdminClaimVerified) successColor else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("login_menu_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = SafarLocalization.text(language, L10nKey.CD_CLOSE)
                    )
                }
            }

            // 2x2 Channel Selector Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MenuChannelTile(
                        label = SafarLocalization.text(language, L10nKey.MENU_TAB_GOOGLE),
                        icon = Icons.Filled.AccountCircle,
                        selected = selectedTab == LoginMenuTab.GOOGLE,
                        onClick = {
                            selectedTab = LoginMenuTab.GOOGLE
                            localError = null
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("menu_tab_google")
                    )
                    MenuChannelTile(
                        label = SafarLocalization.text(language, L10nKey.MENU_TAB_PHONE),
                        icon = Icons.Filled.Phone,
                        selected = selectedTab == LoginMenuTab.PHONE,
                        onClick = {
                            selectedTab = LoginMenuTab.PHONE
                            localError = null
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("menu_tab_phone")
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MenuChannelTile(
                        label = SafarLocalization.text(language, L10nKey.MENU_TAB_TELEGRAM),
                        icon = Icons.AutoMirrored.Filled.Send,
                        selected = selectedTab == LoginMenuTab.TELEGRAM,
                        onClick = {
                            selectedTab = LoginMenuTab.TELEGRAM
                            localError = null
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("menu_tab_telegram")
                    )
                    MenuChannelTile(
                        label = SafarLocalization.text(language, L10nKey.MENU_TAB_ADMIN),
                        icon = Icons.Filled.AdminPanelSettings,
                        selected = selectedTab == LoginMenuTab.ADMIN,
                        onClick = {
                            selectedTab = LoginMenuTab.ADMIN
                            localError = null
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("menu_tab_admin")
                    )
                }
            }

            when (selectedTab) {
                LoginMenuTab.GOOGLE -> {
                    GlassCard(
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        subtle = true,
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = SafarLocalization.text(language, L10nKey.MENU_GOOGLE_ACTIVE),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = userEmail?.takeIf { it.isNotBlank() } ?: profile.fullName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            GlassSecondaryButton(
                                onClick = {
                                    onDismiss()
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
                                    .testTag("menu_sign_out_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(SafarLocalization.text(language, L10nKey.MENU_SIGN_OUT_OTHER))
                            }
                        }
                    }
                }

                LoginMenuTab.PHONE -> {
                    GlassCard(
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        subtle = true,
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = SafarLocalization.text(language, L10nKey.MENU_PHONE_TITLE),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            GlassInput(
                                value = phoneInput,
                                onValueChange = { phoneInput = it; localError = null },
                                label = SafarLocalization.text(language, L10nKey.MENU_PHONE_LABEL),
                                placeholder = SafarLocalization.text(language, L10nKey.AUTH_PHONE_PLACEHOLDER),
                                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Done
                                ),
                                textStyle = TabularCodeStyle,
                                modifier = Modifier.testTag("menu_phone_input")
                            )
                            if (localError != null) {
                                GlassNoticeBanner(
                                    text = localError!!,
                                    tone = GlassNoticeTone.ERROR
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlassSecondaryButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(SafarLocalization.text(language, L10nKey.CANCEL_LABEL))
                                }
                                GlassButton(
                                    text = SafarLocalization.text(language, L10nKey.SAVE_LABEL),
                                    isLoading = isSavingProfile,
                                    enabled = !isSavingProfile,
                                    onClick = {
                                        if (phoneInput.filter { it.isDigit() }.length < 9) {
                                            localError = SafarLocalization.text(language, L10nKey.MENU_PHONE_ERR)
                                        } else {
                                            onUpdatePhone(phoneInput.trim())
                                            onDismiss()
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("menu_save_phone_button")
                                )
                            }
                        }
                    }
                }

                LoginMenuTab.TELEGRAM -> {
                    GlassCard(
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        subtle = true,
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Telegram ($resolvedBotHandle)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                GlassSurface(
                                    shape = CircleShape,
                                    subtle = true,
                                    elevation = 0.dp
                                ) {
                                    Text(
                                        text = if (profile.telegramChatId != 0L) {
                                            SafarLocalization.text(language, L10nKey.MENU_TELEGRAM_CONNECTED)
                                        } else {
                                            SafarLocalization.text(language, L10nKey.MENU_TELEGRAM_NOT_CONFIGURED)
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (profile.telegramChatId != 0L) successColor else warningColor,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            GlassNoticeBanner(
                                text = if (profile.telegramChatId != 0L) {
                                    SafarLocalization.text(
                                        language,
                                        L10nKey.MENU_TELEGRAM_STATUS_CONNECTED,
                                        profile.telegramUsername
                                    )
                                } else {
                                    SafarLocalization.text(
                                        language,
                                        L10nKey.MENU_TELEGRAM_STATUS_UNSET,
                                        profile.telegramUsername.ifBlank {
                                            SafarLocalization.text(language, L10nKey.MENU_TELEGRAM_NO_USERNAME)
                                        }
                                    )
                                },
                                tone = if (profile.telegramChatId != 0L) {
                                    GlassNoticeTone.SUCCESS
                                } else {
                                    GlassNoticeTone.WARNING
                                }
                            )
                            GlassSecondaryButton(
                                onClick = { openTelegramBotUrl(context, resolvedBotHandle) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    SafarLocalization.text(
                                        language,
                                        L10nKey.AUTH_OPEN_BOT_CTA,
                                        resolvedBotHandle
                                    )
                                )
                            }
                            TextButton(
                                onClick = {
                                    onDismiss()
                                    onOpenAdminConsoleInProfile()
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .minimumInteractiveComponentSize()
                            ) {
                                Text(SafarLocalization.text(language, L10nKey.MENU_OPEN_PROFILE_SETTINGS))
                            }
                        }
                    }
                }

                LoginMenuTab.ADMIN -> {
                    GlassCard(
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        subtle = true,
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.VerifiedUser,
                                    contentDescription = null,
                                    tint = if (adminState.isAdminClaimVerified) {
                                        successColor
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    }
                                )
                                Text(
                                    text = if (adminState.isAdminClaimVerified) {
                                        SafarLocalization.text(language, L10nKey.MENU_ADMIN_VERIFIED)
                                    } else {
                                        SafarLocalization.text(language, L10nKey.MENU_ADMIN_UNVERIFIED)
                                    },
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Text(
                                text = if (adminState.isAdminClaimVerified) {
                                    SafarLocalization.text(language, L10nKey.MENU_ADMIN_ACTIVE_DESC)
                                } else {
                                    SafarLocalization.text(language, L10nKey.MENU_ADMIN_LOCKED_DESC)
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlassSecondaryButton(
                                    onClick = onRefreshAdminClaim,
                                    enabled = !adminState.isCheckingClaim,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("menu_refresh_claim_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(SafarLocalization.text(language, L10nKey.MENU_CHECK_CLAIM))
                                }
                                if (adminState.isAdminClaimVerified) {
                                    GlassButton(
                                        text = SafarLocalization.text(language, L10nKey.MENU_OPEN_ADMIN_PANEL),
                                        onClick = {
                                            onDismiss()
                                            onOpenAdminConsoleInProfile()
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuChannelTile(
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
        border = if (selected) BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.65f)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
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
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
