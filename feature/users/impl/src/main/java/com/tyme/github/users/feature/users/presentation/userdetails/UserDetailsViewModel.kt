package com.tyme.github.users.feature.users.presentation.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.core.common.providers.DispatchersProvider
import com.tyme.github.users.feature.users.data.mapper.toUserDetailUiState
import com.tyme.github.users.feature.favorites.domain.usecase.AddFavoriteUseCase
import com.tyme.github.users.feature.users.domain.usecase.GetUserDetailsUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.IsFavoriteUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import com.tyme.github.users.core.ui.extensions.toUiText
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
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val navigator: AppNavigator,
    private val dispatchers: DispatchersProvider,
) : ViewModel() {

    private val destination: UserDetailsDestination = savedStateHandle.toRoute()

    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = isFavoriteUseCase(destination.username)
        .catch { emit(false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    init {
        loadUserDetails()
    }

    fun loadUserDetails() {
        viewModelScope.launch(dispatchers.io) {
            _uiState.value = UserDetailUiState.Loading
            getUserDetailsUseCase(destination.username)
                .catch { e -> _uiState.value = UserDetailUiState.Error(e.toUiText()) }
                .collect { details -> _uiState.value = details.toUserDetailUiState() }
        }
    }

    fun onAction(action: UserDetailsUiAction) {
        when (action) {
            UserDetailsUiAction.NavigateBack -> onNavigateBack()
            UserDetailsUiAction.FavoriteToggle -> onFavoriteClick()
            UserDetailsUiAction.ConfirmRemoveFavorite -> onConfirmRemoveFavorite()
            UserDetailsUiAction.DismissRemoveFavorite -> onDismissRemoveFavorite()
            UserDetailsUiAction.DismissError -> dismissError()
            is UserDetailsUiAction.BlogClick ->
                viewModelScope.launch { navigator.navigate(NavigationIntent.OpenUrl(action.url)) }
        }
    }

    private fun onFavoriteClick() {
        if (isFavorite.value) {
            _uiState.update { current ->
                if (current is UserDetailUiState.Success) current.copy(showRemoveConfirmDialog = true) else current
            }
        } else {
            viewModelScope.launch(dispatchers.io) {
                addFavoriteUseCase(
                    UserModel(
                        username = destination.username,
                        avatarUrl = destination.avatarUrl,
                        url = destination.url,
                    )
                ).onFailure { e -> _uiState.value = UserDetailUiState.Error(e.toUiText()) }
            }
        }
    }

    private fun onConfirmRemoveFavorite() {
        _uiState.update { current ->
            if (current is UserDetailUiState.Success) current.copy(showRemoveConfirmDialog = false) else current
        }
        viewModelScope.launch(dispatchers.io) {
            removeFavoriteUseCase(destination.username)
                .onFailure { e -> _uiState.value = UserDetailUiState.Error(e.toUiText()) }
        }
    }

    private fun onDismissRemoveFavorite() {
        _uiState.update { current ->
            if (current is UserDetailUiState.Success) current.copy(showRemoveConfirmDialog = false) else current
        }
    }

    private fun dismissError() {
        _uiState.value = UserDetailUiState.Idle
    }

    private fun onNavigateBack() {
        viewModelScope.launch { navigator.navigate(NavigationIntent.NavigateUp) }
    }
}
