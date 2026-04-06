package com.tyme.github.users.feature.favorites.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.core.common.providers.DispatchersProvider
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.feature.favorites.presentation.mappers.toUserListItem
import com.tyme.github.users.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val navigator: AppNavigator,
    private val dispatchers: DispatchersProvider,
) : ViewModel() {

    private val _pendingRemoval = MutableStateFlow<UserListItem?>(null)

    val uiState: StateFlow<FavoritesUiState> =
        combine(
            getFavoritesUseCase().map { list -> list.map { it.toUserListItem() } },
            _pendingRemoval,
        ) { favorites, pending ->
            if (favorites.isEmpty()) FavoritesUiState.Empty
            else FavoritesUiState.Success(favorites = favorites, pendingRemoval = pending)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState.Loading,
        )

    fun onRemoveFavoriteClick(user: UserListItem) {
        _pendingRemoval.value = user
    }

    fun onConfirmRemoveFavorite() {
        val user = _pendingRemoval.value ?: return
        _pendingRemoval.value = null
        viewModelScope.launch(dispatchers.io) {
            removeFavoriteUseCase(user.username)
        }
    }

    fun onDismissRemoveFavorite() {
        _pendingRemoval.value = null
    }

    fun onNavigateToUser(user: UserListItem) {
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
}
