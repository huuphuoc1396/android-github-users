package com.tyme.github.users.ui.features.users

import com.tyme.github.users.providers.dispatchers.DispatchersProvider
import com.tyme.github.users.ui.features.users.models.UserListUiState
import com.tyme.github.users.ui.uistate.models.NoEvent
import com.tyme.github.users.ui.uistate.viewmodel.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class UserListViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
) : UiStateViewModel<UserListUiState, NoEvent>(
    initialUiState = UserListUiState,
) {

}