package com.tyme.github.users.data.repositories.users.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tyme.github.users.data.remote.services.UserService
import com.tyme.github.users.data.repositories.users.mappers.toUserEntity
import com.tyme.github.users.data.storages.databases.UserDatabase
import com.tyme.github.users.data.storages.databases.entities.UserEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class UserRemoteMediator @Inject constructor(
    private val userService: UserService,
    private val userDatabase: UserDatabase,
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        try {
            val since = when (loadType) {
                LoadType.REFRESH -> INITIAL_SINCE
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
            val entities = responses.map { response -> response.toUserEntity() }
            val userDao = userDatabase.userDao()
            userDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.deleteAll()
                }
                userDao.upsertAll(entities)
            }
            return MediatorResult.Success(
                endOfPaginationReached = responses.size < state.config.pageSize
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    companion object {
        private const val INITIAL_SINCE = 0
    }
}