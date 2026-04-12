package com.example.github.users.feature.users.domain.repository

import androidx.paging.PagingData
import com.example.github.users.core.common.model.UserModel
import com.example.github.users.feature.users.domain.model.UserDetailsModel
import kotlinx.coroutines.flow.Flow

internal interface UserRepository {

    fun getUserPaging(): Flow<PagingData<UserModel>>

    fun getUserDetails(username: String): Flow<UserDetailsModel>
}
