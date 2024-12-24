package com.tyme.github.users.domain.usecases.users

import com.tyme.github.users.domain.models.users.UserDetailsModel
import com.tyme.github.users.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(username: String): Flow<UserDetailsModel> {
        return userRepository.getUserDetails(username)
    }
}