package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.model.BookingStatusTag
import com.example.data.model.CurrencyCode
import com.example.data.model.L10nKey
import com.example.data.model.SafarLocalization
import com.example.data.model.SafarStrings
import com.example.ui.components.BoardingPassQrCanvas
import com.example.ui.components.CurrencyAmountText
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassIndigoHeroBox
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.components.StatusBadgePill
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.theme.TabularTimeStyle
import com.example.ui.viewmodel.SingleBookingUiState

@Composable
fun TicketDetailScreen(
    bookingState: SingleBookingUiState,
    isCheckingIn: Boolean = false,
    isCancelling: Boolean = false,
    language: AppLanguage,
    currency: CurrencyCode,
    strings: SafarStrings,
    onBack: () -> Unit,
    onCheckInBooking: (String) -> Unit,
    onCancelBooking: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var confirmCancelDialog by remember { mutableStateOf(false) }
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val glassDialogColor = if (isDark) Color(0xF010182E) else Color(0xF2EEF3FF)

    SafarGoGlassBackdrop(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            GlassSurface(
                shape = RoundedCornerShape(0.dp),
                elevation = GlassTokens.NavElevation
            ) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("ticket_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = SafarLocalization.text(language, L10nKey.CD_BACK)
                            )
                        }
                        Text(
                            text = SafarLocalization.text(language, L10nKey.TICKET_TITLE),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                }
            }

            when (bookingState) {
                SingleBookingUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(color = IndigoPrimary)
                            Text(
                                text = SafarLocalization.text(language, L10nKey.TICKET_LOADING),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                SingleBookingUiState.NotFound -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassCard(
                            shape = RoundedCornerShape(GlassTokens.CardRadius),
                            elevation = GlassTokens.DefaultElevation
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.TICKET_NOT_FOUND),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                GlassButton(
                                    text = strings.backLabel,
                                    onClick = onBack
                                )
                            }
                        }
                    }
                }

                is SingleBookingUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassCard(
                            shape = RoundedCornerShape(GlassTokens.CardRadius),
                            elevation = GlassTokens.DefaultElevation
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = bookingState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                GlassButton(
                                    text = strings.backLabel,
                                    onClick = onBack
                                )
                            }
                        }
                    }
                }

                is SingleBookingUiState.Ready -> {
                    val booking = bookingState.booking
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .widthIn(max = 640.dp),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                GlassCard(
                                    shape = RoundedCornerShape(GlassTokens.CardRadius),
                                    elevation = GlassTokens.DefaultElevation
                                ) {
                                    Column {
                                        GlassIndigoHeroBox(
                                            shape = RoundedCornerShape(
                                                topStart = GlassTokens.CardRadius,
                                                topEnd = GlassTokens.CardRadius
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(18.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = booking.carrier,
                                                        style = MaterialTheme.typography.titleLarge,
                                                        color = Color.White
                                                    )
                                                    Text(
                                                        text = "${booking.carrierCode} · ${booking.fareClass}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Color.White.copy(alpha = 0.88f)
                                                    )
                                                }
                                                StatusBadgePill(statusTag = booking.statusTag, strings = strings)
                                            }
                                        }

                                        Column(modifier = Modifier.padding(18.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = booking.originCode,
                                                        style = MaterialTheme.typography.displayLarge,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = booking.originCity,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = booking.departureTime,
                                                        style = TabularTimeStyle,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }

                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(
                                                        text = booking.travelDate,
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    GlassSurface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        subtle = true,
                                                        elevation = 0.dp
                                                    ) {
                                                        Text(
                                                            text = booking.durationText,
                                                            style = TabularCodeStyle,
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                        )
                                                    }
                                                }

                                                Column(horizontalAlignment = Alignment.End) {
                                                    Text(
                                                        text = booking.destinationCode,
                                                        style = MaterialTheme.typography.displayLarge,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = booking.destinationCity,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = booking.arrivalTime,
                                                        style = TabularTimeStyle,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(18.dp))
                                            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                                            Spacer(modifier = Modifier.height(16.dp))

                                            ManifestPairRow(
                                                SafarLocalization.text(language, L10nKey.TICKET_LBL_PASSENGER),
                                                booking.passengerName,
                                                SafarLocalization.text(language, L10nKey.TICKET_LBL_PASSPORT),
                                                booking.passportSeries
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            ManifestPairRow(
                                                SafarLocalization.text(language, L10nKey.TICKET_LBL_PNR),
                                                booking.pnrCode,
                                                SafarLocalization.text(language, L10nKey.TICKET_LBL_SEAT),
                                                booking.seatNumber
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            ManifestPairRow(
                                                SafarLocalization.text(language, L10nKey.TICKET_LBL_GATE),
                                                booking.platformOrGate,
                                                SafarLocalization.text(language, L10nKey.TICKET_LBL_NUMBER),
                                                booking.ticketNumber
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            ManifestPairRow(
                                                SafarLocalization.text(language, L10nKey.TICKET_LBL_BAGGAGE),
                                                booking.baggageInfo,
                                                SafarLocalization.text(language, L10nKey.TICKET_LBL_PAYMENT),
                                                booking.paymentMethod
                                            )

                                            Spacer(modifier = Modifier.height(18.dp))
                                            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                                            Spacer(modifier = Modifier.height(16.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Surface(
                                                    shape = RoundedCornerShape(14.dp),
                                                    color = Color.White,
                                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                                ) {
                                                    BoardingPassQrCanvas(
                                                        pnrCode = booking.pnrCode,
                                                        modifier = Modifier
                                                            .padding(10.dp)
                                                            .size(104.dp)
                                                    )
                                                }

                                                Column(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(start = 12.dp),
                                                    horizontalAlignment = Alignment.End,
                                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = SafarLocalization.text(
                                                            language,
                                                            L10nKey.TICKET_LBL_TOTAL_PAID
                                                        ),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    CurrencyAmountText(
                                                        priceUzs = booking.totalUzs,
                                                        currency = currency,
                                                        fontSizeSp = 20
                                                    )
                                                    Text(
                                                        text = SafarLocalization.text(
                                                            language,
                                                            L10nKey.TICKET_SCAN_HINT
                                                        ),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Medium,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        textAlign = TextAlign.End
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (!booking.isCompleted && booking.statusTag != BookingStatusTag.CANCELLED) {
                                if (booking.statusTag != BookingStatusTag.CHECKED_IN) {
                                    item {
                                        GlassButton(
                                            text = if (isCheckingIn) strings.loadingLabel else strings.onlineCheckIn,
                                            isLoading = isCheckingIn,
                                            enabled = !isCheckingIn && !isCancelling,
                                            onClick = { onCheckInBooking(booking.id) },
                                            modifier = Modifier.testTag("ticket_checkin_button")
                                        )
                                    }
                                }
                                item {
                                    GlassSecondaryButton(
                                        onClick = { confirmCancelDialog = true },
                                        enabled = !isCheckingIn && !isCancelling,
                                        contentColor = MaterialTheme.colorScheme.error,
                                        borderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.65f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("cancel_ticket_button")
                                    ) {
                                        if (isCancelling) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(18.dp),
                                                strokeWidth = 2.dp,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }
                                        Text(
                                            text = SafarLocalization.text(language, L10nKey.TICKET_CANCEL_CTA)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (confirmCancelDialog) {
                        AlertDialog(
                            onDismissRequest = { confirmCancelDialog = false },
                            containerColor = glassDialogColor,
                            title = {
                                Text(
                                    text = SafarLocalization.text(
                                        language,
                                        L10nKey.TICKET_CANCEL_DIALOG_TITLE
                                    )
                                )
                            },
                            text = {
                                Text(
                                    text = SafarLocalization.text(
                                        language,
                                        L10nKey.TICKET_CANCEL_DIALOG_BODY,
                                        booking.pnrCode
                                    )
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        confirmCancelDialog = false
                                        onCancelBooking(booking.id)
                                    }
                                ) {
                                    Text(
                                        text = SafarLocalization.text(
                                            language,
                                            L10nKey.TICKET_CANCEL_CONFIRM
                                        ),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { confirmCancelDialog = false }) {
                                    Text(
                                        text = SafarLocalization.text(
                                            language,
                                            L10nKey.TICKET_CANCEL_KEEP
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ManifestPairRow(
    leftLabel: String,
    leftValue: String,
    rightLabel: String,
    rightValue: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = leftLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = leftValue,
                style = TabularCodeStyle,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = rightLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = rightValue,
                style = TabularCodeStyle,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
