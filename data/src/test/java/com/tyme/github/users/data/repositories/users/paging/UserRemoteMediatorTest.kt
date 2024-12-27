package com.tyme.github.users.data.repositories.users.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.InitializeAction
import androidx.paging.RemoteMediator.MediatorResult
import com.tyme.github.users.data.remote.responses.users.UserResponse
import com.tyme.github.users.data.remote.services.UserService
import com.tyme.github.users.data.repositories.users.mappers.toUserEntity
import com.tyme.github.users.data.storages.databases.daos.UserDao
import com.tyme.github.users.data.storages.databases.entities.UserEntity
import com.tyme.github.users.data.storages.datastores.PreferencesDataStore
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS

@ExperimentalPagingApi
internal class UserRemoteMediatorTest {

    private val userService: UserService = mockk()
    private var userDao: UserDao = mockk()
    private val preferencesDataStore: PreferencesDataStore = mockk()
    private var userRemoteMediator: UserRemoteMediator = UserRemoteMediator(
        preferencesDataStore = preferencesDataStore,
        userDao = userDao,
        userService = userService,
    )

    @Test
    fun `refresh loadType fetches users and upsert entities`() = runTest {
        // Given
        val userResponses = MutableList(20) { UserResponse(id = it) }
        val userEntities = userResponses.map { response -> response.toUserEntity() }
        val pagingState = getPagingState()
        coEvery { userService.getUsers(perPage = 20, since = 0) } returns userResponses
        coEvery { userDao.upsertAndDeleteAll(needToDelete = true, userEntities) } returns Unit
        coEvery { preferencesDataStore.setLastUpdatedUserList(any()) } returns Unit

        // When
        val result = userRemoteMediator.load(
            loadType = LoadType.REFRESH,
            state = pagingState,
        )

        // Then
        result should beInstanceOf<MediatorResult.Success>()
        (result as MediatorResult.Success).endOfPaginationReached shouldBe false
    }

    @Test
    fun `refresh loadType fetches users error`() = runTest {
        // Given
        val pagingState = getPagingState()
        coEvery { userService.getUsers(perPage = 20, since = 0) } throws Exception()

        // When
        val result = userRemoteMediator.load(
            loadType = LoadType.REFRESH,
            state = pagingState,
        )

        // Then
        result should beInstanceOf<MediatorResult.Error>()
    }

    @Test
    fun `prepend loadType ends pagination`() = runTest {
        // Given
        val pagingState = getPagingState()

        // When
        val result = userRemoteMediator.load(
            loadType = LoadType.PREPEND,
            state = pagingState,
        )

        // Then
        result should beInstanceOf<MediatorResult.Success>()
        (result as MediatorResult.Success).endOfPaginationReached shouldBe true
    }

    @Test
    fun `append loadType ends pagination`() = runTest {
        // Given
        val pagingState = getPagingState()

        // When
        val result = userRemoteMediator.load(
            loadType = LoadType.APPEND,
            state = pagingState,
        )

        // Then
        result should beInstanceOf<MediatorResult.Success>()
        (result as MediatorResult.Success).endOfPaginationReached shouldBe true
    }

    @Test
    fun `appends loadType fetches users and upsert entities`() = runTest {
        // Given
        val lastUserEntities = MutableList(20) { UserEntity(id = it + 1) }
        val userResponses = MutableList(20) { UserResponse(id = it + 21) }
        val userEntities = userResponses.map { response -> response.toUserEntity() }
        val pages = listOf(
            Page<Int, UserEntity>(
                data = lastUserEntities,
                prevKey = null,
                nextKey = null,
            )
        )
        val pagingState = getPagingState(pages)
        coEvery { userService.getUsers(perPage = 20, since = 20) } returns userResponses
        coEvery { userDao.upsertAndDeleteAll(needToDelete = false, userEntities) } returns Unit
        coEvery { preferencesDataStore.setLastUpdatedUserList(any()) } returns Unit

        // When
        val result = userRemoteMediator.load(
            loadType = LoadType.APPEND,
            state = pagingState,
        )

        // Then
        result should beInstanceOf<MediatorResult.Success>()
        (result as MediatorResult.Success).endOfPaginationReached shouldBe false
    }

    @Test
    fun `appends loadType fetches last users page`() = runTest {
        // Given
        val lastUserEntities = MutableList(20) { UserEntity(id = it + 1) }
        val userResponses = MutableList(19) { UserResponse(id = it + 21) }
        val userEntities = userResponses.map { response -> response.toUserEntity() }
        val pages = listOf(
            Page<Int, UserEntity>(
                data = lastUserEntities,
                prevKey = null,
                nextKey = null,
            )
        )
        val pagingState = getPagingState(pages)
        coEvery { userService.getUsers(perPage = 20, since = 20) } returns userResponses
        coEvery { userDao.upsertAndDeleteAll(needToDelete = false, userEntities) } returns Unit
        coEvery { preferencesDataStore.setLastUpdatedUserList(any()) } returns Unit

        // When
        val result = userRemoteMediator.load(
            loadType = LoadType.APPEND,
            state = pagingState,
        )

        // Then
        result should beInstanceOf<MediatorResult.Success>()
        (result as MediatorResult.Success).endOfPaginationReached shouldBe true
    }

    @Test
    fun `initialize should return LAUNCH_INITIAL_REFRESH when cache is expired`() = runTest {
        // Given
        val lastUpdated = System.currentTimeMillis() - MILLISECONDS.convert(2, HOURS)
        coEvery { preferencesDataStore.getLastUpdatedUserList() } returns flowOf(lastUpdated)

        // When
        val result = userRemoteMediator.initialize()

        // Then
        result shouldBe InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    @Test
    fun `initialize should return SKIP_INITIAL_REFRESH when cache is not expired`() = runTest {
        // Given
        val lastUpdated = System.currentTimeMillis() + MILLISECONDS.convert(1, HOURS)
        coEvery { preferencesDataStore.getLastUpdatedUserList() } returns flowOf(lastUpdated)

        // When
        val result = userRemoteMediator.initialize()

        // Then
        result shouldBe InitializeAction.SKIP_INITIAL_REFRESH
    }

    private fun getPagingState(pages: List<Page<Int, UserEntity>> = listOf()) =
        PagingState(
            config = PagingConfig(pageSize = 20),
            anchorPosition = 0,
            pages = pages,
            leadingPlaceholderCount = 0,
        )
}