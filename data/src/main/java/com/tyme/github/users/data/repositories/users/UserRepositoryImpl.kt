package com.tyme.github.users.data.repositories.users

import com.tyme.github.users.data.remote.services.UserService
import com.tyme.github.users.data.repositories.users.mappers.toUserModel
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
) : UserRepository {

    override fun getUserList(): Flow<List<UserModel>> = flow {
        val userResponses = userService.getUsers()
        val users = userResponses.map { response -> response.toUserModel() }
        emit(users)
    }
}