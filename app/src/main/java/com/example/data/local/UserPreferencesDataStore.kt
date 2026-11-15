package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.AppLanguage
import com.example.data.model.CurrencyCode
import com.example.data.model.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "safargo_prefs")

data class UserPreferences(
    val currency: CurrencyCode = CurrencyCode.UZS,
    val language: AppLanguage = AppLanguage.UZ,
    val themePreference: ThemePreference = ThemePreference.LIGHT,
    val savedPassengerName: String = "",
    val savedPassportSeries: String = "",
    val savedPhone: String = "",
    val preferredPaymentId: String = "uzcard",
    val pendingAuthPhone: String = "",
    val pendingTelegramHandle: String = "",
    val pendingTelegramNotifications: Boolean = false
)

class UserPreferencesDataStore(private val context: Context) {
    private object Keys {
        val CURRENCY = stringPreferencesKey("currency")
        val LANGUAGE = stringPreferencesKey("language")
        val THEME = stringPreferencesKey("theme_preference")
        val PASSENGER_NAME = stringPreferencesKey("passenger_name")
        val PASSPORT = stringPreferencesKey("passport_series")
        val PHONE = stringPreferencesKey("phone")
        val PAYMENT_ID = stringPreferencesKey("preferred_payment_id")
        val PENDING_AUTH_PHONE = stringPreferencesKey("pending_auth_phone")
        val PENDING_TELEGRAM_HANDLE = stringPreferencesKey("pending_telegram_handle")
        val PENDING_TELEGRAM_NOTIF = booleanPreferencesKey("pending_telegram_notif")
    }

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            currency = prefs[Keys.CURRENCY]?.let { runCatching { CurrencyCode.valueOf(it) }.getOrNull() }
                ?: CurrencyCode.UZS,
            language = prefs[Keys.LANGUAGE]?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() }
                ?: AppLanguage.UZ,
            themePreference = prefs[Keys.THEME]?.let { runCatching { ThemePreference.valueOf(it) }.getOrNull() }
                ?: ThemePreference.LIGHT,
            savedPassengerName = prefs[Keys.PASSENGER_NAME] ?: "",
            savedPassportSeries = prefs[Keys.PASSPORT] ?: "",
            savedPhone = prefs[Keys.PHONE] ?: "",
            preferredPaymentId = prefs[Keys.PAYMENT_ID] ?: "uzcard",
            pendingAuthPhone = prefs[Keys.PENDING_AUTH_PHONE] ?: "",
            pendingTelegramHandle = prefs[Keys.PENDING_TELEGRAM_HANDLE] ?: "",
            pendingTelegramNotifications = prefs[Keys.PENDING_TELEGRAM_NOTIF] ?: false
        )
    }

    suspend fun setCurrency(currency: CurrencyCode) {
        context.dataStore.edit { it[Keys.CURRENCY] = currency.name }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    suspend fun setThemePreference(theme: ThemePreference) {
        context.dataStore.edit { it[Keys.THEME] = theme.name }
    }

    suspend fun setPreferredPayment(paymentId: String) {
        context.dataStore.edit { it[Keys.PAYMENT_ID] = paymentId }
    }

    suspend fun saveTravelerProfile(name: String, passport: String, phone: String) {
        context.dataStore.edit {
            it[Keys.PASSENGER_NAME] = name.trim().uppercase()
            it[Keys.PASSPORT] = passport.trim().uppercase()
            it[Keys.PHONE] = phone.trim()
        }
    }

    suspend fun savePendingAuthChannels(
        phone: String? = null,
        telegramHandle: String? = null,
        telegramNotifications: Boolean? = null
    ) {
        context.dataStore.edit { prefs ->
            if (phone != null) prefs[Keys.PENDING_AUTH_PHONE] = phone.trim()
            if (telegramHandle != null) prefs[Keys.PENDING_TELEGRAM_HANDLE] = telegramHandle.trim()
            if (telegramNotifications != null) prefs[Keys.PENDING_TELEGRAM_NOTIF] = telegramNotifications
        }
    }

    suspend fun clearPendingAuthChannels() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.PENDING_AUTH_PHONE)
            prefs.remove(Keys.PENDING_TELEGRAM_HANDLE)
            prefs.remove(Keys.PENDING_TELEGRAM_NOTIF)
        }
    }
}
