# SafarGo Localization Guide (`UZ / EN / RU`)

SafarGo supports real-time switching between **Uzbek Latin (`UZ`, default)**, **English (`EN`)**, and **Russian (`RU`)**. Changing the language updates every screen, dialog, bottom bar, navigation rail, validation banner, and ViewModel status message immediately, and the preference survives app restarts.

---

## 1. Architecture

All UI copy lives in a centralized, type-safe registry rather than scattered `when (language)` blocks inside Composables:

- **`AppLanguage`** (`app/src/main/java/com/example/data/model/TravelModels.kt`):
  - `UZ("UZ", "O‘zbekcha")` *(default)*
  - `EN("EN", "English")`
  - `RU("RU", "Русский")`
- **`L10nKey` & `SafarLocalization`** (`app/src/main/java/com/example/data/model/SafarLocalization.kt`):
  - Every user-visible string and `contentDescription` corresponds to an `L10nKey` enum entry backed by a `LocalizedTriple(uz, en, ru)`.
  - Access formatted text via:
    ```kotlin
    SafarLocalization.text(language, L10nKey.RESULTS_OFFERS_COUNT, results.size)
    ```
  - Access raw templates (for testing) via `SafarLocalization.rawTemplate(language, key)`.
- **`stringsFor(lang: AppLanguage): SafarStrings`** (`TravelModels.kt`):
  - Populates shared Home/Components labels cleanly from `SafarLocalization.text(...)`.
- **Localized Catalog Models** (`TravelModels.kt` & `SafarGoRepository.kt`):
  - `CityHub.localizedName(lang)`
  - `PaymentRail.localizedSubtitle(lang)`
  - `CorridorDeal.localizedTitle(lang)`, `localizedSubtitle(lang)`, `localizedBadge(lang)`
  - `SafarGoRepository.searchRoutes(..., language = lang)` produces localized carrier/duration/baggage/amenity copy.

---

## 2. Persistence & Live Switching Points

`UserPreferencesDataStore` (`app/src/main/java/com/example/data/local/UserPreferencesDataStore.kt`) stores the active selection under key `language` in `safargo_prefs`, defaulting to `AppLanguage.UZ`.

Users can change the language from three visible locations:
1. **`AuthGateScreen` Header**: `auth_language_toggle` cycles `UZ -> EN -> RU`.
2. **Main App Top Bar (`SafarGoGlassTopBar`)**: `language_toggle_button` cycles `UZ -> EN -> RU`.
3. **Profile Screen (`ProfileScreen`)**: Direct 3-way selector pills (`pref_lang_UZ`, `pref_lang_EN`, `pref_lang_RU`).

---

## 3. Rules for Adding New Strings

1. Add a descriptive enum key to `L10nKey` in `SafarLocalization.kt` (keep prefix conventions like `AUTH_`, `HOME_`, `CHECKOUT_`, `ADMIN_`, `VM_`, `CD_`).
2. Add the corresponding `LocalizedTriple(uz = "...", en = "...", ru = "...")` entry inside `entriesMap`.
3. Use explicit positional format specifiers (`%1$s`, `%1$d`, `%2$d`) whenever interpolating arguments, and keep the exact same placeholder sequence across **UZ**, **EN**, and **RU** (important for Russian word order).
4. **Never** write hardcoded user-facing strings or inline `when (language)` text branches in `ui/` screens, components, navigation, or ViewModels.

---

## 4. Automated Verification

`SafarLocalizationTest` (`app/src/test/java/com/example/data/SafarLocalizationTest.kt`) enforces:
- Default `UserPreferences().language` and fresh `UserPreferencesDataStore` emit `AppLanguage.UZ`, and updates persist `EN` / `RU`.
- Every `L10nKey.entries` item has a non-null, non-blank string in `UZ`, `EN`, and `RU`.
- Extracted format placeholders (`%\d+\$[sd]`) match identically across all three languages for every parameterized key, and formatting with sample arguments succeeds without errors.
- `SafarCatalog` hubs, payment rails, and corridor deals return non-blank translations for all three languages.

`SafarGoUiPreviewTest` (`app/src/test/java/com/example/ui/SafarGoUiPreviewTest.kt`) verifies live `UZ -> EN -> RU` rendering in Compose UI via Robolectric.
