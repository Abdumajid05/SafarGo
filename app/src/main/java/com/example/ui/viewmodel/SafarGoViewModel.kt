package com.example.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferences
import com.example.data.model.AdminSystemConfigDoc
import com.example.data.model.AppLanguage
import com.example.data.model.BookingDoc
import com.example.data.model.CorridorDeal
import com.example.data.model.CurrencyCode
import com.example.data.model.L10nKey
import com.example.data.model.SafarCatalog
import com.example.data.model.SafarLocalization
import com.example.data.model.SeatItem
import com.example.data.model.ThemePreference
import com.example.data.model.TravelMode
import com.example.data.model.TravelOption
import com.example.data.model.UserProfileDoc
import com.example.data.remote.TelegramBotService
import com.example.data.remote.TelegramIntegrationStatus
import com.example.data.repository.FirestoreTravelRepository
import com.example.data.repository.SafarGoRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ResultFilter {
    ALL, DIRECT, MORNING, BEST_PRICE, VIP
}

sealed interface ProfileGateState {
    data object Loading : ProfileGateState
    data object NeedsRegistration : ProfileGateState
    data class Ready(val profile: UserProfileDoc) : ProfileGateState
    data class Error(val message: String) : ProfileGateState
}

sealed interface BookingsUiState {
    data object Loading : BookingsUiState
    data class Success(val items: List<BookingDoc>) : BookingsUiState
    data class Error(val message: String) : BookingsUiState
}

sealed interface SingleBookingUiState {
    data object Loading : SingleBookingUiState
    data object NotFound : SingleBookingUiState
    data class Ready(val booking: BookingDoc) : SingleBookingUiState
    data class Error(val message: String) : SingleBookingUiState
}

data class SearchUiState(
    val mode: TravelMode = TravelMode.RAIL,
    val originCode: String = "TAS",
    val destinationCode: String = "SKD",
    val departureDate: String = "18 Noy, 2026",
    val passengerCount: Int = 1,
    val rawResults: List<TravelOption> = emptyList(),
    val activeFilter: ResultFilter = ResultFilter.ALL
) {
    val filteredResults: List<TravelOption>
        get() = when (activeFilter) {
            ResultFilter.ALL -> rawResults
            ResultFilter.DIRECT -> rawResults.filter { it.isDirect }
            ResultFilter.MORNING -> rawResults.filter { it.isMorning }
            ResultFilter.BEST_PRICE -> rawResults.sortedBy { it.priceUzs }
            ResultFilter.VIP -> rawResults.filter {
                it.fareClass.contains("VIP", ignoreCase = true) ||
                    it.fareClass.contains("Business", ignoreCase = true) ||
                    it.fareClass.contains("Biznes", ignoreCase = true) ||
                    it.fareClass.contains("Bistro", ignoreCase = true)
            }
        }
}

data class CheckoutUiState(
    val option: TravelOption? = null,
    val seatMap: List<SeatItem> = emptyList(),
    val selectedSeat: SeatItem? = null,
    val passengerName: String = "",
    val passportSeries: String = "",
    val phone: String = "",
    val selectedPaymentId: String = "uzcard",
    val applyClubPoints: Boolean = false,
    val step: Int = 0,
    val isSubmitting: Boolean = false,
    val validationError: String? = null
)

