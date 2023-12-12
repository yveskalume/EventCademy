package com.yveskalume.eventcademy.feature.eventdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.repository.EventBookingRepository
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import com.yveskalume.eventcademy.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val eventBookingRepository: EventBookingRepository
) : ViewModel() {

    private val eventUid = savedStateHandle.get<String>("eventUid")

    private val _uiState: MutableStateFlow<EventDetailUiState> =
        MutableStateFlow(EventDetailUiState.Loading)
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    private val _eventBookingState: MutableStateFlow<EventBookingState> =
        MutableStateFlow(EventBookingState.LOADING)
    val eventBookingState: StateFlow<EventBookingState> = _eventBookingState.asStateFlow()

    private val _uiEffect: Channel<EventDetailUiEffect> = Channel()
    val uiEffect: Flow<EventDetailUiEffect> = _uiEffect.receiveAsFlow()

    init {
        getData()
    }

    private fun getData() {
        if (eventUid != null) {
            getEvent(eventUid)
            checkIfUserHasBooked(eventUid)
        }
    }

    private fun checkIfUserHasBooked(eventUid: String) {
        viewModelScope.launch {
            try {
                eventBookingRepository.checkIfUserHasBooked(eventUid).collect { hasBooked ->
                    if (hasBooked) {
                        _eventBookingState.emit(EventBookingState.BOOKED)
                    } else {
                        _eventBookingState.emit(EventBookingState.NOT_BOOKED)
                    }
                }
            } catch (t: Throwable) {
                Log.e("EventDetailViewModel", "checkIfUserHasBooked: ${t.message}")
            }
        }
    }

    private fun getEvent(eventUid: String) {
        viewModelScope.launch {
            _uiState.emit(EventDetailUiState.Loading)
            try {
                val eventDeferred = async {
                    eventRepository.getEventByUid(eventUid)
                }
                val event = eventDeferred.await()
                if (event == null) {
                    _uiState.emit(EventDetailUiState.Error("Cet evenment n'est pas encore disponible ou a été supprimé"))
                    return@launch
                }

                val userDeferred = async {
                    userRepository.getUserByUid(event.userUid)
                }
                eventBookingRepository.getAllBookingByEventUid(eventUid).collect { bookings ->
                    _uiState.emit(EventDetailUiState.Success(event, userDeferred.await(), bookings))
                }
            } catch (t: Throwable) {
                _uiState.emit(EventDetailUiState.Error(t.message ?: "Une erreur est survenue"))
            }
        }
    }

    fun toggleUserEventBooking(event: Event, eventBookingState: EventBookingState) {
        viewModelScope.launch {
            try {
                if (eventBookingState == EventBookingState.BOOKED) {
                    eventBookingRepository.deleteBooking(event.uid)
                } else {
                    eventBookingRepository.createBooking(event)
                    _uiEffect.trySend(EventDetailUiEffect.ShowCongratulations)
                }
            } catch (t: Throwable) {
                _uiState.emit(EventDetailUiState.Error(t.message ?: "Une erreur est survenue"))
            }
        }
    }
}