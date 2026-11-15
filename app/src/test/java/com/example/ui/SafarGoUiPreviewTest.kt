package com.example.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.example.data.model.AppLanguage
import com.example.data.model.BookingDoc
import com.example.data.model.BookingStatusTag
import com.example.data.model.CurrencyCode
import com.example.data.model.TravelMode
import com.example.data.model.UserProfileDoc
import com.example.data.model.stringsFor
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.screens.AuthGateScreen
import com.example.ui.screens.ExploreCategory
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RegistrationProfileScreen
import com.example.ui.theme.SafarGoTheme
import com.example.ui.viewmodel.SearchUiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.data.local.UserPreferences
import com.example.data.model.ThemePreference
import com.example.ui.screens.ProfileScreen
import com.example.ui.viewmodel.AdminPanelUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SafarGoUiPreviewTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun authGate_initialGoogleState_isCompactWithoutHype() {
        composeRule.setContent {
            SafarGoTheme(darkTheme = false) {
                AuthGateScreen(
                    language = AppLanguage.UZ,
                    onCycleLanguage = {},
                    onAuthSuccess = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithText("SafarGo’ga kiring").assertIsDisplayed()
        composeRule.onNodeWithTag("auth_tab_login_password").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("auth_tab_phone").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("auth_tab_telegram").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("google_sign_in_button").performScrollTo().assertIsDisplayed()

        assertEquals(0, composeRule.onAllNodesWithText("25 000", substring = true).fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("Firestore", substring = true).fetchSemanticsNodes().size)
    }

    @Test
    fun authGate_loginPasswordTab_rendersRealFormAndAllModesWithoutGoogleRedirect() {
        composeRule.setContent {
            SafarGoTheme(darkTheme = false) {
                AuthGateScreen(
                    language = AppLanguage.UZ,
                    onCycleLanguage = {},
                    onAuthSuccess = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("auth_tab_login_password").performScrollTo().performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("auth_login_email_input").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("auth_login_password_input").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("auth_login_password_toggle").assertIsDisplayed()
        composeRule.onNodeWithTag("auth_forgot_password_button").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("auth_login_submit_button").performScrollTo().assertIsDisplayed()

        // Ensure no Google button and no technical Firebase/Custom Claims jargon in Login/Parol tab
        composeRule.onNodeWithTag("google_sign_in_button").assertDoesNotExist()
        assertEquals(0, composeRule.onAllNodesWithText("Custom Claims", substring = true).fetchSemanticsNodes().size)
        assertEquals(0, composeRule.onAllNodesWithText("SHA-256", substring = true).fetchSemanticsNodes().size)

        // Password starts masked, toggle reveals it
        composeRule.onNode(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.Password)
        ).assertExists()
        composeRule.onNodeWithTag("auth_login_password_toggle").performScrollTo().performClick()
        composeRule.waitForIdle()
        assertEquals(
            0,
            composeRule.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.Password)).fetchSemanticsNodes().size
        )

        // Invalid email triggers local inline error
        composeRule.onNodeWithTag("auth_login_email_input").performScrollTo().performTextInput("invalid")
        composeRule.onNodeWithTag("auth_login_submit_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_error_banner").performScrollTo().assertIsDisplayed()

        // Valid input triggers honest unconfigured banner without switching to Google
        composeRule.onNodeWithTag("auth_login_email_input").performScrollTo().performTextReplacement("foydalanuvchi@misol.uz")
        composeRule.onNodeWithTag("auth_login_password_input").performScrollTo().performTextReplacement("parol123")
        composeRule.onNodeWithTag("auth_login_submit_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_info_banner").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("auth_login_email_input").performScrollTo().assertIsDisplayed()

        // Switch to Create Account mode -> confirm password appears and validates mismatch
        composeRule.onNodeWithTag("login_mode_signup").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_login_confirm_password_input").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("auth_login_confirm_password_input").performTextInput("boshqa123")
        composeRule.onNodeWithTag("auth_login_submit_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_error_banner").performScrollTo().assertIsDisplayed()

        // Switch back to Sign In, open Forgot Password, and return with Back icon
        composeRule.onNodeWithTag("login_mode_signin").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_forgot_password_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Tiklash so‘rovini yuborish").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("auth_forgot_back_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_login_password_input").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun authGate_phoneAndTelegramTabs_validateAndSaveWithoutJargon() {
        var savedPhone = ""
        var savedTgHandle = ""
        var savedTgNotif = false

        composeRule.setContent {
            SafarGoTheme(darkTheme = false) {
                AuthGateScreen(
                    language = AppLanguage.UZ,
                    onSavePendingPhone = { savedPhone = it },
                    onSavePendingTelegram = { h, n ->
                        savedTgHandle = h
                        savedTgNotif = n
                    },
                    onCycleLanguage = {},
                    onAuthSuccess = {}
                )
            }
        }
        composeRule.waitForIdle()

        // Phone tab validation + save
        composeRule.onNodeWithTag("auth_tab_phone").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_phone_save_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_error_banner").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("auth_phone_input").performScrollTo().performTextReplacement("+998 90 123 45 67")
        composeRule.onNodeWithTag("auth_phone_save_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_info_banner").performScrollTo().assertIsDisplayed()
        assertEquals("+998 90 123 45 67", savedPhone)

        // Telegram tab validation + save
        composeRule.onNodeWithTag("auth_tab_telegram").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_telegram_save_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_error_banner").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("auth_telegram_input").performScrollTo().performTextInput("farrux_uz")
        composeRule.onNodeWithTag("auth_telegram_notif_switch").performScrollTo().performClick()
        composeRule.onNodeWithTag("auth_telegram_save_button").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("auth_info_banner").performScrollTo().assertIsDisplayed()
        assertEquals("@farrux_uz", savedTgHandle)
        assertTrue(savedTgNotif)
    }

    @Test
    fun authGate_liveLanguageSwitch_updatesAllVisibleCopyImmediately_andSupportsDarkGlass() {
        var lang by mutableStateOf(AppLanguage.UZ)

        composeRule.setContent {
            SafarGoTheme(darkTheme = true) {
                AuthGateScreen(
                    language = lang,
                    onCycleLanguage = {
                        lang = when (lang) {
                            AppLanguage.UZ -> AppLanguage.EN
                            AppLanguage.EN -> AppLanguage.RU
                            AppLanguage.RU -> AppLanguage.UZ
                        }
                    },
                    onAuthSuccess = {}
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithText("SafarGo’ga kiring").assertIsDisplayed()
        composeRule.onNodeWithTag("auth_tab_login_password").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Parolni unutdingizmi?").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("auth_language_toggle").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Sign in to SafarGo").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Forgot password?").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("auth_language_toggle").performScrollTo().performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Войдите в SafarGo").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Забыли пароль?").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun registrationScreen_validatesRequiredFieldsAndShowsCancel() {
        composeRule.setContent {
            SafarGoTheme(darkTheme = false) {
                RegistrationProfileScreen(
                    language = AppLanguage.UZ,
                    initialName = "",
                    isSubmitting = false,
                    errorMessage = null,
                    onSubmitRegistration = { _, _, _, _, _, _ -> }
                )
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Profilni yakunlash").assertIsDisplayed()
        composeRule.onNodeWithTag("registration_cancel_signout_button").assertIsDisplayed()

        composeRule.onNodeWithTag("registration_list")
            .performScrollToNode(hasTestTag("complete_registration_button"))
        composeRule.onNodeWithTag("complete_registration_button").performScrollTo().performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("registration_list")
            .performScrollToNode(hasTestTag("registration_error_banner"))
        composeRule.onNodeWithTag("registration_error_banner").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun homeAndExploreScreens_renderCalmCardsAndPrimaryActions() {
        composeRule.setContent {
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
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("home_list")
            .performScrollToNode(hasTestTag("home_search_bar"))
        composeRule.onNodeWithTag("home_search_bar").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("home_list")
            .performScrollToNode(hasTestTag("service_tile_flights"))
        composeRule.onNodeWithTag("service_tile_flights").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("service_tile_rail").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithTag("home_list")
            .performScrollToNode(hasTestTag("upcoming_trip_card"))
        composeRule.onNodeWithTag("upcoming_trip_card").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun homeAndExplore_liveLanguageSwitch_andDarkGlassThemeRenderCleanly() {
        var lang by mutableStateOf(AppLanguage.UZ)

        composeRule.setContent {
            SafarGoTheme(darkTheme = true) {
                SafarGoGlassBackdrop {
                    ExploreScreen(
                        category = ExploreCategory.HOTELS,
                        language = lang,
                        currency = CurrencyCode.UZS,
                        onLaunchDeal = {},
                        onBookHotelCity = {}
                    )
                }
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Mehmonxonalar va Butik uylar").performScrollTo().assertIsDisplayed()

        composeRule.runOnUiThread { lang = AppLanguage.EN }
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Hotels & Boutique Stays").performScrollTo().assertIsDisplayed()

        composeRule.runOnUiThread { lang = AppLanguage.RU }
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Отели и бутик-дома").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun profileScreen_languagePillsSwitchLive_andDarkThemeRenders() {
        var lang by mutableStateOf(AppLanguage.UZ)
        var themePref by mutableStateOf(ThemePreference.DARK)

        composeRule.setContent {
            SafarGoTheme(darkTheme = themePref == ThemePreference.DARK) {
                SafarGoGlassBackdrop {
                    ProfileScreen(
                        profile = UserProfileDoc(
                            fullName = "Farrux Alimov",
                            passportSeries = "AB 1234567",
                            phone = "+998 90 123 45 67",
                            clubPoints = 25_000L,
                            tierLabel = "VIP GOLD"
                        ),
                        bookings = emptyList(),
                        preferences = UserPreferences(
                            language = lang,
                            themePreference = themePref
                        ),
                        adminConfig = null,
                        adminState = AdminPanelUiState(),
                        isSavingProfile = false,
                        onSetCurrency = {},
                        onSetLanguage = { lang = it },
                        onSetTheme = { themePref = it },
                        onSelectPreferredPayment = {},
                        onSaveTraveler = { _, _, _, _, _ -> },
                        onRefreshAdminClaim = {},
                        onSaveAdminConfig = { _, _, _ -> },
                        onSignedOut = {}
                    )
                }
            }
        }
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Hududiy va ko‘rinish sozlamalari").assertIsDisplayed()

        composeRule.onNodeWithTag("pref_lang_EN").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Display & Regional Settings").assertIsDisplayed()

        composeRule.onNodeWithTag("pref_lang_RU").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Язык, валюта и тема").assertIsDisplayed()
    }

    @Test
    fun exploreScreen_rendersHotelsCatalog() {
        composeRule.setContent {
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
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Mehmonxonalar va Butik uylar").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("explore_list")
            .performScrollToNode(hasTestTag("hotel_card_SKD"))
        composeRule.onNodeWithTag("hotel_card_SKD").performScrollTo().assertIsDisplayed()
    }
}
