---
name: SafarGo Modern Indigo & Sky
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#474651'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#777682'
  outline-variant: '#c8c5d3'
  surface-tint: '#5654a8'
  primary: '#1a146b'
  on-primary: '#ffffff'
  primary-container: '#312e81'
  on-primary-container: '#9c9af4'
  inverse-primary: '#c3c0ff'
  secondary: '#4648d4'
  on-secondary: '#ffffff'
  secondary-container: '#6063ee'
  on-secondary-container: '#fffbff'
  tertiary: '#002839'
  on-tertiary: '#ffffff'
  tertiary-container: '#003f57'
  on-tertiary-container: '#1eb0ea'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e2dfff'
  primary-fixed-dim: '#c3c0ff'
  on-primary-fixed: '#100563'
  on-primary-fixed-variant: '#3e3c8f'
  secondary-fixed: '#e1e0ff'
  secondary-fixed-dim: '#c0c1ff'
  on-secondary-fixed: '#07006c'
  on-secondary-fixed-variant: '#2f2ebe'
  tertiary-fixed: '#c4e7ff'
  tertiary-fixed-dim: '#7bd0ff'
  on-tertiary-fixed: '#001e2c'
  on-tertiary-fixed-variant: '#004c69'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 3.5rem
    fontWeight: '700'
    lineHeight: 4rem
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Inter
    fontSize: 2.25rem
    fontWeight: '700'
    lineHeight: 2.75rem
    letterSpacing: -0.015em
  headline-lg:
    fontFamily: Inter
    fontSize: 2rem
    fontWeight: '600'
    lineHeight: 2.5rem
    letterSpacing: -0.01em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 1.5rem
    fontWeight: '600'
    lineHeight: 2rem
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 1.25rem
    fontWeight: '600'
    lineHeight: 1.75rem
    letterSpacing: -0.005em
  body-lg:
    fontFamily: Inter
    fontSize: 1.125rem
    fontWeight: '400'
    lineHeight: 1.75rem
  body-md:
    fontFamily: Inter
    fontSize: 1rem
    fontWeight: '400'
    lineHeight: 1.5rem
  body-sm:
    fontFamily: Inter
    fontSize: 0.875rem
    fontWeight: '400'
    lineHeight: 1.25rem
  label-lg:
    fontFamily: Inter
    fontSize: 0.875rem
    fontWeight: '600'
    lineHeight: 1.25rem
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 0.75rem
    fontWeight: '600'
    lineHeight: 1rem
    letterSpacing: 0.025em
  label-sm:
    fontFamily: Inter
    fontSize: 0.6875rem
    fontWeight: '500'
    lineHeight: 0.875rem
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1.5rem
  gutter-mobile: 1rem
  margin: 2rem
  margin-mobile: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2.5rem
---

## Brand & Style

The design system establishes a high-trust, frictionless commercial travel super-app for Uzbekistan and the Central Asian corridor. It serves domestic travelers, international tourists, corporate bookers, and transit aggregators navigating flights, high-speed rail (Afrosiyob), hotels, intercity transfers, and localized payment rails.

The visual direction merges **Modern Corporate Precision** with **Atmospheric Mobility**. Clean, structured functional surfaces meet airy travel-inspired highlights. Depth is conveyed through subtle tonal elevation, restrained translucent navigation overlays, and high-legibility typography optimized for bilingual Latin/Cyrillic scripts and high-value currency displays.

### Design Principles
- **Clarity Under Density:** Multi-leg itineraries, fare rules, and complex pricing matrices maintain generous legibility without requiring excessive viewport scrolling.
- **Regional Authenticity & Modernity:** Respect for regional mobility workflows (Click, Payme, Uzcard, Humo) framed in an international modern travel aesthetic.
- **Deterministic Hierarchy:** Primary actions and vital reservation states are immediately distinct through high-contrast deep indigo anchoring and vibrant sky-blue directional cues.

## Colors

The palette relies on deep oceanic foundation tones balanced by luminous aerial accents.

