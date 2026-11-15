package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.BookingDoc
import com.example.data.model.BookingStatusTag
import com.example.data.model.CityHub
import com.example.data.model.CorridorDeal
import com.example.data.model.CurrencyCode
import com.example.data.model.CurrencyFormatter
import com.example.data.model.L10nKey
import com.example.data.model.SafarCatalog
import com.example.data.model.SafarLocalization
import com.example.data.model.SafarStrings
import com.example.data.model.TravelMode
import com.example.data.model.UserProfileDoc
import com.example.data.model.stringsFor
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassIndigoHeroBox
import com.example.ui.components.GlassNoticeBanner
import com.example.ui.components.GlassNoticeTone
import com.example.ui.components.GlassSecondaryButton
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.components.StatusBadgePill
import com.example.ui.components.TravelModeIcon
import com.example.ui.components.glassBorderBrush
import com.example.ui.components.glassFillColor
import com.example.ui.theme.HumoGold
import com.example.ui.theme.IndigoDeep
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SafarGoTheme
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TabularCodeStyle
import com.example.ui.theme.TabularPriceStyle
import com.example.ui.theme.TabularTimeStyle
import com.example.ui.theme.UzcardBlue
import com.example.ui.theme.VisaNavy
import com.example.ui.viewmodel.SearchUiState
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private data class ServiceTileSpec(
    val mode: TravelMode,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    profile: UserProfileDoc,
    upcomingBooking: BookingDoc?,
    checkingInBookingId: String? = null,
    searchState: SearchUiState,
    currency: CurrencyCode,
    language: AppLanguage,
    strings: SafarStrings,
    onSelectMode: (TravelMode) -> Unit,
    onSelectOrigin: (String) -> Unit,
    onSelectDestination: (String) -> Unit,
    onSwapOriginDest: () -> Unit,
    onSelectDate: (String) -> Unit,
    onChangePassengers: (Int) -> Unit,
    onSearchRoutes: () -> Unit,
    onLaunchDeal: (CorridorDeal) -> Unit,
    onQuickCityClick: (String) -> Unit,
    onToggleFavoriteDestination: (String) -> Unit,
    onSpendClubPoints: () -> Unit,
    onCheckInUpcoming: (String) -> Unit,
    onOpenTicket: (String) -> Unit,
    onOpenExploreTab: () -> Unit,
    onOpenSupportInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSearchSheet by remember { mutableStateOf(false) }
    var pickingOrigin by remember { mutableStateOf(false) }
    var pickingDestination by remember { mutableStateOf(false) }
    var showPromoModal by remember { mutableStateOf(false) }
    var showClubModal by remember { mutableStateOf(false) }

    val originHub = SafarCatalog.hubs.find { it.code == searchState.originCode } ?: SafarCatalog.hubs.first()
    val destHub = SafarCatalog.hubs.find { it.code == searchState.destinationCode } ?: SafarCatalog.hubs[1]
    val datePresets = listOf("18 Noy, 2026", "19 Noy, 2026", "22 Noy, 2026", "01 Dek, 2026")
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val glassSheetColor = if (isDark) Color(0xF010182E) else Color(0xF2EEF3FF)

    val pointsFormatter = remember {
        DecimalFormat("#,###", DecimalFormatSymbols(Locale.US).apply { groupingSeparator = ' ' })
    }

    val services = remember(strings, isDark) {
        listOf(
            ServiceTileSpec(
                mode = TravelMode.FLIGHTS,
                title = strings.flights,
                subtitle = strings.flightsSub,
                icon = Icons.Filled.Flight,
                iconBg = if (isDark) Color(0xFF0284C7).copy(alpha = 0.24f) else Color(0xFFE0F2FE).copy(alpha = 0.85f),
                iconTint = if (isDark) SkyBlueAccent else IndigoPrimary
            ),
            ServiceTileSpec(
                mode = TravelMode.RAIL,
                title = strings.rail,
                subtitle = strings.railSub,
                icon = Icons.Filled.Train,
                iconBg = if (isDark) IndigoPrimary.copy(alpha = 0.32f) else Color(0xFFE0E7FF).copy(alpha = 0.85f),
                iconTint = if (isDark) Color(0xFFA5B4FC) else IndigoPrimary
            ),
            ServiceTileSpec(
                mode = TravelMode.HOTELS,
                title = strings.hotels,
                subtitle = strings.hotelsSub,
                icon = Icons.Filled.Hotel,
                iconBg = if (isDark) Color(0xFF7C3AED).copy(alpha = 0.28f) else Color(0xFFF3E8FF).copy(alpha = 0.85f),
                iconTint = if (isDark) Color(0xFFC4B5FD) else Color(0xFF6D28D9)
            ),
            ServiceTileSpec(
                mode = TravelMode.TOURS,
                title = strings.tours,
                subtitle = strings.toursSub,
                icon = Icons.Filled.Explore,
                iconBg = if (isDark) SuccessEmerald.copy(alpha = 0.24f) else Color(0xFFDCFCE7).copy(alpha = 0.85f),
                iconTint = if (isDark) Color(0xFF6EE7B7) else Color(0xFF047857)
            ),
            ServiceTileSpec(
                mode = TravelMode.ESIM,
                title = strings.esim,
                subtitle = strings.esimSub,
                icon = Icons.Filled.SimCard,
                iconBg = if (isDark) Color(0xFFD97706).copy(alpha = 0.26f) else Color(0xFFFEF3C7).copy(alpha = 0.88f),
                iconTint = if (isDark) Color(0xFFFCD34D) else Color(0xFFB45309)
            ),
            ServiceTileSpec(
                mode = TravelMode.TRANSFERS,
                title = strings.transfers,
                subtitle = strings.transfersSub,
                icon = Icons.Filled.DirectionsCar,
                iconBg = if (isDark) Color.White.copy(alpha = 0.12f) else Color(0xFFE2E8F0).copy(alpha = 0.85f),
                iconTint = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155)
            )
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 720.dp)
                .testTag("home_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Greeting Header + Glass VIP GOLD Badge
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    val greetingName = profile.firstName.ifBlank {
                        SafarLocalization.text(language, L10nKey.HOME_GREETING_FALLBACK)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${strings.greetingPrefix},\n$greetingName!",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = 26.sp,
                                lineHeight = 32.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.greetingSubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    GlassSurface(
                        onClick = { showClubModal = true },
                        modifier = Modifier.minimumInteractiveComponentSize(),
                        shape = CircleShape,
                        isSelected = true,
                        elevation = GlassTokens.DefaultElevation
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.WorkspacePremium,
                                contentDescription = profile.tierLabel,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = profile.tierLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 2. SafarGo Club Balansi Glass Hero Card
            item {
                val discountUzs = profile.clubPoints * 10L
                GlassIndigoHeroBox(
                    modifier = Modifier.testTag("safargo_club_card"),
                    shape = RoundedCornerShape(GlassTokens.CardRadius)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Stars,
                                    contentDescription = null,
                                    tint = SkyBlueAccent,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = strings.clubBalanceLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.9.sp),
                                    color = Color.White.copy(alpha = 0.82f),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = pointsFormatter.format(profile.clubPoints),
                                    style = TabularPriceStyle.copy(fontSize = 28.sp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.HOME_CLUB_POINTS_UNIT),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.88f),
                                    modifier = Modifier.padding(bottom = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "≈ ${pointsFormatter.format(discountUzs)} UZS ${strings.clubDiscountHint}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SkyBlueAccent
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Surface(
                            onClick = { showClubModal = true },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("spend_points_button"),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = strings.spendPointsCta,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 3. Frosted Glass Search Bar + Quick City Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    GlassCard(
                        onClick = { showSearchSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_search_bar"),
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        elevation = GlassTokens.DefaultElevation
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = strings.searchPrompt,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Filled.Tune,
                                contentDescription = SafarLocalization.text(
                                    language,
                                    L10nKey.HOME_SEARCH_FILTERS_CD
                                ),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    val quickHubs = listOf(
                        "SKD" to SafarLocalization.text(language, L10nKey.CITY_SAMARKAND),
                        "BHK" to SafarLocalization.text(language, L10nKey.CITY_BUKHARA),
                        "IST" to SafarLocalization.text(language, L10nKey.CITY_ISTANBUL),
                        "UGC" to SafarLocalization.text(language, L10nKey.CITY_KHIVA),
                        "ALA" to SafarLocalization.text(language, L10nKey.CITY_ALMATY)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(quickHubs) { (code, label) ->
                            GlassSurface(
                                onClick = { onQuickCityClick(code) },
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .heightIn(min = 40.dp)
                                    .testTag("quick_chip_$code"),
                                shape = CircleShape,
                                elevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Asosiy xizmatlar (3x2 Frosted Glass Grid)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strings.mainServicesTitle,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { showSearchSheet = true }) {
                            Text(
                                text = strings.allLabel,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    val rows = services.chunked(3)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { spec ->
                                    ServiceGridTile(
                                        spec = spec,
                                        onClick = {
                                            onSelectMode(spec.mode)
                                            if (spec.mode == TravelMode.HOTELS || spec.mode == TravelMode.ESIM) {
                                                onSearchRoutes()
                                            } else {
                                                showSearchSheet = true
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Yaqinlashayotgan safar (Frosted Glass Boarding Card)
            if (upcomingBooking != null) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = strings.upcomingTripTitle,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                            Text(
                                text = upcomingBooking.travelDate,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        UpcomingTripCard(
                            booking = upcomingBooking,
                            isCheckingIn = checkingInBookingId == upcomingBooking.id,
                            language = language,
                            strings = strings,
                            onCheckIn = { onCheckInUpcoming(upcomingBooking.id) },
                            onOpenQr = { onOpenTicket(upcomingBooking.id) }
                        )
                    }
                }
            }

            // 6. Silk Road Featured Route Banner (Glass Indigo Hero)
            item {
                GlassIndigoHeroBox(
                    modifier = Modifier.testTag("promo_banner_card"),
                    shape = RoundedCornerShape(GlassTokens.CardRadius)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.16f))
                                .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "% ${strings.promoBadge}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = strings.promoTitle,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 21.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = strings.promoBody,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.88f)
                        )
                        Surface(
                            onClick = { showPromoModal = true },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("promo_details_button"),
                            shape = CircleShape,
                            color = Color.White
                        ) {
                            Text(
                                text = strings.promoCta,
                                style = MaterialTheme.typography.labelLarge,
                                color = IndigoPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                            )
                        }
                    }
                }
            }

            // 7. Ommabop yo'nalishlar (2-column Glass Destination Grid)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.popularDestinationsTitle,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = SafarLocalization.text(language, L10nKey.HOME_POPULAR_SUBTITLE),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(onClick = onOpenExploreTab) {
                            Text(
                                text = strings.allLabel,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    val dealRows = SafarCatalog.corridorDeals.chunked(2)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        dealRows.forEach { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                pair.forEach { deal ->
                                    val isFav = deal.destinationCode in profile.favoriteDestinations
                                    DestinationGridCard(
                                        deal = deal,
                                        language = language,
                                        strings = strings,
                                        currency = currency,
                                        isFavorite = isFav,
                                        onToggleFavorite = { onToggleFavoriteDestination(deal.destinationCode) },
                                        onClick = { onLaunchDeal(deal) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (pair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // 8. Kafolatlangan xizmat & to'lovlar Glass Footer
            item {
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
                                    imageVector = Icons.Filled.Shield,
                                    contentDescription = null,
                                    tint = SuccessEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = strings.guaranteedTitle,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = strings.support247,
                                style = MaterialTheme.typography.labelSmall,
                                color = SuccessEmerald,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PaymentBadgeChip("HUMO", HumoGold, Modifier.weight(1f))
                            PaymentBadgeChip("UZCARD", UzcardBlue, Modifier.weight(1f))
                            PaymentBadgeChip("VISA", VisaNavy, Modifier.weight(1f))
                            PaymentBadgeChip("MC", Color(0xFFDC2626), Modifier.weight(1f))
                        }

                        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SupportAgent,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = SafarLocalization.text(language, L10nKey.HOME_CALL_CENTER),
                                    style = TabularCodeStyle.copy(fontSize = 11.sp, fontWeight = FontWeight.Normal),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = SafarLocalization.text(language, L10nKey.HOME_HELP),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .clickable { onOpenSupportInfo() }
                                    .padding(horizontal = 8.dp, vertical = 10.dp)
                            )
                        }

                        GlassNoticeBanner(
                            text = strings.demoNotice,
                            tone = GlassNoticeTone.INFO
                        )
                    }
                }
            }
        }
    }

    if (showClubModal) {
        ModalBottomSheet(
            onDismissRequest = { showClubModal = false },
            shape = RoundedCornerShape(topStart = GlassTokens.SheetRadius, topEnd = GlassTokens.SheetRadius),
            containerColor = glassSheetColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "SafarGo Club · ${profile.tierLabel}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = SafarLocalization.text(
                        language,
                        L10nKey.HOME_CLUB_MODAL_DESC,
                        pointsFormatter.format(profile.clubPoints),
                        pointsFormatter.format(profile.clubPoints * 10L)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                GlassButton(
                    text = if (profile.clubPoints >= 5_000L) {
                        SafarLocalization.text(language, L10nKey.HOME_CLUB_REDEEM_5K)
                    } else {
                        SafarLocalization.text(language, L10nKey.HOME_CLUB_MIN_5K)
                    },
                    enabled = profile.clubPoints >= 5_000L,
                    onClick = {
                        showClubModal = false
                        onSpendClubPoints()
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }

    if (showSearchSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSearchSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            shape = RoundedCornerShape(topStart = GlassTokens.SheetRadius, topEnd = GlassTokens.SheetRadius),
            containerColor = glassSheetColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = strings.searchRoutesCta,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val modes = listOf(
                        TravelMode.FLIGHTS to strings.flights,
                        TravelMode.RAIL to strings.rail,
                        TravelMode.HOTELS to strings.hotels,
                        TravelMode.TOURS to strings.tours,
                        TravelMode.ESIM to strings.esim,
                        TravelMode.TRANSFERS to strings.transfers
                    )
                    items(modes) { (mode, label) ->
                        val selected = searchState.mode == mode
                        GlassSurface(
                            onClick = { onSelectMode(mode) },
                            modifier = Modifier
                                .heightIn(min = 42.dp)
                                .testTag("sheet_mode_${mode.name.lowercase()}"),
                            shape = CircleShape,
                            isSelected = selected,
                            subtle = !selected,
                            elevation = if (selected) GlassTokens.DefaultElevation else 0.dp,
                            containerColor = if (selected) IndigoPrimary else null,
                            border = if (selected) BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.65f)) else null
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                TravelModeIcon(
                                    mode = mode,
                                    tint = if (selected) Color.White else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp),
                                    language = language
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (searchState.mode != TravelMode.HOTELS && searchState.mode != TravelMode.ESIM) {
                            HubInputField(
                                label = strings.fromLabel,
                                hub = originHub,
                                language = language,
                                onClick = { pickingOrigin = true },
                                testTag = "origin_input_field"
                            )
                        }
                        HubInputField(
                            label = strings.toLabel,
                            hub = destHub,
                            language = language,
                            onClick = { pickingDestination = true },
                            testTag = "destination_input_field"
                        )
                    }

                    if (searchState.mode != TravelMode.HOTELS && searchState.mode != TravelMode.ESIM) {
                        GlassSurface(
                            onClick = onSwapOriginDest,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 12.dp)
                                .size(48.dp)
                                .testTag("swap_routes_button"),
                            shape = CircleShape,
                            elevation = GlassTokens.DefaultElevation
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SwapVert,
                                    contentDescription = SafarLocalization.text(
                                        language,
                                        L10nKey.CD_SWAP_ROUTES
                                    ),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = strings.dateLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(datePresets) { date ->
                        val selected = searchState.departureDate == date
                        GlassSurface(
                            onClick = { onSelectDate(date) },
                            modifier = Modifier.heightIn(min = 38.dp),
                            shape = RoundedCornerShape(10.dp),
                            isSelected = selected,
                            subtle = !selected,
                            elevation = 0.dp,
                            containerColor = if (selected) IndigoPrimary else null,
                            border = if (selected) BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.65f)) else null
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.substringBefore(","),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                GlassSurface(
                    shape = RoundedCornerShape(GlassTokens.ControlRadius),
                    subtle = true,
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Column {
                                Text(
                                    text = strings.passengersLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${searchState.passengerCount} ${strings.travelerUnit} · ${
                                        SafarLocalization.text(language, L10nKey.HOME_ECONOMY_VIP)
                                    }",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onChangePassengers(searchState.passengerCount - 1) }) {
                                Icon(
                                    Icons.Filled.Remove,
                                    contentDescription = SafarLocalization.text(language, L10nKey.HOME_DECREASE_CD)
                                )
                            }
                            Text("${searchState.passengerCount}", style = TabularCodeStyle)
                            IconButton(onClick = { onChangePassengers(searchState.passengerCount + 1) }) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = SafarLocalization.text(language, L10nKey.HOME_INCREASE_CD)
                                )
                            }
                        }
                    }
                }

                GlassButton(
                    text = strings.searchRoutesCta,
                    onClick = {
                        showSearchSheet = false
                        onSearchRoutes()
                    },
                    modifier = Modifier.testTag("search_routes_button")
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showPromoModal) {
        ModalBottomSheet(
            onDismissRequest = { showPromoModal = false },
            shape = RoundedCornerShape(topStart = GlassTokens.SheetRadius, topEnd = GlassTokens.SheetRadius),
            containerColor = glassSheetColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = strings.promoTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = strings.promoBody,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                GlassButton(
                    text = strings.searchRoutesCta,
                    onClick = {
                        showPromoModal = false
                        onLaunchDeal(SafarCatalog.corridorDeals.first())
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (pickingOrigin) {
        CityPickerSheet(
            title = strings.fromLabel,
            language = language,
            selectedCode = searchState.originCode,
            onSelect = {
                onSelectOrigin(it)
                pickingOrigin = false
            },
            onDismiss = { pickingOrigin = false }
        )
    }

    if (pickingDestination) {
        CityPickerSheet(
            title = strings.toLabel,
            language = language,
            selectedCode = searchState.destinationCode,
            onSelect = {
                onSelectDestination(it)
                pickingDestination = false
            },
            onDismiss = { pickingDestination = false }
        )
    }
}

@Composable
private fun ServiceGridTile(
    spec: ServiceTileSpec,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = 108.dp)
            .testTag("service_tile_${spec.mode.name.lowercase()}"),
        shape = RoundedCornerShape(GlassTokens.CardRadius),
        elevation = GlassTokens.DefaultElevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(spec.iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = spec.icon,
                    contentDescription = spec.title,
                    tint = spec.iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = spec.title,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = spec.subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun UpcomingTripCard(
    booking: BookingDoc,
    isCheckingIn: Boolean,
    language: AppLanguage,
    strings: SafarStrings,
    onCheckIn: () -> Unit,
    onOpenQr: () -> Unit
) {
    val isCheckedIn = booking.statusTag == BookingStatusTag.CHECKED_IN
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("upcoming_trip_card"),
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        TravelModeIcon(
                            mode = booking.mode,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                            language = language
                        )
                    }
                    Column {
                        Text(
                            text = booking.carrier,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${booking.carrierCode} · ${booking.fareClass}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
                StatusBadgePill(statusTag = booking.statusTag, strings = strings)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = booking.departureTime,
                        style = TabularTimeStyle.copy(fontSize = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = booking.originCode,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = booking.originCity,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = booking.durationText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth().height(4.dp)) {
                            val y = size.height / 2f
                            drawLine(
                                color = SkyBlueAccent,
                                start = Offset(6f, y),
                                end = Offset(size.width - 6f, y),
                                strokeWidth = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                            )
                            drawCircle(IndigoPrimary, radius = 6f, center = Offset(6f, y))
                            drawCircle(SkyBlueAccent, radius = 6f, center = Offset(size.width - 6f, y))
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(glassFillColor(isDark = isDark))
                                .padding(horizontal = 4.dp)
                        ) {
                            TravelModeIcon(
                                mode = booking.mode,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp),
                                language = language
                            )
                        }
                    }
                    Text(
                        text = strings.directLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = booking.arrivalTime,
                        style = TabularTimeStyle.copy(fontSize = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = booking.destinationCode,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = booking.destinationCity,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCheckIn,
                    enabled = !isCheckedIn && !isCheckingIn,
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .heightIn(min = 48.dp)
                        .testTag("upcoming_checkin_button"),
                    shape = RoundedCornerShape(GlassTokens.ControlRadius),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCheckedIn) SuccessEmerald else IndigoPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = SuccessEmerald.copy(alpha = 0.85f),
                        disabledContentColor = Color.White
                    )
                ) {
                    if (isCheckingIn) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        if (isCheckedIn) {
                            Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = if (isCheckedIn) strings.checkedInLabel else strings.onlineCheckIn,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                GlassSecondaryButton(
                    onClick = onOpenQr,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("upcoming_qr_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.QrCode2,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = strings.qrTicket,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun DestinationGridCard(
    deal: CorridorDeal,
    language: AppLanguage,
    strings: SafarStrings,
    currency: CurrencyCode,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        onClick = onClick,
        modifier = modifier.testTag("corridor_deal_${deal.id}"),
        shape = RoundedCornerShape(GlassTokens.CardRadius),
        elevation = GlassTokens.DefaultElevation
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(114.dp)
            ) {
                Image(
                    painter = painterResource(id = deal.imageRes),
                    contentDescription = deal.localizedTitle(language),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(IndigoDeep.copy(alpha = 0.84f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = deal.localizedBadge(language),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color.White,
                        maxLines = 1
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("favorite_deal_${deal.destinationCode}"),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.90f),
                        border = BorderStroke(1.dp, Color.White)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = SafarLocalization.text(language, L10nKey.HOME_FAVORITE_CD),
                                tint = if (isFavorite) Color(0xFFEF4444) else IndigoPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = deal.localizedTitle(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = deal.rating,
                            style = TabularCodeStyle.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Text(
                    text = deal.localizedSubtitle(language),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${strings.fromPricePrefix} ",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = CurrencyFormatter.formatFull(deal.fromPriceUzs, currency),
                        style = TabularPriceStyle.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentBadgeChip(
    label: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    GlassSurface(
        modifier = modifier.height(32.dp),
        shape = RoundedCornerShape(8.dp),
        subtle = true,
        elevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = accent,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HubInputField(
    label: String,
    hub: CityHub,
    language: AppLanguage,
    onClick: () -> Unit,
    testTag: String
) {
    GlassSurface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(GlassTokens.ControlRadius),
        subtle = true,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(IndigoPrimary.copy(alpha = 0.14f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = hub.code,
                    style = TabularCodeStyle,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${hub.localizedName(language)} · ${hub.stationOrAirport}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityPickerSheet(
    title: String,
    language: AppLanguage,
    selectedCode: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val glassSheetColor = if (isDark) Color(0xF010182E) else Color(0xF2EEF3FF)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = GlassTokens.SheetRadius, topEnd = GlassTokens.SheetRadius),
        containerColor = glassSheetColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            SafarCatalog.hubs.forEach { hub ->
                val isSelected = hub.code == selectedCode
                GlassSurface(
                    onClick = { onSelect(hub.code) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("city_option_${hub.code}"),
                    shape = RoundedCornerShape(GlassTokens.ControlRadius),
                    isSelected = isSelected,
                    subtle = !isSelected,
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${hub.localizedName(language)} (${hub.code})",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${hub.stationOrAirport} · ${hub.country}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isSelected) {
                            Text(
                                text = SafarLocalization.text(language, L10nKey.HOME_CITY_SELECTED),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, name = "Home - Glass Light")
@Composable
private fun HomeScreenPreview() {
    SafarGoTheme(darkTheme = false) {
        SafarGoGlassBackdrop {
            HomeScreen(
                profile = UserProfileDoc(
                    fullName = "Farrux Alimov",
                    tierLabel = "Gold",
                    clubPoints = 25_000L
                ),
                upcomingBooking = BookingDoc(
                    id = "demo_1",
                    pnrCode = "SF-89240",
                    modeName = TravelMode.RAIL.name,
                    carrier = "Afrosiyob Express",
                    carrierCode = "#762",
                    originCode = "TAS",
                    originCity = "Toshkent",
                    destinationCode = "SKD",
                    destinationCity = "Samarqand",
                    departureTime = "08:00",
                    arrivalTime = "10:08",
                    durationText = "2s 08d",
                    travelDate = "18 Noy, 2026",
                    fareClass = "VIP",
                    statusName = BookingStatusTag.CONFIRMED.name,
                    seatNumber = "3-vagon · 14B",
                    platformOrGate = "2-platforma"
                ),
                searchState = SearchUiState(),
                currency = CurrencyCode.UZS,
                language = AppLanguage.UZ,
                strings = stringsFor(AppLanguage.UZ),
                onSelectMode = {},
                onSelectOrigin = {},
                onSelectDestination = {},
                onSwapOriginDest = {},
                onSelectDate = {},
                onChangePassengers = {},
                onSearchRoutes = {},
                onLaunchDeal = {},
                onQuickCityClick = {},
                onToggleFavoriteDestination = {},
                onSpendClubPoints = {},
                onCheckInUpcoming = {},
                onOpenTicket = {},
                onOpenExploreTab = {},
                onOpenSupportInfo = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Home - Glass Dark")
@Composable
private fun HomeScreenDarkPreview() {
    SafarGoTheme(darkTheme = true) {
        SafarGoGlassBackdrop {
            HomeScreen(
                profile = UserProfileDoc(
                    fullName = "Farrux Alimov",
                    tierLabel = "Gold",
                    clubPoints = 25_000L
                ),
                upcomingBooking = BookingDoc(
                    id = "demo_1",
                    pnrCode = "SF-89240",
                    modeName = TravelMode.RAIL.name,
                    carrier = "Afrosiyob Express",
                    carrierCode = "#762",
                    originCode = "TAS",
                    originCity = "Toshkent",
                    destinationCode = "SKD",
                    destinationCity = "Samarqand",
                    departureTime = "08:00",
                    arrivalTime = "10:08",
                    durationText = "2s 08d",
                    travelDate = "18 Noy, 2026",
                    fareClass = "VIP",
                    statusName = BookingStatusTag.CONFIRMED.name,
                    seatNumber = "3-vagon · 14B",
                    platformOrGate = "2-platforma"
                ),
                searchState = SearchUiState(),
                currency = CurrencyCode.UZS,
                language = AppLanguage.UZ,
                strings = stringsFor(AppLanguage.UZ),
                onSelectMode = {},
                onSelectOrigin = {},
                onSelectDestination = {},
                onSwapOriginDest = {},
                onSelectDate = {},
                onChangePassengers = {},
                onSearchRoutes = {},
                onLaunchDeal = {},
                onQuickCityClick = {},
                onToggleFavoriteDestination = {},
                onSpendClubPoints = {},
                onCheckInUpcoming = {},
                onOpenTicket = {},
                onOpenExploreTab = {},
                onOpenSupportInfo = {}
            )
        }
    }
}
