package com.example.data.repository

import com.example.data.local.BookingDao
import com.example.data.local.BookingEntity
import com.example.data.local.UserPreferences
import com.example.data.local.UserPreferencesDataStore
import com.example.data.model.AppLanguage
import com.example.data.model.BookingStatusTag
import com.example.data.model.CurrencyCode
import com.example.data.model.SafarCatalog
import com.example.data.model.SeatItem
import com.example.data.model.ThemePreference
import com.example.data.model.TravelMode
import com.example.data.model.TravelOption
import kotlinx.coroutines.flow.Flow
import kotlin.math.abs

class SafarGoRepository(
    private val bookingDao: BookingDao,
    private val prefsDataStore: UserPreferencesDataStore
) {
    val allBookings: Flow<List<BookingEntity>> = bookingDao.observeAllBookings()
    val userPreferences: Flow<UserPreferences> = prefsDataStore.userPreferences

    fun observeBooking(id: Long): Flow<BookingEntity?> = bookingDao.observeBookingById(id)

    suspend fun setCurrency(currency: CurrencyCode) = prefsDataStore.setCurrency(currency)
    suspend fun setLanguage(language: AppLanguage) = prefsDataStore.setLanguage(language)
    suspend fun setThemePreference(theme: ThemePreference) = prefsDataStore.setThemePreference(theme)
    suspend fun setPreferredPayment(paymentId: String) = prefsDataStore.setPreferredPayment(paymentId)
    suspend fun saveTravelerProfile(name: String, passport: String, phone: String) =
        prefsDataStore.saveTravelerProfile(name, passport, phone)

    suspend fun savePendingAuthChannels(
        phone: String? = null,
        telegramHandle: String? = null,
        telegramNotifications: Boolean? = null
    ) = prefsDataStore.savePendingAuthChannels(
        phone = phone,
        telegramHandle = telegramHandle,
        telegramNotifications = telegramNotifications
    )

    suspend fun clearPendingAuthChannels() = prefsDataStore.clearPendingAuthChannels()

    private fun tr(lang: AppLanguage, uz: String, en: String, ru: String): String = when (lang) {
        AppLanguage.UZ -> uz
        AppLanguage.EN -> en
        AppLanguage.RU -> ru
    }

    fun searchRoutes(
        mode: TravelMode,
        originCode: String,
        destinationCode: String,
        language: AppLanguage = AppLanguage.UZ
    ): List<TravelOption> {
        val origin = SafarCatalog.hubs.find { it.code == originCode } ?: SafarCatalog.hubs.first()
        val dest = SafarCatalog.hubs.find { it.code == destinationCode } ?: SafarCatalog.hubs[1]
        val isInternational = dest.country != origin.country
        val seed = abs((originCode + destinationCode).hashCode())
        val originCity = origin.localizedName(language)
        val destCity = dest.localizedName(language)

        return when (mode) {
            TravelMode.RAIL -> {
                if (isInternational) {
                    listOf(
                        TravelOption(
                            id = "rail_${originCode}_${destinationCode}_1",
                            mode = TravelMode.RAIL,
                            carrier = "Silk Road Night Express",
                            carrierCode = tr(language, "Poyezd #650", "Train #650", "Поезд #650"),
                            originCode = origin.code,
                            originCity = originCity,
                            destinationCode = dest.code,
                            destinationCity = destCity,
                            departureTime = "18:45",
                            arrivalTime = "10:15",
                            durationText = tr(language, "15s 30d", "15h 30m", "15ч 30м"),
                            stopsText = tr(language, "2 chegara bekati", "2 border stops", "2 пограничные остановки"),
                            isDirect = false,
                            isMorning = false,
                            fareClass = tr(language, "VIP Kupe", "VIP Sleeper", "VIP Купе"),
                            statusTag = BookingStatusTag.CONFIRMED,
                            priceUzs = 1_120_000L,
                            seatsLeft = 9,
                            baggageInfo = tr(language, "36 kg bagaj · Nonushta", "36 kg Luggage · Breakfast", "36 кг багаж · Завтрак"),
                            amenities = listOf(
                                tr(language, "Alohida kupe", "Private Coupe", "Отдельное купе"),
                                "Wi-Fi Hub",
                                tr(language, "Choy to‘plami", "Tea Service", "Чайный сервис")
                            ),
                            platformOrGate = tr(language, "4-platforma", "Platform 4", "Платформа 4")
                        )
                    )
                } else {
                    listOf(
                        TravelOption(
                            id = "rail_${originCode}_${destinationCode}_afro1",
                            mode = TravelMode.RAIL,
                            carrier = tr(language, "Afrosiyob Tezkor", "Afrosiyob High-Speed", "Афросиаб Скоростной"),
                            carrierCode = tr(language, "Poyezd #762", "Train #762", "Поезд #762"),
                            originCode = origin.code,
                            originCity = originCity,
                            destinationCode = dest.code,
                            destinationCity = destCity,
                            departureTime = "08:00",
                            arrivalTime = "10:08",
                            durationText = tr(language, "2s 08d", "2h 08m", "2ч 08м"),
                            stopsText = tr(language, "To‘g‘ri · 250 km/soat", "Direct · 250 km/h", "Прямой · 250 км/ч"),
                            isDirect = true,
                            isMorning = true,
                            fareClass = "Afrosiyob Bistro",
                            statusTag = BookingStatusTag.PRIORITY,
                            priceUzs = 245_000L + (seed % 3) * 25_000L,
                            seatsLeft = 14,
                            baggageInfo = tr(language, "2×20 kg qo‘l yuki", "2×20 kg Carry-On", "2×20 кг ручная кладь"),
                            amenities = listOf(
                                tr(language, "Bistro taomi", "Bistro Meal", "Бистро-обед"),
                                tr(language, "Elektr rozetka", "Power Outlets", "Розетки"),
                                "5G Yo‘lak"
                            ),
                            platformOrGate = tr(language, "2-platforma", "Platform 2", "Платформа 2")
                        ),
                        TravelOption(
                            id = "rail_${originCode}_${destinationCode}_afro_vip",
                            mode = TravelMode.RAIL,
                            carrier = tr(language, "Afrosiyob Tezkor", "Afrosiyob High-Speed", "Афросиаб Скоростной"),
                            carrierCode = tr(language, "Poyezd #764", "Train #764", "Поезд #764"),
                            originCode = origin.code,
                            originCity = originCity,
                            destinationCode = dest.code,
                            destinationCity = destCity,
                            departureTime = "08:30",
                            arrivalTime = "10:42",
                            durationText = tr(language, "2s 12d", "2h 12m", "2ч 12м"),
                            stopsText = tr(language, "To‘g‘ridan-to‘g‘ri", "Direct", "Прямой"),
                            isDirect = true,
                            isMorning = true,
                            fareClass = "VIP Lounge",
                            statusTag = BookingStatusTag.DELAYED,
                            priceUzs = 490_000L,
                            seatsLeft = 3,
                            baggageInfo = tr(language, "35 kg + Vokzal zalı", "35 kg + Station Lounge", "35 кг + Лаунж вокзала"),
                            amenities = listOf(
                                tr(language, "Keng o‘rindiq", "Leather Recliner", "Кожаное кресло"),
                                tr(language, "Issiq nonushta", "Hot Breakfast", "Горячий завтрак"),
                                tr(language, "Tezkor kirish", "Priority Gate", "Приоритетный выход")
                            ),
                            platformOrGate = tr(language, "1-platforma", "Platform 1", "Платформа 1")
                        ),
                        TravelOption(
                            id = "rail_${originCode}_${destinationCode}_afro_eve",
                            mode = TravelMode.RAIL,
                            carrier = tr(language, "Afrosiyob Tezkor", "Afrosiyob High-Speed", "Афросиаб Скоростной"),
                            carrierCode = tr(language, "Poyezd #766", "Train #766", "Поезд #766"),
                            originCode = origin.code,
                            originCity = originCity,
                            destinationCode = dest.code,
                            destinationCity = destCity,
                            departureTime = "18:50",
                            arrivalTime = "20:58",
                            durationText = tr(language, "2s 08d", "2h 08m", "2ч 08м"),
                            stopsText = tr(language, "To‘g‘ridan-to‘g‘ri", "Direct", "Прямой"),
                            isDirect = true,
                            isMorning = false,
                            fareClass = tr(language, "Ekonom", "Economy", "Эконом"),
                            statusTag = BookingStatusTag.CONFIRMED,
                            priceUzs = 195_000L,
                            seatsLeft = 22,
                            baggageInfo = tr(language, "20 kg standart yuk", "20 kg Standard", "20 кг стандарт"),
                            amenities = listOf(
                                tr(language, "Konditsioner", "Climate Control", "Климат-контроль"),
                                "USB-C",
                                tr(language, "Tinch vagon", "Quiet Coach", "Тихий вагон")
                            ),
                            platformOrGate = tr(language, "3-platforma", "Platform 3", "Платформа 3")
                        ),
                        TravelOption(
                            id = "rail_${originCode}_${destinationCode}_sharq",
                            mode = TravelMode.RAIL,
                            carrier = "Sharq Express",
                            carrierCode = tr(language, "Poyezd #010", "Train #010", "Поезд #010"),
                            originCode = origin.code,
                            originCity = originCity,
                            destinationCode = dest.code,
                            destinationCity = destCity,
                            departureTime = "09:15",
                            arrivalTime = "12:55",
                            durationText = tr(language, "3s 40d", "3h 40m", "3ч 40м"),
                            stopsText = tr(language, "1 bekat · Jizzax", "1 stop · Jizzax", "1 остановка · Джизак"),
                            isDirect = false,
                            isMorning = true,
                            fareClass = tr(language, "Biznes klass", "Business Class", "Бизнес-класс"),
                            statusTag = BookingStatusTag.CONFIRMED,
                            priceUzs = 165_000L,
                            seatsLeft = 18,
                            baggageInfo = tr(language, "25 kg bagaj kiritilgan", "25 kg Included", "25 кг включено"),
                            amenities = listOf(
                                tr(language, "Restoran vagon", "Dining Car", "Вагон-ресторан"),
                                tr(language, "Panoramik oynalar", "Panoramic Windows", "Панорамные окна")
                            ),
                            platformOrGate = tr(language, "5-platforma", "Platform 5", "Платформа 5")
                        )
                    )
                }
            }

            TravelMode.FLIGHTS -> {
                val basePrice = if (isInternational) 3_150_000L else 780_000L
                val duration = if (isInternational) {
                    tr(language, "4s 55d", "4h 55m", "4ч 55м")
                } else {
                    tr(language, "1s 05d", "1h 05m", "1ч 05м")
                }
                listOf(
                    TravelOption(
                        id = "flt_${originCode}_${destinationCode}_hy1",
                        mode = TravelMode.FLIGHTS,
                        carrier = "Uzbekistan Airways",
                        carrierCode = "HY 041",
                        originCode = origin.code,
                        originCity = originCity,
                        destinationCode = dest.code,
                        destinationCity = destCity,
                        departureTime = "07:20",
                        arrivalTime = if (isInternational) "12:15" else "08:25",
                        durationText = duration,
                        stopsText = tr(language, "To‘g‘ri · A320neo", "Direct · A320neo", "Прямой · A320neo"),
                        isDirect = true,
                        isMorning = true,
                        fareClass = tr(language, "Ekonom Flex", "Economy Flex", "Эконом Flex"),
                        statusTag = BookingStatusTag.CONFIRMED,
                        priceUzs = basePrice,
                        seatsLeft = 16,
                        baggageInfo = tr(language, "23 kg yuk + 8 kg qo‘l yuki", "23 kg Checked + 8 kg Cabin", "23 кг багаж + 8 кг кладь"),
                        amenities = listOf(
                            tr(language, "Issiq taom", "Hot Meal", "Горячее питание"),
                            tr(language, "Joy tanlash", "Seat Choice", "Выбор места"),
                            tr(language, "Bepul o‘zgartirish", "Free Rebook", "Бесплатный обмен")
                        ),
                        platformOrGate = tr(language, "2-terminal · A3 darvoza", "Terminal 2 · Gate A3", "Терминал 2 · Выход A3")
                    ),
                    TravelOption(
                        id = "flt_${originCode}_${destinationCode}_ca2",
                        mode = TravelMode.FLIGHTS,
                        carrier = "Centrum Air",
                        carrierCode = "C6 314",
                        originCode = origin.code,
                        originCity = originCity,
                        destinationCode = dest.code,
                        destinationCity = destCity,
                        departureTime = "10:45",
                        arrivalTime = if (isInternational) "15:50" else "11:50",
                        durationText = duration,
                        stopsText = tr(language, "To‘g‘ri · A321neo", "Direct · A321neo", "Прямой · A321neo"),
                        isDirect = true,
                        isMorning = true,
                        fareClass = tr(language, "Smart Layt", "Smart Light", "Смарт Лайт"),
                        statusTag = BookingStatusTag.DELAYED,
                        priceUzs = (basePrice * 0.84).toLong(),
                        seatsLeft = 4,
                        baggageInfo = tr(language, "Faqat 10 kg qo‘l yuki", "10 kg Cabin Only", "Только 10 кг ручная кладь"),
                        amenities = listOf(
                            tr(language, "Tezkor check-in", "Fast Check-in", "Быстрая регистрация"),
                            "USB Power"
                        ),
                        platformOrGate = tr(language, "3-terminal · C1 darvoza", "Terminal 3 · Gate C1", "Терминал 3 · Выход C1")
                    ),
                    TravelOption(
                        id = "flt_${originCode}_${destinationCode}_hy_biz",
                        mode = TravelMode.FLIGHTS,
                        carrier = "Uzbekistan Airways",
                        carrierCode = "HY 271",
                        originCode = origin.code,
                        originCity = originCity,
                        destinationCode = dest.code,
                        destinationCity = destCity,
                        departureTime = "17:10",
                        arrivalTime = if (isInternational) "22:25" else "18:15",
                        durationText = duration,
                        stopsText = tr(language, "To‘g‘ri · Boeing 787", "Direct · Boeing 787", "Прямой · Boeing 787"),
                        isDirect = true,
                        isMorning = false,
                        fareClass = tr(language, "Biznes klass", "Business Class", "Бизнес-класс"),
                        statusTag = BookingStatusTag.PRIORITY,
                        priceUzs = (basePrice * 2.15).toLong(),
                        seatsLeft = 5,
                        baggageInfo = tr(language, "2×32 kg + CIP zal", "2×32 kg + CIP Lounge", "2×32 кг + CIP-зал"),
                        amenities = listOf(
                            tr(language, "Yotadigan kreslo", "Flat-Bed Seat", "Раскладное кресло"),
                            tr(language, "CIP tez o‘tish", "CIP Fast-Track", "CIP Fast-Track"),
                            tr(language, "Mualliflik menyusi", "Gourmet Menu", "Авторское меню")
                        ),
                        platformOrGate = tr(language, "2-terminal · B1 darvoza", "Terminal 2 · Gate B1", "Терминал 2 · Выход B1")
                    ),
                    TravelOption(
                        id = "flt_${originCode}_${destinationCode}_qs",
                        mode = TravelMode.FLIGHTS,
                        carrier = "Qanot Sharq",
                        carrierCode = "HH 512",
                        originCode = origin.code,
                        originCity = originCity,
                        destinationCode = dest.code,
                        destinationCity = destCity,
                        departureTime = "13:30",
                        arrivalTime = if (isInternational) "19:45" else "15:20",
                        durationText = if (isInternational) {
                            tr(language, "6s 15d", "6h 15m", "6ч 15м")
                        } else {
                            tr(language, "1s 50d", "1h 50m", "1ч 50м")
                        },
                        stopsText = tr(language, "1 qo‘nish · Navoiy", "1 stop · Navoi", "1 пересадка · Навои"),
                        isDirect = false,
                        isMorning = false,
                        fareClass = tr(language, "Ekonom Tejamkor", "Economy Saver", "Эконом Выгодный"),
                        statusTag = BookingStatusTag.CONFIRMED,
                        priceUzs = (basePrice * 0.78).toLong(),
                        seatsLeft = 12,
                        baggageInfo = tr(language, "20 kg bagaj kiritilgan", "20 kg Checked", "20 кг багаж"),
                        amenities = listOf(
                            tr(language, "Yengil tamaddi", "Snack Box", "Перекус"),
                            tr(language, "Ustuvor chiqish", "Priority Board", "Приоритетная посадка")
                        ),
                        platformOrGate = tr(language, "2-terminal · A7 darvoza", "Terminal 2 · Gate A7", "Терминал 2 · Выход A7")
                    )
                )
            }

            TravelMode.HOTELS -> listOf(
                TravelOption(
                    id = "htl_${destinationCode}_1",
                    mode = TravelMode.HOTELS,
                    carrier = "Zarafshan Grand Plaza",
                    carrierCode = tr(language, "5★ Butik mehmonxona", "5★ Boutique", "5★ Бутик-отель"),
                    originCode = dest.code,
                    originCity = destCity,
                    destinationCode = dest.code,
                    destinationCity = tr(language, "Shahar markazi", "City Center", "Центр города"),
                    departureTime = "14:00",
                    arrivalTime = "12:00",
                    durationText = tr(language, "1 kecha · Lyuks", "1 Night · Suite", "1 ночь · Люкс"),
                    stopsText = tr(language, "Registon manzarasi · 400 m", "Registan View · 400m", "Вид на Регистан · 400 м"),
                    isDirect = true,
                    isMorning = true,
                    fareClass = "VIP Suite",
                    statusTag = BookingStatusTag.CONFIRMED,
                    priceUzs = 1_480_000L,
                    seatsLeft = 4,
                    baggageInfo = tr(language, "Nonushta + Aeroport transferi", "Breakfast + Airport Shuttle", "Завтрак + Трансфер"),
                    amenities = listOf(
                        tr(language, "Tom terassasi", "Rooftop Terrace", "Терраса на крыше"),
                        tr(language, "Spa basseyn", "Spa Pool", "Спа-бассейн"),
                        tr(language, "Kech chiqish", "Late Check-Out", "Поздний выезд")
                    ),
                    platformOrGate = tr(language, "Xona turi · Deluxe King", "Room Category · Deluxe King", "Номер · Deluxe King"),
                    hasSeatMap = false
                ),
                TravelOption(
                    id = "htl_${destinationCode}_2",
                    mode = TravelMode.HOTELS,
                    carrier = "Silk Road Indigo Suites",
                    carrierCode = tr(language, "4★ Zamonaviy", "4★ Modern", "4★ Современный"),
                    originCode = dest.code,
                    originCity = destCity,
                    destinationCode = dest.code,
                    destinationCity = tr(language, "Vokzal hududi", "Rail District", "Район вокзала"),
                    departureTime = "14:00",
                    arrivalTime = "12:00",
                    durationText = tr(language, "1 kecha · King", "1 Night · King", "1 ночь · King"),
                    stopsText = tr(language, "Afrosiyobdan 5 daqiqa", "5 min from Afrosiyob", "5 мин от Афросиаба"),
                    isDirect = true,
                    isMorning = true,
                    fareClass = tr(language, "Biznes xona", "Business Room", "Бизнес-номер"),
                    statusTag = BookingStatusTag.PRIORITY,
                    priceUzs = 940_000L,
                    seatsLeft = 9,
                    baggageInfo = tr(language, "Bepul bekor qilish · Nonushta", "Free Cancellation · Breakfast", "Бесплатная отмена · Завтрак"),
                    amenities = listOf(
                        "Fiber Wi-Fi",
                        tr(language, "Kovorking zali", "Co-Work Lounge", "Коворкинг"),
                        "Uzcard / Humo"
                    ),
                    platformOrGate = tr(language, "Xona turi · Executive", "Room Category · Executive", "Номер · Executive"),
                    hasSeatMap = false
                )
            )

            TravelMode.TOURS -> listOf(
                TravelOption(
                    id = "tour_${destinationCode}_1",
                    mode = TravelMode.TOURS,
                    carrier = "Silk Road Heritage Tours",
                    carrierCode = tr(language, "VIP Gid #SR-08", "VIP Guide #SR-08", "VIP Гид #SR-08"),
                    originCode = origin.code,
                    originCity = originCity,
                    destinationCode = dest.code,
                    destinationCity = destCity,
                    departureTime = "09:00",
                    arrivalTime = "18:00",
                    durationText = tr(language, "1 kun · Gid bilan", "1 Day · Guided", "1 день · С гидом"),
                    stopsText = tr(language, "Obidalar + Afrosiyob kiritilgan", "Monuments + Afrosiyob Included", "Памятники + Афросиаб"),
                    isDirect = true,
                    isMorning = true,
                    fareClass = "VIP Tour",
                    statusTag = BookingStatusTag.CONFIRMED,
                    priceUzs = 890_000L,
                    seatsLeft = 6,
                    baggageInfo = tr(language, "Milliy tushlik + Muzey chiptalari", "National Lunch + Museum Tickets", "Национальный обед + Музеи"),
                    amenities = listOf(
                        tr(language, "Sertifikatli gid", "Certified Guide", "Лицензированный гид"),
                        tr(language, "Transfer", "Transfer", "Трансфер"),
                        tr(language, "Foto-tur", "Photo Tour", "Фото-тур")
                    ),
                    platformOrGate = tr(language, "Uchrashuv · Vokzal oldi", "Meetup · Station Front", "Встреча · У вокзала"),
                    hasSeatMap = false
                )
            )

            TravelMode.ESIM -> listOf(
                TravelOption(
                    id = "esim_${destinationCode}_10gb",
                    mode = TravelMode.ESIM,
                    carrier = "SafarGo Global eSIM",
                    carrierCode = tr(language, "10 GB · 15 kun", "10 GB · 15 Days", "10 ГБ · 15 дней"),
                    originCode = "UZB",
                    originCity = tr(language, "140+ davlat", "140+ Countries", "140+ стран"),
                    destinationCode = dest.code,
                    destinationCity = destCity,
                    departureTime = "00:00",
                    arrivalTime = tr(language, "15 kun", "15 Days", "15 дней"),
                    durationText = "5G / LTE",
                    stopsText = tr(language, "Darhol QR faollashtirish", "Instant QR Activation", "Мгновенная QR-активация"),
                    isDirect = true,
                    isMorning = true,
                    fareClass = "Roam Unlimited",
                    statusTag = BookingStatusTag.PRIORITY,
                    priceUzs = 145_000L,
                    seatsLeft = 99,
                    baggageInfo = tr(language, "Messenjerlar uchun qulay trafik", "Zero-rated Messenger Data", "Выгодный трафик для мессенджеров"),
                    amenities = listOf(
                        tr(language, "QR o‘rnatish", "QR Setup", "QR-установка"),
                        "Hotspot",
                        tr(language, "24/7 yordam", "24/7 Support", "Поддержка 24/7")
                    ),
                    platformOrGate = tr(language, "Raqamli eSIM profil", "Digital eSIM Profile", "Цифровой профиль eSIM"),
                    hasSeatMap = false
                ),
                TravelOption(
                    id = "esim_${destinationCode}_25gb",
                    mode = TravelMode.ESIM,
                    carrier = "SafarGo Global eSIM",
                    carrierCode = tr(language, "25 GB · 30 kun", "25 GB · 30 Days", "25 ГБ · 30 дней"),
                    originCode = "UZB",
                    originCity = tr(language, "140+ davlat", "140+ Countries", "140+ стран"),
                    destinationCode = dest.code,
                    destinationCity = destCity,
                    departureTime = "00:00",
                    arrivalTime = tr(language, "30 kun", "30 Days", "30 дней"),
                    durationText = "5G Pro",
                    stopsText = tr(language, "Turkiya, BAA, Yevropa, Qozog‘iston", "Türkiye, UAE, Europe, Kazakhstan", "Турция, ОАЭ, Европа, Казахстан"),
                    isDirect = true,
                    isMorning = true,
                    fareClass = "Business Data",
                    statusTag = BookingStatusTag.CONFIRMED,
                    priceUzs = 285_000L,
                    seatsLeft = 99,
                    baggageInfo = tr(language, "Cheksiz messenjerlar + Qo‘ng‘iroq", "Unlimited Messengers + Calls", "Безлимитные мессенджеры + Звонки"),
                    amenities = listOf(
                        tr(language, "Darhol QR", "Instant QR", "Быстрый QR"),
                        "Uzcard / Humo"
                    ),
                    platformOrGate = tr(language, "Raqamli eSIM profil", "Digital eSIM Profile", "Цифровой профиль eSIM"),
                    hasSeatMap = false
                )
            )

            TravelMode.TRANSFERS -> listOf(
                TravelOption(
                    id = "trn_${originCode}_${destinationCode}_1",
                    mode = TravelMode.TRANSFERS,
                    carrier = "SafarGo Black Sedan",
                    carrierCode = "BYD Han EV",
                    originCode = origin.code,
                    originCity = originCity,
                    destinationCode = dest.code,
                    destinationCity = destCity,
                    departureTime = "09:00",
                    arrivalTime = "12:15",
                    durationText = tr(language, "3s 15d", "3h 15m", "3ч 15м"),
                    stopsText = tr(language, "Manzildan manzilga to‘g‘ri", "Door-to-Door Direct", "От двери до двери"),
                    isDirect = true,
                    isMorning = true,
                    fareClass = tr(language, "Biznes klass", "Business Class", "Бизнес-класс"),
                    statusTag = BookingStatusTag.CONFIRMED,
                    priceUzs = 680_000L,
                    seatsLeft = 3,
                    baggageInfo = tr(language, "3 ta chamadon + Ichimlik suvi", "3 Suitcases + Bottled Water", "3 чемодана + Вода"),
                    amenities = listOf(
                        tr(language, "Malakali haydovchi", "Licensed Chauffeur", "Опытный водитель"),
                        "M39 Trassa",
                        tr(language, "Konditsioner va Wi-Fi", "AC & Wi-Fi", "Кондиционер и Wi-Fi")
                    ),
                    platformOrGate = tr(language, "Kutib olish · Terminal chiqishi", "Pickup · Terminal Exit", "Подача · Выход терминала"),
                    hasSeatMap = false
                ),
                TravelOption(
                    id = "trn_${originCode}_${destinationCode}_2",
                    mode = TravelMode.TRANSFERS,
                    carrier = "Silk Executive Minivan",
                    carrierCode = "Mercedes V-Class",
                    originCode = origin.code,
                    originCity = originCity,
                    destinationCode = dest.code,
                    destinationCity = destCity,
                    departureTime = "13:00",
                    arrivalTime = "16:20",
                    durationText = tr(language, "3s 20d", "3h 20m", "3ч 20м"),
                    stopsText = tr(language, "6 yo‘lovchigacha", "Up to 6 Passengers", "До 6 пассажиров"),
                    isDirect = true,
                    isMorning = false,
                    fareClass = "VIP Van",
                    statusTag = BookingStatusTag.PRIORITY,
                    priceUzs = 1_190_000L,
                    seatsLeft = 2,
                    baggageInfo = tr(language, "6 ta katta yuk · Bolalar o‘rindig‘i", "6 Large Bags · Child Seat", "6 больших сумок · Детское кресло"),
                    amenities = listOf(
                        tr(language, "Qulay kreslolar", "Leather Captain Chairs", "Кожаные кресла"),
                        tr(language, "USB-C portlar", "USB-C Ports", "Порты USB-C")
                    ),
                    platformOrGate = tr(language, "Kutib olish · Mehmonxona foyesi", "Pickup · Hotel Lobby", "Подача · Лобби отеля"),
                    hasSeatMap = false
                )
            )
        }
    }

    fun generateSeatMap(option: TravelOption): List<SeatItem> {
        val cols = listOf("A", "B", "C", "D")
        val occupiedSet = setOf("1B", "2C", "3A", "3D", "4B", "5C")
        val seats = mutableListOf<SeatItem>()
        for (row in 1..6) {
            for (col in cols) {
                val code = "${row.toString().padStart(2, '0')}$col"
                val key = "$row$col"
                val isVip = row <= 2
                seats.add(
                    SeatItem(
                        code = code,
                        row = row,
                        col = col,
                        isOccupied = key in occupiedSet,
                        isVipOrExtraLegroom = isVip,
                        extraPriceUzs = if (isVip) 65_000L else 0L
                    )
                )
            }
        }
        return seats
    }
}
