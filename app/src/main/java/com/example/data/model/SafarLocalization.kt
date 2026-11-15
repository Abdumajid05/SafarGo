package com.example.data.model

import java.util.Locale

enum class L10nKey {
    // Shared & Content Descriptions
    APP_NAME,
    CD_SAFARGO_LOGO,
    CD_SWITCH_LANGUAGE,
    CD_LOGIN_MENU,
    CD_NOTIFICATIONS,
    CD_PROFILE,
    CD_BACK,
    CD_SWAP_ROUTES,
    CD_CLOSE,
    CD_BOARDING_QR,
    CD_SELECTED,
    CD_TRAVEL_MODE,
    LOADING_LABEL,
    RETRY_LABEL,
    CANCEL_LABEL,
    BACK_LABEL,
    SAVE_LABEL,
    DEMO_NOTICE,
    FROM_PRICE_PREFIX,
    TRAVELER_UNIT,
    DIRECT_LABEL,
    CONFIRMED_LABEL,
    PRIORITY_LABEL,
    DELAYED_LABEL,
    CHECKED_IN_LABEL,
    CANCELLED_LABEL,
    SEARCH_ROUTES_CTA,
    SELECT_CTA,
    PAY_AND_ISSUE_CTA,

    // Navigation & Gates
    NAV_HOME,
    NAV_HOTELS,
    NAV_TOURS,
    NAV_TRIPS,
    NAV_PROFILE,
    GATE_LOADING_PROFILE,
    GATE_SIGN_IN_AGAIN,
    NOTIF_SAMPLE_CHECKIN,
    SUPPORT_SAMPLE_CONTACT,

    // Auth Screen
    AUTH_TITLE,
    AUTH_SUBTITLE,
    AUTH_CHOOSE_METHOD,
    AUTH_METHOD_GOOGLE,
    AUTH_METHOD_LOGIN_PASS,
    AUTH_METHOD_PHONE,
    AUTH_METHOD_TELEGRAM,
    AUTH_GOOGLE_HINT,
    AUTH_GOOGLE_CTA,
    AUTH_SIGN_IN,
    AUTH_CREATE_ACCOUNT,
    AUTH_FORGOT_PASSWORD,
    AUTH_FORGOT_BACK_CD,
    AUTH_LOGIN_UNCONFIGURED_NOTICE,
    AUTH_EMAIL_LABEL,
    AUTH_EMAIL_PLACEHOLDER,
    AUTH_PASSWORD_LABEL,
    AUTH_PASSWORD_PLACEHOLDER,
    AUTH_CONFIRM_PASSWORD_LABEL,
    AUTH_CONFIRM_PASSWORD_PLACEHOLDER,
    AUTH_SHOW_PASSWORD_CD,
    AUTH_HIDE_PASSWORD_CD,
    AUTH_SEND_RESET_CTA,
    AUTH_ERR_INVALID_EMAIL_SIGNIN,
    AUTH_ERR_INVALID_EMAIL_SIGNUP,
    AUTH_ERR_INVALID_EMAIL_RESET,
    AUTH_ERR_SHORT_PASSWORD,
    AUTH_ERR_PASSWORD_MISMATCH,
    AUTH_INFO_SIGNIN_UNCONFIGURED,
    AUTH_INFO_SIGNUP_UNCONFIGURED,
    AUTH_INFO_RESET_UNCONFIGURED,
    AUTH_PHONE_UNCONFIGURED_NOTICE,
    AUTH_PHONE_LABEL,
    AUTH_PHONE_PLACEHOLDER,
    AUTH_PHONE_SAVE_CTA,
    AUTH_ERR_INVALID_PHONE,
    AUTH_INFO_PHONE_SAVED,
    AUTH_TELEGRAM_UNCONFIGURED_BADGE,
    AUTH_TELEGRAM_UNCONFIGURED_NOTICE,
    AUTH_TELEGRAM_USERNAME_LABEL,
    AUTH_TELEGRAM_USERNAME_PLACEHOLDER,
    AUTH_TELEGRAM_NOTIF_PREF,
    AUTH_TELEGRAM_SAVE_CTA,
    AUTH_OPEN_BOT_CTA,
    AUTH_ERR_INVALID_TELEGRAM,
    AUTH_INFO_TELEGRAM_SAVED,
    AUTH_INFO_BOT_OPENED,
    AUTH_ERR_BOT_NOT_FOUND,
    AUTH_ERR_MISSING_GOOGLE_CONFIG,
    AUTH_ERR_NO_ACTIVE_ACTIVITY,
    AUTH_ERR_CREDENTIAL_FAILED,
    AUTH_ERR_SIGNIN_GENERIC,

    // Registration Profile Screen
    REG_TITLE,
    REG_CANCEL,
    REG_SUBTITLE,
    REG_PERSONAL_SECTION,
    REG_FULLNAME_LABEL,
    REG_FULLNAME_PLACEHOLDER,
    REG_PASSPORT_LABEL,
    REG_PASSPORT_PLACEHOLDER,
    REG_PHONE_SECTION,
    REG_PHONE_LABEL,
    REG_TELEGRAM_OPTIONAL_BADGE,
    REG_TELEGRAM_NOTICE,
    REG_TELEGRAM_LABEL,
    REG_TELEGRAM_NOTIF_LABEL,
    REG_PAYMENT_SECTION,
    REG_SAVING,
    REG_SUBMIT_CTA,
    REG_ERR_NAME,
    REG_ERR_PHONE,
    REG_ERR_PASSPORT,
    REG_ERR_TELEGRAM,

    // Login Menu Sheet
    MENU_TITLE,
    MENU_SUBTITLE,
    MENU_ROLE_ADMIN,
    MENU_ROLE_TRAVELER,
    MENU_TAB_GOOGLE,
    MENU_TAB_PHONE,
    MENU_TAB_TELEGRAM,
    MENU_TAB_ADMIN,
    MENU_GOOGLE_ACTIVE,
    MENU_SIGN_OUT_OTHER,
    MENU_PHONE_TITLE,
    MENU_PHONE_LABEL,
    MENU_PHONE_ERR,
    MENU_TELEGRAM_CONNECTED,
    MENU_TELEGRAM_NOT_CONFIGURED,
    MENU_TELEGRAM_STATUS_CONNECTED,
    MENU_TELEGRAM_STATUS_UNSET,
    MENU_TELEGRAM_NO_USERNAME,
    MENU_OPEN_PROFILE_SETTINGS,
    MENU_ADMIN_VERIFIED,
    MENU_ADMIN_UNVERIFIED,
    MENU_ADMIN_ACTIVE_DESC,
    MENU_ADMIN_LOCKED_DESC,
    MENU_CHECK_CLAIM,
    MENU_OPEN_ADMIN_PANEL,

    // Home Screen
    HOME_GREETING_PREFIX,
    HOME_GREETING_FALLBACK,
    HOME_GREETING_SUBTITLE,
    HOME_CLUB_BALANCE_LABEL,
    HOME_CLUB_POINTS_UNIT,
    HOME_CLUB_DISCOUNT_HINT,
    HOME_SPEND_POINTS,
    HOME_SEARCH_PROMPT,
    HOME_SEARCH_FILTERS_CD,
    HOME_MAIN_SERVICES,
    HOME_ALL_LABEL,
    HOME_SERVICE_FLIGHTS,
    HOME_SERVICE_FLIGHTS_SUB,
    HOME_SERVICE_RAIL,
    HOME_SERVICE_RAIL_SUB,
    HOME_SERVICE_HOTELS,
    HOME_SERVICE_HOTELS_SUB,
    HOME_SERVICE_TOURS,
    HOME_SERVICE_TOURS_SUB,
    HOME_SERVICE_ESIM,
    HOME_SERVICE_ESIM_SUB,
    HOME_SERVICE_TRANSFERS,
    HOME_SERVICE_TRANSFERS_SUB,
    HOME_UPCOMING_TRIP,
    HOME_ONLINE_CHECKIN,
    HOME_QR_TICKET,
    HOME_PROMO_BADGE,
    HOME_PROMO_TITLE,
    HOME_PROMO_BODY,
    HOME_PROMO_CTA,
    HOME_POPULAR_TITLE,
    HOME_POPULAR_SUBTITLE,
    HOME_GUARANTEED_TITLE,
    HOME_SUPPORT_247,
    HOME_CALL_CENTER,
    HOME_HELP,
    HOME_CLUB_MODAL_DESC,
    HOME_CLUB_REDEEM_5K,
    HOME_CLUB_MIN_5K,
    HOME_FROM_LABEL,
    HOME_TO_LABEL,
    HOME_DATE_LABEL,
    HOME_PASSENGERS_LABEL,
    HOME_ECONOMY_VIP,
    HOME_DECREASE_CD,
    HOME_INCREASE_CD,
    HOME_CITY_SELECTED,
    HOME_FAVORITE_CD,
    CITY_SAMARKAND,
    CITY_BUKHARA,
    CITY_ISTANBUL,
    CITY_KHIVA,
    CITY_ALMATY,

    // Explore Screen
    EXPLORE_HOTELS_TITLE,
    EXPLORE_HOTELS_SUBTITLE,
    EXPLORE_VETTED_5STAR,
    EXPLORE_PER_NIGHT,
    EXPLORE_SELECT_ROOM,
    EXPLORE_TOURS_TITLE,
    EXPLORE_TOURS_SUBTITLE,
    EXPLORE_AFROSIYOB_SCHEDULE,
    EXPLORE_STARTING_FROM,
    EXPLORE_VIEW_ROUTES,
    EXPLORE_HOTEL_ZARAFSHAN,
    EXPLORE_HOTEL_INDIGO,
    EXPLORE_HOTEL_LABI,
    EXPLORE_DUR_2H08,
    EXPLORE_DUR_3H51,

    // Results Screen
    RESULTS_OFFERS_COUNT,
    RESULTS_FILTER_ALL,
    RESULTS_FILTER_MORNING,
    RESULTS_FILTER_BEST_PRICE,
    RESULTS_FILTER_VIP,
    RESULTS_CLEAR_FILTER_CD,
    RESULTS_EMPTY_TITLE,
    RESULTS_EMPTY_BODY,
    RESULTS_RESET_FILTERS,

