package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class TravelMode {
    FLIGHTS, RAIL, HOTELS, TOURS, ESIM, TRANSFERS
}

enum class CurrencyCode {
    UZS, USD
}

enum class AppLanguage(val shortLabel: String, val fullName: String) {
    UZ("UZ", "O‘zbekcha"),
    EN("EN", "English"),
    RU("RU", "Русский")
}

enum class BookingStatusTag {
    CONFIRMED, PRIORITY, DELAYED, CHECKED_IN, CANCELLED
}

enum class ThemePreference {
    SYSTEM, LIGHT, DARK
}

data class CityHub(
    val code: String,
    val nameEn: String,
    val nameUz: String,
    val nameRu: String,
    val country: String,
    val stationOrAirport: String
) {
    fun localizedName(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> nameEn
        AppLanguage.UZ -> nameUz
        AppLanguage.RU -> nameRu
    }
}

data class TravelOption(
    val id: String,
    val mode: TravelMode,
    val carrier: String,
    val carrierCode: String,
    val originCode: String,
    val originCity: String,
    val destinationCode: String,
    val destinationCity: String,
    val departureTime: String,
    val arrivalTime: String,
    val durationText: String,
    val stopsText: String,
    val isDirect: Boolean,
    val isMorning: Boolean,
    val fareClass: String,
    val statusTag: BookingStatusTag,
    val priceUzs: Long,
    val seatsLeft: Int,
    val baggageInfo: String,
    val amenities: List<String>,
    val platformOrGate: String,
    val hasSeatMap: Boolean = true
)

data class PaymentRail(
    val id: String,
    val title: String,
    val subtitleUz: String,
    val subtitleEn: String,
    val subtitleRu: String,
    val badgeText: String,
    val isLocalRail: Boolean
) {
    fun localizedSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.UZ -> subtitleUz
        AppLanguage.EN -> subtitleEn
        AppLanguage.RU -> subtitleRu
    }
}

data class CorridorDeal(
    val id: String,
    val titleUz: String,
    val titleEn: String,
    val titleRu: String,
    val subtitleUz: String,
    val subtitleEn: String,
    val subtitleRu: String,
    val originCode: String,
    val destinationCode: String,
    val mode: TravelMode,
    val durationBadgeUz: String,
    val durationBadgeEn: String,
    val durationBadgeRu: String,
    val rating: String,
    val fromPriceUzs: Long,
    @param:DrawableRes val imageRes: Int
) {
    val title: String get() = titleUz
    val subtitle: String get() = subtitleUz
    val durationBadge: String get() = durationBadgeUz

    fun localizedTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.UZ -> titleUz
        AppLanguage.EN -> titleEn
        AppLanguage.RU -> titleRu
    }

    fun localizedSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.UZ -> subtitleUz
        AppLanguage.EN -> subtitleEn
        AppLanguage.RU -> subtitleRu
    }

    fun localizedBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.UZ -> durationBadgeUz
        AppLanguage.EN -> durationBadgeEn
        AppLanguage.RU -> durationBadgeRu
    }
}

data class SeatItem(
    val code: String,
    val row: Int,
    val col: String,
    val isOccupied: Boolean,
    val isVipOrExtraLegroom: Boolean,
    val extraPriceUzs: Long
)

object CurrencyFormatter {
    private val uzsSymbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
    }
    private val uzsFormat = DecimalFormat("#,###", uzsSymbols)
    private const val UZS_PER_USD = 12_680.0

    fun formatAmountOnly(priceUzs: Long, currency: CurrencyCode): String {
        return when (currency) {
            CurrencyCode.UZS -> uzsFormat.format(priceUzs)
            CurrencyCode.USD -> {
                val usd = priceUzs / UZS_PER_USD
                String.format(Locale.US, "$%.2f", usd)
            }
        }
    }

    fun formatUnit(currency: CurrencyCode): String = when (currency) {
        CurrencyCode.UZS -> "UZS"
        CurrencyCode.USD -> "USD"
    }

    fun formatFull(priceUzs: Long, currency: CurrencyCode): String {
        return "${formatAmountOnly(priceUzs, currency)} ${formatUnit(currency)}"
    }
}

