package com.tyme.github.users.domain.repositories

import androidx.paging.PagingData
import com.tyme.github.users.domain.models.users.UserDetailsModel
import com.tyme.github.users.domain.models.users.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserPaging(): Flow<PagingData<UserModel>>

    fun getUserDetails(username: String): Flow<UserDetailsModel>
}