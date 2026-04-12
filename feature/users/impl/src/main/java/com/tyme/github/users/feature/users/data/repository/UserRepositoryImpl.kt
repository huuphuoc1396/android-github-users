package com.tyme.github.users.feature.users.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.core.database.dao.UserDao
import com.tyme.github.users.feature.users.data.mapper.toUserDetailsModel
import com.tyme.github.users.feature.users.data.mapper.toUserModel
import com.tyme.github.users.feature.users.data.remote.UserService
import com.tyme.github.users.feature.users.data.repository.paging.UserRemoteMediator
import com.tyme.github.users.feature.users.domain.model.UserDetailsModel
import com.tyme.github.users.feature.users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class UserRepositoryImpl @Inject constructor(
    private val remoteMediator: UserRemoteMediator,
    private val userDao: UserDao,
    private val userService: UserService,
) : UserRepository {

    override fun getUserPaging(): Flow<PagingData<UserModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = PAGE_SIZE / 2,
                initialLoadSize = PAGE_SIZE,
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { userDao.getPagingSource() },
        )
            .flow
            .map { pagingData -> pagingData.map { entity -> entity.toUserModel() } }
    }

    override fun getUserDetails(username: String): Flow<UserDetailsModel> {
        return flow {
            val userResponse = userService.getUserDetails(username)
            val userDetails = userResponse.toUserDetailsModel()
            emit(userDetails)
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
