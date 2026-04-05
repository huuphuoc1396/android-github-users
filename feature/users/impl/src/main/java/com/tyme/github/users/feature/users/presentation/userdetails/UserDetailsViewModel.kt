package com.tyme.github.users.feature.users.presentation.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.feature.users.api.model.UserModel
import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val favoriteRepository: FavoriteRepository,
    private val navigator: AppNavigator,
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

    fun onFavoriteClick() {
        if (isFavorite.value) {
            _uiState.update { current ->
                if (current is UserDetailUiState.Success) current.copy(showRemoveConfirmDialog = true) else current
            }
        } else {
            viewModelScope.launch {
                favoriteRepository.addFavorite(
                    UserModel(
                        username = destination.username,
                        avatarUrl = destination.avatarUrl,
                        url = destination.url,
                    )
                )
            }
        }
    }

    fun onConfirmRemoveFavorite() {
        _uiState.update { current ->
            if (current is UserDetailUiState.Success) current.copy(showRemoveConfirmDialog = false) else current
        }
        viewModelScope.launch { favoriteRepository.removeFavorite(destination.username) }
    }

    fun onDismissRemoveFavorite() {
        _uiState.update { current ->
            if (current is UserDetailUiState.Success) current.copy(showRemoveConfirmDialog = false) else current
        }
    }

    fun dismissError() {
        _uiState.value = UserDetailUiState.Idle
    }

    fun onNavigateBack() {
        viewModelScope.launch { navigator.navigate(NavigationIntent.NavigateUp) }
    }
}