### Light Mode Foundations
- **Primary (`#312E81`):** Deep Indigo. Used for primary booking triggers, top-level navigation app bars, active state rings, and verified status badges.
- **Secondary (`#6366F1`):** Electric Violet. Applied to secondary interactions, progressive checkout buttons, and interactive filter controls.
- **Tertiary (`#38BDF8`):** Sky Blue. Used for route indicators, direct departure callouts, seat map selections, and live status signals.
- **Surface Canvas (`#F8FAFC`):** Slate 50. Canvas background for pages and main scrollable areas.
- **Surface Elevation (`#FFFFFF`):** Pure White. Card containers, sheets, dialogs, and elevated data modules.
- **Borders & Dividers (`#E2E8F0`):** Slate 200. Structural divisions, input perimeters, and table dividers.
- **Text Primary (`#0F172A`):** Slate 900. High-contrast headlines, route destinations, passenger names, and currency amounts.
- **Text Secondary (`#64748B`):** Slate 500. Metadata, baggage policies, seat classes, and helper notes.

### Dark Mode Adaptations
- **Canvas Base:** `#0B0F19` deep void background.
- **Surface Container:** `#111827` base elevation container.
- **Surface Container High:** `#1F2937` elevated floating cards and active modals.
- **Borders:** `#374151` refined edge contrast.
- **Primary Tint:** `#818CF8` accessible high-contrast primary for dark backdrops.
- **Text Shifts:** `#F8FAFC` for primary reading; `#94A3B8` for secondary labels.

### Functional Alerts
- **Success (`#10B981`):** Confirmed bookings, ticket issuance, successful Uzcard/Humo charges.
- **Warning (`#F59E0B`):** Gate changes, expiring fare holds, low seat inventory.
- **Error (`#EF4444`):** Payment failure, cancelled departures, booking timeouts.

## Typography

