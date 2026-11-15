package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.BookingStatusTag
import com.example.data.model.CurrencyCode
import com.example.data.model.CurrencyFormatter
import com.example.data.model.L10nKey
import com.example.data.model.PaymentRail
import com.example.data.model.SafarLocalization
import com.example.data.model.SafarStrings
import com.example.data.model.TravelMode
import com.example.data.model.TravelOption
import com.example.ui.theme.ClickCyan
import com.example.ui.theme.HumoGold
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.PaymeTeal
import com.example.ui.theme.PrioritySkyBg
import com.example.ui.theme.PrioritySkyDarkBg
import com.example.ui.theme.PrioritySkyDarkText
import com.example.ui.theme.PrioritySkyText
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.SuccessEmeraldBg
import com.example.ui.theme.SuccessEmeraldDarkBg
import com.example.ui.theme.SuccessEmeraldDarkText
import com.example.ui.theme.SuccessEmeraldText
import com.example.ui.theme.TabularPriceStyle
import com.example.ui.theme.TabularTimeStyle
import com.example.ui.theme.UzcardBlue
import com.example.ui.theme.UzumPurple
import com.example.ui.theme.VisaNavy
import com.example.ui.theme.WarningAmberBg
import com.example.ui.theme.WarningAmberDarkBg
import com.example.ui.theme.WarningAmberDarkText
import com.example.ui.theme.WarningAmberText
import kotlin.math.abs

@Composable
fun SafarGoGlassTopBar(
    language: AppLanguage,
    hasUnreadNotification: Boolean,
    isAdminUnlocked: Boolean = false,
    onCycleLanguage: () -> Unit,
    onLoginMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    GlassSurface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        elevation = GlassTokens.NavElevation,
        border = BorderStroke(0.dp, Color.Transparent)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(IndigoPrimary, IndigoPrimary.copy(alpha = 0.85f))
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(11.dp)),
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
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GlassSurface(
                        onClick = onCycleLanguage,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("language_toggle_button"),
                        shape = CircleShape,
                        subtle = true,
                        elevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Translate,
                                contentDescription = SafarLocalization.text(language, L10nKey.CD_SWITCH_LANGUAGE),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = language.shortLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    GlassSurface(
                        onClick = onLoginMenuClick,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(44.dp)
                            .testTag("topbar_login_menu_button"),
                        shape = CircleShape,
                        subtle = !isAdminUnlocked,
                        elevation = 2.dp,
                        containerColor = if (isAdminUnlocked) {
                            if (isDark) SuccessEmeraldDarkBg else SuccessEmeraldBg
                        } else {
                            null
                        },
                        border = if (isAdminUnlocked) {
                            BorderStroke(1.dp, SuccessEmerald)
                        } else {
                            null
                        }
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Key,
                                contentDescription = SafarLocalization.text(language, L10nKey.CD_LOGIN_MENU),
                                tint = if (isAdminUnlocked) SuccessEmerald else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    GlassSurface(
                        onClick = onNotificationsClick,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(44.dp)
                            .testTag("notifications_button"),
                        shape = CircleShape,
                        subtle = true,
                        elevation = 2.dp
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = SafarLocalization.text(language, L10nKey.CD_NOTIFICATIONS),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            if (hasUnreadNotification) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 8.dp, end = 8.dp)
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444))
                                )
                            }
                        }
                    }

                    GlassSurface(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(44.dp)
                            .testTag("topbar_profile_button"),
                        shape = CircleShape,
                        elevation = 3.dp,
                        containerColor = IndigoPrimary,
                        border = BorderStroke(
                            1.dp,
                            Brush.linearGradient(
                                listOf(Color.White.copy(alpha = 0.45f), SkyBlueAccent.copy(alpha = 0.35f))
                            )
                        )
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = SafarLocalization.text(language, L10nKey.CD_PROFILE),
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}

