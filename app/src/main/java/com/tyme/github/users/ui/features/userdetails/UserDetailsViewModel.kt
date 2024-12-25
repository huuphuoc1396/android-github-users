package com.tyme.github.users.ui.features.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.tyme.github.users.domain.usecases.users.GetUserDetailsUseCase
import com.tyme.github.users.providers.dispatchers.DispatchersProvider
import com.tyme.github.users.ui.features.userdetails.mappers.toUserDetailsUiState
import com.tyme.github.users.ui.features.userdetails.models.UserDetailUiState
import com.tyme.github.users.ui.features.userdetails.models.UserDetailsDestination
import com.tyme.github.users.ui.uistate.models.NoEvent
import com.tyme.github.users.ui.uistate.viewmodel.UiStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class UserDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dispatchersProvider: DispatchersProvider,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
) : UiStateViewModel<UserDetailUiState, NoEvent>(
    initialUiState = UserDetailUiState(),
) {

    private val destination: UserDetailsDestination by lazy {
        savedStateHandle.toRoute<UserDetailsDestination>()
    }

    init {
        updateUiState {
            copy(
                username = destination.username,
                avatarUrl = destination.avatarUrl,
                url = destination.url,
            )
        }
        getUserDetails()
    }

    private fun getUserDetails() {
        getUserDetailsUseCase(destination.username).collectSafe(
            context = dispatchersProvider.io,
            onError = ::showError,
            hasLoading = true,
        ) { userDetails ->
            updateUiState { userDetails.toUserDetailsUiState() }
        }
    }
}