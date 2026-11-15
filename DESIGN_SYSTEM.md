# SafarGo Glassmorphism Design System

SafarGo uses a single, unified **Glassmorphism** design system across every screen, navigation bar/rail, bottom sheet, dialog, and control—in both **Light** and **Dark** themes.

---

## 1. Visual Philosophy & Atmospheric Canvas

Glassmorphism requires depth behind translucent foreground surfaces. Every full-screen route renders inside **`SafarGoGlassBackdrop`** (`app/src/main/java/com/example/ui/components/GlassPrimitives.kt`):

1. **Vertical Atmospheric Gradient**:
   - **Light**: `LightBackdropTop` (`#EEF3FF`) -> `LightBackdropMid` (`#E3ECFD`) -> `LightBackdropBottom` (`#F3F6FF`)
   - **Dark**: `DarkBackdropTop` (`#070B16`) -> `DarkBackdropMid` (`#0C1326`) -> `DarkBackdropBottom` (`#111B36`)
2. **Blurred Ambient Radial Orbs** (`GlassTokens.BackdropOrbBlur = 52.dp`):
   - Soft Indigo, Sky Blue (`#38BDF8`), and Periwinkle radial orbs sit behind foreground cards so glass opacity creates depth without blurring any text or interactive controls.

---

## 2. Color Tokens (`ui/theme/Color.kt` & `Theme.kt`)

### Brand Palette
| Token | Hex | Role |
| --- | --- | --- |
| `IndigoDeep` | `#161C54` | Deep brand anchor, hero gradient start |
| `IndigoPrimary` | `#2B328C` | Primary CTA fills, active selection accents |
| `VioletSecondary` | `#3F47B8` | Supporting accent |
| `SkyBlueAccent` | `#38BDF8` | Luminous highlight on Indigo/Dark glass cards |
| `DarkPrimaryTint` | `#93C5FD` | Accessible high-contrast primary tint in Dark mode |

### Translucent Glass Surfaces & Borders
| Surface / Border | Light Mode | Dark Mode |
| --- | --- | --- |
| Default Glass Card (`glassFillColor()`) | `#CCFFFFFF` (80% frosted white) | `#B3141D33` (70% frosted indigo-slate) |
| Subtle Glass Layer (`subtle = true`) | `#A8F7F9FF` (66% tint) | `#9919243D` (60% tint) |
| Selected / Focused Glass (`isSelected = true`) | `#E6FFFFFF` (90% frosted) | `#CC1E2B4A` (80% frosted) |
| Glass Top Specular Sheen (`glassSpecularBrush`) | White 45% -> 5% | White 8% -> Transparent |
| Glass Gradient Border (`glassBorderBrush`) | `#E6FFFFFF` -> `#6694A3B8` | `#4DFFFFFF` -> `#1F64748B` |
| Selected Border | `IndigoPrimary` -> `SkyBlueAccent` | `SkyBlueAccent` -> `primary` |

### Semantic Notice Tones (`GlassNoticeBanner`)
- **INFO**: `PrioritySkyBg` / `PrioritySkyDarkBg` with `PrioritySkyText` / `PrioritySkyDarkText`
- **WARNING**: `WarningAmberBg` / `WarningAmberDarkBg` with `WarningAmberText` / `WarningAmberDarkText`
- **SUCCESS**: `SuccessEmeraldBg` / `SuccessEmeraldDarkBg` with `SuccessEmeraldText` / `SuccessEmeraldDarkText`
- **ERROR**: `MaterialTheme.colorScheme.errorContainer` & `onErrorContainer`

---

## 3. Core Glass Primitives (`GlassPrimitives.kt`)

All screens and shared components compose from these primitives:

| Primitive | Purpose | Key Tokens (`GlassTokens`) |
| --- | --- | --- |
| `SafarGoGlassBackdrop` | Atmospheric gradient + 3 blurred radial orbs | `BackdropOrbBlur = 52.dp` |
| `GlassSurface` | Base translucent container with gradient border + specular overlay | `CardRadius = 20.dp`, `DefaultElevation = 4.dp`, `SelectedElevation = 8.dp` |
| `GlassCard` | `Column`-scoped `GlassSurface` for content cards | `CardPadding = 16.dp`, `SectionSpacing = 16.dp` |
| `GlassIndigoHeroBox` | Deep indigo gradient card (`IndigoDeep` -> `IndigoPrimary`) with sky glow for club wallet, loyalty card, and admin header | `CardRadius = 20.dp`, `SelectedElevation = 8.dp` |
| `GlassButton` | Primary Indigo CTA with luminous border highlight & inline loading spinner | `ControlRadius = 14.dp`, `heightIn(min = 52.dp)` |
| `GlassSecondaryButton` | Subtle translucent outlined action button | `ControlRadius = 14.dp`, `heightIn(min = 48.dp)` |
| `GlassInput` | Translucent `OutlinedTextField` that brightens when focused | `ControlRadius = 14.dp` |
| `GlassNoticeBanner` | Semantic frosted alert strip with status icon (`INFO`, `WARNING`, `SUCCESS`, `ERROR`) | `ControlRadius = 14.dp` |

Modal containers (`ModalBottomSheet`, `AlertDialog`, city/date pickers, login menu) also use frosted high-opacity surfaces (`0xF2EEF3FF` in Light, `0xF010182E` in Dark) so overlays remain legible above underlying glass cards.

---

## 4. Typography & Numeric Precision (`ui/theme/Type.kt`)

SafarGo bundles the **Inter** font family (`res/font/inter.ttf`) across all weights (`Normal`, `Medium`, `SemiBold`, `Bold`):
- `displayLarge` (`36sp`), `headlineLarge` (`24sp`), `headlineMedium` (`20sp`), `titleLarge` (`18sp`), `titleMedium` (`16sp`)
- `bodyLarge` (`16sp`), `bodyMedium` (`14sp`), `bodySmall` (`13sp`), `labelLarge` (`14sp`), `labelMedium` (`12sp`), `labelSmall` (`11sp`)
- **Tabular numerals (`fontFeatureSettings = "tnum"`)**:
  - `TabularTimeStyle` (`20sp` Bold) — departure/arrival times
  - `TabularPriceStyle` (`17sp` SemiBold) — UZS / USD fares
  - `TabularCodeStyle` (`13sp` SemiBold, `0.6sp` tracking) — PNR codes, seat codes, passport IDs

---

## 5. Responsive Layout & Accessibility Standards

- **360dp & Tablet Adaptive Layouts**:
  - Scrollable containers apply `widthIn(max = 600.dp)`–`widthIn(max = 720.dp)` centered horizontally so tablet screens do not over-stretch cards.
  - Compact devices use `SafarGoGlassBottomBar`; `Medium` / `Expanded` window widths automatically switch to `SafarGoGlassNavRail` (`SafarGoNavigation.kt`).
- **Safe Insets & Keyboard Handling**:
  - Root `MainActivity` enables `enableEdgeToEdge()`.
  - Screens and sheets apply `statusBarsPadding()`, `navigationBarsPadding()`, and `imePadding()` so form fields and primary CTAs stay clear of software keyboards and system bars.
- **Accessibility**:
  - Interactive targets enforce `minimumInteractiveComponentSize()` (`>= 48.dp`).
  - Icons/images carry localized `contentDescription` strings via `L10nKey.CD_*`, and `PasswordVisualTransformation` preserves semantic password attributes for TalkBack and testing.
