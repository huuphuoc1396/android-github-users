package com.tyme.github.users.ui.features.users

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.tyme.github.users.domain.usecases.users.GetUserPagingUseCase
import com.tyme.github.users.ui.features.users.models.UserListUiState
import com.tyme.github.users.ui.uistate.models.NoEvent
import com.tyme.github.users.ui.uistate.viewmodel.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class UserListViewModel @Inject constructor(
    getUserPagingUseCase: GetUserPagingUseCase,
) : UiStateViewModel<UserListUiState, NoEvent>(
    initialUiState = UserListUiState(),
) {

    val userPaging = getUserPagingUseCase()
        .cachedIn(viewModelScope)

    fun setRefreshing(isRefreshing: Boolean) {
        updateUiState { copy(isRefreshing = isRefreshing) }
    }
}