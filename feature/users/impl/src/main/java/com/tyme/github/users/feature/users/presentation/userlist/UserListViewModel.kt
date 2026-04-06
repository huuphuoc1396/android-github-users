package com.tyme.github.users.feature.users.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.map
import com.tyme.github.users.core.navigation.AppNavigator
import com.tyme.github.users.core.navigation.NavigationIntent
import com.tyme.github.users.core.common.providers.DispatchersProvider
import com.tyme.github.users.core.ui.components.UserListItem
import com.tyme.github.users.feature.favorites.domain.usecase.AddFavoriteUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.GetFavoritesUseCase
import com.tyme.github.users.feature.users.domain.usecase.GetUserPagingUseCase
import com.tyme.github.users.feature.favorites.domain.usecase.RemoveFavoriteUseCase
import com.tyme.github.users.feature.users.navigation.UserDetailsDestination
import com.tyme.github.users.feature.users.presentation.mappers.toUserListItem
import com.tyme.github.users.feature.users.presentation.mappers.toUserModel
import com.tyme.github.users.feature.users.presentation.mappers.toUserListError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    getUserPagingUseCase: GetUserPagingUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val navigator: AppNavigator,
    private val dispatchers: DispatchersProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Idle)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    val userPaging = getUserPagingUseCase()
        .map { pagingData -> pagingData.map { it.toUserListItem() } }
        .cachedIn(viewModelScope)

    val favoriteUsernames: StateFlow<Set<String>> = getFavoritesUseCase()
        .map { list -> list.map { it.username }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet(),
        )

    fun onRefreshLoadState(loadState: LoadState) {
        _uiState.update { current ->
            when (loadState) {
                is LoadState.Loading -> current as? UserListUiState.Success ?: UserListUiState.Loading
                is LoadState.NotLoading -> if (current is UserListUiState.Success) current.copy(isRefreshing = false) else UserListUiState.Success()
                is LoadState.Error -> loadState.error.toUserListError()
            }
        }
    }

    fun onRefreshTriggered() {
        _uiState.update { current ->
            if (current is UserListUiState.Success) current.copy(isRefreshing = true) else UserListUiState.Loading
        }
    }

    fun dismissError() {
        _uiState.update { UserListUiState.Success() }
    }

    fun onFavoriteClick(user: UserListItem) {
        if (user.username in favoriteUsernames.value) {
            _uiState.update { current ->
                if (current is UserListUiState.Success) current.copy(pendingRemoval = user) else current
            }
        } else {
            viewModelScope.launch(dispatchers.io) { addFavoriteUseCase(user.toUserModel()) }
        }
    }

    fun onConfirmRemoveFavorite() {
        val user = (_uiState.value as? UserListUiState.Success)?.pendingRemoval ?: return
        _uiState.update { current ->
            if (current is UserListUiState.Success) current.copy(pendingRemoval = null) else current
        }
        viewModelScope.launch(dispatchers.io) { removeFavoriteUseCase(user.username) }
    }

    fun onDismissRemoveFavorite() {
        _uiState.update { current ->
            if (current is UserListUiState.Success) current.copy(pendingRemoval = null) else current
        }
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
