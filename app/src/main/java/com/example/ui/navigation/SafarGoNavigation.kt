package com.example.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.data.local.UserPreferences
import com.example.data.model.AppLanguage
import com.example.data.model.BookingStatusTag
import com.example.data.model.L10nKey
import com.example.data.model.SafarLocalization
import com.example.data.model.ThemePreference
import com.example.data.model.TravelMode
import com.example.data.model.UserProfileDoc
import com.example.data.model.stringsFor
import com.example.data.repository.FirestoreTravelRepository
import com.example.data.repository.SafarGoRepository
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSurface
import com.example.ui.components.GlassTokens
import com.example.ui.components.SafarGoGlassBackdrop
import com.example.ui.components.SafarGoGlassTopBar
import com.example.ui.components.glassFillColor
import com.example.ui.screens.AuthGateScreen
import com.example.ui.screens.BookingCheckoutScreen
import com.example.ui.screens.ExploreCategory
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginMenuBottomSheet
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RegistrationProfileScreen
import com.example.ui.screens.ResultsScreen
import com.example.ui.screens.TicketDetailScreen
import com.example.ui.screens.TripsScreen
import com.example.ui.screens.signOutSafarGo
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SafarGoTheme
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.viewmodel.ProfileGateState
import com.example.ui.viewmodel.SafarGoViewModel
import com.example.ui.viewmodel.SingleBookingUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute
@Serializable data object HotelsRoute
@Serializable data object ToursRoute
@Serializable data object TripsRoute
@Serializable data object ProfileRoute
@Serializable data object ResultsRoute
@Serializable data object BookingRoute
@Serializable data class TicketDetailRoute(val bookingId: String)

private data class TopLevelNavItem(
    val route: Any,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

private fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
    val auth = Firebase.auth
    val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        trySend(firebaseAuth.currentUser)
    }
    auth.addAuthStateListener(listener)
    trySend(auth.currentUser)
    awaitClose { auth.removeAuthStateListener(listener) }
}

