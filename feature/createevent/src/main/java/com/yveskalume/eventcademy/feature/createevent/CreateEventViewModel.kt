package com.yveskalume.eventcademy.feature.createevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.yveskalume.eventcademy.core.data.firebase.repository.EventRepositoryImpl
import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<CreateEventUiState> =
        MutableStateFlow(CreateEventUiState.Initial)

    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    fun createEvent(event: Event) {
        viewModelScope.launch {
            _uiState.emit(CreateEventUiState.Loading)
            try {
                eventRepository.createEvent(event)
                _uiState.emit(CreateEventUiState.Success)
            } catch (e: Exception) {
                _uiState.emit(CreateEventUiState.Error("Une erreur est survenue"))
            }
        }
    }
}