The typography system relies exclusively on **Inter** to ensure maximum legibility across multi-lingual interfaces: Uzbek Latin (`Oâ€˜`, `D &ÚÚ
K\ÜÚX[ˆŞ\š[XË[™[™Û\Ú‚‚ˆÈÈÈ[\[Y[][Ûˆ[\Â‹H
Š“[Û›ÜÜXÙHX[\ˆšYİ\™\ÎŠŠˆ[[Y\šXÈ˜]\Ë[YHÛÙ\È
K™Ë‹H8¡¤ˆLŒMX
K”ˆÛÙ\ËÙX][X™\œË[™šXÙH[™XØ]ÜœÈ]\İ][^™H›ÛY™X]\™K\Ù][™ÜÎˆ[HˆXÈ™]™[š]\ˆ\š[™Èš[\š[™È[™X[\ˆØØ[›š[™Ë‚‹H
Šİ\œ™[˜ŞH™\™\Ù[][ÛŠŠˆZ\ˆ™Yİ[\ˆ›ÙHÙZYÚ›Üˆİ\œ™[˜ŞHÛÙ\ÈÚ]Ù[ZX›ÛšYİ\™\È
K™Ë‹HLV”ØÜˆ	NLTÑ
Kˆ]›ÚYZ^[™ÈZ\ÛX]ÚY›ÛØØ[\ÈÚ][ˆY[XØ[˜[œØXİ[Ûˆ›ØÚÜË‚‹H
Š’Y\˜\˜ÚHÙ\\˜][ÛŠŠˆ\™ÙHXY[™ÜÈ\™HYÚ]˜XÚÙY
]\‹\ÜXÚ[™ÎˆLŒ™[X
HÈXZ[Z[ˆİXİ\˜[[˜ÚÛˆX\šÙ][™Èİ\™˜XÙ\ËÚ[HÛX[›YÚY]Y]H[™İ]\ÈYÜÈ™X]\™H^[™Y˜XÚÚ[™È›ÜˆYÚXš[]H]ÛX[Ú^™\Ë‚‚ˆÈÈ^[İ]	ˆÜXÚ[™Â‚•H^[İ]]™\˜YÙ\ÈH\ØÚ\[™Y›ZYÜšY˜[[˜ÙYÚ]İšXİÛÛ\Û™[[œÚ]HØØ[\ÈÈX[˜YÙH›İÛÛœİ[Y\‹Y˜XÚ[™ÈÙX\˜Ú›İÜÈ[™[\œš\ÙH\™\ˆÛÛœÛÛ\Ë‚‚ˆÈÈÈÜšY	ˆœ™XZÜÚ[Â‹H
Š“[Øš[H
Í
NŠŠˆXÛÛ[[ˆ^[İ]MœX\™Ú[œËMœİ]\œËˆÚ[™ÛKXÛÛ[[ˆİXÚÙYØ\™È›Üˆ˜]™[YÜËˆš^Y›İÛH›ÛÚÚ[™ÈÚY]›ÜˆÚXÚÛİ]›İÜË‚‹H
Š•X›]
Í8 $ÈL
NŠŠˆXÛÛ[[ˆ^[İ]X\™Ú[œËŒİ]\œËˆÛÛ\ÚX›Hš[\ˆ˜]Ù\œËÚYKXK\ÚYHÛÛ\\š\ÛÛˆ[\Ë‚‹H
Š‘\ÚİÜ
ˆL
NŠŠˆL‹XÛÛ[[ˆ^[İ]Ú]HLX^[][HÛÛ[[™[ÜKˆÜ]ØÜ™Y[ˆšY]ÜÈ›ÜˆÙX\˜ÚX]šXÙ\È[™[˜[ZXÈX\Ú][™\˜\HÚYX˜\œËˆÌœX\™Ú[œËİ]\œË‚‚ˆÈÈÈÜXÚ[™È[ÜÛÜB‹H˜\ÙH^[İ][˜Ü™[Y[ÈİšXİH›ÛİÈ[ˆ\Ú[Ş\İ[H
\™[X\™[XK\™[Xœ™[X‹\™[X
K‚‹H[\›˜[ÛÛ\Û™[Y[™È™[Y\ÈÛˆÛÛ™[œÙY\Ú[ÜXÚ[™Èİ\È
Œ\™[XÍ\™[X
HÈXZ[Z[ˆÛÛ\Xİ[œÚ]HÚ][ˆ›YÚXÚÙ]Ë^[Y[Y]ÙÙ[XİÜœË[™\ÜÙ[™Ù\ˆX[šY™\İË‚‚ˆÈÈ[]˜][Ûˆ	ˆ\‚•š\İX[Y\˜\˜ÚH\Ù\ÈÛX[ˆİXİ\˜[İ\™˜XÙ\È^Y\™Yİ™\ˆ™]]˜[˜\ÙHÛÛÜœËXØÙ[YHÛÙ][ÜÜ\šXÈ[™YÛË][Y›ÜÚYİÜÈ˜]\ˆ[ˆX]H›XÚÈØÜš[\Ë‚‚ˆÈÈÈ[]˜][Ûˆ]™[Â‹H
Š“]™[
›]
NŠŠˆ˜\ÙHØ[˜\È˜XÚÙÜ›İ[™
ÑQØYÚÈÌŒŒNX\šÊKˆ›\Ú]HÜšYÈ[™İXİ\˜[\Ú›Ø\™ÛÛ[[œË‚‹H
Š“]™[H
İXH[]˜][ÛŠNŠŠˆİ[™\™›YÚ˜Z[‹[™İ[Ø\™Ëˆ\Ù\È\™HÚ]Hİ\™˜XÙHZ\™YÚ]HÛÙYÙH[H
\ÛÛYÑL‘NŒ
H[™[XšY[ÚYİÎˆ›Ş\ÚYİÎˆ\Ü™Ø˜JMKŒË‹Œ
K\œ™Ø˜JMKŒË‹ŒŠX‚‹H
Š“]™[ˆ
[\˜Xİ]™H›Ø][™ÊNŠŠˆİ™\™Y][™\˜\HØ\™Ë›Ø][™ÈšXÙH˜\œË›ÜİÛˆ[™[Ë[™ÙX]Ù[XİÜİ™\œÎˆ›Ş\ÚYİÎˆLM\LÜ™Ø˜JK‹LKŒŠKœM™Ø˜JK‹LKŒÊX‚‹H
Š“]™[È
[Ù[È	ˆİXÚŞH˜]šYØ][ÛŠNŠŠˆXY\ˆÙX\˜Ú˜\‹š[\œÈ˜]Ù\‹\ÜÜÜØ\\™H[Ù[Îˆ›Ş\ÚYİÎˆŒ\M\™Ø˜JMKŒË‹ŒJKLMœ™Ø˜JMKŒË‹ŒJX‚‚ˆÈÈÈ˜[œÛXÙ[˜ŞH	ˆÛ\ÜÈ™X]Y[Â”İXÚŞH˜]šYØ][Ûˆ˜\œÈ[™[˜[ZXÈšXÚ[™ÈXÚÙ\œÈ[\[Y[H™\İ˜Z[™YÛ\ÜÈY™™Xİˆ˜XÚÙ›ÜYš[\ˆ›\ŠLœ
Xİ™\ˆ[ˆIHÜXÚ]Hİ\™˜XÙH
™Ø˜JMKMKMKJX[ˆYÚ[ÙK™Ø˜JMËÎKJX[ˆ\šÈ[ÙJK‚‚ˆÈÈÚ\\Â‚•H\ÚYÛˆ\Ù\ÈH™Yš[™Yİ\˜]\™H›Ùš[H
›İ[™Y™\ÜÎˆ˜Ú\™H˜\ÙHÛÛ\Û™[È\ÙH\™[XÈØ\™È[™Ü›İ\[™[È\ÙH\™[XÈMœ[™[Ù[ÚY]ÈØØ[HÈK\™[XÈ
K‚‚ˆÈÈÈÚ\H[\Â‹H
Š’[\˜Xİ]™HÛÛ›ÛÈ
[œ]Ë]ÛœÊNŠŠˆİ[™\™^™YÛˆÍ\™[X
Lœ
HÈ›İšYHHÛÙÙ[ÛÛZ[™Ë[™[X‹YœšY[™H[\˜Xİ[Ûˆ\™Ù]‚‹H
ŠÛÛ[Ø\™È	ˆ[Ù[\ÎŠŠˆ›İ[™H\™[X
Mœ
HÛÜ›™\œËˆ™\İY[[Y[È
İXÚ\ÈØ\œšY\ˆÙÛÜÈÜˆ˜\™HÛ\ÜÈ[ÊHØØ[HİÛˆ›ÜÜ[Û˜[HÈŒÍÍ\™[XÜˆ\™[XÈ]›ÚYÛÜ›™\ˆÛ\Ú\Ë‚‹H
Š‘[˜Y]\È
[
NŠŠˆ™\Ù\™Y^Û\Ú]™[H›Üˆİ]\ÈÚ\Ë›YÚÛ\ÜÈX\šÙ\œÈ
K™Ë‹’TYœ›ÜÚ^[Øˆš\İ›Ø\Ú[™\ÜÈÛ\ÜØ
K˜[œÚ]\˜][Ûˆ˜YÙ\Ë[™İ\Z[™XØ]ÜˆX\šÙ\œË‚‚ˆÈÈÛÛ\Û™[Â‚ˆÈÈÈ]ÛœÂ‹H
Š”š[X\NŠŠˆ˜XÚÙÜ›İ[™Y\[™YÛÈ
ÌÌL‘NX
K^\™HÚ]H
Ñ‘‘‘‘‘˜
KˆÍ\™[XÛÜ›™\ˆ˜Y]\ËYY][HÙZYÚ\ÙÜ˜\Kˆ›Øİ\Èİ]HÚİÜÈHÜš[™È[ˆÌÎ‘Ú]HœÙ™œÙ]‚‹H
Š”ÙXÛÛ™\NŠŠˆİ\™˜XÙH\™HÚ]HÚ]H\ÛÛYÑL‘NŒ›Ü™\‹^Û]HL
ÌŒMÌX
Kˆİ™\ˆ˜[œÚ][ÛœÈÈÛ]HLÚ][ˆ[™YÛÈ[Y›Ü™\‹‚‹H
ŠXØÙ[ÈØ[]ËPXİ[ÛŠŠˆš[Û]
ÍŒÍ‘ŒX
HÚ]\™HÚ]H^™\Ù\™Y›Üˆ\™XİÚXÚÛİ]Xİ[ÛœÈ[™[Z]Y›Û[İ[Û˜[X[Ë‚‚ˆÈÈÈ[œ]šY[È	ˆÙX\˜Ú˜\œÂ‹Hš^YZYÚÙˆ›Üˆİ[™\™[œ]ÎÈMœ›Üˆš[X\HÜšYÚ[‹Ù\İ[˜][Ûˆ][K[[Ù[ÙX\˜Ú[œ]Ë‚‹Hİ][™\È\ÙH\ÛÛYÛ]HŒ
ÑL‘NŒ
KˆÛˆ›Øİ\ËÚYÈÈHK\Y\[™YÛÈİ][™HÚ]HÚŞH›YHÛİÈš[™È
™Ø˜JM‹NKŒŠX
K‚‹H[œ]X™[È™\İX›İ™HH[œ]šY[[ˆX™[[YÛ]HL
ÍÍ˜
K‚‚ˆÈÈÈÚ\È	ˆ˜YÙ\Â‹H
Š”İ]\ÈYÜÎŠŠˆ[\Ú\Y
›İ[™YY[
KYYL‚ˆHÛÛ™š\›YYˆ[Y\˜[Lš[
ÑPÑ‘X
K[Y\˜[Ì^
ÌÎMØ
K‚ˆH›Ø\™[™ÈÈš[Üš]NˆÚŞH›YHLš[
ÑŒQ‘˜
KÚŞH›YHÌ^
ÌÍPLX
K‚ˆH[^YYˆ[X™\ˆLš[
Ñ‘‘‘P˜
K[X™\ˆÌ^
ĞLÌX
K‚‹H
Š‘š[\ˆÚ\ÎŠŠˆÍœZYÚ›İ[™Y[Ë\›Ü™\‹ˆÚ[ˆÙ[XİYÛÛYY\[™YÛÈİ\™˜XÙHÚ]Ú]H^[™[ˆ[YÜ˜]Y\ÛZ\ÜØ[XÛÛ‹‚‚ˆÈÈÈØ\™È
˜]™[][™\˜\šY\ÊB‹H]šYY[È™YH[˜İ[Û˜[›Û™\Îˆ\\\™KĞ\œš]˜[[˜ÚÜˆ[Y\Ë˜[œÚ]›İ]H[™H
™X]\š[™ÈHÚŞKX›YH][™HÚ][™Kİ˜Z[ˆXÛÛŠK[™šXÚ[™ËØXİ[ÛˆØÚË‚‹HÛX[ˆÙ\\˜][Ûˆ\Ú[™È\ÑL‘NŒZ\›[™H[\Ëˆ›ÈX]HÚYİÜÎÈ[]˜][ÛˆÚ[™Ù\ÈØØİ\ˆšXHÛ[ÛİÔÔÈ˜[œÙ›Ü›\È
˜[œÛ]VJLœ
X
HÛˆİ™\‹‚‚ˆÈÈÈ^[Y[Ù[XİÜˆ[\Â‹HÜXÚX[^™Y[Üš^›Û[Ù[XİÜˆ[\Èİ\Ü[™È™YÚ[Û˜[^[Y[Ø]]Ø^\È
^˜Ø\™[[ËÛXÚË^[YK^[H^Kš\ØKX\İ\˜Ø\™
K‚‹H\Ü^\ÈØ]]Ø^HÙÛÈ[Û™ÜÚYHØ\™šXÚÛ˜[YHÜˆ]]Ë\^H›Û\‚‹HXİ]™Hİ]Nˆœ›Ü™\ˆ[ˆ[™YÛÈ
ÌÌL‘NX
HZ\™YÚ]HÜ\šYÚÚXÚÙY[™XØ]Üˆ˜YÙK‚‚ˆÈÈÈ\ÜÙ[™Ù\ˆ	ˆ›ÛÚÚ[™È]H\İÂ‹H[œÙHX[\ˆİXİ\™\È™X]\š[™È[\›˜][™ÈÜˆİ™\˜X›H›İÈYÚYÚÈ
ÑQØ
K‚‹Hİ[™\™›İÈZYÚÈÚ][YÜ˜]Y[Û›ÜÜXÙY˜XÚÚ[™È›Üˆ\ÜÜÜÙ\šY\ËXÚÙ][X™\œË[™”ˆ™Y™\™[˜Ù\Ë