package com.tyme.github.users.data.repositories.users

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.android.template.data.storages.datastore.preferences.PreferencesDataStore
import com.tyme.github.users.data.remote.services.UserService
import com.tyme.github.users.data.repositories.users.mappers.toUserDetailsModel
import com.tyme.github.users.data.repositories.users.mappers.toUserModel
import com.tyme.github.users.data.repositories.users.paging.UserRemoteMediator
import com.tyme.github.users.data.storages.databases.daos.UserDao
import com.tyme.github.users.domain.models.users.UserDetailsModel
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val userDao: UserDao,
    private val preferencesDataStore: PreferencesDataStore,
) : UserRepository {

    override fun getUserPaging(): Flow<PagingData<UserModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = PAGE_SIZE / 2,
                initialLoadSize = PAGE_SIZE,
            ),
            remoteMediator = UserRemoteMediator(
                userService = userService,
                userDao = userDao,
                preferencesDataStore = preferencesDataStore,
            ),
            pagingSourceFactory = {
                userDao.getPagingSource()
            },
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