    // Booking Checkout Screen
    CHECKOUT_STEP_SEAT,
    CHECKOUT_STEP_PASSENGER,
    CHECKOUT_STEP_PAYMENT,
    CHECKOUT_NO_ROUTE,
    CHECKOUT_FILL_PASSENGER_FIRST,
    CHECKOUT_TOTAL_PAYABLE,
    CHECKOUT_ENTER_PASSENGER,
    CHECKOUT_CONFIRM_SEAT,
    CHECKOUT_TO_PAYMENT,
    CHECKOUT_ISSUING_TICKET,
    CHECKOUT_SELECT_PAYMENT,
    CHECKOUT_CLUB_REDEEM_TITLE,
    CHECKOUT_CLUB_REDEEM_SUB,
    CHECKOUT_CLUB_APPLIED,
    CHECKOUT_MANIFEST_SUMMARY,
    CHECKOUT_ROW_PASSENGER,
    CHECKOUT_ROW_PASSPORT,
    CHECKOUT_ROW_SEAT,
    CHECKOUT_ROW_BASE_FARE,
    CHECKOUT_ROW_VIP_SEAT,
    CHECKOUT_SEATMAP_RAIL,
    CHECKOUT_SEATMAP_CABIN,
    CHECKOUT_SEATMAP_VIP_HINT,
    CHECKOUT_LEGEND_FREE,
    CHECKOUT_LEGEND_SELECTED,
    CHECKOUT_LEGEND_TAKEN,
    CHECKOUT_PASSENGER_DETAILS,
    CHECKOUT_USE_PROFILE,
    CHECKOUT_FULLNAME_UPPER_LABEL,
    CHECKOUT_FULLNAME_UPPER_HINT,
    CHECKOUT_PASSPORT_UPPER_LABEL,
    CHECKOUT_PHONE_UPPER_LABEL,
    CHECKOUT_SUITE_PRIORITY,

    // Trips Screen
    TRIPS_UPCOMING_TAB,
    TRIPS_COMPLETED_TAB,
    TRIPS_ALL_TAB,
    TRIPS_ERROR_TITLE,
    TRIPS_EMPTY_TITLE,
    TRIPS_EMPTY_BODY,

    // Ticket Detail Screen
    TICKET_TITLE,
    TICKET_LOADING,
    TICKET_NOT_FOUND,
    TICKET_LBL_PASSENGER,
    TICKET_LBL_PASSPORT,
    TICKET_LBL_PNR,
    TICKET_LBL_SEAT,
    TICKET_LBL_GATE,
    TICKET_LBL_NUMBER,
    TICKET_LBL_BAGGAGE,
    TICKET_LBL_PAYMENT,
    TICKET_LBL_TOTAL_PAID,
    TICKET_SCAN_HINT,
    TICKET_CANCEL_CTA,
    TICKET_CANCEL_DIALOG_TITLE,
    TICKET_CANCEL_DIALOG_BODY,
    TICKET_CANCEL_CONFIRM,
    TICKET_CANCEL_KEEP,

    // Profile & Admin Screen
    PROFILE_FALLBACK_NAME,
    PROFILE_PASSPORT_VALUE,
    PROFILE_PASSPORT_EMPTY,
    PROFILE_CLUB_SUMMARY,
    PROFILE_DISPLAY_SETTINGS,
    PROFILE_CURRENCY,
    PROFILE_LANGUAGE,
    PROFILE_THEME,
    PROFILE_THEME_SYSTEM,
    PROFILE_THEME_LIGHT,
    PROFILE_THEME_DARK,
    PROFILE_PAYMENT_RAILS,
    PROFILE_TRAVELER_DOC,
    PROFILE_FULLNAME_LABEL,
    PROFILE_PASSPORT_LABEL,
    PROFILE_PHONE_LABEL,
    PROFILE_TELEGRAM_VERIFIED,
    PROFILE_TELEGRAM_NOT_SET,
    PROFILE_TELEGRAM_PREF_BOT,
    PROFILE_SAVE_DETAILS,
    PROFILE_SIGN_OUT,
    ADMIN_CHECKING_CLAIM,
    ADMIN_PANEL_TITLE,
    ADMIN_SERVER_REQUIRED,
    ADMIN_SECURITY_DESC,
    ADMIN_SETUP_HINT,
    ADMIN_RECHECK_CLAIM,
    ADMIN_CONSOLE_HEADER,
    ADMIN_CLAIM_ACTIVE,
    ADMIN_REFRESH_TOKEN_CD,
    ADMIN_METRIC_TOTAL,
    ADMIN_METRIC_TOTAL_VAL,
    ADMIN_METRIC_CHECKIN,
    ADMIN_METRIC_CHECKIN_VAL,
    ADMIN_METRIC_TELEGRAM,
    ADMIN_TG_CONNECTED,
    ADMIN_TG_PENDING,
    ADMIN_TG_INTEGRATION_TITLE,
    ADMIN_TG_SETUP_REQUIRED,
    ADMIN_TG_SECURITY_NOTE,
    ADMIN_TG_HANDLE_LABEL,
    ADMIN_NOTICE_LABEL,
    ADMIN_WEBHOOK_SWITCH,
    ADMIN_OPEN_BOT,

    // ViewModel Messages
    VM_ERR_PROFILE_LOAD,
    VM_ERR_TRIPS_LOAD,
    VM_ERR_TICKET_LOAD,
    VM_ADMIN_CLAIM_MISSING,
    VM_ADMIN_CLAIM_OK,
    VM_ADMIN_NO_CLAIM,
    VM_ADMIN_CONFIG_SAVED,
    VM_ADMIN_CONFIG_DENIED,
    VM_REG_ENTER_NAME,
    VM_REG_SAVED,
    VM_REG_ERR,
    VM_FAV_ERR,
    VM_CLUB_MIN_POINTS,
    VM_CLUB_REDEEMED,
    VM_CLUB_ERR,
    VM_CHECKIN_OK,
    VM_CHECKIN_ERR,
    VM_CANCEL_OK,
    VM_CANCEL_ERR,
    VM_PROFILE_SAVE_FIRST,
    VM_PAY_SAVE_ERR,
    VM_CHECKOUT_ENTER_DETAILS,
    VM_CHECKOUT_INVALID_PHONE,
    VM_CHECKOUT_SUCCESS,
    VM_CHECKOUT_ERR,
    VM_PROFILE_NAME_REQUIRED,
    VM_PROFILE_SAVED,
    VM_PROFILE_ERR,
    VM_TG_PREF_SAVED_WEBHOOK,
    VM_TG_PREF_SAVED,
    VM_TG_ERR,
    VM_SEAT_COACH_FORMAT
}

object SafarLocalization {

    private data class LocalizedTriple(
        val uz: String,
        val en: String,
        val ru: String
    ) {
        fun forLanguage(lang: AppLanguage): String = when (lang) {
            AppLanguage.UZ -> uz
            AppLanguage.EN -> en
            AppLanguage.RU -> ru
        }
    }

