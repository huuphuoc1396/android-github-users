package com.tyme.github.users.domain.repositories

import com.tyme.github.users.domain.models.users.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserList(): Flow<List<UserModel>>
}