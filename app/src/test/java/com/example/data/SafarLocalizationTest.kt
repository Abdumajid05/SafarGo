package com.example.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.BookingDao
import com.example.data.local.BookingEntity
import com.example.data.local.UserPreferences
import com.example.data.local.UserPreferencesDataStore
import com.example.data.model.AppLanguage
import com.example.data.model.L10nKey
import com.example.data.model.SafarCatalog
import com.example.data.model.SafarLocalization
import com.example.data.model.TravelMode
import com.example.data.model.stringsFor
import com.example.data.repository.SafarGoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SafarLocalizationTest {

    private class FakeBookingDao : BookingDao {
        override fun observeAllBookings(): Flow<List<BookingEntity>> = flowOf(emptyList())
        override fun observeBookingById(bookingId: Long): Flow<BookingEntity?> = flowOf(null)
        override suspend fun getBookingCount(): Int = 0
        override suspend fun insertBooking(booking: BookingEntity): Long = 1L
        override suspend fun insertAll(bookings: List<BookingEntity>) = Unit
        override suspend fun updateBookingStatus(bookingId: Long, statusName: String) = Unit
    }

    @Test
    fun userPreferences_defaultLanguageIsUzbekLatin() {
        val defaults = UserPreferences()
        assertEquals(AppLanguage.UZ, defaults.language)
        assertEquals("UZ", defaults.language.shortLabel)
        assertEquals("O‘zbekcha", defaults.language.fullName)
    }

    @Test
    fun userPreferencesDataStore_defaultsToUzAndPersistsLanguageSwitches() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dataStore = UserPreferencesDataStore(context)

        val initial = dataStore.userPreferences.first()
        assertEquals(AppLanguage.UZ, initial.language)

        dataStore.setLanguage(AppLanguage.EN)
        assertEquals(AppLanguage.EN, dataStore.userPreferences.first().language)

        dataStore.setLanguage(AppLanguage.RU)
        assertEquals(AppLanguage.RU, dataStore.userPreferences.first().language)

        // Restore default for clean subsequent test state
        dataStore.setLanguage(AppLanguage.UZ)
        assertEquals(AppLanguage.UZ, dataStore.userPreferences.first().language)
    }

    @Test
    fun allL10nKeys_existNonBlankAcrossUzEnRu_withMatchingPlaceholders() {
        val placeholderRegex = Regex("%(\\d+)\\$([sd])")

        L10nKey.entries.forEach { key ->
            val templatesByLang = AppLanguage.entries.associateWith { lang ->
                val raw = SafarLocalization.rawTemplate(lang, key)
                assertNotNull("Missing template for key=$key lang=$lang", raw)
                assertTrue("Blank template for key=$key lang=$lang", raw!!.isNotBlank())
                raw
            }

            val placeholdersByLang = templatesByLang.mapValues { (_, template) ->
                placeholderRegex.findAll(template)
                    .map { match -> match.groupValues[1].toInt() to match.groupValues[2] }
                    .toSet()
            }

            val uzPlaceholders = placeholdersByLang.getValue(AppLanguage.UZ)
            assertEquals("Placeholder mismatch in EN for $key", uzPlaceholders, placeholdersByLang.getValue(AppLanguage.EN))
            assertEquals("Placeholder mismatch in RU for $key", uzPlaceholders, placeholdersByLang.getValue(AppLanguage.RU))

            val orderedArgs: Array<Any> = uzPlaceholders
                .sortedBy { it.first }
                .map<Pair<Int, String>, Any> { (_, type) -> if (type == "d") 5 else "Sample" }
                .toTypedArray()

            AppLanguage.entries.forEach { lang ->
                val formatted = SafarLocalization.text(lang, key, *orderedArgs)
                assertTrue("Formatted output blank for key=$key lang=$lang", formatted.isNotBlank())
                assertFalse("Unformatted placeholder leaked for key=$key lang=$lang: $formatted", formatted.contains("%1$"))
            }
        }
    }

    @Test
    fun stringsForAndCatalogAndRepository_supportAllThreeLanguages() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = SafarGoRepository(FakeBookingDao(), UserPreferencesDataStore(context))

        AppLanguage.entries.forEach { lang ->
            val strings = stringsFor(lang)
            assertTrue(strings.greetingPrefix.isNotBlank())
            assertTrue(strings.navHome.isNotBlank())
            assertTrue(strings.navHotels.isNotBlank())
            assertTrue(strings.navTours.isNotBlank())
            assertTrue(strings.navTrips.isNotBlank())
            assertTrue(strings.navProfile.isNotBlank())
            assertTrue(strings.demoNotice.isNotBlank())

            SafarCatalog.hubs.forEach { hub ->
                assertTrue("Blank hub name for ${hub.code} in $lang", hub.localizedName(lang).isNotBlank())
            }
            SafarCatalog.paymentRails.forEach { rail ->
                assertTrue("Blank rail subtitle for ${rail.id} in $lang", rail.localizedSubtitle(lang).isNotBlank())
            }
            SafarCatalog.corridorDeals.forEach { deal ->
                assertTrue("Blank deal title for ${deal.id} in $lang", deal.localizedTitle(lang).isNotBlank())
                assertTrue("Blank deal subtitle for ${deal.id} in $lang", deal.localizedSubtitle(lang).isNotBlank())
                assertTrue("Blank deal badge for ${deal.id} in $lang", deal.localizedBadge(lang).isNotBlank())
            }

            TravelMode.entries.forEach { mode ->
                val options = repo.searchRoutes(
                    mode = mode,
                    originCode = "TAS",
                    destinationCode = "SKD",
                    language = lang
                )
                assertTrue("Expected options for $mode in $lang", options.isNotEmpty())
                options.forEach { opt ->
                    assertTrue(opt.carrier.isNotBlank())
                    assertTrue(opt.durationText.isNotBlank())
                    assertTrue(opt.baggageInfo.isNotBlank())
                }
            }
        }
    }
}