@Composable
fun StatusBadgePill(
    statusTag: BookingStatusTag,
    strings: SafarStrings,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val (bg, fg, label) = when (statusTag) {
        BookingStatusTag.CONFIRMED -> Triple(
            if (isDark) SuccessEmeraldDarkBg else SuccessEmeraldBg,
            if (isDark) SuccessEmeraldDarkText else SuccessEmeraldText,
            strings.confirmedLabel
        )
        BookingStatusTag.PRIORITY -> Triple(
            if (isDark) PrioritySkyDarkBg else PrioritySkyBg,
            if (isDark) PrioritySkyDarkText else PrioritySkyText,
            strings.priorityLabel
        )
        BookingStatusTag.CHECKED_IN -> Triple(
            if (isDark) PrioritySkyDarkBg else PrioritySkyBg,
            if (isDark) PrioritySkyDarkText else PrioritySkyText,
            strings.checkedInLabel
        )
        BookingStatusTag.DELAYED -> Triple(
            if (isDark) WarningAmberDarkBg else WarningAmberBg,
            if (isDark) WarningAmberDarkText else WarningAmberText,
            strings.delayedLabel
        )
        BookingStatusTag.CANCELLED -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            strings.cancelledLabel
        )
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bg)
            .border(1.dp, fg.copy(alpha = 0.25f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = fg,
            maxLines = 1
        )
    }
}

@Composable
fun CurrencyAmountText(
    priceUzs: Long,
    currency: CurrencyCode,
    amountColor: Color = MaterialTheme.colorScheme.onSurface,
    unitColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    fontSizeSp: Int = 17
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = CurrencyFormatter.formatAmountOnly(priceUzs, currency),
            style = TabularPriceStyle.copy(fontSize = fontSizeSp.sp),
            color = amountColor,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = CurrencyFormatter.formatUnit(currency),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            color = unitColor,
            modifier = Modifier.padding(bottom = 1.dp)
        )
    }
}

