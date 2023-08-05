package com.yveskalume.eventcademy.ui.screen.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yveskalume.eventcademy.data.repository.EventBookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val eventBookingRepository: EventBookingRepository
) : ViewModel() {

    val uiState: StateFlow<BookmarkUiState> = eventBookingRepository.getAllUserEventBookings().map {
        BookmarkUiState.Success(it)
    }.catch<BookmarkUiState> {
        emit(BookmarkUiState.Error(it.message ?: "An error occurred"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BookmarkUiState.Loading
    )

}