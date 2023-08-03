package com.yvkalume.eventcademy.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yvkalume.eventcademy.data.entity.User
import com.yvkalume.eventcademy.data.repository.AdvertisementRepository
import com.yvkalume.eventcademy.data.repository.EventRepository
import com.yvkalume.eventcademy.data.repository.UserRepository
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
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val advertisementRepository: AdvertisementRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser : StateFlow<User?> = _user.asStateFlow()

    val uiState: StateFlow<HomeUiState> = combine(
        eventRepository.getAllUpComingEventsStream(),
        advertisementRepository.getAllAdvertisementsStream()
    ) { events, advertisements ->
        HomeUiState.Success(
            events = events,
            advertisements = advertisements
        )
    }.catch<HomeUiState> {
        emit(HomeUiState.Error(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    init {
        getCurrentUser()
    }


    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                _user.emit(userRepository.getCurrentUser())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "getCurrentUser: ", e)
            }
        }
    }
}