data class SafarStrings(
    val greetingPrefix: String,
    val greetingSubtitle: String,
    val clubBalanceLabel: String,
    val clubDiscountHint: String,
    val spendPointsCta: String,
    val searchPrompt: String,
    val mainServicesTitle: String,
    val allLabel: String,
    val flights: String,
    val flightsSub: String,
    val rail: String,
    val railSub: String,
    val hotels: String,
    val hotelsSub: String,
    val tours: String,
    val toursSub: String,
    val esim: String,
    val esimSub: String,
    val transfers: String,
    val transfersSub: String,
    val upcomingTripTitle: String,
    val onlineCheckIn: String,
    val qrTicket: String,
    val promoBadge: String,
    val promoTitle: String,
    val promoBody: String,
    val promoCta: String,
    val popularDestinationsTitle: String,
    val guaranteedTitle: String,
    val support247: String,
    val navHome: String,
    val navHotels: String,
    val navTours: String,
    val navTrips: String,
    val navProfile: String,
    val fromLabel: String,
    val toLabel: String,
    val dateLabel: String,
    val passengersLabel: String,
    val searchRoutesCta: String,
    val selectCta: String,
    val payAndIssueCta: String,
    val directLabel: String,
    val confirmedLabel: String,
    val priorityLabel: String,
    val delayedLabel: String,
    val checkedInLabel: String,
    val cancelledLabel: String,
    val upcomingTab: String,
    val completedTab: String,
    val allTab: String,
    val seatMapStep: String,
    val passengerStep: String,
    val paymentStep: String,
    val emptyTripsTitle: String,
    val emptyTripsBody: String,
    val demoNotice: String,
    val loadingLabel: String,
    val retryLabel: String,
    val cancelLabel: String,
    val backLabel: String,
    val fromPricePrefix: String,
    val travelerUnit: String
)

fun stringsFor(lang: AppLanguage): SafarStrings = SafarStrings(
    greetingPrefix = SafarLocalization.text(lang, L10nKey.HOME_GREETING_PREFIX),
    greetingSubtitle = SafarLocalization.text(lang, L10nKey.HOME_GREETING_SUBTITLE),
    clubBalanceLabel = SafarLocalization.text(lang, L10nKey.HOME_CLUB_BALANCE_LABEL),
    clubDiscountHint = SafarLocalization.text(lang, L10nKey.HOME_CLUB_DISCOUNT_HINT),
    spendPointsCta = SafarLocalization.text(lang, L10nKey.HOME_SPEND_POINTS),
    searchPrompt = SafarLocalization.text(lang, L10nKey.HOME_SEARCH_PROMPT),
    mainServicesTitle = SafarLocalization.text(lang, L10nKey.HOME_MAIN_SERVICES),
    allLabel = SafarLocalization.text(lang, L10nKey.HOME_ALL_LABEL),
    flights = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_FLIGHTS),
    flightsSub = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_FLIGHTS_SUB),
    rail = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_RAIL),
    railSub = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_RAIL_SUB),
    hotels = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_HOTELS),
    hotelsSub = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_HOTELS_SUB),
    tours = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_TOURS),
    toursSub = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_TOURS_SUB),
    esim = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_ESIM),
    esimSub = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_ESIM_SUB),
    transfers = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_TRANSFERS),
    transfersSub = SafarLocalization.text(lang, L10nKey.HOME_SERVICE_TRANSFERS_SUB),
    upcomingTripTitle = SafarLocalization.text(lang, L10nKey.HOME_UPCOMING_TRIP),
    onlineCheckIn = SafarLocalization.text(lang, L10nKey.HOME_ONLINE_CHECKIN),
    qrTicket = SafarLocalization.text(lang, L10nKey.HOME_QR_TICKET),
    promoBadge = SafarLocalization.text(lang, L10nKey.HOME_PROMO_BADGE),
    promoTitle = SafarLocalization.text(lang, L10nKey.HOME_PROMO_TITLE),
    promoBody = SafarLocalization.text(lang, L10nKey.HOME_PROMO_BODY),
    promoCta = SafarLocalization.text(lang, L10nKey.HOME_PROMO_CTA),
    popularDestinationsTitle = SafarLocalization.text(lang, L10nKey.HOME_POPULAR_TITLE),
    guaranteedTitle = SafarLocalization.text(lang, L10nKey.HOME_GUARANTEED_TITLE),
    support247 = SafarLocalization.text(lang, L10nKey.HOME_SUPPORT_247),
    navHome = SafarLocalization.text(lang, L10nKey.NAV_HOME),
    navHotels = SafarLocalization.text(lang, L10nKey.NAV_HOTELS),
    navTours = SafarLocalization.text(lang, L10nKey.NAV_TOURS),
    navTrips = SafarLocalization.text(lang, L10nKey.NAV_TRIPS),
    navProfile = SafarLocalization.text(lang, L10nKey.NAV_PROFILE),
    fromLabel = SafarLocalization.text(lang, L10nKey.HOME_FROM_LABEL),
    toLabel = SafarLocalization.text(lang, L10nKey.HOME_TO_LABEL),
    dateLabel = SafarLocalization.text(lang, L10nKey.HOME_DATE_LABEL),
    passengersLabel = SafarLocalization.text(lang, L10nKey.HOME_PASSENGERS_LABEL),
    searchRoutesCta = SafarLocalization.text(lang, L10nKey.SEARCH_ROUTES_CTA),
    selectCta = SafarLocalization.text(lang, L10nKey.SELECT_CTA),
    payAndIssueCta = SafarLocalization.text(lang, L10nKey.PAY_AND_ISSUE_CTA),
    directLabel = SafarLocalization.text(lang, L10nKey.DIRECT_LABEL),
    confirmedLabel = SafarLocalization.text(lang, L10nKey.CONFIRMED_LABEL),
    priorityLabel = SafarLocalization.text(lang, L10nKey.PRIORITY_LABEL),
    delayedLabel = SafarLocalization.text(lang, L10nKey.DELAYED_LABEL),
    checkedInLabel = SafarLocalization.text(lang, L10nKey.CHECKED_IN_LABEL),
    cancelledLabel = SafarLocalization.text(lang, L10nKey.CANCELLED_LABEL),
    upcomingTab = SafarLocalization.text(lang, L10nKey.TRIPS_UPCOMING_TAB),
    completedTab = SafarLocalization.text(lang, L10nKey.TRIPS_COMPLETED_TAB),
    allTab = SafarLocalization.text(lang, L10nKey.TRIPS_ALL_TAB),
    seatMapStep = SafarLocalization.text(lang, L10nKey.CHECKOUT_STEP_SEAT),
    passengerStep = SafarLocalization.text(lang, L10nKey.CHECKOUT_STEP_PASSENGER),
    paymentStep = SafarLocalization.text(lang, L10nKey.CHECKOUT_STEP_PAYMENT),
    emptyTripsTitle = SafarLocalization.text(lang, L10nKey.TRIPS_EMPTY_TITLE),
    emptyTripsBody = SafarLocalization.text(lang, L10nKey.TRIPS_EMPTY_BODY),
    demoNotice = SafarLocalization.text(lang, L10nKey.DEMO_NOTICE),
    loadingLabel = SafarLocalization.text(lang, L10nKey.LOADING_LABEL),
    retryLabel = SafarLocalization.text(lang, L10nKey.RETRY_LABEL),
    cancelLabel = SafarLocalization.text(lang, L10nKey.CANCEL_LABEL),
    backLabel = SafarLocalization.text(lang, L10nKey.BACK_LABEL),
    fromPricePrefix = SafarLocalization.text(lang, L10nKey.FROM_PRICE_PREFIX),
    travelerUnit = SafarLocalization.text(lang, L10nKey.TRAVELER_UNIT)
)

