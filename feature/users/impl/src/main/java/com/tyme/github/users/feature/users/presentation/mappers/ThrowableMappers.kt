package com.tyme.github.users.feature.users.presentation.mappers

import com.tyme.github.users.core.network.models.errors.toNetworkErrorMessage
import com.tyme.github.users.feature.users.presentation.userdetails.UserDetailUiState
import com.tyme.github.users.feature.users.presentation.userlist.UserListUiState

internal fun Throwable.toUserListError(): UserListUiState.Error {
    val errorMessage = toNetworkErrorMessage()
    return UserListUiState.Error(message = errorMessage.message, messageRes = errorMessage.messageRes)
}

internal fun Throwable.toUserDetailError(): UserDetailUiState.Error {
    val errorMessage = toNetworkErrorMessage()
    return UserDetailUiState.Error(message = errorMessage.message, messageRes = errorMessage.messageRes)
}
