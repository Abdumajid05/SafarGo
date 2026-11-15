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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.model.CurrencyCode
import com.example.data.model.L10nKey
import com.example.data.model.SafarLocalization
import com.example.data.model.SafarStrings
import com.example.data.model.TravelMode
import com.example.data.model.TravelOption
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassNoticeBanner
import com.example.ui.components.GlassNoticeTone
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.components.TravelItineraryCard
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.viewmodel.ResultFilter
import com.example.ui.viewmodel.SearchUiState

@Composable
fun ResultsScreen(
    searchState: SearchUiState,
    language: AppLanguage,
    currency: CurrencyCode,
    strings: SafarStrings,
    onBack: () -> Unit,
    onToggleCurrency: () -> Unit,
    onSelectFilter: (ResultFilter) -> Unit,
    onSelectOption: (TravelOption) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val results = searchState.filteredResults
    val offersSuffix = SafarLocalization.text(language, L10nKey.RESULTS_OFFERS_COUNT, results.size)

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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("results_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = SafarLocalization.text(language, L10nKey.CD_BACK),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Column {
                                val headerRoute = when (searchState.mode) {
                                    TravelMode.HOTELS -> "${searchState.destinationCode} · ${strings.hotels}"
                                    TravelMode.ESIM -> "${searchState.destinationCode} · ${strings.esim}"
                                    else -> "${searchState.originCode} → ${searchState.destinationCode}"
                                }
                                Text(
                                    text = headerRoute,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${searchState.departureDate} · ${searchState.passengerCount} ${strings.travelerUnit} · $offersSuffix",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        GlassSurface(
                            onClick = onToggleCurrency,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .minimumInteractiveComponentSize()
                                .testTag("results_currency_button"),
                            shape = RoundedCornerShape(10.dp),
                            subtle = true,
                            elevation = 0.dp
                        ) {
                            Text(
                                text = currency.name,
                                style = TabularCodeStyle,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            )
                        }
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf(
                            ResultFilter.ALL to SafarLocalization.text(language, L10nKey.RESULTS_FILTER_ALL),
                            ResultFilter.DIRECT to strings.directLabel,
                            ResultFilter.MORNING to SafarLocalization.text(language, L10nKey.RESULTS_FILTER_MORNING),
                            ResultFilter.BEST_PRICE to SafarLocalization.text(language, L10nKey.RESULTS_FILTER_BEST_PRICE),
                            ResultFilter.VIP to SafarLocalization.text(language, L10nKey.RESULTS_FILTER_VIP)
                        )
                        items(filters) { (filter, label) ->
                            val selected = searchState.activeFilter == filter
                            GlassSurface(
                                onClick = {
                                    if (selected && filter != ResultFilter.ALL) {
                                        onSelectFilter(ResultFilter.ALL)
                                    } else {
                                        onSelectFilter(filter)
                                    }
                                },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .heightIn(min = 40.dp)
                                    .testTag("filter_chip_${filter.name.lowercase()}"),
                                shape = CircleShape,
                                isSelected = selected,
                                subtle = !selected,
                                elevation = if (selected) 3.dp else 0.dp,
                                containerColor = if (selected) IndigoPrimary else null,
                                border = if (selected) BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.65f)) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (selected && filter != ResultFilter.ALL) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = SafarLocalization.text(
                                                language,
                                                L10nKey.RESULTS_CLEAR_FILTER_CD
                                            ),
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                if (results.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(24.dp)
                            .widthIn(max = 420.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassCard(
                            shape = RoundedCornerShape(GlassTokens.CardRadius),
                            elevation = GlassTokens.DefaultElevation
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.RESULTS_EMPTY_TITLE),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.RESULTS_EMPTY_BODY),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                GlassButton(
                                    text = SafarLocalization.text(language, L10nKey.RESULTS_RESET_FILTERS),
                                    onClick = { onSelectFilter(ResultFilter.ALL) },
                                    modifier = Modifier.testTag("reset_filters_button")
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = 720.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            GlassNoticeBanner(
                                text = strings.demoNotice,
                                tone = GlassNoticeTone.INFO
                            )
                        }
                        items(results, key = { it.id }) { option ->
                            TravelItineraryCard(
                                option = option,
                                currency = currency,
                                strings = strings,
                                onSelect = { onSelectOption(option) },
                                language = language
                            )
                        }
                    }
                }
            }
        }
    }
}
