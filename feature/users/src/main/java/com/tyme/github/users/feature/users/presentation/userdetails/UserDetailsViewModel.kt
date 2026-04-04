package com.tyme.github.users.feature.users.presentation.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tyme.github.users.feature.favorites.api.model.FavoriteUser
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import com.tyme.github.users.feature.users.data.mapper.toUserDetailUiState
import com.tyme.github.users.feature.users.domain.usecase.GetUserDetailsUseCase
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import com.tyme.github.users.feature.users.presentation.mappers.toUserDetailError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val destination: UserDetailsDestination = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favoriteRepository.isFavorite(destination.username)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    init {
        loadUserDetails()
    }

    fun loadUserDetails() {
        viewModelScope.launch {
            _uiState.value = UserDetailUiState.Loading
            getUserDetailsUseCase(destination.username)
                .catch { e -> _uiState.value = e.toUserDetailError() }
                .collect { details -> _uiState.value = details.toUserDetailUiState() }
        }
    }

    fun onFavoriteToggle() {
        viewModelScope.launch {
            if (isFavorite.value) {
                favoriteRepository.removeFavorite(destination.username)
            } else {
                favoriteRepository.addFavorite(
                    FavoriteUser(
                        username = destination.username,
                        avatarUrl = destination.avatarUrl,
                        url = destination.url,
                    )
                )
            }
        }
    }

    fun dismissError() {
        _uiState.value = UserDetailUiState.Idle
    }
}
