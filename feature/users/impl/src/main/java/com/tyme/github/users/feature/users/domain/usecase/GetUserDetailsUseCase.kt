package com.tyme.github.users.feature.users.domain.usecase

import com.tyme.github.users.feature.users.domain.model.UserDetailsModel
import com.tyme.github.users.feature.users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetailsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(username: String): Flow<UserDetailsModel> {
        return userRepository.getUserDetails(username)
    }
}