@Composable
fun TravelModeIcon(
    mode: TravelMode,
    tint: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    language: AppLanguage = AppLanguage.UZ
) {
    val icon = when (mode) {
        TravelMode.FLIGHTS -> Icons.Filled.Flight
        TravelMode.RAIL -> Icons.Filled.Train
        TravelMode.HOTELS -> Icons.Filled.Hotel
        TravelMode.TOURS -> Icons.Filled.Explore
        TravelMode.ESIM -> Icons.Filled.SimCard
        TravelMode.TRANSFERS -> Icons.Filled.DirectionsCar
    }
    Icon(
        imageVector = icon,
        contentDescription = contentDescription ?: SafarLocalization.text(language, L10nKey.CD_TRAVEL_MODE),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun TravelItineraryCard(
    option: TravelOption,
    currency: CurrencyCode,
    strings: SafarStrings,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    language: AppLanguage = AppLanguage.UZ
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("itinerary_card_${option.id}"),
        shape = RoundedCornerShape(GlassTokens.CardRadius),
        elevation = GlassTokens.DefaultElevation,
        onClick = onSelect
    ) {
        Column(modifier = Modifier.padding(GlassTokens.CardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(glassFillColor(isDark = isDark, subtle = true))
                            .border(1.dp, glassBorderBrush(isDark = isDark), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        TravelModeIcon(
                            mode = option.mode,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp),
                            language = language
                        )
                    }
                    Column {
                        Text(
                            text = option.carrier,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${option.carrierCode} · ${option.platformOrGate}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StatusBadgePill(statusTag = option.statusTag, strings = strings)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(glassFillColor(isDark = isDark, subtle = true))
                            .border(1.dp, glassBorderBrush(isDark = isDark), CircleShape)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = option.fareClass,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = option.departureTime,
                        style = TabularTimeStyle,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = option.originCode,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(glassFillColor(isDark = isDark, subtle = true))
                            .border(1.dp, glassBorderBrush(isDark = isDark), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = option.durationText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth().height(4.dp)) {
                            val y = size.height / 2f
                            drawLine(
                                color = SkyBlueAccent,
                                start = Offset(6f, y),
                                end = Offset(size.width - 6f, y),
                                strokeWidth = 4f,
                                pathEffect = if (option.isDirect) null else PathEffect.dashPathEffect(
                                    floatArrayOf(10f, 8f), 0f
                                )
                            )
                            drawCircle(
                                color = IndigoPrimary,
                                radius = 7f,
                                center = Offset(6f, y)
                            )
                            drawCircle(
                                color = SkyBlueAccent,
                                radius = 7f,
                                center = Offset(size.width - 6f, y)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(glassFillColor(isDark = isDark, subtle = true))
                                .padding(horizontal = 4.dp)
                        ) {
                            TravelModeIcon(
                                mode = option.mode,
                                tint = SkyBlueAccent,
                                modifier = Modifier.size(16.dp),
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        text = option.stopsText,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (option.isDirect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = option.arrivalTime,
                        style = TabularTimeStyle,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = option.destinationCode,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = option.baggageInfo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    CurrencyAmountText(
                        priceUzs = option.priceUzs,
                        currency = currency,
                        amountColor = MaterialTheme.colorScheme.onSurface,
                        unitColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSizeSp = 18
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onSelect,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .heightIn(min = 48.dp)
                        .testTag("select_option_${option.id}"),
                    shape = RoundedCornerShape(GlassTokens.ControlRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IndigoPrimary,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(
                        1.dp,
                        Brush.linearGradient(
                            listOf(Color.White.copy(alpha = 0.35f), SkyBlueAccent.copy(alpha = 0.25f))
                        )
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = strings.selectCta,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentSelectorTile(
    rail: PaymentRail,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    language: AppLanguage = AppLanguage.UZ
) {
    val badgeColor = when (rail.id) {
        "uzcard" -> UzcardBlue
        "humo" -> HumoGold
        "click" -> ClickCyan
        "payme" -> PaymeTeal
        "uzum" -> UzumPurple
        else -> VisaNavy
    }

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 66.dp)
            .testTag("payment_tile_${rail.id}"),
        shape = RoundedCornerShape(GlassTokens.ControlRadius),
        isSelected = selected,
        elevation = if (selected) GlassTokens.SelectedElevation else 2.dp,
        onClick = onSelect
    ) {
        Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(64.dp)
                        .height(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = rail.badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = rail.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = rail.localizedSubtitle(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(IndigoPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = SafarLocalization.text(language, L10nKey.CD_SELECTED),
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun BoardingPassQrCanvas(
    pnrCode: String,
    modifier: Modifier = Modifier
) {
    val moduleColor = Color(0xFF0F172A)
    val accentColor = IndigoPrimary
    Canvas(modifier = modifier) {
        val gridSize = 15
        val cellW = size.width / gridSize
        val cellH = size.height / gridSize
        val seedBytes = pnrCode.ifBlank { "SAFARGO" }.encodeToByteArray()

        fun isFinderCell(r: Int, c: Int): Boolean {
            val inTopLeft = r < 5 && c < 5
            val inTopRight = r < 5 && c >= gridSize - 5
            val inBottomLeft = r >= gridSize - 5 && c < 5
            return inTopLeft || inTopRight || inBottomLeft
        }

        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val drawCell: Boolean
                val color: Color
                if (isFinderCell(r, c)) {
                    val lr = if (r >= gridSize - 5) r - (gridSize - 5) else r
                    val lc = if (c >= gridSize - 5) c - (gridSize - 5) else c
                    drawCell = lr == 0 || lr == 4 || lc == 0 || lc == 4 || (lr == 2 && lc == 2)
                    color = accentColor
                } else {
                    val hash = abs((seedBytes[(r + c) % seedBytes.size].toInt() * 31 + r * 17 + c * 13))
                    drawCell = (hash % 5) < 2
                    color = moduleColor
                }
                if (drawCell) {
                    drawRect(
                        color = color,
                        topLeft = Offset(c * cellW + 1f, r * cellH + 1f),
                        size = Size(cellW - 2f, cellH - 2f)
                    )
                }
            }
        }
    }
}

@Composable
fun PrimaryVioletButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    GlassButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    )
}
