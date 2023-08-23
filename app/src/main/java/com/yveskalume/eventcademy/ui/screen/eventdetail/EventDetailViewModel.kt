package com.yveskalume.eventcademy.ui.screen.eventdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yveskalume.eventcademy.data.entity.Event
import com.yveskalume.eventcademy.data.repository.EventBookingRepository
import com.yveskalume.eventcademy.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val userRepository: EventRepository,
    private val eventRepository: EventRepository,
    private val eventBookingRepository: EventBookingRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<EventDetailUiState> =
        MutableStateFlow(EventDetailUiState.Loading)
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    private val _eventBookingState: MutableStateFlow<EventBookingState> =
        MutableStateFlow(EventBookingState.LOADING)
    val eventBookingState: StateFlow<EventBookingState> = _eventBookingState.asStateFlow()

    fun getData(eventUid: String) {
        getEvent(eventUid)
        checkIfUserHasBooked(eventUid)
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
                }
            } catch (t: Throwable) {
                _uiState.emit(EventDetailUiState.Error(t.message ?: "Une erreur est survenue"))
            }
        }
    }
}