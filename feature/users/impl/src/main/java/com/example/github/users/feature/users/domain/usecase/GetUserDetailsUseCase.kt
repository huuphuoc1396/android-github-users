package com.example.github.users.feature.users.domain.usecase

import com.example.github.users.feature.users.domain.model.UserDetailsModel
import com.example.github.users.feature.users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetUserDetailsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(username: String): Flow<UserDetailsModel> {
        return userRepository.getUserDetails(username)
    }
}
