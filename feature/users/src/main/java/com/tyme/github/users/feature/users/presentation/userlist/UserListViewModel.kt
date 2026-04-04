package com.tyme.github.users.feature.users.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.tyme.github.users.feature.favorites.api.model.FavoriteUser
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import com.tyme.github.users.feature.users.domain.model.UserModel
import com.tyme.github.users.feature.users.domain.usecase.GetUserPagingUseCase
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
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Idle)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    val userPaging = getUserPagingUseCase().cachedIn(viewModelScope)

    val favoriteUsernames: StateFlow<Set<String>> = favoriteRepository.getFavorites()
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

    fun onFavoriteToggle(user: UserModel) {
        viewModelScope.launch {
            if (user.username in favoriteUsernames.value) {
                favoriteRepository.removeFavorite(user.username)
            } else {
                favoriteRepository.addFavorite(
                    FavoriteUser(
                        id = user.id,
                        username = user.username,
                        avatarUrl = user.avatarUrl,
                        url = user.url,
                    )
                )
            }
        }
    }
}
