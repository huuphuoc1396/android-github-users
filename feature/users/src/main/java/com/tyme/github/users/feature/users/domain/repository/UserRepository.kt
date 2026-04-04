package com.tyme.github.users.feature.users.domain.repository

import androidx.paging.PagingData
import com.tyme.github.users.feature.users.domain.model.UserDetailsModel
import com.tyme.github.users.feature.users.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserPaging(): Flow<PagingData<UserModel>>

    fun getUserDetails(username: String): Flow<UserDetailsModel>
}
