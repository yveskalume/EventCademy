package com.yveskalume.eventcademy.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.yveskalume.eventcademy.data.repository.EventRepository
import com.yveskalume.eventcademy.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _isEventDeleting: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isEventDeleting: StateFlow<Boolean> = _isEventDeleting.asStateFlow()

    val uiState: StateFlow<ProfileUiState> = combine(
        userRepository.getCurrentUserStream(),
        eventRepository.getEventCreatedByCurrentUser()
    ) { user, events ->
        ProfileUiState.Success(user, events)
    }.catch<ProfileUiState> {
        emit(ProfileUiState.Error(it.localizedMessage ?: "Une erreur est survenue"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProfileUiState.Loading
    )


    fun deleteEvent(eventUid: String) {
        viewModelScope.launch {
            try {
                _isEventDeleting.emit(true)
                eventRepository.deleteEvent(eventUid)
                _isEventDeleting.emit(false)
            } catch (t: Throwable) {
                Firebase.crashlytics.recordException(t)
            }
        }
    }


}