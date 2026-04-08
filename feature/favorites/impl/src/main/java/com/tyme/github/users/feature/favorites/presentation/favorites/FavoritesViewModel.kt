package com.tyme.github.users.feature.favorites.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.core.common.providers.DispatchersProvider
import com.tyme.github.users.core.network.models.errors.toNetworkErrorMessage
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.feature.favorites.domain.usecase.GetFavoritePagingUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.feature.favorites.presentation.mappers.toUserListItem
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoritePagingUseCase: GetFavoritePagingUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val navigator: AppNavigator,
    private val dispatchers: DispatchersProvider,
) : ViewModel() {

    val favorites: Flow<PagingData<UserListItem>> = getFavoritePagingUseCase()
        .map { pagingData -> pagingData.map { it.toUserListItem() } }
        .cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Idle)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun onRemoveFavoriteClick(user: UserListItem) {
        _uiState.value = FavoritesUiState.ConfirmRemoval(user)
    }

    fun onConfirmRemoveFavorite() {
        val item = (_uiState.value as? FavoritesUiState.ConfirmRemoval)?.item ?: return
        _uiState.value = FavoritesUiState.Idle
        viewModelScope.launch(dispatchers.io) {
            removeFavoriteUseCase(item.username).onFailure { throwable ->
                val params = throwable.toNetworkErrorMessage()
                _uiState.value = FavoritesUiState.RemovalError(
                    message = params.message,
                    messageRes = params.messageRes,
                )
            }
        }
    }

    fun onDismissRemoveFavorite() {
        _uiState.value = FavoritesUiState.Idle
    }

    fun onDismissError() {
        _uiState.value = FavoritesUiState.Idle
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
