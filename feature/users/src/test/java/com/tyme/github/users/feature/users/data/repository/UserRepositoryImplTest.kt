package com.tyme.github.users.feature.users.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.InitializeAction
import androidx.paging.RemoteMediator.MediatorResult
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.tyme.github.users.feature.users.data.local.UserDao
import com.tyme.github.users.feature.users.data.local.UserEntity
import com.tyme.github.users.feature.users.data.mapper.toUserDetailsModel
import com.tyme.github.users.feature.users.data.mapper.toUserModel
import com.tyme.github.users.feature.users.data.remote.UserService
import com.tyme.github.users.feature.users.data.remote.dto.UserDetailsResponse
import com.tyme.github.users.feature.users.data.repository.paging.UserRemoteMediator
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalPagingApi
internal class UserRepositoryImplTest {

    private val userService: UserService = mockk()

    private val userDao: UserDao = mockk()

    private val remoteMediator: UserRemoteMediator = mockk()

    private val userRepository = UserRepositoryImpl(
        remoteMediator = remoteMediator,
        userDao = userDao,
        userService = userService,
    )

    @Test
    fun `getUserPaging returns Users`() = runTest {
        // Given
        val userEntities = MutableList(20) { index -> UserEntity(id = index) }
        val pagingSource = userEntities.asPagingSourceFactory().invoke()
        val mediatorResult = MediatorResult.Success(true)
        coEvery { remoteMediator.initialize() } returns InitializeAction.LAUNCH_INITIAL_REFRESH
        coEvery { remoteMediator.load(LoadType.REFRESH, any()) } returns mediatorResult
        coEvery { userDao.getPagingSource() } returns pagingSource

        // When
        val result = userRepository.getUserPaging().asSnapshot()

        // Then
        val expectedUserModels = userEntities.map { entity -> entity.toUserModel() }
        result shouldBe expectedUserModels
    }

    @Test
    fun `getUserDetails returns UserDetailsModel`() = runTest {
        // Given
        val username = "username"
        val response = UserDetailsResponse()
        val expected = response.toUserDetailsModel()
        coEvery { userService.getUserDetails(username) } returns response

        // When
        val result = userRepository.getUserDetails(username)

        // Then
        result.test {
            expectMostRecentItem() shouldBe expected
        }
    }
}
