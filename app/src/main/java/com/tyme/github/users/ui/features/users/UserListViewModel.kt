package com.tyme.github.users.ui.features.users

import com.tyme.github.users.domain.usecases.users.GetUserListUseCase
import com.tyme.github.users.providers.dispatchers.DispatchersProvider
import com.tyme.github.users.ui.features.users.models.UserListUiState
import com.tyme.github.users.ui.uistate.models.NoEvent
import com.tyme.github.users.ui.uistate.viewmodel.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@HiltViewModel
internal class UserListViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val getUserListUseCase: GetUserListUseCase,
) : UiStateViewModel<UserListUiState, NoEvent>(
    initialUiState = UserListUiState(),
) {

    init {
        getUserList()
    }

    private fun getUserList() {
        getUserListUseCase().collectSafe(
            context = dispatchersProvider.io,
            onError = ::showError,
            hasLoading = true,
        ) { userList ->
            updateUiState { copy(userList = userList.toImmutableList()) }
        }
    }
}