object SafarCatalog {
    val hubs = listOf(
        CityHub("TAS", "Tashkent", "Toshkent", "Ташкент", "Uzbekistan", "TAS / Toshkent-Markaziy"),
        CityHub("SKD", "Samarkand", "Samarqand", "Самарканд", "Uzbekistan", "SKD / Samarkand Rail"),
        CityHub("BHK", "Bukhara", "Buxoro", "Бухара", "Uzbekistan", "BHK / Kogon Express"),
        CityHub("UGC", "Khiva / Urgench", "Xiva / Urganch", "Хива / Ургенч", "Uzbekistan", "UGC / Khiva Station"),
        CityHub("ALA", "Almaty", "Olmaota", "Алматы", "Kazakhstan", "ALA International"),
        CityHub("IST", "Istanbul", "Istanbul", "Стамбул", "Türkiye", "IST Grand Airport")
    )

    val paymentRails = listOf(
        PaymentRail(
            id = "uzcard",
            title = "Uzcard",
            subtitleUz = "Tezkor UZS karta to‘lovi (Namuna)",
            subtitleEn = "Instant UZS Card Rail (Demo)",
            subtitleRu = "Мгновенная оплата UZS (Демо)",
            badgeText = "UZCARD",
            isLocalRail = true
        ),
        PaymentRail(
            id = "humo",
            title = "Humo",
            subtitleUz = "Kontaktsiz NFC va karta (Namuna)",
            subtitleEn = "Contactless NFC & Card (Demo)",
            subtitleRu = "Бесконтактная оплата NFC (Демо)",
            badgeText = "HUMO",
            isLocalRail = true
        ),
        PaymentRail(
            id = "click",
            title = "Click Evolution",
            subtitleUz = "Avto-to‘lov hamyoni · 0% komissiya (Namuna)",
            subtitleEn = "Auto-Pay Wallet · 0% Fee (Demo)",
            subtitleRu = "Кошелёк Click · 0% комиссия (Демо)",
            badgeText = "CLICK",
            isLocalRail = true
        ),
        PaymentRail(
            id = "payme",
            title = "Payme",
            subtitleUz = "Tezkor mobil hamyon orqali to‘lov (Namuna)",
            subtitleEn = "Instant Mobile Wallet Rail (Demo)",
            subtitleRu = "Быстрая оплата через Payme (Демо)",
            badgeText = "PAYME",
            isLocalRail = true
        ),
        PaymentRail(
            id = "uzum",
            title = "Uzum Pay",
            subtitleUz = "3 oylik bo‘lib to‘lash imkoniyati (Namuna)",
            subtitleEn = "0% 3-Month Split Option (Demo)",
            subtitleRu = "Рассрочка 0% на 3 месяца (Демо)",
            badgeText = "UZUM",
            isLocalRail = true
        ),
        PaymentRail(
            id = "visa",
            title = "Visa / Mastercard",
            subtitleUz = "Xalqaro UZS/USD karta to‘lovi (Namuna)",
            subtitleEn = "International Dual UZS/USD Card (Demo)",
            subtitleRu = "Международная карта UZS/USD (Демо)",
            badgeText = "VISA",
            isLocalRail = false
        )
    )

