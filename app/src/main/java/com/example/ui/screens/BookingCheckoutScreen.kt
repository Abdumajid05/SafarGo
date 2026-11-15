package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AirlineSeatReclineExtra
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.model.CurrencyCode
import com.example.data.model.CurrencyFormatter
import com.example.data.model.L10nKey
import com.example.data.model.SafarCatalog
import com.example.data.model.SafarLocalization
import com.example.data.model.SafarStrings
import com.example.data.model.SeatItem
import com.example.data.model.TravelMode
import com.example.ui.components.CurrencyAmountText
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassInput
import com.example.ui.components.GlassNoticeBanner
import com.example.ui.components.GlassNoticeTone
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.PaymentSelectorTile
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.components.glassBorderBrush
import com.example.ui.components.glassFillColor
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.CheckoutUiState

@Composable
fun BookingCheckoutScreen(
    checkoutState: CheckoutUiState,
    travelDate: String,
    language: AppLanguage,
    currency: CurrencyCode,
    strings: SafarStrings,
    clubPoints: Long = 0L,
    onBack: () -> Unit,
    onSelectStep: (Int) -> Unit,
    onSelectSeat: (SeatItem) -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdatePassport: (String) -> Unit,
    onUpdatePhone: (String) -> Unit,
    onFillSavedTraveler: () -> Unit,
    onSelectPayment: (String) -> Unit,
    onToggleClubPoints: () -> Unit = {},
    onConfirmBooking: () -> Unit,
    modifier: Modifier = Modifier
) {
    val option = checkoutState.option
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    var localStepError by remember { mutableStateOf<String?>(null) }

    val handleBackNavigation: () -> Unit = {
        val minStep = if (option?.hasSeatMap == true) 0 else 1
        if (option != null && checkoutState.step > minStep && !checkoutState.isSubmitting) {
            localStepError = null
            onSelectStep(checkoutState.step - 1)
        } else if (!checkoutState.isSubmitting) {
            onBack()
        }
    }

    BackHandler(onBack = handleBackNavigation)

    if (option == null) {
        SafarGoGlassBackdrop(modifier = modifier) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                GlassCard(
                    modifier = Modifier.padding(24.dp),
                    shape = RoundedCornerShape(GlassTokens.CardRadius)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = SafarLocalization.text(language, L10nKey.CHECKOUT_NO_ROUTE),
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
        return
    }

    val seatExtra = checkoutState.selectedSeat?.extraPriceUzs ?: 0L
    val clubDiscount = if (checkoutState.applyClubPoints && clubPoints >= 5_000L) 50_000L else 0L
    val totalUzs = (option.priceUzs + seatExtra - clubDiscount).coerceAtLeast(0L)

    SafarGoGlassBackdrop(modifier = modifier) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
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
                                onClick = handleBackNavigation,
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("checkout_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = SafarLocalization.text(language, L10nKey.CD_BACK)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${option.carrier} · ${option.carrierCode}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${option.originCode} → ${option.destinationCode} · $travelDate · ${option.departureTime}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val steps = buildList {
                                if (option.hasSeatMap) add(0 to strings.seatMapStep)
                                add(1 to strings.passengerStep)
                                add(2 to strings.paymentStep)
                            }
                            steps.forEach { (stepIdx, label) ->
                                val active = checkoutState.step == stepIdx
                                val completed = checkoutState.step > stepIdx
                                GlassSurface(
                                    onClick = {
                                        if (stepIdx <= checkoutState.step || (stepIdx == 2 && checkoutState.passengerName.isNotBlank() && checkoutState.passportSeries.isNotBlank())) {
                                            localStepError = null
                                            onSelectStep(stepIdx)
                                        } else {
                                            localStepError = SafarLocalization.text(
                                                language,
                                                L10nKey.CHECKOUT_FILL_PASSENGER_FIRST
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .minimumInteractiveComponentSize()
                                        .heightIn(min = 40.dp)
                                        .testTag("checkout_step_$stepIdx"),
                                    shape = CircleShape,
                                    isSelected = active || completed,
                                    subtle = !active && !completed,
                                    elevation = if (active) 3.dp else 0.dp,
                                    containerColor = if (active) IndigoPrimary else null,
                                    border = if (active) {
                                        BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.65f))
                                    } else {
                                        null
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = when {
                                                active -> Color.White
                                                completed -> MaterialTheme.colorScheme.primary
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                                            },
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            },
            bottomBar = {
                GlassSurface(
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    elevation = GlassTokens.NavElevation
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.CHECKOUT_TOTAL_PAYABLE),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                CurrencyAmountText(
                                    priceUzs = totalUzs,
                                    currency = currency,
                                    fontSizeSp = 20
                                )
                            }

                            if (checkoutState.step < 2) {
                                Button(
                                    onClick = {
                                        if (checkoutState.step == 1 && (checkoutState.passengerName.trim().length < 2 || checkoutState.passportSeries.trim().length < 5)) {
                                            localStepError = SafarLocalization.text(
                                                language,
                                                L10nKey.CHECKOUT_ENTER_PASSENGER
                                            )
                                        } else {
                                            localStepError = null
                                            onSelectStep(checkoutState.step + 1)
                                        }
                                    },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .heightIn(min = 48.dp)
                                        .testTag("checkout_next_step_button"),
                                    shape = RoundedCornerShape(GlassTokens.ControlRadius),
                                    border = BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.4f)),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                                ) {
                                    Text(
                                        text = if (checkoutState.step == 0) {
                                            SafarLocalization.text(language, L10nKey.CHECKOUT_CONFIRM_SEAT)
                                        } else {
                                            SafarLocalization.text(language, L10nKey.CHECKOUT_TO_PAYMENT)
                                        },
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }

                        if (checkoutState.step == 2) {
                            Spacer(modifier = Modifier.height(10.dp))
                            GlassButton(
                                text = if (checkoutState.isSubmitting) {
                                    SafarLocalization.text(language, L10nKey.CHECKOUT_ISSUING_TICKET)
                                } else {
                                    strings.payAndIssueCta
                                },
                                isLoading = checkoutState.isSubmitting,
                                enabled = !checkoutState.isSubmitting,
                                onClick = onConfirmBooking,
                                modifier = Modifier.testTag("pay_and_issue_button")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
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
                    if (checkoutState.step == 0 && option.hasSeatMap) {
                        item {
                            SeatMapSection(
                                language = language,
                                mode = option.mode,
                                seats = checkoutState.seatMap,
                                selectedSeat = checkoutState.selectedSeat,
                                currency = currency,
                                onSelectSeat = onSelectSeat
                            )
                        }
                    }

                    if (checkoutState.step == 1) {
                        item {
                            PassengerManifestSection(
                                language = language,
                                name = checkoutState.passengerName,
                                passport = checkoutState.passportSeries,
                                phone = checkoutState.phone,
                                errorMessage = localStepError ?: checkoutState.validationError,
                                onUpdateName = { localStepError = null; onUpdateName(it) },
                                onUpdatePassport = { localStepError = null; onUpdatePassport(it) },
                                onUpdatePhone = { localStepError = null; onUpdatePhone(it) },
                                onFillSaved = { localStepError = null; onFillSavedTraveler() }
                            )
                        }
                    }

                    if (checkoutState.step == 2) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.CHECKOUT_SELECT_PAYMENT),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                GlassNoticeBanner(
                                    text = strings.demoNotice,
                                    tone = GlassNoticeTone.INFO
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                SafarCatalog.paymentRails.forEach { rail ->
                                    PaymentSelectorTile(
                                        rail = rail,
                                        selected = checkoutState.selectedPaymentId == rail.id,
                                        onSelect = { onSelectPayment(rail.id) },
                                        language = language
                                    )
                                }
                            }
                        }

                        if (clubPoints >= 5_000L) {
                            item {
                                GlassCard(
                                    onClick = onToggleClubPoints,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("toggle_club_points_card"),
                                    shape = RoundedCornerShape(GlassTokens.CardRadius),
                                    isSelected = checkoutState.applyClubPoints,
                                    elevation = GlassTokens.DefaultElevation
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = SafarLocalization.text(
                                                    language,
                                                    L10nKey.CHECKOUT_CLUB_REDEEM_TITLE
                                                ),
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = SafarLocalization.text(
                                                    language,
                                                    L10nKey.CHECKOUT_CLUB_REDEEM_SUB,
                                                    CurrencyFormatter.formatFull(50_000L, currency),
                                                    clubPoints
                                                ),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Text(
                                            text = if (checkoutState.applyClubPoints) {
                                                SafarLocalization.text(language, L10nKey.CHECKOUT_CLUB_APPLIED)
                                            } else {
                                                strings.spendPointsCta
                                            },
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        if (checkoutState.validationError != null) {
                            item {
                                GlassNoticeBanner(
                                    text = checkoutState.validationError,
                                    tone = GlassNoticeTone.ERROR
                                )
                            }
                        }

                        item {
                            GlassCard(
                                shape = RoundedCornerShape(GlassTokens.CardRadius),
                                elevation = GlassTokens.DefaultElevation
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = SafarLocalization.text(language, L10nKey.CHECKOUT_MANIFEST_SUMMARY),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    SummaryRow(
                                        SafarLocalization.text(language, L10nKey.CHECKOUT_ROW_PASSENGER),
                                        checkoutState.passengerName
                                    )
                                    SummaryRow(
                                        SafarLocalization.text(language, L10nKey.CHECKOUT_ROW_PASSPORT),
                                        checkoutState.passportSeries
                                    )
                                    SummaryRow(
                                        SafarLocalization.text(language, L10nKey.CHECKOUT_ROW_SEAT),
                                        checkoutState.selectedSeat?.code
                                            ?: SafarLocalization.text(language, L10nKey.CHECKOUT_SUITE_PRIORITY)
                                    )
                                    SummaryRow(
                                        SafarLocalization.text(language, L10nKey.CHECKOUT_ROW_BASE_FARE),
                                        CurrencyFormatter.formatFull(option.priceUzs, currency)
                                    )
                                    if (seatExtra > 0) {
                                        SummaryRow(
                                            SafarLocalization.text(language, L10nKey.CHECKOUT_ROW_VIP_SEAT),
                                            CurrencyFormatter.formatFull(seatExtra, currency)
                                        )
                                    }
                                    if (clubDiscount > 0) {
                                        SummaryRow(
                                            "SafarGo Club (-5 000)",
                                            "-${CurrencyFormatter.formatFull(clubDiscount, currency)}"
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
}

@Composable
private fun SeatMapSection(
    language: AppLanguage,
    mode: TravelMode,
    seats: List<SeatItem>,
    selectedSeat: SeatItem?,
    currency: CurrencyCode,
    onSelectSeat: (SeatItem) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    GlassCard(
        shape = RoundedCornerShape(GlassTokens.CardRadius),
        elevation = GlassTokens.DefaultElevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AirlineSeatReclineExtra,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = if (mode == TravelMode.RAIL) {
                            SafarLocalization.text(language, L10nKey.CHECKOUT_SEATMAP_RAIL)
                        } else {
                            SafarLocalization.text(language, L10nKey.CHECKOUT_SEATMAP_CABIN)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = SafarLocalization.text(
                            language,
                            L10nKey.CHECKOUT_SEATMAP_VIP_HINT,
                            CurrencyFormatter.formatFull(65_000L, currency)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SeatLegendDot(
                    SafarLocalization.text(language, L10nKey.CHECKOUT_LEGEND_FREE),
                    glassFillColor(isDark = isDark, subtle = true),
                    MaterialTheme.colorScheme.outlineVariant
                )
                SeatLegendDot(
                    SafarLocalization.text(language, L10nKey.CHECKOUT_LEGEND_SELECTED),
                    IndigoPrimary,
                    IndigoPrimary
                )
                SeatLegendDot("VIP", WarningAmber.copy(alpha = 0.25f), WarningAmber)
                SeatLegendDot(
                    SafarLocalization.text(language, L10nKey.CHECKOUT_LEGEND_TAKEN),
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.outlineVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val rows = seats.groupBy { it.row }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                rows.toSortedMap().forEach { (rowNum, rowSeats) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val left = rowSeats.filter { it.col == "A" || it.col == "B" }
                        val right = rowSeats.filter { it.col == "C" || it.col == "D" }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            left.forEach { seat ->
                                SeatTile(
                                    seat = seat,
                                    isSelected = selectedSeat?.code == seat.code,
                                    onSelect = { onSelectSeat(seat) }
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.width(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$rowNum",
                                style = TabularCodeStyle,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            right.forEach { seat ->
                                SeatTile(
                                    seat = seat,
                                    isSelected = selectedSeat?.code == seat.code,
                                    onSelect = { onSelectSeat(seat) }
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
private fun SeatLegendDot(label: String, fill: Color, border: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Surface(
            modifier = Modifier.size(14.dp),
            shape = RoundedCornerShape(4.dp),
            color = fill,
            border = BorderStroke(1.dp, border)
        ) {}
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SeatTile(
    seat: SeatItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val bg = when {
        seat.isOccupied -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)
        isSelected -> IndigoPrimary
        seat.isVipOrExtraLegroom -> WarningAmber.copy(alpha = 0.22f)
        else -> glassFillColor(isDark = isDark, subtle = true)
    }
    val borderStroke = when {
        seat.isOccupied -> BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        isSelected -> BorderStroke(2.dp, SkyBlueAccent)
        seat.isVipOrExtraLegroom -> BorderStroke(1.dp, WarningAmber)
        else -> BorderStroke(1.dp, glassBorderBrush(isDark = isDark))
    }
    val textColor = when {
        seat.isOccupied -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
        isSelected -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .size(width = 56.dp, height = 48.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(enabled = !seat.isOccupied, onClick = onSelect)
            .testTag("seat_${seat.code}"),
        shape = RoundedCornerShape(10.dp),
        color = bg,
        border = borderStroke
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = seat.code,
                style = TabularCodeStyle,
                color = textColor
            )
        }
    }
}

@Composable
private fun PassengerManifestSection(
    language: AppLanguage,
    name: String,
    passport: String,
    phone: String,
    errorMessage: String?,
    onUpdateName: (String) -> Unit,
    onUpdatePassport: (String) -> Unit,
    onUpdatePhone: (String) -> Unit,
    onFillSaved: () -> Unit
) {
    GlassCard(
        shape = RoundedCornerShape(GlassTokens.CardRadius),
        elevation = GlassTokens.DefaultElevation
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        imageVector = Icons.Filled.Badge,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = SafarLocalization.text(language, L10nKey.CHECKOUT_PASSENGER_DETAILS),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                TextButton(
                    onClick = onFillSaved,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("autofill_passenger_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(SafarLocalization.text(language, L10nKey.CHECKOUT_USE_PROFILE))
                }
            }

            GlassInput(
                value = name,
                onValueChange = onUpdateName,
                label = SafarLocalization.text(language, L10nKey.CHECKOUT_FULLNAME_UPPER_LABEL),
                placeholder = SafarLocalization.text(language, L10nKey.CHECKOUT_FULLNAME_UPPER_HINT),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.testTag("passenger_name_input")
            )

            GlassInput(
                value = passport,
                onValueChange = onUpdatePassport,
                label = SafarLocalization.text(language, L10nKey.CHECKOUT_PASSPORT_UPPER_LABEL),
                placeholder = SafarLocalization.text(language, L10nKey.REG_PASSPORT_PLACEHOLDER),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next
                ),
                textStyle = TabularCodeStyle,
                modifier = Modifier.testTag("passenger_passport_input")
            )

            GlassInput(
                value = phone,
                onValueChange = onUpdatePhone,
                label = SafarLocalization.text(language, L10nKey.CHECKOUT_PHONE_UPPER_LABEL),
                placeholder = SafarLocalization.text(language, L10nKey.AUTH_PHONE_PLACEHOLDER),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                textStyle = TabularCodeStyle,
                modifier = Modifier.testTag("passenger_phone_input")
            )

            if (errorMessage != null) {
                GlassNoticeBanner(
                    text = errorMessage,
                    tone = GlassNoticeTone.ERROR
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = TabularCodeStyle,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
