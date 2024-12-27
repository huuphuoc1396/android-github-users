package com.tyme.github.users.data.repositories.users

import androidx.paging.PagingData
import androidx.paging.PagingSource
import app.cash.turbine.test
import com.tyme.github.users.data.remote.responses.users.UserDetailsResponse
import com.tyme.github.users.data.remote.services.UserService
import com.tyme.github.users.data.repositories.users.mappers.toUserDetailsModel
import com.tyme.github.users.data.storages.databases.daos.UserDao
import com.tyme.github.users.data.storages.databases.entities.UserEntity
import com.tyme.github.users.data.storages.datastores.PreferencesDataStore
import com.tyme.github.users.domain.models.users.UserModel
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class UserRepositoryImplTest {

    private val userService: UserService = mockk()

    private val userDao: UserDao = mockk()

    private val preferencesDataStore: PreferencesDataStore = mockk()

    private val userRepository = UserRepositoryImpl(userService, userDao, preferencesDataStore)

    @Test
    fun `getUserPaging returns Users`() = runTest {
        // Given
        val pagingSource = mockk<PagingSource<Int, UserEntity>>(relaxed = true)
        coEvery { userDao.getPagingSource() } returns pagingSource
        coEvery { preferencesDataStore.getLastUpdatedUserList() } returns flowOf(0)

        // When
        val result = userRepository.getUserPaging()

        // Then
        result.test {
            expectMostRecentItem() should beInstanceOf<PagingData<UserModel>>()
        }
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