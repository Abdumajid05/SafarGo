# SafarGo

**SafarGo** is an Android mobility & travel super-app built with **Kotlin**, **Jetpack Compose (Material Design 3)**, and a unified **Glassmorphism** visual system in both Light and Dark themes. It supports **Uzbek Latin (default)**, **English**, and **Russian** (`UZ / EN / RU`) live language switching across every screen, dialog, navigation item, and status banner.

---

## Feature & Integration Status

SafarGo separates production cloud persistence from demonstration travel inventory and backend-gated authentication channels:

| Capability | Status | Details |
| --- | --- | --- |
| **Google Sign-In + Firebase Auth** | Real | Uses Jetpack `CredentialManager` (`GetSignInWithGoogleOption`) + `FirebaseAuth` (`GoogleAuthProvider`). |
| **User Profile & Bookings Sync** | Real | Persisted in Cloud Firestore (`/users/{userId}` and `/users/{userId}/bookings/{bookingId}`) with strict `firestore.rules`. |
| **Language, Currency & Theme Prefs** | Real | Persisted locally via `UserPreferencesDataStore` (`safargo_prefs`). Defaults to `AppLanguage.UZ`, `CurrencyCode.UZS`, `ThemePreference.LIGHT`. |
| **Catalog, Routes, Seats & Pricing** | Demo | Provided locally by `SafarCatalog` and `SafarGoRepository` (Afrosiyob rail, flights, hotels, guided tours, eSIM, VIP transfers). |
| **Checkout, Payment Rails & Club Points** | Demo | Simulated payment flow (`UZCARD`, `HUMO`, `Click`, `Payme`, `Uzum Pay`, `Visa/Mastercard`), demo voucher redemption, and local QR pass rendering — **no real card charges**. |
| **Email / Password (`Login / Parol`)** | Unconfigured | Complete form, password visibility toggles, create-account, and forgot-password UX with validation; displays an unconfigured status notice instead of fake login. |
| **Phone / SMS OTP (`Telefon`)** | Unconfigured | Validates and stores the user's pending `+998` phone number locally to prefill profile after Google sign-in; SMS OTP verification is not provisioned. |
| **Telegram Bot Integration** | Unconfigured | Allows saving a `@username` preference and opening the public `t.me` bot link; bot tokens are **never** embedded in APK/client code and require Cloud Functions + Secret Manager. |
| **Admin Console (`/adminConfig`)** | Server-Gated | UI & Firestore writes require a server-assigned Firebase Auth Custom Claim (`admin == true`). |

---

## Tech Stack

- **Language & Concurrency**: Kotlin, Coroutines, `StateFlow` / `Flow`
- **UI**: Jetpack Compose, Material Design 3, custom Glassmorphism primitives (`GlassPrimitives.kt`), Inter variable font (`res/font/inter.ttf`), edge-to-edge insets
- **Navigation**: Jetpack Navigation Compose with `@Serializable` type-safe routes (`SafarGoNavigation.kt`)
- **Cloud Backend**: Firebase Authentication & Cloud Firestore (`FirestoreTravelRepository.kt`, `firestore.rules`)
- **Local Persistence**: AndroidX Preferences DataStore (`UserPreferencesDataStore.kt`) & Room schema (`SafarGoDatabase.kt`)
- **Identity**: AndroidX `CredentialManager` & Google Identity Services (`googleid`)
- **Testing**: JUnit4, Robolectric (SDK 36), Compose UI Test, Firebase Local Emulator Suite (Auth & Firestore)

---

## Project Structure

```text
app/src/main/java/com/example/
├── MainActivity.kt                      # Edge-to-edge host initializing DataStore, Room & Firestore repos
├── data/
│   ├── local/                           # UserPreferencesDataStore, SafarGoDatabase, Room DAO/Entities
│   ├── model/
│   │   ├── FirestoreModels.kt           # UserProfileDoc, AdminSystemConfigDoc, BookingDoc
│   │   ├── SafarLocalization.kt         # Centralized L10nKey registry + UZ/EN/RU translations
│   │   └── TravelModels.kt              # SafarCatalog, CityHub, PaymentRail, TravelOption, CurrencyFormatter
│   ├── remote/
│   │   └── TelegramBotService.kt        # Public bot config & security boundary (no secrets/tokens)
│   └── repository/
│       ├── FirestoreTravelRepository.kt # Auth-scoped Firestore CRUD + Admin claim verification
│       └── SafarGoRepository.kt         # Demo travel catalog search, seat maps & preference proxy
└── ui/
    ├── components/
    │   ├── GlassPrimitives.kt           # SafarGoGlassBackdrop, GlassSurface, GlassCard, GlassButton, GlassInput, GlassNoticeBanner
    │   └── SafarGoComponents.kt         # Top bar, ticket cards, QR boarding pass, payment tiles
    ├── navigation/
    │   └── SafarGoNavigation.kt         # Auth/Registration/Main routing, LoginMenuBottomSheet trigger, adaptive Rail/Bar
    ├── screens/                         # AuthGate, Registration, Home, Explore, Results, Checkout, Trips, TicketDetail, Profile, Admin, LoginMenuSheet
    ├── theme/                           # Color.kt, Theme.kt, Type.kt, Shape.kt
    └── viewmodel/
        └── SafarGoViewModel.kt          # Reactive UI state, search, booking, check-in, profile & admin logic
```

---

## Build & Verification Commands

> **Note:** Use `gradle` directly (the environment does not use `./gradlew`).

- **Compile Kotlin debug sources**:
  ```bash
  gradle compileDebugKotlin
  ```
- **Assemble debug APK**:
  ```bash
  gradle :app:assembleDebug
  ```
- **Run full unit, localization, UI, and Firebase Emulator security-rule tests**:
  ```bash
  firebase emulators:exec --only firestore,auth --project demo-no-project 'gradle :app:testDebugUnitTest --no-daemon'
  ```

---

## Related Documentation

- [`DESIGN_SYSTEM.md`](./DESIGN_SYSTEM.md) — SafarGo Glassmorphism tokens, Light/Dark palettes, typography, and component API
- [`LOCALIZATION.md`](./LOCALIZATION.md) — `SafarLocalization` architecture, adding new `L10nKey` entries, and automated parity checks
- [`USER_GUIDE_UZ.md`](./USER_GUIDE_UZ.md) — Foydalanuvchi qo‘llanmasi (O‘zbek lotin yozuvida)
- [`SETUP.md`](./SETUP.md) — Environment, Firebase, Google Sign-In, Telegram Cloud Functions, and Admin Custom Claims setup
