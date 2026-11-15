package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.model.BookingDoc
import com.example.data.model.CurrencyCode
import com.example.data.model.L10nKey
import com.example.data.model.SafarLocalization
import com.example.data.model.SafarStrings
import com.example.ui.components.CurrencyAmountText
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.StatusBadgePill
import com.example.ui.components.TravelModeIcon
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.theme.TabularTimeStyle
import com.example.ui.viewmodel.BookingsUiState

private enum class TripTab { UPCOMING, COMPLETED, ALL }

@Composable
fun TripsScreen(
    bookingsState: BookingsUiState,
    language: AppLanguage,
    currency: CurrencyCode,
    strings: SafarStrings,
    onOpenTicket: (String) -> Unit,
    onExploreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(TripTab.UPCOMING) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        when (bookingsState) {
            BookingsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(color = IndigoPrimary)
                        Text(
                            text = strings.loadingLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is BookingsUiState.Error -> {
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
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ErrorOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(34.dp)
                            )
                            Text(
                                text = SafarLocalization.text(language, L10nKey.TRIPS_ERROR_TITLE),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = bookingsState.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            is BookingsUiState.Success -> {
                val bookings = bookingsState.items
                val filtered = remember(bookings, activeTab) {
                    when (activeTab) {
                        TripTab.UPCOMING -> bookings.filter { !it.isCompleted }
                        TripTab.COMPLETED -> bookings.filter { it.isCompleted }
                        TripTab.ALL -> bookings
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 720.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val tabs = listOf(
                            TripTab.UPCOMING to strings.upcomingTab,
                            TripTab.COMPLETED to strings.completedTab,
                            TripTab.ALL to strings.allTab
                        )
                        tabs.forEach { (tab, label) ->
                            val selected = activeTab == tab
                            GlassSurface(
                                onClick = { activeTab = tab },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .heightIn(min = 40.dp)
                                    .testTag("trips_tab_${tab.name.lowercase()}"),
                                shape = CircleShape,
                                isSelected = selected,
                                subtle = !selected,
                                elevation = if (selected) 3.dp else 0.dp,
                                containerColor = if (selected) IndigoPrimary else null,
                                border = if (selected) {
                                    BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.65f))
                                } else {
                                    null
                                }
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    if (filtered.isEmpty()) {
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
                                    modifier = Modifier.padding(28.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(CircleShape)
                                            .background(IndigoPrimary.copy(alpha = 0.14f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ConfirmationNumber,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = strings.emptyTripsTitle,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = strings.emptyTripsBody,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    GlassButton(
                                        text = strings.searchRoutesCta,
                                        onClick = onExploreClick,
                                        modifier = Modifier.testTag("empty_trips_search_cta")
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filtered, key = { it.id }) { booking ->
                                TripCard(
                                    booking = booking,
                                    language = language,
                                    currency = currency,
                                    strings = strings,
                                    onClick = { onOpenTicket(booking.id) }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TripCard(
    booking: BookingDoc,
    language: AppLanguage,
    currency: CurrencyCode,
    strings: SafarStrings,
    onClick: () -> Unit
) {
    GlassCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("trip_card_${booking.pnrCode}"),
        shape = RoundedCornerShape(GlassTokens.CardRadius),
        elevation = GlassTokens.DefaultElevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    GlassSurface(
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(10.dp),
                        subtle = true,
                        elevation = 0.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TravelModeIcon(
                                mode = booking.mode,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp),
                                language = language
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "${booking.carrier} · ${booking.carrierCode}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "PNR ${booking.pnrCode} · ${booking.travelDate}",
                            style = TabularCodeStyle.copy(
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = FontWeight.Normal
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                StatusBadgePill(statusTag = booking.statusTag, strings = strings)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.departureTime,
                        style = TabularTimeStyle,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${booking.originCode} · ${booking.originCity}",
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
                        text = booking.durationText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = booking.arrivalTime,
                        style = TabularTimeStyle,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${booking.destinationCode} · ${booking.destinationCity}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.QrCode2,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "${booking.seatNumber} · ${booking.platformOrGate}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                CurrencyAmountText(
                    priceUzs = booking.totalUzs,
                    currency = currency,
                    fontSizeSp = 15
                )
            }
        }
    }
}
