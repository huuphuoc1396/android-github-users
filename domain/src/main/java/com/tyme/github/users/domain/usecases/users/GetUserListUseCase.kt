package com.tyme.github.users.domain.usecases.users

import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<List<UserModel>> {
        return userRepository.getUserList()
    }
}