    private val entriesMap: Map<L10nKey, LocalizedTriple> = mapOf(
        L10nKey.APP_NAME to LocalizedTriple("SafarGo", "SafarGo", "SafarGo"),
        L10nKey.CD_SAFARGO_LOGO to LocalizedTriple("SafarGo logotipi", "SafarGo logo", "Логотип SafarGo"),
        L10nKey.CD_SWITCH_LANGUAGE to LocalizedTriple("Tilni almashtirish", "Switch language", "Сменить язык"),
        L10nKey.CD_LOGIN_MENU to LocalizedTriple("Hisob va kirish usullari", "Account and sign-in channels", "Аккаунт и способы входа"),
        L10nKey.CD_NOTIFICATIONS to LocalizedTriple("Bildirishnomalar", "Notifications", "Уведомления"),
        L10nKey.CD_PROFILE to LocalizedTriple("Profil", "Profile", "Профиль"),
        L10nKey.CD_BACK to LocalizedTriple("Orqaga qaytish", "Navigate back", "Назад"),
        L10nKey.CD_SWAP_ROUTES to LocalizedTriple("Jo‘nash va manzilni almashtirish", "Swap origin and destination", "Поменять города местами"),
        L10nKey.CD_CLOSE to LocalizedTriple("Yopish", "Close", "Закрыть"),
        L10nKey.CD_BOARDING_QR to LocalizedTriple("Chipta QR kodi", "Scannable boarding pass QR code", "QR-код посадочного талона"),
        L10nKey.CD_SELECTED to LocalizedTriple("Tanlangan", "Selected", "Выбрано"),
        L10nKey.CD_TRAVEL_MODE to LocalizedTriple("Sayohat turi", "Travel mode", "Тип поездки"),
        L10nKey.LOADING_LABEL to LocalizedTriple("Yuklanmoqda...", "Loading...", "Загрузка..."),
        L10nKey.RETRY_LABEL to LocalizedTriple("Qayta urinish", "Retry", "Повторить"),
        L10nKey.CANCEL_LABEL to LocalizedTriple("Bekor qilish", "Cancel", "Отмена"),
        L10nKey.BACK_LABEL to LocalizedTriple("Orqaga", "Back", "Назад"),
        L10nKey.SAVE_LABEL to LocalizedTriple("Saqlash", "Save", "Сохранить"),
        L10nKey.DEMO_NOTICE to LocalizedTriple(
            "Namuna katalog va sinov to‘lov tizimi — haqiqiy pul yechilmaydi.",
            "Demo catalog & simulated checkout — no real card charges occur.",
            "Демо-каталог и тестовая оплата — реальное списание средств не выполняется."
        ),
        L10nKey.FROM_PRICE_PREFIX to LocalizedTriple("dan", "from", "от"),
        L10nKey.TRAVELER_UNIT to LocalizedTriple("kishi", "pax", "чел."),
        L10nKey.DIRECT_LABEL to LocalizedTriple("To‘g‘ridan-to‘g‘ri", "Non-stop", "Прямой"),
        L10nKey.CONFIRMED_LABEL to LocalizedTriple("Tasdiqlangan", "Confirmed", "Подтверждён"),
        L10nKey.PRIORITY_LABEL to LocalizedTriple("Ustuvor", "Priority", "Приоритет"),
        L10nKey.DELAYED_LABEL to LocalizedTriple("Joy kam", "Low Seats", "Мало мест"),
        L10nKey.CHECKED_IN_LABEL to LocalizedTriple("Check-in tayyor", "Checked-In", "Регистрация OK"),
        L10nKey.CANCELLED_LABEL to LocalizedTriple("Bekor qilingan", "Cancelled", "Отменён"),
        L10nKey.SEARCH_ROUTES_CTA to LocalizedTriple("Yo‘nalishlarni izlash", "Search Routes", "Найти маршруты"),
        L10nKey.SELECT_CTA to LocalizedTriple("Tanlash", "Select", "Выбрать"),
        L10nKey.PAY_AND_ISSUE_CTA to LocalizedTriple("To‘lash va chipta olish", "Pay & Issue Ticket", "Оплатить и оформить"),

        // Navigation & Gates
        L10nKey.NAV_HOME to LocalizedTriple("Bosh sahifa", "Home", "Главная"),
        L10nKey.NAV_HOTELS to LocalizedTriple("Mehmonxona", "Stays", "Отели"),
        L10nKey.NAV_TOURS to LocalizedTriple("Turlar", "Tours", "Туры"),
        L10nKey.NAV_TRIPS to LocalizedTriple("Safarlarim", "My Trips", "Поездки"),
        L10nKey.NAV_PROFILE to LocalizedTriple("Profil", "Profile", "Профиль"),
        L10nKey.GATE_LOADING_PROFILE to LocalizedTriple("SafarGo profilingiz yuklanmoqda...", "Loading your SafarGo profile...", "Загрузка профиля SafarGo..."),
        L10nKey.GATE_SIGN_IN_AGAIN to LocalizedTriple("Qayta kirish", "Sign in again", "Войти снова"),
        L10nKey.NOTIF_SAMPLE_CHECKIN to LocalizedTriple(
            "Namuna bildirishnoma: Afrosiyob #762 va HY-023 uchun onlayn Check-in ochiq.",
            "Sample notice: Online Check-in is open for Afrosiyob #762 and HY-023.",
            "Демо-уведомление: онлайн Check-in открыт для Афросиаб #762 и HY-023."
        ),
        L10nKey.SUPPORT_SAMPLE_CONTACT to LocalizedTriple(
            "24/7 SafarGo Aloqa markazi (namuna): +998 71 200 00 00",
            "24/7 SafarGo Support (demo): +998 71 200 00 00",
            "Поддержка SafarGo 24/7 (демо): +998 71 200 00 00"
        ),

        // Auth Screen
        L10nKey.AUTH_TITLE to LocalizedTriple("SafarGo’ga kiring", "Sign in to SafarGo", "Войдите в SafarGo"),
        L10nKey.AUTH_SUBTITLE to LocalizedTriple(
            "Chiptalar, bronlar va safar tarixingizni bir joyda boshqaring.",
            "Manage your tickets, bookings, and trip history in one place.",
            "Управляйте билетами, бронированиями и поездками в одном месте."
        ),
        L10nKey.AUTH_CHOOSE_METHOD to LocalizedTriple("Kirish usulini tanlang", "Choose a sign-in method", "Выберите способ входа"),
        L10nKey.AUTH_METHOD_GOOGLE to LocalizedTriple("Google", "Google", "Google"),
        L10nKey.AUTH_METHOD_LOGIN_PASS to LocalizedTriple("Login / Parol", "Login / Pass", "Логин / Пароль"),
        L10nKey.AUTH_METHOD_PHONE to LocalizedTriple("Telefon", "Phone", "Телефон"),
        L10nKey.AUTH_METHOD_TELEGRAM to LocalizedTriple("Telegram", "Telegram", "Telegram"),
        L10nKey.AUTH_GOOGLE_HINT to LocalizedTriple(
            "Google hisobingiz bilan tezkor kiring — chiptalar va safarlar tarixi xavfsiz saqlanadi.",
            "Sign in quickly with Google to keep your tickets and trips synced safely.",
            "Быстрый вход через Google — билеты и история поездок сохраняются автоматически."
        ),
        L10nKey.AUTH_GOOGLE_CTA to LocalizedTriple("Google orqali kirish", "Continue with Google", "Войти через Google"),
        L10nKey.AUTH_SIGN_IN to LocalizedTriple("Kirish", "Sign in", "Войти"),
        L10nKey.AUTH_CREATE_ACCOUNT to LocalizedTriple("Hisob yaratish", "Create account", "Создать аккаунт"),
        L10nKey.AUTH_FORGOT_PASSWORD to LocalizedTriple("Parolni unutdingizmi?", "Forgot password?", "Забыли пароль?"),
        L10nKey.AUTH_FORGOT_BACK_CD to LocalizedTriple("Kirish shakliga qaytish", "Back to sign in", "Вернуться ко входу"),
        L10nKey.AUTH_LOGIN_UNCONFIGURED_NOTICE to LocalizedTriple(
            "Bu kirish usuli hozircha sozlanmagan. Shaklni tekshirishingiz mumkin, hisobga kirish uchun Google’dan foydalaning.",
            "This sign-in option is not set up yet. You can check the form or use Google to sign in.",
            "Этот способ входа пока не настроен. Вы можете проверить форму или войти через Google."
        ),
        L10nKey.AUTH_EMAIL_LABEL to LocalizedTriple("Email", "Email", "Email"),
        L10nKey.AUTH_EMAIL_PLACEHOLDER to LocalizedTriple("nom@misol.uz", "name@example.com", "name@example.com"),
        L10nKey.AUTH_PASSWORD_LABEL to LocalizedTriple("Parol", "Password", "Пароль"),
        L10nKey.AUTH_PASSWORD_PLACEHOLDER to LocalizedTriple("Kamida 6 ta belgi", "At least 6 characters", "Минимум 6 символов"),
        L10nKey.AUTH_CONFIRM_PASSWORD_LABEL to LocalizedTriple("Parolni tasdiqlang", "Confirm password", "Подтвердите пароль"),
        L10nKey.AUTH_CONFIRM_PASSWORD_PLACEHOLDER to LocalizedTriple("Parolni qayta kiriting", "Re-enter password", "Повторите пароль"),
        L10nKey.AUTH_SHOW_PASSWORD_CD to LocalizedTriple("Parolni ko‘rsatish", "Show password", "Показать пароль"),
        L10nKey.AUTH_HIDE_PASSWORD_CD to LocalizedTriple("Parolni yashirish", "Hide password", "Скрыть пароль"),
        L10nKey.AUTH_SEND_RESET_CTA to LocalizedTriple("Tiklash so‘rovini yuborish", "Send reset link", "Отправить ссылку"),
        L10nKey.AUTH_ERR_INVALID_EMAIL_SIGNIN to LocalizedTriple(
            "To‘g‘ri email manzilini kiriting (masalan: nom@misol.uz).",
            "Enter a valid email address (e.g. name@example.com).",
            "Введите корректный email (например: name@example.com)."
        ),
        L10nKey.AUTH_ERR_INVALID_EMAIL_SIGNUP to LocalizedTriple(
            "Ro‘yxatdan o‘tish uchun to‘g‘ri email manzilini kiriting.",
            "Enter a valid email address to create an account.",
            "Введите корректный email для регистрации."
        ),
        L10nKey.AUTH_ERR_INVALID_EMAIL_RESET to LocalizedTriple(
            "Parolni tiklash uchun email manzilingizni kiriting.",
            "Enter your email address to reset your password.",
            "Введите email для сброса пароля."
        ),
        L10nKey.AUTH_ERR_SHORT_PASSWORD to LocalizedTriple(
            "Parol kamida 6 ta belgidan iborat bo‘lishi kerak.",
            "Password must be at least 6 characters.",
            "Пароль должен содержать минимум 6 символов."
        ),
        L10nKey.AUTH_ERR_PASSWORD_MISMATCH to LocalizedTriple(
            "Parollar bir-biriga mos kelmadi. Qayta tekshiring.",
            "Passwords do not match. Please check again.",
            "Пароли не совпадают. Проверьте ещё раз."
        ),
        L10nKey.AUTH_INFO_SIGNIN_UNCONFIGURED to LocalizedTriple(
            "Login va parol orqali kirish hozircha sozlanmagan. Hisobga kirish uchun yuqoridan Google usulini tanlang.",
            "Email/password sign-in is not set up yet. Select Google above to sign in.",
            "Вход по логину и паролю пока не настроен. Выберите Google выше для входа."
        ),
        L10nKey.AUTH_INFO_SIGNUP_UNCONFIGURED to LocalizedTriple(
            "Email orqali ro‘yxatdan o‘tish hozircha sozlanmagan. Hisob yaratish uchun Google usulidan foydalaning.",
            "Email account creation is not set up yet. Use Google above to register.",
            "Регистрация по email пока не настроена. Используйте Google выше."
        ),
        L10nKey.AUTH_INFO_RESET_UNCONFIGURED to LocalizedTriple(
            "Parolni tiklash xizmati hozircha sozlanmagan. Yordam uchun Google orqali kiring.",
            "Password reset is not set up yet. Please sign in with Google.",
            "Сброс пароля пока не настроен. Войдите через Google."
        ),
        L10nKey.AUTH_PHONE_UNCONFIGURED_NOTICE to LocalizedTriple(
            "SMS kod orqali kirish hozircha sozlanmagan. Raqamingizni saqlab qo‘yishingiz mumkin.",
            "SMS code sign-in is not set up yet. You can save your number for your profile.",
            "Вход по SMS пока не настроен. Вы можете сохранить номер для профиля."
        ),
        L10nKey.AUTH_PHONE_LABEL to LocalizedTriple("Telefon raqamingiz (+998)", "Phone number (+998)", "Номер телефона (+998)"),
        L10nKey.AUTH_PHONE_PLACEHOLDER to LocalizedTriple("+998 90 000 00 00", "+998 90 000 00 00", "+998 90 000 00 00"),
        L10nKey.AUTH_PHONE_SAVE_CTA to LocalizedTriple("Telefon raqamni saqlash", "Save phone number", "Сохранить номер"),
        L10nKey.AUTH_ERR_INVALID_PHONE to LocalizedTriple(
            "Iltimos, to‘liq +998 telefon raqamini kiriting (kamida 9 ta raqam).",
            "Please enter a valid phone number (at least 9 digits).",
            "Введите корректный номер телефона (минимум 9 цифр)."
        ),
        L10nKey.AUTH_INFO_PHONE_SAVED to LocalizedTriple(
            "Raqam saqlandi! Google orqali kirganingizda profilingizga avtomatik qo‘shiladi.",
            "Phone number saved! It will prefill your profile after signing in with Google.",
            "Номер сохранён! Он автоматически подставится после входа через Google."
        ),
        L10nKey.AUTH_TELEGRAM_UNCONFIGURED_BADGE to LocalizedTriple("Sozlanmagan", "Not configured", "Не настроено"),
        L10nKey.AUTH_TELEGRAM_UNCONFIGURED_NOTICE to LocalizedTriple(
            "Telegram orqali avtomatik kirish va chipta xabarlari hozircha sozlanmagan.",
            "Automatic Telegram sign-in and bot alerts are not set up yet.",
            "Автоматический вход и уведомления через Telegram пока не настроены."
        ),
        L10nKey.AUTH_TELEGRAM_USERNAME_LABEL to LocalizedTriple("Telegram @username", "Telegram @username", "Telegram @username"),
        L10nKey.AUTH_TELEGRAM_USERNAME_PLACEHOLDER to LocalizedTriple("@username", "@username", "@username"),
        L10nKey.AUTH_TELEGRAM_NOTIF_PREF to LocalizedTriple(
            "Chipta xabarlarini Telegramda olish istagi",
            "Prefer ticket alerts on Telegram",
            "Предпочитать уведомления в Telegram"
        ),
        L10nKey.AUTH_TELEGRAM_SAVE_CTA to LocalizedTriple("Telegram @username ni saqlash", "Save Telegram @username", "Сохранить Telegram @username"),
        L10nKey.AUTH_OPEN_BOT_CTA to LocalizedTriple("%1\$s ni ochish", "Open %1\$s", "Открыть %1\$s"),
        L10nKey.AUTH_ERR_INVALID_TELEGRAM to LocalizedTriple(
            "To‘g‘ri Telegram @username kiriting (kamida 3 ta belgi).",
            "Enter a valid Telegram @username (at least 3 characters).",
            "Введите корректный Telegram @username (минимум 3 символа)."
        ),
        L10nKey.AUTH_INFO_TELEGRAM_SAVED to LocalizedTriple(
            "%1\$s saqlandi! Google orqali kirganingizda profilingizga biriktiriladi.",
            "%1\$s saved! It will prefill your profile after Google sign-in.",
            "%1\$s сохранён! Подставится в профиль после входа через Google."
        ),
        L10nKey.AUTH_INFO_BOT_OPENED to LocalizedTriple(
            "%1\$s Telegramda ochildi.",
            "Opened %1\$s in Telegram.",
            "Открыт %1\$s в Telegram."
        ),
        L10nKey.AUTH_ERR_BOT_NOT_FOUND to LocalizedTriple(
            "Telegram ilovasi yoki brauzer topilmadi.",
            "Telegram or browser app not found.",
            "Не найден Telegram или браузер."
        ),
        L10nKey.AUTH_ERR_MISSING_GOOGLE_CONFIG to LocalizedTriple(
            "Google orqali kirish sozlamasi topilmadi.",
            "Google sign-in configuration was not found.",
            "Настройка входа через Google не найдена."
        ),
        L10nKey.AUTH_ERR_NO_ACTIVE_ACTIVITY to LocalizedTriple(
            "Faol oyna topilmadi, ilovani qayta oching.",
            "Active window not found. Please reopen the app.",
            "Активное окно не найдено, перезапустите приложение."
        ),
        L10nKey.AUTH_ERR_CREDENTIAL_FAILED to LocalizedTriple(
            "Hisob ma’lumoti tasdiqlanmadi.",
            "Account credentials could not be verified.",
            "Не удалось подтвердить данные аккаунта."
        ),
        L10nKey.AUTH_ERR_SIGNIN_GENERIC to LocalizedTriple(
            "Kirishda xatolik yuz berdi.",
            "An error occurred during sign-in.",
            "Произошла ошибка при входе."
        ),

        // Registration Profile Screen
        L10nKey.REG_TITLE to LocalizedTriple("Profilni yakunlash", "Complete Profile", "Заполнение профиля"),
        L10nKey.REG_CANCEL to LocalizedTriple("Bekor qilish", "Cancel", "Отмена"),
        L10nKey.REG_SUBTITLE to LocalizedTriple(
            "Chipta rasmiylashtirishda vaqtni tejash uchun yo‘lovchi ma’lumotlarini kiriting.",
            "Enter your traveler details once for faster ticket checkout.",
            "Укажите данные пассажира для быстрого оформления билетов."
        ),
        L10nKey.REG_PERSONAL_SECTION to LocalizedTriple("Shaxsiy ma’lumotlar", "Personal Information", "Личные данные"),
        L10nKey.REG_FULLNAME_LABEL to LocalizedTriple("Ism va familiya", "Full name", "Имя и фамилия"),
        L10nKey.REG_FULLNAME_PLACEHOLDER to LocalizedTriple("Masalan: Ism Familiya", "e.g. Full Name", "Например: Имя Фамилия"),
        L10nKey.REG_PASSPORT_LABEL to LocalizedTriple("Pasport yoki ID seriyasi", "Passport or ID series", "Серия паспорта или ID"),
        L10nKey.REG_PASSPORT_PLACEHOLDER to LocalizedTriple("AA 0000000", "AA 0000000", "AA 0000000"),
        L10nKey.REG_PHONE_SECTION to LocalizedTriple("Aloqa telefoni (+998)", "Contact phone (+998)", "Контактный телефон (+998)"),
        L10nKey.REG_PHONE_LABEL to LocalizedTriple("Telefon raqam", "Phone number", "Номер телефона"),
        L10nKey.REG_TELEGRAM_OPTIONAL_BADGE to LocalizedTriple("Ixtiyoriy", "Optional", "Необязательно"),
        L10nKey.REG_TELEGRAM_NOTICE to LocalizedTriple(
            "Telegram orqali chipta xabarlari hozircha sozlanmagan, lekin @username ni saqlab qo‘yishingiz mumkin.",
            "Telegram ticket alerts are not set up yet, but you can still save your @username.",
            "Уведомления о билетах в Telegram пока не настроены, но можно сохранить @username."
        ),
        L10nKey.REG_TELEGRAM_LABEL to LocalizedTriple("Telegram @username (ixtiyoriy)", "Telegram @username (optional)", "Telegram @username (необязательно)"),
        L10nKey.REG_TELEGRAM_NOTIF_LABEL to LocalizedTriple(
            "Chipta xabarlarini Telegramda olish istagi",
            "Prefer QR ticket alerts on Telegram",
            "Предпочитать QR-билеты в Telegram"
        ),
        L10nKey.REG_PAYMENT_SECTION to LocalizedTriple("Asosiy to‘lov usuli (Namuna)", "Preferred Payment Method (Demo)", "Способ оплаты (Демо)"),
        L10nKey.REG_SAVING to LocalizedTriple("Saqlanmoqda...", "Saving...", "Сохранение..."),
        L10nKey.REG_SUBMIT_CTA to LocalizedTriple("Ro‘yxatdan o‘tishni yakunlash", "Complete registration", "Завершить регистрацию"),
        L10nKey.REG_ERR_NAME to LocalizedTriple("Iltimos, to‘liq ism va familiyangizni kiriting.", "Please enter your full name.", "Пожалуйста, введите имя и фамилию."),
        L10nKey.REG_ERR_PHONE to LocalizedTriple("Iltimos, to‘liq +998 telefon raqamini kiriting.", "Please enter a valid +998 phone number.", "Введите корректный номер +998."),
        L10nKey.REG_ERR_PASSPORT to LocalizedTriple(
            "Iltimos, pasport yoki ID seriyasini kiriting (masalan: AA 0000000).",
            "Please enter your passport or ID series (e.g. AA 0000000).",
            "Введите серию паспорта или ID (например: AA 0000000)."
        ),
        L10nKey.REG_ERR_TELEGRAM to LocalizedTriple(
            "Telegram @username kamida 3 ta belgi (harf, raqam yoki _) bo‘lishi kerak.",
            "Telegram @username must be at least 3 characters (letters, digits, or _).",
            "Telegram @username должен содержать минимум 3 символа."
        ),

        // Login Menu Sheet
        L10nKey.MENU_TITLE to LocalizedTriple("Hisob va bog‘langan usullar", "Account & Linked Channels", "Аккаунт и способы входа"),
        L10nKey.MENU_SUBTITLE to LocalizedTriple("Google, Telefon, Telegram yoki Admin holati", "Google, Phone, Telegram, or Admin status", "Статус Google, Телефона, Telegram и Админа"),
        L10nKey.MENU_ROLE_ADMIN to LocalizedTriple("Admin", "Admin", "Админ"),
        L10nKey.MENU_ROLE_TRAVELER to LocalizedTriple("Yo‘lovchi", "Traveler", "Пассажир"),
        L10nKey.MENU_TAB_GOOGLE to LocalizedTriple("Google", "Google", "Google"),
        L10nKey.MENU_TAB_PHONE to LocalizedTriple("Telefon (+998)", "Phone (+998)", "Телефон (+998)"),
        L10nKey.MENU_TAB_TELEGRAM to LocalizedTriple("Telegram", "Telegram", "Telegram"),
        L10nKey.MENU_TAB_ADMIN to LocalizedTriple("Admin huquqi", "Admin role", "Права админа"),
        L10nKey.MENU_GOOGLE_ACTIVE to LocalizedTriple("Faol Google hisobi", "Active Google account", "Активный аккаунт Google"),
        L10nKey.MENU_SIGN_OUT_OTHER to LocalizedTriple("Boshqa hisobga o‘tish / Chiqish", "Switch account / Sign out", "Сменить аккаунт / Выйти"),
        L10nKey.MENU_PHONE_TITLE to LocalizedTriple("Aloqa telefon raqami", "Contact phone number", "Контактный номер телефона"),
        L10nKey.MENU_PHONE_LABEL to LocalizedTriple("Telefon raqam (+998)", "Phone number (+998)", "Номер телефона (+998)"),
        L10nKey.MENU_PHONE_ERR to LocalizedTriple("To‘liq telefon raqamini kiriting (+998).", "Enter a complete phone number (+998).", "Введите корректный номер телефона (+998)."),
        L10nKey.MENU_TELEGRAM_CONNECTED to LocalizedTriple("Ulangan", "Connected", "Подключено"),
        L10nKey.MENU_TELEGRAM_NOT_CONFIGURED to LocalizedTriple("Sozlanmagan", "Not set up", "Не настроено"),
        L10nKey.MENU_TELEGRAM_STATUS_CONNECTED to LocalizedTriple("Ulangan: %1\$s", "Linked: %1\$s", "Привязан: %1\$s"),
        L10nKey.MENU_TELEGRAM_STATUS_UNSET to LocalizedTriple(
            "Username: %1\$s · Chipta xabarlari hozircha sozlanmagan.",
            "Username: %1\$s · Ticket alerts are not configured yet.",
            "Имя пользователя: %1\$s · Уведомления пока не настроены."
        ),
        L10nKey.MENU_TELEGRAM_NO_USERNAME to LocalizedTriple("Kiritilmagan", "Not set", "Не указано"),
        L10nKey.MENU_OPEN_PROFILE_SETTINGS to LocalizedTriple("Profil sozlamalariga o‘tish", "Open profile settings", "Перейти в настройки профиля"),
        L10nKey.MENU_ADMIN_VERIFIED to LocalizedTriple("Admin huquqi tasdiqlangan", "Admin access verified", "Права админа подтверждены"),
        L10nKey.MENU_ADMIN_UNVERIFIED to LocalizedTriple("Admin huquqi biriktirilmagan", "Admin role not assigned", "Права админа не назначены"),
        L10nKey.MENU_ADMIN_ACTIVE_DESC to LocalizedTriple(
            "Admin huquqingiz faol. Profil bo‘limida tizim sozlamalarini boshqarishingiz mumkin.",
            "Your admin role is active. Manage system settings in the Profile tab.",
            "Права администратора активны. Настройки системы доступны в Профиле."
        ),
        L10nKey.MENU_ADMIN_LOCKED_DESC to LocalizedTriple(
            "Bu hisobda boshqaruv huquqi yo‘q. Admin huquqi faqat server tomonidan beriladi.",
            "This account does not have admin privileges. Access is granted server-side only.",
            "У этого аккаунта нет прав администратора. Доступ выдаётся только на сервере."
        ),
        L10nKey.MENU_CHECK_CLAIM to LocalizedTriple("Tekshirish", "Verify", "Проверить"),
        L10nKey.MENU_OPEN_ADMIN_PANEL to LocalizedTriple("Admin panel", "Admin console", "Панель админа"),

        // Home Screen
        L10nKey.HOME_GREETING_PREFIX to LocalizedTriple("Assalomu alaykum", "Welcome back", "Здравствуйте"),
        L10nKey.HOME_GREETING_FALLBACK to LocalizedTriple("Sayohatchi", "Traveler", "Путешественник"),
        L10nKey.HOME_GREETING_SUBTITLE to LocalizedTriple(
            "Bugun qayerga safar rejalashtiramiz?",
            "Ready for your next journey across the Silk Road?",
            "Куда планируем путешествие сегодня?"
        ),
        L10nKey.HOME_CLUB_BALANCE_LABEL to LocalizedTriple("SAFARGO CLUB (NAMUNA)", "SAFARGO REWARDS (DEMO)", "БОНУСЫ SAFARGO (ДЕМО)"),
        L10nKey.HOME_CLUB_POINTS_UNIT to LocalizedTriple("ball", "pts", "баллов"),
        L10nKey.HOME_CLUB_DISCOUNT_HINT to LocalizedTriple("chegirma (namuna)", "demo voucher", "демо-скидка"),
        L10nKey.HOME_SPEND_POINTS to LocalizedTriple("Ballarni sarflash", "Redeem", "Потратить"),
        L10nKey.HOME_SEARCH_PROMPT to LocalizedTriple("Shahar, aeroport yoki mehmonxona", "City, airport or hotel stay", "Куда отправимся в путешествие?"),
        L10nKey.HOME_SEARCH_FILTERS_CD to LocalizedTriple("Qidiruv sozlamalari", "Search filters", "Фильтры поиска"),
        L10nKey.HOME_MAIN_SERVICES to LocalizedTriple("Asosiy xizmatlar", "Services & Bookings", "Сервисы поездок"),
        L10nKey.HOME_ALL_LABEL to LocalizedTriple("Hammasi", "All 12 hubs", "Все категории"),
        L10nKey.HOME_SERVICE_FLIGHTS to LocalizedTriple("Parvozlar", "Flights", "Авиабилеты"),
        L10nKey.HOME_SERVICE_FLIGHTS_SUB to LocalizedTriple("100+ aviakompaniya", "100+ airlines", "100+ компаний"),
        L10nKey.HOME_SERVICE_RAIL to LocalizedTriple("Afrosiyob", "Afrosiyob Rail", "Афросиаб"),
        L10nKey.HOME_SERVICE_RAIL_SUB to LocalizedTriple("Tezkor poyezd", "High-speed", "Скоростной"),
        L10nKey.HOME_SERVICE_HOTELS to LocalizedTriple("Mehmonxona", "Stays & Villas", "Гостиницы"),
        L10nKey.HOME_SERVICE_HOTELS_SUB to LocalizedTriple("2 500+ turar joy", "2,500+ vetted", "2 500+ мест"),
        L10nKey.HOME_SERVICE_TOURS to LocalizedTriple("Turlar", "Guided Tours", "Авторские туры"),
        L10nKey.HOME_SERVICE_TOURS_SUB to LocalizedTriple("Gid bilan sayohat", "Silk Road trips", "С гидами"),
        L10nKey.HOME_SERVICE_ESIM to LocalizedTriple("Global eSIM", "Travel eSIM", "eSIM онлайн"),
        L10nKey.HOME_SERVICE_ESIM_SUB to LocalizedTriple("140+ davlatda", "140+ countries", "140+ стран"),
        L10nKey.HOME_SERVICE_TRANSFERS to LocalizedTriple("VIP Transfer", "Transfers", "VIP Трансфер"),
        L10nKey.HOME_SERVICE_TRANSFERS_SUB to LocalizedTriple("Aeroport va vokzal", "24/7 Meet&Greet", "Аэропорт"),
        L10nKey.HOME_UPCOMING_TRIP to LocalizedTriple("Yaqinlashayotgan safar", "Upcoming Trip", "Ближайшая поездка"),
        L10nKey.HOME_ONLINE_CHECKIN to LocalizedTriple("Onlayn Check-in", "Online Check-in", "Онлайн Check-in"),
        L10nKey.HOME_QR_TICKET to LocalizedTriple("QR Chipta", "Boarding Pass", "QR Билет"),
        L10nKey.HOME_PROMO_BADGE to LocalizedTriple("Namuna taklif", "Featured Route", "Подборка маршрутов"),
        L10nKey.HOME_PROMO_TITLE to LocalizedTriple("Ipak Yo‘li yo‘nalishlari", "Explore Silk Road Journeys", "Путешествия по Шёлковому пути"),
        L10nKey.HOME_PROMO_BODY to LocalizedTriple(
            "Samarqand, Buxoro va Xiva yo‘nalishlari bo‘yicha namunaviy sayohat jadvali va tariflarini ko‘ring.",
            "Browse demo train, flight, and boutique stay routes across Samarkand, Bukhara, and Khiva.",
            "Изучите демо-маршруты и расписание поездов, авиарейсов и отелей в Самарканд, Бухару и Хиву."
        ),
        L10nKey.HOME_PROMO_CTA to LocalizedTriple("Yo‘nalishlarni ko‘rish", "Browse Routes", "Смотреть маршруты"),
        L10nKey.HOME_POPULAR_TITLE to LocalizedTriple("Ommabop yo‘nalishlar", "Trending Destinations", "Популярные направления"),
        L10nKey.HOME_POPULAR_SUBTITLE to LocalizedTriple(
            "O‘zbekiston va dunyo bo‘ylab sayohat",
            "Best regional fares & boutique stays",
            "Рекомендовано для вас на выходные"
        ),
        L10nKey.HOME_GUARANTEED_TITLE to LocalizedTriple("Kafolatlangan xizmat va to‘lovlar", "Supported Payment Rails", "Безопасная оплата 24/7"),
        L10nKey.HOME_SUPPORT_247 to LocalizedTriple("24/7 Qo‘llab-quvvatlash", "24/7 Concierge", "Поддержка онлайн"),
        L10nKey.HOME_CALL_CENTER to LocalizedTriple(
            "Aloqa markazi (namuna): +998 71 200 00 00",
            "Support line (demo): +998 71 200 00 00",
            "Колл-центр (демо): +998 71 200 00 00"
        ),
        L10nKey.HOME_HELP to LocalizedTriple("Yordam", "Help", "Помощь"),
        L10nKey.HOME_CLUB_MODAL_DESC to LocalizedTriple(
            "Joriy balans: %1\$s ball (≈ %2\$s UZS). Har bir bron uchun +500 ball keshbek beriladi (namuna).",
            "Current balance: %1\$s pts (≈ %2\$s UZS). Each demo booking earns +500 pts.",
            "Текущий баланс: %1\$s баллов (≈ %2\$s UZS). За каждое бронирование +500 баллов (демо)."
        ),
        L10nKey.HOME_CLUB_REDEEM_5K to LocalizedTriple(
            "5 000 ball → 50 000 UZS vaucher olish",
            "Redeem 5,000 pts → 50,000 UZS voucher",
            "Обменять 5 000 баллов → 50 000 UZS"
        ),
        L10nKey.HOME_CLUB_MIN_5K to LocalizedTriple("Kamida 5 000 ball zarur", "At least 5,000 pts required", "Требуется минимум 5 000 баллов"),
        L10nKey.HOME_FROM_LABEL to LocalizedTriple("Qayerdan", "Origin", "Откуда"),
        L10nKey.HOME_TO_LABEL to LocalizedTriple("Qayerga", "Destination", "Куда"),
        L10nKey.HOME_DATE_LABEL to LocalizedTriple("Jo‘nash sanasi", "Departure Date", "Дата выезда"),
        L10nKey.HOME_PASSENGERS_LABEL to LocalizedTriple("Yo‘lovchilar va klass", "Travelers & Class", "Пассажиры и класс"),
        L10nKey.HOME_ECONOMY_VIP to LocalizedTriple("Ekonom / VIP", "Economy / VIP", "Эконом / VIP"),
        L10nKey.HOME_DECREASE_CD to LocalizedTriple("Kamaytirish", "Decrease", "Уменьшить"),
        L10nKey.HOME_INCREASE_CD to LocalizedTriple("Ko‘paytirish", "Increase", "Увеличить"),
        L10nKey.HOME_CITY_SELECTED to LocalizedTriple("Tanlandi", "Selected", "Выбрано"),
        L10nKey.HOME_FAVORITE_CD to LocalizedTriple("Sevimli yo‘nalish", "Favorite destination", "Избранное направление"),
        L10nKey.CITY_SAMARKAND to LocalizedTriple("Samarqand", "Samarkand", "Самарканд"),
        L10nKey.CITY_BUKHARA to LocalizedTriple("Buxoro", "Bukhara", "Бухара"),
        L10nKey.CITY_ISTANBUL to LocalizedTriple("Istanbul", "Istanbul", "Стамбул"),
        L10nKey.CITY_KHIVA to LocalizedTriple("Xiva", "Khiva", "Хива"),
        L10nKey.CITY_ALMATY to LocalizedTriple("Olmaota", "Almaty", "Алматы"),

        // Explore Screen
        L10nKey.EXPLORE_HOTELS_TITLE to LocalizedTriple("Mehmonxonalar va Butik uylar", "Hotels & Boutique Stays", "Отели и бутик-дома"),
        L10nKey.EXPLORE_HOTELS_SUBTITLE to LocalizedTriple(
            "O‘zbekiston bo‘ylab tanlangan mehmonxonalar (namuna katalog) — darhol bron qilish imkoniyati.",
            "Curated hotels across Uzbekistan (demo catalog) with simulated checkout.",
            "Подборка отелей по Узбекистану (демо-каталог) с тестовым бронированием."
        ),
        L10nKey.EXPLORE_VETTED_5STAR to LocalizedTriple("5★ Tasdiqlangan", "5★ Vetted", "5★ Проверено"),
        L10nKey.EXPLORE_PER_NIGHT to LocalizedTriple("1 KECHA UCHUN", "PER NIGHT", "ЗА 1 НОЧЬ"),
        L10nKey.EXPLORE_SELECT_ROOM to LocalizedTriple("Xona tanlash", "Select Room", "Выбрать номер"),
        L10nKey.EXPLORE_TOURS_TITLE to LocalizedTriple("Turlar va Ipak Yo‘li yo‘nalishlari", "Guided Tours & Silk Road Routes", "Туры и маршруты Шёлкового пути"),
        L10nKey.EXPLORE_TOURS_SUBTITLE to LocalizedTriple(
            "Afrosiyob tezkor poyezdi, gid bilan sayohatlar va xalqaro reyslar (namuna jadval).",
            "Afrosiyob high-speed rail, guided tours & flights (demo schedule).",
            "Скоростной поезд Афросиаб, авторские туры и авиарейсы (демо-расписание)."
        ),
        L10nKey.EXPLORE_AFROSIYOB_SCHEDULE to LocalizedTriple(
            "Afrosiyob kundalik jadvali (namuna)",
            "Afrosiyob Daily Schedule (Demo)",
            "Расписание Афросиаб (демо)"
        ),
        L10nKey.EXPLORE_STARTING_FROM to LocalizedTriple("BOSHLANISH NARXI", "STARTING FROM", "ОТ"),
        L10nKey.EXPLORE_VIEW_ROUTES to LocalizedTriple("Yo‘nalishlar", "View Routes", "Маршруты"),
        L10nKey.EXPLORE_HOTEL_ZARAFSHAN to LocalizedTriple(
            "Zarafshan Grand Plaza · Samarqand",
            "Zarafshan Grand Plaza · Samarkand",
            "Zarafshan Grand Plaza · Самарканд"
        ),
        L10nKey.EXPLORE_HOTEL_INDIGO to LocalizedTriple(
            "Silk Road Indigo Suites · Toshkent",
            "Silk Road Indigo Suites · Tashkent",
            "Silk Road Indigo Suites · Ташкент"
        ),
        L10nKey.EXPLORE_HOTEL_LABI to LocalizedTriple(
            "Labi Hovuz Heritage · Buxoro",
            "Labi Hovuz Heritage · Bukhara",
            "Labi Hovuz Heritage · Бухара"
        ),
        L10nKey.EXPLORE_DUR_2H08 to LocalizedTriple("2s 08d", "2h 08m", "2ч 08м"),
        L10nKey.EXPLORE_DUR_3H51 to LocalizedTriple("3s 51d", "3h 51m", "3ч 51м"),

        // Results Screen
        L10nKey.RESULTS_OFFERS_COUNT to LocalizedTriple("%1\$d ta taklif", "%1\$d options", "%1\$d вариантов"),
        L10nKey.RESULTS_FILTER_ALL to LocalizedTriple("Barchasi", "All", "Все"),
        L10nKey.RESULTS_FILTER_MORNING to LocalizedTriple("Ertalabki", "Morning", "Утренние"),
        L10nKey.RESULTS_FILTER_BEST_PRICE to LocalizedTriple("Arzon narx", "Best Price", "Лучшая цена"),
        L10nKey.RESULTS_FILTER_VIP to LocalizedTriple("VIP / Biznes", "VIP / Business", "VIP / Бизнес"),
        L10nKey.RESULTS_CLEAR_FILTER_CD to LocalizedTriple("Filtrni tozalash", "Clear filter", "Сбросить фильтр"),
        L10nKey.RESULTS_EMPTY_TITLE to LocalizedTriple("Mos reyslar topilmadi", "No matching departures", "Подходящих рейсов нет"),
        L10nKey.RESULTS_EMPTY_BODY to LocalizedTriple(
            "Barcha mavjud yo‘nalishlarni ko‘rish uchun filtrni tozalang.",
            "Try clearing the active filter chip to see all available routes.",
            "Сбросьте фильтр, чтобы увидеть все доступные маршруты."
        ),
        L10nKey.RESULTS_RESET_FILTERS to LocalizedTriple("Filtrlarni tozalash", "Reset Filters", "Сбросить фильтры"),

        // Booking Checkout Screen
        L10nKey.CHECKOUT_STEP_SEAT to LocalizedTriple("1. O‘rindiq", "1. Seat", "1. Место"),
        L10nKey.CHECKOUT_STEP_PASSENGER to LocalizedTriple("2. Yo‘lovchi", "2. Traveler", "2. Пассажир"),
        L10nKey.CHECKOUT_STEP_PAYMENT to LocalizedTriple("3. To‘lov", "3. Payment", "3. Оплата"),
        L10nKey.CHECKOUT_NO_ROUTE to LocalizedTriple("Yo‘nalish tanlanmagan", "No route selected", "Маршрут не выбран"),
        L10nKey.CHECKOUT_FILL_PASSENGER_FIRST to LocalizedTriple(
            "Avval yo‘lovchi ma’lumotlarini to‘ldiring.",
            "Please complete passenger details first.",
            "Сначала заполните данные пассажира."
        ),
        L10nKey.CHECKOUT_TOTAL_PAYABLE to LocalizedTriple("Jami to‘lov", "Total Payable", "Итого к оплате"),
        L10nKey.CHECKOUT_ENTER_PASSENGER to LocalizedTriple(
            "Iltimos, yo‘lovchi ismi va pasport seriyasini kiriting.",
            "Please enter passenger name and passport ID.",
            "Введите имя пассажира и серию паспорта."
        ),
        L10nKey.CHECKOUT_CONFIRM_SEAT to LocalizedTriple("O‘rindiqni tasdiqlash", "Confirm Seat", "Подтвердить место"),
        L10nKey.CHECKOUT_TO_PAYMENT to LocalizedTriple("To‘lovga o‘tish", "To Payment", "К оплате"),
        L10nKey.CHECKOUT_ISSUING_TICKET to LocalizedTriple("Chipta rasmiylashtirilmoqda...", "Issuing Ticket...", "Оформление билета..."),
        L10nKey.CHECKOUT_SELECT_PAYMENT to LocalizedTriple(
            "To‘lov usulini tanlang (Namuna)",
            "Select Payment Rail (Demo)",
            "Выберите способ оплаты (Демо)"
        ),
        L10nKey.CHECKOUT_CLUB_REDEEM_TITLE to LocalizedTriple(
            "SafarGo Club · 5 000 ball sarflash",
            "SafarGo Club · Redeem 5,000 pts",
            "SafarGo Club · Списать 5 000 баллов"
        ),
        L10nKey.CHECKOUT_CLUB_REDEEM_SUB to LocalizedTriple(
            "%1\$s chegirma (Balans: %2\$d ball)",
            "%1\$s discount (Balance: %2\$d pts)",
            "Скидка %1\$s (Баланс: %2\$d)"
        ),
        L10nKey.CHECKOUT_CLUB_APPLIED to LocalizedTriple("Qo‘llandi", "Applied", "Применено"),
        L10nKey.CHECKOUT_MANIFEST_SUMMARY to LocalizedTriple("Buyurtma xulosasi", "Manifest Summary", "Детали заказа"),
        L10nKey.CHECKOUT_ROW_PASSENGER to LocalizedTriple("Yo‘lovchi", "Passenger", "Пассажир"),
        L10nKey.CHECKOUT_ROW_PASSPORT to LocalizedTriple("Pasport", "Passport", "Паспорт"),
        L10nKey.CHECKOUT_ROW_SEAT to LocalizedTriple("O‘rindiq", "Seat", "Место"),
        L10nKey.CHECKOUT_ROW_BASE_FARE to LocalizedTriple("Asosiy tarif", "Base Fare", "Базовый тариф"),
        L10nKey.CHECKOUT_ROW_VIP_SEAT to LocalizedTriple("VIP o‘rindiq", "VIP Seat", "VIP место"),
        L10nKey.CHECKOUT_SEATMAP_RAIL to LocalizedTriple("Afrosiyob 3-vagon · O‘rindiq xaritasi", "Afrosiyob Coach 3 · Seat Map", "Афросиаб Вагон 3 · Схема мест"),
        L10nKey.CHECKOUT_SEATMAP_CABIN to LocalizedTriple("Salon o‘rindiqlarini tanlash", "Cabin Seat Selection", "Выбор места в салоне"),
        L10nKey.CHECKOUT_SEATMAP_VIP_HINT to LocalizedTriple(
            "1–2 qatorlar VIP keng joy (+%1\$s)",
            "Rows 1–2 VIP extra legroom (+%1\$s)",
            "Ряды 1–2 VIP больше места (+%1\$s)"
        ),
        L10nKey.CHECKOUT_LEGEND_FREE to LocalizedTriple("Bo‘sh", "Free", "Свободно"),
        L10nKey.CHECKOUT_LEGEND_SELECTED to LocalizedTriple("Tanlangan", "Selected", "Выбрано"),
        L10nKey.CHECKOUT_LEGEND_TAKEN to LocalizedTriple("Band", "Taken", "Занято"),
        L10nKey.CHECKOUT_PASSENGER_DETAILS to LocalizedTriple("Yo‘lovchi ma’lumotlari", "Passenger Details", "Данные пассажира"),
        L10nKey.CHECKOUT_USE_PROFILE to LocalizedTriple("Profildan to‘ldirish", "Use Profile", "Из профиля"),
        L10nKey.CHECKOUT_FULLNAME_UPPER_LABEL to LocalizedTriple(
            "TO‘LIQ ISM VA FAMILIYA (PASPORTDAGIDEK)",
            "FULL NAME (AS ON PASSPORT)",
            "ФАМИЛИЯ И ИМЯ (КАК В ПАСПОРТЕ)"
        ),
        L10nKey.CHECKOUT_FULLNAME_UPPER_HINT to LocalizedTriple("ISM FAMILIYA", "FULL NAME", "ИМЯ ФАМИЛИЯ"),
        L10nKey.CHECKOUT_PASSPORT_UPPER_LABEL to LocalizedTriple("PASPORT / ID SERIYASI", "PASSPORT / ID NUMBER", "СЕРИЯ ПАСПОРТА / ID"),
        L10nKey.CHECKOUT_PHONE_UPPER_LABEL to LocalizedTriple("ALOQA TELEFONI (+998)", "CONTACT PHONE (+998)", "КОНТАКТНЫЙ ТЕЛЕФОН (+998)"),
        L10nKey.CHECKOUT_SUITE_PRIORITY to LocalizedTriple("Suite / Ustuvor", "Suite / Priority", "Люкс / Приоритет"),

        // Trips Screen
        L10nKey.TRIPS_UPCOMING_TAB to LocalizedTriple("Faol", "Upcoming", "Предстоящие"),
        L10nKey.TRIPS_COMPLETED_TAB to LocalizedTriple("Yakunlangan", "Completed", "Завершённые"),
        L10nKey.TRIPS_ALL_TAB to LocalizedTriple("Barchasi", "All", "Все"),
        L10nKey.TRIPS_ERROR_TITLE to LocalizedTriple("Safarlar ro‘yxatini yuklab bo‘lmadi", "Could not load trips", "Не удалось загрузить поездки"),
        L10nKey.TRIPS_EMPTY_TITLE to LocalizedTriple("Chiptalar topilmadi", "No trips yet", "Поездок пока нет"),
        L10nKey.TRIPS_EMPTY_BODY to LocalizedTriple(
            "Yangi elektron chipta olish uchun Afrosiyob yoki reyslarni qidiring.",
            "Search Afrosiyob rail, flights, or hotels to issue your next boarding pass.",
            "Выберите рейс или билет на Афросиаб для оформления посадочного талона."
        ),

        // Ticket Detail Screen
        L10nKey.TICKET_TITLE to LocalizedTriple("Elektron QR Chipta", "Electronic Boarding Pass", "Электронный QR-билет"),
        L10nKey.TICKET_LOADING to LocalizedTriple("Chipta yuklanmoqda...", "Loading ticket...", "Загрузка билета..."),
        L10nKey.TICKET_NOT_FOUND to LocalizedTriple("Chipta topilmadi", "Ticket not found", "Билет не найден"),
        L10nKey.TICKET_LBL_PASSENGER to LocalizedTriple("YO‘LOVCHI", "PASSENGER", "ПАССАЖИР"),
        L10nKey.TICKET_LBL_PASSPORT to LocalizedTriple("PASPORT", "PASSPORT", "ПАСПОРТ"),
        L10nKey.TICKET_LBL_PNR to LocalizedTriple("PNR KOD", "PNR CODE", "PNR КОД"),
        L10nKey.TICKET_LBL_SEAT to LocalizedTriple("O‘RINDIQ / VAGON", "SEAT / COACH", "МЕСТО / ВАГОН"),
        L10nKey.TICKET_LBL_GATE to LocalizedTriple("DARVOZA / PLATFORMA", "GATE / PLATFORM", "ВЫХОД / ПЛАТФОРМА"),
        L10nKey.TICKET_LBL_NUMBER to LocalizedTriple("CHIPTA NO.", "TICKET NO.", "БИЛЕТ №"),
        L10nKey.TICKET_LBL_BAGGAGE to LocalizedTriple("YUK (BAGAJ)", "BAGGAGE", "БАГАЖ"),
        L10nKey.TICKET_LBL_PAYMENT to LocalizedTriple("TO‘LOV", "PAYMENT", "ОПЛАТА"),
        L10nKey.TICKET_LBL_TOTAL_PAID to LocalizedTriple("TO‘LANGAN SUMMA", "TOTAL PAID", "ИТОГО ОПЛАЧЕНО"),
        L10nKey.TICKET_SCAN_HINT to LocalizedTriple(
            "Darvozada / Vagonda skanerlang (namuna)",
            "Scan at Gate / Coach (demo pass)",
            "Предъявите при посадке (демо)"
        ),
        L10nKey.TICKET_CANCEL_CTA to LocalizedTriple("Chiptani bekor qilish", "Cancel Ticket", "Отменить билет"),
        L10nKey.TICKET_CANCEL_DIALOG_TITLE to LocalizedTriple("Chiptani bekor qilasizmi?", "Cancel Ticket?", "Отменить билет?"),
        L10nKey.TICKET_CANCEL_DIALOG_BODY to LocalizedTriple(
            "%1\$s raqamli namuna chipta bekor qilinadi.",
            "Demo ticket %1\$s will be cancelled.",
            "Демо-билет %1\$s будет отменён."
        ),
        L10nKey.TICKET_CANCEL_CONFIRM to LocalizedTriple("Ha, bekor qilish", "Confirm Cancel", "Да, отменить"),
        L10nKey.TICKET_CANCEL_KEEP to LocalizedTriple("Yo‘q", "Keep Ticket", "Нет"),

        // Profile & Admin Screen
        L10nKey.PROFILE_FALLBACK_NAME to LocalizedTriple("SafarGo Sayohatchi", "SafarGo Traveler", "Путешественник SafarGo"),
        L10nKey.PROFILE_PASSPORT_VALUE to LocalizedTriple("Pasport: %1\$s", "Passport: %1\$s", "Паспорт: %1\$s"),
        L10nKey.PROFILE_PASSPORT_EMPTY to LocalizedTriple("Pasport kiritilmagan", "Passport not added", "Паспорт не указан"),
        L10nKey.PROFILE_CLUB_SUMMARY to LocalizedTriple(
            "SafarGo Club %1\$s · %2\$s ball",
            "SafarGo Club %1\$s · %2\$s pts",
            "SafarGo Club %1\$s · %2\$s баллов"
        ),
        L10nKey.PROFILE_DISPLAY_SETTINGS to LocalizedTriple("Hududiy va ko‘rinish sozlamalari", "Display & Regional Settings", "Язык, валюта и тема"),
        L10nKey.PROFILE_CURRENCY to LocalizedTriple("Valyuta", "Currency", "Валюта"),
        L10nKey.PROFILE_LANGUAGE to LocalizedTriple("Ilova tili", "Interface Language", "Язык интерфейса"),
        L10nKey.PROFILE_THEME to LocalizedTriple("Mavzu (Theme)", "Appearance", "Тема оформления"),
        L10nKey.PROFILE_THEME_SYSTEM to LocalizedTriple("Tizim", "System", "Системная"),
        L10nKey.PROFILE_THEME_LIGHT to LocalizedTriple("Yorug‘", "Light", "Светлая"),
        L10nKey.PROFILE_THEME_DARK to LocalizedTriple("Tungi", "Dark", "Тёмная"),
        L10nKey.PROFILE_PAYMENT_RAILS to LocalizedTriple("To‘lov usullari (Namuna)", "Payment Rails (Demo)", "Способы оплаты (Демо)"),
        L10nKey.PROFILE_TRAVELER_DOC to LocalizedTriple("Saqlangan yo‘lovchi hujjati", "Saved Traveler Document", "Сохранённые данные пассажира"),
        L10nKey.PROFILE_FULLNAME_LABEL to LocalizedTriple("Ism va familiya", "Full name", "Имя и фамилия"),
        L10nKey.PROFILE_PASSPORT_LABEL to LocalizedTriple("Pasport seriyasi", "Passport series", "Серия паспорта"),
        L10nKey.PROFILE_PHONE_LABEL to LocalizedTriple("Telefon raqami (+998)", "Phone number (+998)", "Номер телефона (+998)"),
        L10nKey.PROFILE_TELEGRAM_VERIFIED to LocalizedTriple(
            "Telegram chat tasdiqlangan (%1\$s).",
            "Telegram chat verified (%1\$s).",
            "Telegram чат подтверждён (%1\$s)."
        ),
        L10nKey.PROFILE_TELEGRAM_NOT_SET to LocalizedTriple(
            "Telegram xabarlari hozircha sozlanmagan.",
            "Telegram alerts are not set up yet.",
            "Уведомления в Telegram пока не настроены."
        ),
        L10nKey.PROFILE_TELEGRAM_PREF_BOT to LocalizedTriple(
            "Chipta va QR kodlarni %1\$s ga yuborish istagi",
            "Prefer QR tickets via %1\$s",
            "Предпочитать QR-билеты в %1\$s"
        ),
        L10nKey.PROFILE_SAVE_DETAILS to LocalizedTriple("Ma’lumotlarni saqlash", "Save details", "Сохранить данные"),
        L10nKey.PROFILE_SIGN_OUT to LocalizedTriple("Hisobdan chiqish", "Sign Out", "Выйти из аккаунта"),
        L10nKey.ADMIN_CHECKING_CLAIM to LocalizedTriple(
            "Admin huquqi Firebase tokenida tekshirilmoqda...",
            "Verifying admin claim in Firebase token...",
            "Проверка прав администратора в Firebase..."
        ),
        L10nKey.ADMIN_PANEL_TITLE to LocalizedTriple("Admin boshqaruv paneli", "Admin Console", "Панель администратора"),
        L10nKey.ADMIN_SERVER_REQUIRED to LocalizedTriple("Server huquqi talab qilinadi", "Server authorization required", "Требуются права сервера"),
        L10nKey.ADMIN_SECURITY_DESC to LocalizedTriple(
            "Admin huquqlari faqat ilova ko‘rinishi bilan emas, balki Firebase Auth Custom Claims (admin: true) hamda Cloud Firestore Security Rules (/adminConfig) orqali serverda himoyalangan.",
            "Admin privileges are enforced server-side via Firebase Auth Custom Claims (admin: true) and Cloud Firestore Security Rules (/adminConfig).",
            "Права администратора защищены на сервере через Firebase Auth Custom Claims (admin: true) и правила Cloud Firestore (/adminConfig)."
        ),
        L10nKey.ADMIN_SETUP_HINT to LocalizedTriple(
            "Sozlash: Admin SDK orqali foydalanuvchi UID’ga { admin: true } custom claim biriktiring va quyidagi tugma bilan tokenni yangilang.",
            "Setup: Assign { admin: true } custom claim to the user UID via Admin SDK, then refresh your token below.",
            "Настройка: назначьте claim { admin: true } для UID через Admin SDK и обновите токен ниже."
        ),
        L10nKey.ADMIN_RECHECK_CLAIM to LocalizedTriple("Admin huquqini qayta tekshirish", "Re-check admin access", "Проверить права снова"),
        L10nKey.ADMIN_CONSOLE_HEADER to LocalizedTriple("SafarGo Admin Konsol", "SafarGo Admin Console", "Консоль админа SafarGo"),
        L10nKey.ADMIN_CLAIM_ACTIVE to LocalizedTriple("Firebase Custom Claim (admin=true)", "Firebase Custom Claim (admin=true)", "Firebase Custom Claim (admin=true)"),
        L10nKey.ADMIN_REFRESH_TOKEN_CD to LocalizedTriple("Tokenni yangilash", "Refresh token", "Обновить токен"),
        L10nKey.ADMIN_METRIC_TOTAL to LocalizedTriple("Jami bronlar", "Total bookings", "Всего броней"),
        L10nKey.ADMIN_METRIC_TOTAL_VAL to LocalizedTriple("%1\$d ta", "%1\$d", "%1\$d"),
        L10nKey.ADMIN_METRIC_CHECKIN to LocalizedTriple("Check-in", "Check-in", "Check-in"),
        L10nKey.ADMIN_METRIC_CHECKIN_VAL to LocalizedTriple("%1\$d ta", "%1\$d", "%1\$d"),
        L10nKey.ADMIN_METRIC_TELEGRAM to LocalizedTriple("Telegram", "Telegram", "Telegram"),
        L10nKey.ADMIN_TG_CONNECTED to LocalizedTriple("Serverda ulangan", "Server linked", "Подключён"),
        L10nKey.ADMIN_TG_PENDING to LocalizedTriple("Kutilmoqda", "Pending", "Ожидает"),
        L10nKey.ADMIN_TG_INTEGRATION_TITLE to LocalizedTriple("Telegram Bot integratsiyasi", "Telegram Bot Integration", "Интеграция Telegram-бота"),
        L10nKey.ADMIN_TG_SETUP_REQUIRED to LocalizedTriple("Sozlash talab qilinadi", "Setup required", "Требует настройки"),
        L10nKey.ADMIN_TG_SECURITY_NOTE to LocalizedTriple(
            "Bot tokeni APK yoki klient kodida saqlanmaydi. Telegram Bot API so‘rovlari faqat server-side Cloud Functions va Google Cloud Secret Manager orqali bajarilishi kerak.",
            "Bot tokens are never stored in APK or client code. Telegram Bot API requests must run server-side via Cloud Functions and Secret Manager.",
            "Токен бота не хранится в APK или клиенте. Запросы к Telegram Bot API должны выполняться на сервере через Cloud Functions и Secret Manager."
        ),
        L10nKey.ADMIN_TG_HANDLE_LABEL to LocalizedTriple("Ommaviy Telegram Bot @username", "Public Telegram Bot @username", "Публичный Telegram Bot @username"),
        L10nKey.ADMIN_NOTICE_LABEL to LocalizedTriple("Tizim xabarnomasi (ixtiyoriy)", "System notice (optional)", "Системное уведомление (необязательно)"),
        L10nKey.ADMIN_WEBHOOK_SWITCH to LocalizedTriple("Server Cloud Function webhook sozlangan", "Server Cloud Function webhook configured", "Webhook Cloud Function настроен"),
        L10nKey.ADMIN_OPEN_BOT to LocalizedTriple("Botni ochish", "Open bot", "Открыть бота"),

        // ViewModel Status & Validation
        L10nKey.VM_ERR_PROFILE_LOAD to LocalizedTriple("Profil yuklanmadi", "Failed to load profile", "Не удалось загрузить профиль"),
        L10nKey.VM_ERR_TRIPS_LOAD to LocalizedTriple("Safarlar ro‘yxati yuklanmadi", "Failed to load trips list", "Не удалось загрузить список поездок"),
        L10nKey.VM_ERR_TICKET_LOAD to LocalizedTriple("Chipta ma’lumoti yuklanmadi", "Failed to load ticket details", "Не удалось загрузить данные билета"),
        L10nKey.VM_ADMIN_CLAIM_MISSING to LocalizedTriple(
            "Firebase Auth Custom Claim (admin: true) hali biriktirilmagan.",
            "Firebase Auth Custom Claim (admin: true) is not assigned yet.",
            "Firebase Auth Custom Claim (admin: true) ещё не назначен."
        ),
        L10nKey.VM_ADMIN_CLAIM_OK to LocalizedTriple("Server admin huquqi tasdiqlandi.", "Server admin access verified.", "Права администратора подтверждены."),
        L10nKey.VM_ADMIN_NO_CLAIM to LocalizedTriple("Admin huquqi server tokenida tasdiqlanmagan.", "Admin role is not verified in server token.", "Права админа не подтверждены в токене сервера."),
        L10nKey.VM_ADMIN_CONFIG_SAVED to LocalizedTriple("Admin tizim sozlamasi Firestore’ga saqlandi.", "Admin system settings saved to Firestore.", "Системные настройки админа сохранены."),
        L10nKey.VM_ADMIN_CONFIG_DENIED to LocalizedTriple("Sozlamani saqlash rad etildi", "Failed to save settings", "Не удалось сохранить настройки"),
        L10nKey.VM_REG_ENTER_NAME to LocalizedTriple("Iltimos, ism va familiyangizni kiriting.", "Please enter your full name.", "Пожалуйста, введите имя и фамилию."),
        L10nKey.VM_REG_SAVED to LocalizedTriple("Profil muvaffaqiyatli saqlandi!", "Profile saved successfully!", "Профиль успешно сохранён!"),
        L10nKey.VM_REG_ERR to LocalizedTriple("Ro‘yxatdan o‘tishda xatolik", "Registration error", "Ошибка регистрации"),
        L10nKey.VM_FAV_ERR to LocalizedTriple("Sevimlilarni yangilashda xatolik", "Failed to update favorites", "Ошибка обновления избранного"),
        L10nKey.VM_CLUB_MIN_POINTS to LocalizedTriple(
            "Kamida %1\$d ball bo‘lishi kerak (Joriy: %2\$d ball).",
            "At least %1\$d pts required (Current: %2\$d pts).",
            "Требуется минимум %1\$d баллов (Текущий баланс: %2\$d)."
        ),
        L10nKey.VM_CLUB_REDEEMED to LocalizedTriple(
            "5 000 ball = 50 000 UZS vaucher faollashtirildi (namuna)!",
            "5,000 pts = 50,000 UZS demo voucher activated!",
            "Активирован демо-ваучер: 5 000 баллов = 50 000 UZS!"
        ),
        L10nKey.VM_CLUB_ERR to LocalizedTriple("Ballarni sarflashda xatolik", "Failed to redeem points", "Ошибка списания баллов"),
        L10nKey.VM_CHECKIN_OK to LocalizedTriple("Onlayn Check-in muvaffaqiyatli yakunlandi!", "Online Check-in completed!", "Онлайн Check-in успешно выполнен!"),
        L10nKey.VM_CHECKIN_ERR to LocalizedTriple("Check-in bajarilmadi", "Check-in failed", "Не удалось выполнить Check-in"),
        L10nKey.VM_CANCEL_OK to LocalizedTriple("Chipta bekor qilindi.", "Ticket cancelled.", "Билет отменён."),
        L10nKey.VM_CANCEL_ERR to LocalizedTriple("Bekor qilishda xatolik", "Cancellation failed", "Ошибка отмены билета"),
        L10nKey.VM_PROFILE_SAVE_FIRST to LocalizedTriple(
            "Avval Profil bo‘limida yo‘lovchi ma’lumotlarini saqlang.",
            "Save your traveler details in Profile first.",
            "Сначала сохраните данные пассажира в Профиле."
        ),
        L10nKey.VM_PAY_SAVE_ERR to LocalizedTriple("To‘lov usulini saqlashda xatolik", "Failed to save payment method", "Ошибка сохранения способа оплаты"),
        L10nKey.VM_CHECKOUT_ENTER_DETAILS to LocalizedTriple(
            "Yo‘lovchi ismi va pasport seriyasini kiriting.",
            "Enter passenger name and passport series.",
            "Введите имя пассажира и серию паспорта."
        ),
        L10nKey.VM_CHECKOUT_INVALID_PHONE to LocalizedTriple(
            "Telefon raqamini to‘liq kiriting (masalan, +998 90 123 45 67).",
            "Enter a valid phone number (e.g. +998 90 123 45 67).",
            "Введите полный номер телефона (например, +998 90 123 45 67)."
        ),
        L10nKey.VM_CHECKOUT_SUCCESS to LocalizedTriple("Namuna elektron chipta yaratildi!", "Demo electronic ticket issued!", "Демо-электронный билет оформлен!"),
        L10nKey.VM_CHECKOUT_ERR to LocalizedTriple("Chipta rasmiylashtirishda xatolik", "Failed to issue ticket", "Ошибка оформления билета"),
        L10nKey.VM_PROFILE_NAME_REQUIRED to LocalizedTriple("Ism va familiya bo‘sh bo‘lmasligi kerak.", "Full name cannot be empty.", "Имя и фамилия не могут быть пустыми."),
        L10nKey.VM_PROFILE_SAVED to LocalizedTriple("Profil va aloqa ma’lumotlari saqlandi!", "Profile and contact details saved!", "Профиль и контакты сохранены!"),
        L10nKey.VM_PROFILE_ERR to LocalizedTriple("Saqlashda xatolik", "Failed to save changes", "Ошибка сохранения"),
        L10nKey.VM_TG_PREF_SAVED_WEBHOOK to LocalizedTriple(
            "Telegram xabarnoma istagi saqlandi (server webhook sozlash talab qilinadi).",
            "Telegram alert preference saved (server webhook setup required).",
            "Настройка Telegram сохранена (требуется webhook на сервере)."
        ),
        L10nKey.VM_TG_PREF_SAVED to LocalizedTriple("Telegram sozlamasi saqlandi.", "Telegram setting saved.", "Настройка Telegram сохранена."),
        L10nKey.VM_TG_ERR to LocalizedTriple("Telegram sozlamasida xatolik", "Failed to save Telegram setting", "Ошибка настройки Telegram"),
        L10nKey.VM_SEAT_COACH_FORMAT to LocalizedTriple("Vagon 3 · %1\$s", "Coach 3 · %1\$s", "Вагон 3 · %1\$s")
    )

    fun text(lang: AppLanguage, key: L10nKey, vararg args: Any): String {
        val template = entriesMap[key]?.forLanguage(lang)
            ?: error("Missing localization key $key for language $lang")
        return if (args.isEmpty()) template else String.format(Locale.US, template, *args)
    }

    fun rawTemplate(lang: AppLanguage, key: L10nKey): String? {
        return entriesMap[key]?.forLanguage(lang)
    }
}
