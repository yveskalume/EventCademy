package com.yveskalume.eventcademy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yveskalume.eventcademy.core.domain.repository.AdvertisementRepository
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    advertisementRepository: AdvertisementRepository,
    eventRepository: EventRepository,
) : ViewModel() {


    val uiState: StateFlow<HomeUiState> = eventRepository.getAllEventsStream().map {
        HomeUiState.Success(
            pastEvents = it
        )
    }.catch<HomeUiState> {
        emit(HomeUiState.Error(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )
}