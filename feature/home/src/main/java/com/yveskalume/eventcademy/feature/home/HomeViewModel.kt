package com.yveskalume.eventcademy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yveskalume.eventcademy.core.domain.repository.AdvertisementRepository
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    advertisementRepository: AdvertisementRepository,
    eventRepository: EventRepository,
) : ViewModel() {


    val uiState: StateFlow<HomeUiState> = combine(
        eventRepository.getAllEventsStream(),
        advertisementRepository.getAllAdvertisementsStream()
    ) { events, advertisements ->
        HomeUiState.Success(
            upcomingEvents = events.filter { Date().before(it.endDate) },
            pastEvents = events.filter { Date().after(it.endDate) }
                .sortedByDescending { it.startDate },
            advertisements = advertisements
        )
    }.catch<HomeUiState> {
        emit(HomeUiState.Error(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )
}