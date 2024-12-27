package com.tyme.github.users.data.repositories.users.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tyme.github.users.data.remote.services.UserService
import com.tyme.github.users.data.repositories.users.mappers.toUserEntity
import com.tyme.github.users.data.storages.databases.daos.UserDao
import com.tyme.github.users.data.storages.databases.entities.UserEntity
import com.tyme.github.users.data.storages.datastores.PreferencesDataStore
import com.tyme.github.users.domain.extensions.orZero
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class UserRemoteMediator @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore,
    private val userDao: UserDao,
    private val userService: UserService,
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        try {
            val since = when (loadType) {
                LoadType.REFRESH -> {
                    INITIAL_SINCE
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    state.lastItemOrNull()?.id
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            val responses = userService.getUsers(
                perPage = state.config.pageSize,
                since = since,
            )
            val isRefresh = loadType == LoadType.REFRESH
            val entities = responses.map { response -> response.toUserEntity() }
            userDao.upsertAndDeleteAll(
                needToDelete = isRefresh,
                entities = entities,
            )
            if (isRefresh) {
                preferencesDataStore.setLastUpdatedUserList(System.currentTimeMillis())
            }
            return MediatorResult.Success(
                endOfPaginationReached = responses.size < state.config.pageSize
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val currentTimeMillis = System.currentTimeMillis()
        val lastUpdated = preferencesDataStore.getLastUpdatedUserList().firstOrNull().orZero()
        return if (currentTimeMillis - lastUpdated <= CACHE_TIMEOUT) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    companion object {
        private val CACHE_TIMEOUT = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        private const val INITIAL_SINCE = 0
    }
}