@Composable
fun SafarGoApp(
    firestoreRepo: FirestoreTravelRepository,
    localRepo: SafarGoRepository
) {
    val authFlow = remember { authStateFlow() }
    val currentUser by authFlow.collectAsStateWithLifecycle(initialValue = Firebase.auth.currentUser)
    val preferences by localRepo.userPreferences.collectAsStateWithLifecycle(initialValue = UserPreferences())
    val scope = rememberCoroutineScope()

    val systemDark = isSystemInDarkTheme()
    val isDarkTheme = when (preferences.themePreference) {
        ThemePreference.SYSTEM -> systemDark
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
    }

    SafarGoTheme(darkTheme = isDarkTheme) {
        val user = currentUser
        if (user == null) {
            AuthGateScreen(
                language = preferences.language,
                initialPhone = preferences.pendingAuthPhone,
                initialTelegramHandle = preferences.pendingTelegramHandle,
                initialTelegramNotif = preferences.pendingTelegramNotifications,
                onSavePendingPhone = { phone ->
                    scope.launch {
                        localRepo.savePendingAuthChannels(phone = phone)
                    }
                },
                onSavePendingTelegram = { handle, notif ->
                    scope.launch {
                        localRepo.savePendingAuthChannels(
                            telegramHandle = handle,
                            telegramNotifications = notif
                        )
                    }
                },
                onCycleLanguage = {
                    val langs = AppLanguage.entries
                    val next = langs[(langs.indexOf(preferences.language) + 1) % langs.size]
                    scope.launch { localRepo.setLanguage(next) }
                },
                onAuthSuccess = { }
            )
        } else {
            val viewModel: SafarGoViewModel = viewModel(
                key = user.uid,
                factory = SafarGoViewModel.Factory(
                    currentUserId = user.uid,
                    firestoreRepo = firestoreRepo,
                    localRepo = localRepo
                )
            )
            AuthenticatedSafarGoContent(
                user = user,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun AuthenticatedSafarGoContent(
    user: FirebaseUser,
    viewModel: SafarGoViewModel
) {
    val profileGateState by viewModel.profileGateState.collectAsStateWithLifecycle()
    val registrationSubmitting by viewModel.registrationSubmitting.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val strings = stringsFor(preferences.language)

    when (val gate = profileGateState) {
        ProfileGateState.Loading -> {
            SafarGoGlassBackdrop {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard(shape = RoundedCornerShape(GlassTokens.CardRadius)) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(color = IndigoPrimary)
                            Text(
                                text = SafarLocalization.text(preferences.language, L10nKey.GATE_LOADING_PROFILE),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        ProfileGateState.NeedsRegistration -> {
            RegistrationProfileScreen(
                language = preferences.language,
                initialName = user.displayName.orEmpty(),
                initialPhone = preferences.pendingAuthPhone,
                initialTelegramHandle = preferences.pendingTelegramHandle,
                initialTelegramNotif = preferences.pendingTelegramNotifications,
                isSubmitting = registrationSubmitting,
                errorMessage = statusMessage,
                onSubmitRegistration = { name, phone, passport, paymentId, telegramUsername, telegramNotif ->
                    viewModel.clearStatusMessage()
                    viewModel.submitRegistration(
                        fullName = name,
                        phone = phone,
                        passportSeries = passport,
                        preferredPaymentId = paymentId,
                        telegramUsername = telegramUsername,
                        telegramNotificationsEnabled = telegramNotif
                    )
                }
            )
        }

        is ProfileGateState.Error -> {
            SafarGoGlassBackdrop {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard(shape = RoundedCornerShape(GlassTokens.CardRadius)) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = gate.message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            GlassButton(
                                text = SafarLocalization.text(preferences.language, L10nKey.GATE_SIGN_IN_AGAIN),
                                onClick = { signOutSafarGo(credentialManager, scope) }
                            )
                        }
                    }
                }
            }
        }

        is ProfileGateState.Ready -> {
            MainSafarGoShell(
                profile = gate.profile,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun MainSafarGoShell(
    profile: UserProfileDoc,
    viewModel: SafarGoViewModel
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val checkoutState by viewModel.checkoutState.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val bookingsUiState by viewModel.bookingsUiState.collectAsStateWithLifecycle()
    val adminConfig by viewModel.adminConfig.collectAsStateWithLifecycle()
    val adminState by viewModel.adminPanelState.collectAsStateWithLifecycle()
    val isSavingProfile by viewModel.isSavingProfile.collectAsStateWithLifecycle()
    val checkingInBookingId by viewModel.checkingInBookingId.collectAsStateWithLifecycle()
    val cancellingBookingId by viewModel.cancellingBookingId.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var hasUnreadNotification by remember { mutableStateOf(true) }
    var showLoginMenuSheet by remember { mutableStateOf(false) }

    LaunchedEffect(statusMessage) {
        val msg = statusMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    val strings = stringsFor(preferences.language)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val isTopLevel = currentDestination?.let { dest ->
        dest.hasRoute<HomeRoute>() ||
            dest.hasRoute<HotelsRoute>() ||
            dest.hasRoute<ToursRoute>() ||
            dest.hasRoute<TripsRoute>() ||
            dest.hasRoute<ProfileRoute>()
    } ?: true

    val navItems = listOf(
        TopLevelNavItem(
            route = HomeRoute,
            label = strings.navHome,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            tag = "nav_home"
        ),
        TopLevelNavItem(
            route = HotelsRoute,
            label = strings.navHotels,
            selectedIcon = Icons.Filled.Hotel,
            unselectedIcon = Icons.Outlined.Hotel,
            tag = "nav_hotels"
        ),
        TopLevelNavItem(
            route = ToursRoute,
            label = strings.navTours,
            selectedIcon = Icons.Filled.Explore,
            unselectedIcon = Icons.Outlined.Explore,
            tag = "nav_tours"
        ),
        TopLevelNavItem(
            route = TripsRoute,
            label = strings.navTrips,
            selectedIcon = Icons.Filled.ConfirmationNumber,
            unselectedIcon = Icons.Outlined.ConfirmationNumber,
            tag = "nav_trips"
        ),
        TopLevelNavItem(
            route = ProfileRoute,
            label = strings.navProfile,
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            tag = "nav_profile"
        )
    )

    val navigateTopLevel: (Any) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    SafarGoGlassBackdrop {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 600.dp

            Row(modifier = Modifier.fillMaxSize()) {
                if (useRail && isTopLevel) {
                    GlassSurface(
                        modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding(),
                        shape = RoundedCornerShape(topEnd = GlassTokens.CardRadius, bottomEnd = GlassTokens.CardRadius),
                        elevation = GlassTokens.NavElevation
                    ) {
                        NavigationRail(
                            containerColor = Color.Transparent
                        ) {
                            navItems.forEach { item ->
                                val selected = currentDestination?.hasRoute(item.route::class) == true
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(item.route) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.label
                                        )
                                    },
                                    label = { Text(item.label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                    colors = NavigationRailItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = SkyBlueAccent.copy(alpha = if (isDark) 0.24f else 0.18f)
                                    ),
                                    modifier = Modifier.testTag(item.tag)
                                )
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.weight(1f),
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        if (isTopLevel) {
                            SafarGoGlassTopBar(
                                language = preferences.language,
                                hasUnreadNotification = hasUnreadNotification,
                                isAdminUnlocked = adminState.isAdminClaimVerified,
                                onCycleLanguage = viewModel::cycleLanguage,
                                onLoginMenuClick = { showLoginMenuSheet = true },
                                onNotificationsClick = {
                                    hasUnreadNotification = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            SafarLocalization.text(preferences.language, L10nKey.NOTIF_SAMPLE_CHECKIN)
                                        )
                                    }
                                },
                                onProfileClick = { navigateTopLevel(ProfileRoute) }
                            )
                        }
                    },
                    bottomBar = {
                        if (isTopLevel && !useRail) {
                            FloatingGlassNavigationBar(
                                items = navItems,
                                isSelected = { item -> currentDestination?.hasRoute(item.route::class) == true },
                                onSelect = { item -> navigateTopLevel(item.route) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = HomeRoute
                        ) {
                            composable<HomeRoute> {
                                val upcoming = bookings.firstOrNull {
                                    !it.isCompleted && it.statusTag != BookingStatusTag.CANCELLED
                                }
                                HomeScreen(
                                    profile = profile,
                                    upcomingBooking = upcoming,
                                    checkingInBookingId = checkingInBookingId,
                                    searchState = searchState,
                                    currency = preferences.currency,
                                    language = preferences.language,
                                    strings = strings,
                                    onSelectMode = viewModel::selectMode,
                                    onSelectOrigin = viewModel::setOrigin,
                                    onSelectDestination = viewModel::setDestination,
                                    onSwapOriginDest = viewModel::swapOriginDestination,
                                    onSelectDate = viewModel::setDepartureDate,
                                    onChangePassengers = viewModel::setPassengerCount,
                                    onSearchRoutes = {
                                        viewModel.executeSearch()
                                        navController.navigate(ResultsRoute)
                                    },
                                    onLaunchDeal = { deal ->
                                        viewModel.launchDeal(deal)
                                        navController.navigate(ResultsRoute)
                                    },
                                    onQuickCityClick = { code ->
                                        viewModel.launchQuickDestination(code, TravelMode.RAIL)
                                        navController.navigate(ResultsRoute)
                                    },
                                    onToggleFavoriteDestination = viewModel::toggleFavoriteDestination,
                                    onSpendClubPoints = { viewModel.redeemClubPoints(5_000L) },
                                    onCheckInUpcoming = viewModel::checkInBooking,
                                    onOpenTicket = { bookingId ->
                                        navController.navigate(TicketDetailRoute(bookingId))
                                    },
                                    onOpenExploreTab = { navigateTopLevel(ToursRoute) },
                                    onOpenSupportInfo = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                SafarLocalization.text(preferences.language, L10nKey.SUPPORT_SAMPLE_CONTACT)
                                            )
                                        }
                                    }
                                )
                            }

                            composable<HotelsRoute> {
                                ExploreScreen(
                                    category = ExploreCategory.HOTELS,
                                    language = preferences.language,
                                    currency = preferences.currency,
                                    onLaunchDeal = { deal ->
                                        viewModel.launchDeal(deal)
                                        navController.navigate(ResultsRoute)
                                    },
                                    onBookHotelCity = { cityCode ->
                                        viewModel.launchQuickDestination(cityCode, TravelMode.HOTELS)
                                        navController.navigate(ResultsRoute)
                                    }
                                )
                            }

                            composable<ToursRoute> {
                                ExploreScreen(
                                    category = ExploreCategory.TOURS,
                                    language = preferences.language,
                                    currency = preferences.currency,
                                    onLaunchDeal = { deal ->
                                        viewModel.launchDeal(deal)
                                        navController.navigate(ResultsRoute)
                                    },
                                    onBookHotelCity = { cityCode ->
                                        viewModel.launchQuickDestination(cityCode, TravelMode.TOURS)
                                        navController.navigate(ResultsRoute)
                                    }
                                )
                            }

                            composable<TripsRoute> {
                                TripsScreen(
                                    bookingsState = bookingsUiState,
                                    language = preferences.language,
                                    currency = preferences.currency,
                                    strings = strings,
                                    onOpenTicket = { bookingId ->
                                        navController.navigate(TicketDetailRoute(bookingId))
                                    },
                                    onExploreClick = { navigateTopLevel(HomeRoute) }
                                )
                            }

                            composable<ProfileRoute> {
                                ProfileScreen(
                                    profile = profile,
                                    bookings = bookings,
                                    preferences = preferences,
                                    adminConfig = adminConfig,
                                    adminState = adminState,
                                    isSavingProfile = isSavingProfile,
                                    onSetCurrency = viewModel::setCurrency,
                                    onSetLanguage = viewModel::setLanguage,
                                    onSetTheme = viewModel::setThemePreference,
                                    onSelectPreferredPayment = viewModel::selectPaymentRail,
                                    onSaveTraveler = { name, passport, phone, telegramUsername, telegramEnabled ->
                                        viewModel.saveTravelerProfile(
                                            name = name,
                                            passport = passport,
                                            phone = phone,
                                            telegramUsername = telegramUsername,
                                            telegramNotificationsEnabled = telegramEnabled
                                        )
                                    },
                                    onRefreshAdminClaim = { viewModel.refreshAdminClaim(forceRefresh = true) },
                                    onSaveAdminConfig = viewModel::updateAdminConfig,
                                    onSignedOut = { }
                                )
                            }

                            composable<ResultsRoute> {
                                ResultsScreen(
                                    searchState = searchState,
                                    language = preferences.language,
                                    currency = preferences.currency,
                                    strings = strings,
                                    onBack = { navController.popBackStack() },
                                    onToggleCurrency = viewModel::toggleCurrency,
                                    onSelectFilter = viewModel::setResultFilter,
                                    onSelectOption = { option ->
                                        viewModel.startBooking(option)
                                        navController.navigate(BookingRoute)
                                    }
                                )
                            }

                            composable<BookingRoute> {
                                BookingCheckoutScreen(
                                    checkoutState = checkoutState,
                                    travelDate = searchState.departureDate,
                                    language = preferences.language,
                                    currency = preferences.currency,
                                    strings = strings,
                                    clubPoints = profile.clubPoints,
                                    onBack = { navController.popBackStack() },
                                    onSelectStep = viewModel::setCheckoutStep,
                                    onSelectSeat = viewModel::selectSeat,
                                    onUpdateName = viewModel::updatePassengerName,
                                    onUpdatePassport = viewModel::updatePassportSeries,
                                    onUpdatePhone = viewModel::updatePhone,
                                    onFillSavedTraveler = viewModel::fillSavedTraveler,
                                    onSelectPayment = viewModel::selectPaymentRail,
                                    onToggleClubPoints = viewModel::toggleApplyClubPoints,
                                    onConfirmBooking = {
                                        viewModel.confirmAndIssueTicket { newId ->
                                            navController.navigate(TicketDetailRoute(newId)) {
                                                popUpTo<HomeRoute>()
                                            }
                                        }
                                    }
                                )
                            }

                            composable<TicketDetailRoute> { backStackEntry ->
                                val route = backStackEntry.toRoute<TicketDetailRoute>()
                                val bookingState by viewModel.observeBookingState(route.bookingId)
                                    .collectAsStateWithLifecycle(initialValue = SingleBookingUiState.Loading)
                                TicketDetailScreen(
                                    bookingState = bookingState,
                                    isCheckingIn = checkingInBookingId == route.bookingId,
                                    isCancelling = cancellingBookingId == route.bookingId,
                                    language = preferences.language,
                                    currency = preferences.currency,
                                    strings = strings,
                                    onBack = { navController.popBackStack() },
                                    onCheckInBooking = viewModel::checkInBooking,
                                    onCancelBooking = viewModel::cancelBooking
                                )
                            }
                        }
                    }
                }
            }
        }
        if (showLoginMenuSheet) {
            LoginMenuBottomSheet(
                language = preferences.language,
                userEmail = Firebase.auth.currentUser?.email,
                profile = profile,
                adminConfig = adminConfig,
                adminState = adminState,
                isSavingProfile = isSavingProfile,
                onDismiss = { showLoginMenuSheet = false },
                onRefreshAdminClaim = { viewModel.refreshAdminClaim(forceRefresh = true) },
                onUpdatePhone = { newPhone ->
                    viewModel.saveTravelerProfile(
                        name = profile.fullName,
                        passport = profile.passportSeries,
                        phone = newPhone
                    )
                },
                onOpenAdminConsoleInProfile = { navigateTopLevel(ProfileRoute) },
                onSignedOut = { }
            )
        }
    }
}

@Composable
private fun FloatingGlassNavigationBar(
    items: List<TopLevelNavItem>,
    isSelected: (TopLevelNavItem) -> Boolean,
    onSelect: (TopLevelNavItem) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        GlassSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(GlassTokens.CardRadius),
            elevation = GlassTokens.NavElevation
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val selected = isSelected(item)
                    val activePillColor = glassFillColor(isSelected = true, isDark = isDark)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .clip(RoundedCornerShape(14.dp))
                            .then(
                                if (selected) Modifier.background(activePillColor) else Modifier
                            )
                            .clickable { onSelect(item) }
                            .padding(vertical = 6.dp, horizontal = 2.dp)
                            .testTag(item.tag),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
