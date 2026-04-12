package com.tyme.github.users.feature.favorites.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyme.github.users.core.common.dispatcher.CoroutineDispatchers
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.core.ui.extensions.toUiText
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.feature.favorites.presentation.mappers.toUserListItem
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FavoritesViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val navigator: AppNavigator,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    val favorites: StateFlow<List<UserListItem>> = getFavoritesUseCase()
        .map { list -> list.map { it.toUserListItem() } }
        .catch { throwable -> _uiState.value = FavoritesUiState.LoadError(throwable.toUiText()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Idle)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun onAction(action: FavoritesUiAction) {
        when (action) {
            is FavoritesUiAction.UserClick -> onNavigateToUser(action.user)
            is FavoritesUiAction.RemoveFavoriteClick -> onRemoveFavoriteClick(action.user)
            FavoritesUiAction.ConfirmRemoveFavorite -> onConfirmRemoveFavorite()
            FavoritesUiAction.DismissRemoveFavorite -> onDismissRemoveFavorite()
            FavoritesUiAction.DismissError -> onDismissError()
            is FavoritesUiAction.UrlClick -> onUrlClick(action.url)
        }
    }

    private fun onRemoveFavoriteClick(user: UserListItem) {
        _uiState.value = FavoritesUiState.ConfirmRemoval(user)
    }

    private fun onConfirmRemoveFavorite() {
        val item = (_uiState.value as? FavoritesUiState.ConfirmRemoval)?.item ?: return
        _uiState.value = FavoritesUiState.Idle
        viewModelScope.launch(dispatchers.io) {
            removeFavoriteUseCase(item.username)
                .onFailure { throwable -> _uiState.value = FavoritesUiState.RemovalError(throwable.toUiText()) }
        }
    }

    private fun onDismissRemoveFavorite() {
        _uiState.value = FavoritesUiState.Idle
    }

    private fun onDismissError() {
        _uiState.value = FavoritesUiState.Idle
    }

    private fun onNavigateToUser(user: UserListItem) {
        viewModelScope.launch {
            navigator.navigate(
                NavigationIntent.NavigateTo(
                    route = UserDetailsDestination(
                        username = user.username,
                        avatarUrl = user.avatarUrl,
                        url = user.url,
                    )
                )
            )
        }
    }

    private fun onUrlClick(url: String) {
        viewModelScope.launch { navigator.navigate(NavigationIntent.OpenUrl(url)) }
    }
}