data class AdminPanelUiState(
    val isCheckingClaim: Boolean = true,
    val isAdminClaimVerified: Boolean = false,
    val telegramStatus: TelegramIntegrationStatus = TelegramIntegrationStatus.SERVER_CONFIGURATION_REQUIRED,
    val isSavingConfig: Boolean = false,
    val lastActionMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class SafarGoViewModel(
    private val currentUserId: String,
    private val firestoreRepo: FirestoreTravelRepository,
    private val localRepo: SafarGoRepository,
    val telegramBotService: TelegramBotService = TelegramBotService()
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = localRepo.userPreferences
        .onEach { prefs ->
            _searchState.update { state ->
                state.copy(
                    rawResults = localRepo.searchRoutes(
                        mode = state.mode,
                        originCode = state.originCode,
                        destinationCode = state.destinationCode,
                        language = prefs.language
                    )
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPreferences()
        )

    val profileGateState: StateFlow<ProfileGateState> = firestoreRepo.observeUserProfile(currentUserId)
        .map { doc ->
            if (doc == null || !doc.onboardingCompleted) {
                ProfileGateState.NeedsRegistration
            } else {
                ProfileGateState.Ready(doc)
            }
        }
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            val root = firestoreError ?: error
            Log.w(TAG, "Error observing user profile", root)
            emit(ProfileGateState.Error(root.message ?: SafarLocalization.text(preferences.value.language, L10nKey.VM_ERR_PROFILE_LOAD)))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileGateState.Loading
        )

    val bookingsUiState: StateFlow<BookingsUiState> = firestoreRepo.observeBookings(currentUserId)
        .map<List<BookingDoc>, BookingsUiState> { BookingsUiState.Success(it) }
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            val root = firestoreError ?: error
            Log.w(TAG, "Error observing bookings", root)
            emit(BookingsUiState.Error(root.message ?: SafarLocalization.text(preferences.value.language, L10nKey.VM_ERR_TRIPS_LOAD)))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BookingsUiState.Loading
        )

    val bookings: StateFlow<List<BookingDoc>> = bookingsUiState
        .map { (it as? BookingsUiState.Success)?.items ?: emptyList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _adminPanelState = MutableStateFlow(AdminPanelUiState())
    val adminPanelState: StateFlow<AdminPanelUiState> = _adminPanelState.asStateFlow()

    val isAdminVerifiedByServer: StateFlow<Boolean> = _adminPanelState
        .map { it.isAdminClaimVerified }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val adminConfig: StateFlow<AdminSystemConfigDoc?> = _adminPanelState
        .map { it.isAdminClaimVerified }
        .flatMapLatest { isAdmin ->
            if (!isAdmin) {
                flowOf(null)
            } else {
                firestoreRepo.observeAdminConfig().catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    Log.w(TAG, "Error observing adminConfig", firestoreError ?: error)
                    emit(null)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _searchState = MutableStateFlow(
        SearchUiState(
            rawResults = localRepo.searchRoutes(TravelMode.RAIL, "TAS", "SKD", AppLanguage.UZ)
        )
    )
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    private val _checkoutState = MutableStateFlow(CheckoutUiState())
    val checkoutState: StateFlow<CheckoutUiState> = _checkoutState.asStateFlow()

    private val _registrationSubmitting = MutableStateFlow(false)
    val registrationSubmitting: StateFlow<Boolean> = _registrationSubmitting.asStateFlow()

    private val _isSavingProfile = MutableStateFlow(false)
    val isSavingProfile: StateFlow<Boolean> = _isSavingProfile.asStateFlow()

    private val _checkingInBookingId = MutableStateFlow<String?>(null)
    val checkingInBookingId: StateFlow<String?> = _checkingInBookingId.asStateFlow()

    private val _cancellingBookingId = MutableStateFlow<String?>(null)
    val cancellingBookingId: StateFlow<String?> = _cancellingBookingId.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    init {
        refreshAdminClaim(forceRefresh = false)
    }

    fun observeBookingState(bookingId: String): Flow<SingleBookingUiState> =
        firestoreRepo.observeBookingById(currentUserId, bookingId)
            .map { doc ->
                if (doc == null) {
                    SingleBookingUiState.NotFound
                } else {
                    SingleBookingUiState.Ready(doc)
                }
            }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val root = firestoreError ?: error
                Log.w(TAG, "Error observing single booking", root)
                emit(SingleBookingUiState.Error(root.message ?: SafarLocalization.text(preferences.value.language, L10nKey.VM_ERR_TICKET_LOAD)))
            }

    fun refreshAdminClaim(forceRefresh: Boolean = true) {
        _adminPanelState.update { it.copy(isCheckingClaim = true) }
        viewModelScope.launch {
            val isAdmin = firestoreRepo.checkAdminCustomClaim(
                user = Firebase.auth.currentUser,
                forceRefresh = forceRefresh
            )
            val lang = preferences.value.language
            _adminPanelState.update {
                it.copy(
                    isCheckingClaim = false,
                    isAdminClaimVerified = isAdmin,
                    lastActionMessage = if (forceRefresh && !isAdmin) {
                        SafarLocalization.text(lang, L10nKey.VM_ADMIN_CLAIM_MISSING)
                    } else if (forceRefresh) {
                        SafarLocalization.text(lang, L10nKey.VM_ADMIN_CLAIM_OK)
                    } else {
                        it.lastActionMessage
                    }
                )
            }
        }
    }

    fun updateAdminConfig(
        telegramBotHandle: String,
        telegramWebhookConfigured: Boolean,
        maintenanceNotice: String
    ) {
        val lang = preferences.value.language
        if (!_adminPanelState.value.isAdminClaimVerified) {
            _statusMessage.value = SafarLocalization.text(lang, L10nKey.VM_ADMIN_NO_CLAIM)
            return
        }
        _adminPanelState.update { it.copy(isSavingConfig = true, lastActionMessage = null) }
        viewModelScope.launch {
            val res = firestoreRepo.updateAdminConfig(
                telegramBotHandle = telegramBotHandle,
                telegramWebhookConfigured = telegramWebhookConfigured,
                maintenanceNotice = maintenanceNotice
            )
            if (res.isSuccess) {
                val msg = SafarLocalization.text(preferences.value.language, L10nKey.VM_ADMIN_CONFIG_SAVED)
                _adminPanelState.update {
                    it.copy(
                        isSavingConfig = false,
                        lastActionMessage = msg
                    )
                }
                _statusMessage.value = msg
            } else {
                val errMsg = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(preferences.value.language, L10nKey.VM_ADMIN_CONFIG_DENIED)
                _adminPanelState.update {
                    it.copy(isSavingConfig = false, lastActionMessage = errMsg)
                }
                _statusMessage.value = errMsg
            }
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun submitRegistration(
        fullName: String,
        phone: String,
        passportSeries: String,
        preferredPaymentId: String,
        telegramUsername: String = "",
        telegramNotificationsEnabled: Boolean = false
    ) {
        if (fullName.isBlank()) {
            _statusMessage.value = SafarLocalization.text(preferences.value.language, L10nKey.VM_REG_ENTER_NAME)
            return
        }
        _registrationSubmitting.value = true
        viewModelScope.launch {
            val res = firestoreRepo.registerOrUpdateProfile(
                userId = currentUserId,
                fullName = fullName,
                phone = phone,
                passportSeries = passportSeries,
                preferredPaymentId = preferredPaymentId,
                telegramUsername = telegramUsername,
                telegramNotificationsEnabled = telegramNotificationsEnabled
            )
            val lang = preferences.value.language
            if (res.isSuccess) {
                localRepo.saveTravelerProfile(fullName, passportSeries, phone)
                localRepo.setPreferredPayment(preferredPaymentId)
                localRepo.clearPendingAuthChannels()
                _statusMessage.value = SafarLocalization.text(lang, L10nKey.VM_REG_SAVED)
            } else {
                _statusMessage.value = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(lang, L10nKey.VM_REG_ERR)
            }
            _registrationSubmitting.value = false
        }
    }

    fun toggleFavoriteDestination(destinationCode: String) {
        viewModelScope.launch {
            val res = firestoreRepo.toggleFavoriteDestination(currentUserId, destinationCode)
            if (res.isFailure) {
                _statusMessage.value = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(preferences.value.language, L10nKey.VM_FAV_ERR)
            }
        }
    }

    fun redeemClubPoints(points: Long = 5_000L) {
        val lang = preferences.value.language
        val current = (profileGateState.value as? ProfileGateState.Ready)?.profile?.clubPoints ?: 0L
        if (current < points) {
            _statusMessage.value = SafarLocalization.text(lang, L10nKey.VM_CLUB_MIN_POINTS, points, current)
            return
        }
        viewModelScope.launch {
            val res = firestoreRepo.redeemClubPoints(currentUserId, points)
            if (res.isSuccess) {
                _statusMessage.value = SafarLocalization.text(preferences.value.language, L10nKey.VM_CLUB_REDEEMED)
            } else {
                _statusMessage.value = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(preferences.value.language, L10nKey.VM_CLUB_ERR)
            }
        }
    }

    fun checkInBooking(bookingId: String) {
        if (_checkingInBookingId.value != null) return
        _checkingInBookingId.value = bookingId
        viewModelScope.launch {
            val res = firestoreRepo.checkInBooking(currentUserId, bookingId)
            _checkingInBookingId.value = null
            val lang = preferences.value.language
            if (res.isSuccess) {
                _statusMessage.value = SafarLocalization.text(lang, L10nKey.VM_CHECKIN_OK)
            } else {
                _statusMessage.value = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(lang, L10nKey.VM_CHECKIN_ERR)
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        if (_cancellingBookingId.value != null) return
        _cancellingBookingId.value = bookingId
        viewModelScope.launch {
            val res = firestoreRepo.cancelBooking(currentUserId, bookingId)
            _cancellingBookingId.value = null
            val lang = preferences.value.language
            if (res.isSuccess) {
                _statusMessage.value = SafarLocalization.text(lang, L10nKey.VM_CANCEL_OK)
            } else {
                _statusMessage.value = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(lang, L10nKey.VM_CANCEL_ERR)
            }
        }
    }

    fun selectMode(mode: TravelMode) {
        val lang = preferences.value.language
        _searchState.update { state ->
            val adjustedDest = if (mode == TravelMode.FLIGHTS && state.destinationCode == state.originCode) {
                "IST"
            } else if (state.destinationCode == state.originCode && mode != TravelMode.HOTELS) {
                "SKD"
            } else {
                state.destinationCode
            }
            state.copy(
                mode = mode,
                destinationCode = adjustedDest,
                rawResults = localRepo.searchRoutes(mode, state.originCode, adjustedDest, lang),
                activeFilter = ResultFilter.ALL
            )
        }
    }

    fun setOrigin(code: String) {
        val lang = preferences.value.language
        _searchState.update { state ->
            val nextDest = if (code == state.destinationCode && state.mode != TravelMode.HOTELS) {
                SafarCatalog.hubs.first { it.code != code }.code
            } else {
                state.destinationCode
            }
            state.copy(
                originCode = code,
                destinationCode = nextDest,
                rawResults = localRepo.searchRoutes(state.mode, code, nextDest, lang)
            )
        }
    }

    fun setDestination(code: String) {
        val lang = preferences.value.language
        _searchState.update { state ->
            val nextOrigin = if (code == state.originCode && state.mode != TravelMode.HOTELS) {
                SafarCatalog.hubs.first { it.code != code }.code
            } else {
                state.originCode
            }
            state.copy(
                originCode = nextOrigin,
                destinationCode = code,
                rawResults = localRepo.searchRoutes(state.mode, nextOrigin, code, lang)
            )
        }
    }

    fun swapOriginDestination() {
        val lang = preferences.value.language
        _searchState.update { state ->
            state.copy(
                originCode = state.destinationCode,
                destinationCode = state.originCode,
                rawResults = localRepo.searchRoutes(state.mode, state.destinationCode, state.originCode, lang)
            )
        }
    }

    fun setDepartureDate(date: String) {
        _searchState.update { it.copy(departureDate = date) }
    }

    fun setPassengerCount(count: Int) {
        _searchState.update { it.copy(passengerCount = count.coerceIn(1, 6)) }
    }

    fun executeSearch() {
        val lang = preferences.value.language
        _searchState.update { state ->
            state.copy(
                rawResults = localRepo.searchRoutes(state.mode, state.originCode, state.destinationCode, lang),
                activeFilter = ResultFilter.ALL
            )
        }
    }

    fun launchDeal(deal: CorridorDeal) {
        val lang = preferences.value.language
        _searchState.update { state ->
            state.copy(
                mode = deal.mode,
                originCode = deal.originCode,
                destinationCode = deal.destinationCode,
                rawResults = localRepo.searchRoutes(deal.mode, deal.originCode, deal.destinationCode, lang),
                activeFilter = ResultFilter.ALL
            )
        }
    }

    fun launchQuickDestination(destinationCode: String, mode: TravelMode = TravelMode.RAIL) {
        val lang = preferences.value.language
        _searchState.update { state ->
            val origin = if (destinationCode == "TAS") "SKD" else "TAS"
            state.copy(
                mode = mode,
                originCode = origin,
                destinationCode = destinationCode,
                rawResults = localRepo.searchRoutes(mode, origin, destinationCode, lang),
                activeFilter = ResultFilter.ALL
            )
        }
    }

    fun setResultFilter(filter: ResultFilter) {
        _searchState.update { it.copy(activeFilter = filter) }
    }

    fun startBooking(option: TravelOption) {
        val profile = (profileGateState.value as? ProfileGateState.Ready)?.profile
        val prefs = preferences.value
        val seats = if (option.hasSeatMap) localRepo.generateSeatMap(option) else emptyList()
        val defaultSeat = seats.firstOrNull { !it.isOccupied && !it.isVipOrExtraLegroom }
            ?: seats.firstOrNull { !it.isOccupied }

        _checkoutState.value = CheckoutUiState(
            option = option,
            seatMap = seats,
            selectedSeat = defaultSeat,
            passengerName = (profile?.fullName ?: prefs.savedPassengerName).uppercase(),
            passportSeries = (profile?.passportSeries ?: prefs.savedPassportSeries).uppercase(),
            phone = (profile?.phone ?: prefs.savedPhone),
            selectedPaymentId = profile?.preferredPaymentId ?: prefs.preferredPaymentId,
            applyClubPoints = false,
            step = if (option.hasSeatMap) 0 else 1,
            isSubmitting = false,
            validationError = null
        )
    }

    fun selectSeat(seat: SeatItem) {
        if (!seat.isOccupied) {
            _checkoutState.update { it.copy(selectedSeat = seat) }
        }
    }

    fun setCheckoutStep(step: Int) {
        _checkoutState.update { it.copy(step = step.coerceIn(0, 2), validationError = null) }
    }

    fun updatePassengerName(value: String) {
        _checkoutState.update { it.copy(passengerName = value, validationError = null) }
    }

    fun updatePassportSeries(value: String) {
        _checkoutState.update { it.copy(passportSeries = value, validationError = null) }
    }

    fun updatePhone(value: String) {
        _checkoutState.update { it.copy(phone = value, validationError = null) }
    }

    fun toggleApplyClubPoints() {
        _checkoutState.update { it.copy(applyClubPoints = !it.applyClubPoints) }
    }

    fun fillSavedTraveler() {
        val profile = (profileGateState.value as? ProfileGateState.Ready)?.profile
        val prefs = preferences.value
        val resolvedName = (profile?.fullName ?: prefs.savedPassengerName).uppercase()
        val resolvedPassport = (profile?.passportSeries ?: prefs.savedPassportSeries).uppercase()
        val resolvedPhone = profile?.phone ?: prefs.savedPhone
        if (resolvedName.isBlank() && resolvedPassport.isBlank() && resolvedPhone.isBlank()) {
            _statusMessage.value = SafarLocalization.text(prefs.language, L10nKey.VM_PROFILE_SAVE_FIRST)
            return
        }
        _checkoutState.update {
            it.copy(
                passengerName = resolvedName,
                passportSeries = resolvedPassport,
                phone = resolvedPhone,
                validationError = null
            )
        }
    }

    fun selectPaymentRail(paymentId: String) {
        _checkoutState.update { it.copy(selectedPaymentId = paymentId) }
        viewModelScope.launch {
            localRepo.setPreferredPayment(paymentId)
            val res = firestoreRepo.updatePreferredPayment(currentUserId, paymentId)
            if (res.isFailure) {
                _statusMessage.value = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(preferences.value.language, L10nKey.VM_PAY_SAVE_ERR)
            }
        }
    }

    fun confirmAndIssueTicket(onBookingCreated: (String) -> Unit) {
        val checkout = _checkoutState.value
        val option = checkout.option ?: return
        val lang = preferences.value.language
        if (checkout.passengerName.isBlank() || checkout.passportSeries.isBlank()) {
            _checkoutState.update {
                it.copy(
                    step = 1,
                    validationError = SafarLocalization.text(lang, L10nKey.VM_CHECKOUT_ENTER_DETAILS)
                )
            }
            return
        }
        val cleanPhoneDigits = checkout.phone.filter { it.isDigit() }
        if (checkout.phone.isNotBlank() && cleanPhoneDigits.length < 9) {
            _checkoutState.update {
                it.copy(
                    step = 1,
                    validationError = SafarLocalization.text(lang, L10nKey.VM_CHECKOUT_INVALID_PHONE)
                )
            }
            return
        }

        val rail = SafarCatalog.paymentRails.find { it.id == checkout.selectedPaymentId }
            ?: SafarCatalog.paymentRails.first()
        val paymentLabel = "${rail.title} (${rail.badgeText})"
        val seatLabel = when {
            option.mode == TravelMode.RAIL && checkout.selectedSeat != null ->
                SafarLocalization.text(lang, L10nKey.VM_SEAT_COACH_FORMAT, checkout.selectedSeat.code)
            checkout.selectedSeat != null -> checkout.selectedSeat.code
            option.mode == TravelMode.HOTELS -> "Suite 408"
            option.mode == TravelMode.ESIM -> "QR eSIM"
            else -> "VIP Salon"
        }
        val seatExtra = checkout.selectedSeat?.extraPriceUzs ?: 0L
        val clubPtsAvailable = (profileGateState.value as? ProfileGateState.Ready)?.profile?.clubPoints ?: 0L
        val pointsUsed = if (checkout.applyClubPoints && clubPtsAvailable >= 5_000L) 5_000L else 0L

        _checkoutState.update { it.copy(isSubmitting = true, validationError = null) }
        viewModelScope.launch {
            val res = firestoreRepo.createBooking(
                userId = currentUserId,
                option = option,
                travelDate = _searchState.value.departureDate,
                seatNumber = seatLabel,
                seatExtraUzs = seatExtra,
                passengerName = checkout.passengerName,
                passportSeries = checkout.passportSeries,
                phone = checkout.phone,
                paymentMethodLabel = paymentLabel,
                redeemPointsUsed = pointsUsed
            )
            _checkoutState.update { it.copy(isSubmitting = false) }
            val currentLang = preferences.value.language
            res.onSuccess { bookingId ->
                _statusMessage.value = SafarLocalization.text(currentLang, L10nKey.VM_CHECKOUT_SUCCESS)
                onBookingCreated(bookingId)
            }.onFailure { err ->
                _checkoutState.update {
                    it.copy(
                        validationError = err.localizedMessage
                            ?: SafarLocalization.text(currentLang, L10nKey.VM_CHECKOUT_ERR)
                    )
                }
            }
        }
    }

    fun setCurrency(currency: CurrencyCode) {
        viewModelScope.launch { localRepo.setCurrency(currency) }
    }

    fun toggleCurrency() {
        val next = if (preferences.value.currency == CurrencyCode.UZS) CurrencyCode.USD else CurrencyCode.UZS
        setCurrency(next)
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            localRepo.setLanguage(language)
            _searchState.update { state ->
                state.copy(
                    rawResults = localRepo.searchRoutes(
                        state.mode,
                        state.originCode,
                        state.destinationCode,
                        language
                    )
                )
            }
        }
    }

    fun cycleLanguage() {
        val langs = AppLanguage.entries
        val next = langs[(langs.indexOf(preferences.value.language) + 1) % langs.size]
        setLanguage(next)
    }

    fun setThemePreference(theme: ThemePreference) {
        viewModelScope.launch { localRepo.setThemePreference(theme) }
    }

    fun toggleDarkTheme(currentlyDark: Boolean) {
        setThemePreference(if (currentlyDark) ThemePreference.LIGHT else ThemePreference.DARK)
    }

    fun saveTravelerProfile(
        name: String,
        passport: String,
        phone: String,
        telegramUsername: String = (profileGateState.value as? ProfileGateState.Ready)?.profile?.telegramUsername.orEmpty(),
        telegramNotificationsEnabled: Boolean = (profileGateState.value as? ProfileGateState.Ready)?.profile?.telegramNotificationsEnabled ?: false
    ) {
        if (name.isBlank()) {
            _statusMessage.value = SafarLocalization.text(preferences.value.language, L10nKey.VM_PROFILE_NAME_REQUIRED)
            return
        }
        val preferredPayment = (profileGateState.value as? ProfileGateState.Ready)?.profile?.preferredPaymentId
            ?: preferences.value.preferredPaymentId
        _isSavingProfile.value = true
        viewModelScope.launch {
            val res = firestoreRepo.registerOrUpdateProfile(
                userId = currentUserId,
                fullName = name,
                phone = phone,
                passportSeries = passport,
                preferredPaymentId = preferredPayment,
                telegramUsername = telegramUsername,
                telegramNotificationsEnabled = telegramNotificationsEnabled
            )
            _isSavingProfile.value = false
            val lang = preferences.value.language
            if (res.isSuccess) {
                localRepo.saveTravelerProfile(name, passport, phone)
                _statusMessage.value = SafarLocalization.text(lang, L10nKey.VM_PROFILE_SAVED)
            } else {
                _statusMessage.value = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(lang, L10nKey.VM_PROFILE_ERR)
            }
        }
    }

    fun updateTelegramBotSettings(telegramUsername: String, enabled: Boolean) {
        viewModelScope.launch {
            val res = firestoreRepo.updateTelegramBotSettings(currentUserId, telegramUsername, enabled)
            val lang = preferences.value.language
            if (res.isSuccess) {
                _statusMessage.value = if (enabled) {
                    SafarLocalization.text(lang, L10nKey.VM_TG_PREF_SAVED_WEBHOOK)
                } else {
                    SafarLocalization.text(lang, L10nKey.VM_TG_PREF_SAVED)
                }
            } else {
                _statusMessage.value = res.exceptionOrNull()?.localizedMessage
                    ?: SafarLocalization.text(lang, L10nKey.VM_TG_ERR)
            }
        }
    }

    class Factory(
        private val currentUserId: String,
        private val firestoreRepo: FirestoreTravelRepository,
        private val localRepo: SafarGoRepository,
        private val telegramBotService: TelegramBotService = TelegramBotService()
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SafarGoViewModel(currentUserId, firestoreRepo, localRepo, telegramBotService) as T
        }
    }

    private companion object {
        const val TAG = "SafarGoViewModel"
    }
}
