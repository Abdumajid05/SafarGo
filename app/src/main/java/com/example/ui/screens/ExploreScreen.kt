package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Train
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.CorridorDeal
import com.example.data.model.CurrencyCode
import com.example.data.model.L10nKey
import com.example.data.model.SafarCatalog
import com.example.data.model.SafarLocalization
import com.example.data.model.TravelMode
import com.example.data.model.stringsFor
import com.example.ui.components.CurrencyAmountText
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassNoticeBanner
import com.example.ui.components.GlassNoticeTone
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.theme.IndigoDeep
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SafarGoTheme
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TabularCodeStyle

enum class ExploreCategory { HOTELS, TOURS }

@Composable
fun ExploreScreen(
    category: ExploreCategory,
    language: AppLanguage,
    currency: CurrencyCode,
    onLaunchDeal: (CorridorDeal) -> Unit,
    onBookHotelCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = stringsFor(language)
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 720.dp)
                .testTag("explore_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GlassNoticeBanner(
                    text = strings.demoNotice,
                    tone = GlassNoticeTone.INFO
                )
            }

            if (category == ExploreCategory.HOTELS) {
                item {
                    Column {
                        Text(
                            text = SafarLocalization.text(language, L10nKey.EXPLORE_HOTELS_TITLE),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SafarLocalization.text(language, L10nKey.EXPLORE_HOTELS_SUBTITLE),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                val hotelShowcases = listOf(
                    Triple(
                        SafarLocalization.text(language, L10nKey.EXPLORE_HOTEL_ZARAFSHAN),
                        "SKD",
                        1_480_000L to R.drawable.img_hero_samarkand
                    ),
                    Triple(
                        SafarLocalization.text(language, L10nKey.EXPLORE_HOTEL_INDIGO),
                        "TAS",
                        940_000L to R.drawable.img_hotel_luxury_tashkent
                    ),
                    Triple(
                        SafarLocalization.text(language, L10nKey.EXPLORE_HOTEL_LABI),
                        "BHK",
                        860_000L to R.drawable.img_bukhara_old_city
                    )
                )
                items(hotelShowcases) { (title, code, priceAndRes) ->
                    GlassCard(
                        onClick = { onBookHotelCity(code) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hotel_card_$code"),
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        elevation = GlassTokens.DefaultElevation
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(164.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = priceAndRes.second),
                                    contentDescription = title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, IndigoDeep.copy(alpha = 0.85f))
                                            )
                                        )
                                )
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(12.dp)
                                        .clip(CircleShape)
                                        .background(IndigoPrimary.copy(alpha = 0.92f))
                                        .border(1.dp, Color.White.copy(alpha = 0.28f), CircleShape)
                                        .padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Hotel,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = SafarLocalization.text(language, L10nKey.EXPLORE_VETTED_5STAR),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(12.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.94f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text("4.9", style = TabularCodeStyle, color = IndigoDeep)
                                }
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(14.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = SafarLocalization.text(language, L10nKey.EXPLORE_PER_NIGHT),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    CurrencyAmountText(
                                        priceUzs = priceAndRes.first,
                                        currency = currency,
                                        fontSizeSp = 17
                                    )
                                }
                                Button(
                                    onClick = { onBookHotelCity(code) },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .heightIn(min = 48.dp),
                                    shape = RoundedCornerShape(GlassTokens.ControlRadius),
                                    border = BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.4f)),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                                ) {
                                    Text(
                                        text = SafarLocalization.text(language, L10nKey.EXPLORE_SELECT_ROOM)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Column {
                        Text(
                            text = SafarLocalization.text(language, L10nKey.EXPLORE_TOURS_TITLE),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SafarLocalization.text(language, L10nKey.EXPLORE_TOURS_SUBTITLE),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                item {
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
                                    imageVector = Icons.Filled.Train,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = SafarLocalization.text(
                                        language,
                                        L10nKey.EXPLORE_AFROSIYOB_SCHEDULE
                                    ),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            val dur1 = SafarLocalization.text(language, L10nKey.EXPLORE_DUR_2H08)
                            val dur2 = SafarLocalization.text(language, L10nKey.EXPLORE_DUR_3H51)
                            ScheduleRow("#762", "TAS → SKD", "08:00 → 10:08", dur1)
                            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                            ScheduleRow("#764", "TAS → BHK", "08:30 → 12:21", dur2)
                            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                            ScheduleRow("#766", "SKD → TAS", "17:06 → 19:14", dur1)
                        }
                    }
                }

                items(SafarCatalog.corridorDeals, key = { it.id }) { deal ->
                    val tourDeal = deal.copy(mode = TravelMode.TOURS)
                    GlassCard(
                        onClick = { onLaunchDeal(tourDeal) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("explore_deal_${deal.id}"),
                        shape = RoundedCornerShape(GlassTokens.CardRadius),
                        elevation = GlassTokens.DefaultElevation
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(156.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = deal.imageRes),
                                    contentDescription = deal.localizedTitle(language),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, IndigoDeep.copy(alpha = 0.85f))
                                            )
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(12.dp)
                                        .clip(CircleShape)
                                        .background(IndigoPrimary.copy(alpha = 0.92f))
                                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = deal.localizedBadge(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(14.dp)
                                ) {
                                    Text(
                                        text = deal.localizedTitle(language),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White
                                    )
                                    Text(
                                        text = deal.localizedSubtitle(language),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.9f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = SafarLocalization.text(language, L10nKey.EXPLORE_STARTING_FROM),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    CurrencyAmountText(
                                        priceUzs = deal.fromPriceUzs,
                                        currency = currency,
                                        fontSizeSp = 17
                                    )
                                }
                                Button(
                                    onClick = { onLaunchDeal(tourDeal) },
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .heightIn(min = 48.dp),
                                    shape = RoundedCornerShape(GlassTokens.ControlRadius),
                                    border = BorderStroke(1.dp, SkyBlueAccent.copy(alpha = 0.4f)),
                                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                                ) {
                                    Text(
                                        text = SafarLocalization.text(language, L10nKey.EXPLORE_VIEW_ROUTES)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
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
            }
        }
    }
}

@Composable
private fun ScheduleRow(train: String, route: String, times: String, duration: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = train,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = route,
            style = TabularCodeStyle,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = times,
            style = TabularCodeStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        GlassSurface(
            shape = CircleShape,
            subtle = true,
            elevation = 0.dp
        ) {
            Text(
                text = duration,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Explore - Hotels (Light)")
@Composable
private fun ExploreHotelsPreview() {
    SafarGoTheme(darkTheme = false) {
        SafarGoGlassBackdrop {
            ExploreScreen(
                category = ExploreCategory.HOTELS,
                language = AppLanguage.UZ,
                currency = CurrencyCode.UZS,
                onLaunchDeal = {},
                onBookHotelCity = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Explore - Tours (Dark)")
@Composable
private fun ExploreToursDarkPreview() {
    SafarGoTheme(darkTheme = true) {
        SafarGoGlassBackdrop {
            ExploreScreen(
                category = ExploreCategory.TOURS,
                language = AppLanguage.UZ,
                currency = CurrencyCode.UZS,
                onLaunchDeal = {},
                onBookHotelCity = {}
            )
        }
    }
}