    val corridorDeals = listOf(
        CorridorDeal(
            id = "deal_skd",
            titleUz = "Samarqand",
            titleEn = "Samarkand",
            titleRu = "Самарканд",
            subtitleUz = "Registon maydoni, Go‘ri Amir",
            subtitleEn = "Registan Square, Gur-e-Amir",
            subtitleRu = "Площадь Регистан, Гур-Эмир",
            originCode = "TAS",
            destinationCode = "SKD",
            mode = TravelMode.RAIL,
            durationBadgeUz = "Afrosiyob: 2s 10d",
            durationBadgeEn = "Afrosiyob: 2h 10m",
            durationBadgeRu = "Афросиаб: 2ч 10м",
            rating = "4.9",
            fromPriceUzs = 145_000L,
            imageRes = R.drawable.img_hero_samarkand
        ),
        CorridorDeal(
            id = "deal_bhk",
            titleUz = "Buxoro",
            titleEn = "Bukhara",
            titleRu = "Бухара",
            subtitleUz = "Labi Hovuz, Minorai Kalon",
            subtitleEn = "Lyabi-Hauz, Kalyan Minaret",
            subtitleRu = "Ляби-Хауз, Минарет Калян",
            originCode = "TAS",
            destinationCode = "BHK",
            mode = TravelMode.RAIL,
            durationBadgeUz = "Tarixiy obidalar",
            durationBadgeEn = "Heritage City",
            durationBadgeRu = "Исторический город",
            rating = "4.8",
            fromPriceUzs = 320_000L,
            imageRes = R.drawable.img_bukhara_old_city
        ),
        CorridorDeal(
            id = "deal_ugc",
            titleUz = "Xiva (Urganch)",
            titleEn = "Khiva (Urgench)",
            titleRu = "Хива (Ургенч)",
            subtitleUz = "Ochiq osmon muzeyi · Ichan Qal’a",
            subtitleEn = "Open-Air Museum · Itchan Kala",
            subtitleRu = "Музей под открытым небом · Ичан-Кала",
            originCode = "TAS",
            destinationCode = "UGC",
            mode = TravelMode.FLIGHTS,
            durationBadgeUz = "Aviachipta",
            durationBadgeEn = "Flight Deal",
            durationBadgeRu = "Авиабилет",
            rating = "4.9",
            fromPriceUzs = 1_850_000L,
            imageRes = R.drawable.img_hotel_luxury_tashkent
        ),
        CorridorDeal(
            id = "deal_ist",
            titleUz = "Istanbul",
            titleEn = "Istanbul",
            titleRu = "Стамбул",
            subtitleUz = "Bosfor qirg‘oqlari · Boing 787",
            subtitleEn = "Bosphorus Coast · Boeing 787",
            subtitleRu = "Берега Босфора · Boeing 787",
            originCode = "TAS",
            destinationCode = "IST",
            mode = TravelMode.FLIGHTS,
            durationBadgeUz = "Xalqaro reys",
            durationBadgeEn = "International",
            durationBadgeRu = "Международный",
            rating = "4.9",
            fromPriceUzs = 2_450_000L,
            imageRes = R.drawable.img_tashkent_skyline
        )
    )
}
