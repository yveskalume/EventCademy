package com.yveskalume.eventcademy.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.yveskalume.eventcademy.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState>
        get() = _uiState

    fun signInWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                userRepository.signInWithCredential(credential)
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Une erreur est survenue")
            }
        }
    }

    fun startAuthFlow() {
        _uiState.value = AuthUiState.Loading
    }
}