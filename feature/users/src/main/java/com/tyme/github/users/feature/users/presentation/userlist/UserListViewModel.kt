package com.tyme.github.users.feature.users.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.tyme.github.users.feature.users.domain.usecase.GetUserPagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    getUserPagingUseCase: GetUserPagingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Idle)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    val userPaging = getUserPagingUseCase().cachedIn(viewModelScope)

    fun onRefreshLoadState(loadState: LoadState) {
        _uiState.update { current ->
            when (loadState) {
                is LoadState.Loading -> current as? UserListUiState.Success ?: UserListUiState.Loading
                is LoadState.NotLoading -> if (current is UserListUiState.Success) current.copy(isRefreshing = false) else UserListUiState.Success()
                is LoadState.Error -> UserListUiState.Error(loadState.error.message ?: "An error occurred")
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
}
