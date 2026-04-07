package com.tyme.github.users.feature.favorites.data.repository

import app.cash.turbine.test
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.core.database.dao.FavoriteDao
import com.tyme.github.users.core.database.entity.UserEntity
import com.tyme.github.users.feature.favorites.data.mapper.toUserModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class FavoriteRepositoryImplTest {

    private val dao = mockk<FavoriteDao>()

    private val repository = FavoriteRepositoryImpl(dao)

    @Test
    fun `addFavorite calls dao setFavorite with isFavorite true and returns success`() = runTest {
        // Given
        val user = UserModel(username = "user1", avatarUrl = "avatar", url = "https://github.com/user1")
        coJustRun { dao.setFavorite(user.username, true) }

        // When
        val result = repository.addFavorite(user)

        // Then
        coVerify { dao.setFavorite(user.username, true) }
        result.isSuccess shouldBe true
    }

    @Test
    fun `addFavorite returns failure when dao throws`() = runTest {
        // Given
        val user = UserModel(username = "user1", avatarUrl = "avatar", url = "https://github.com/user1")
        coEvery { dao.setFavorite(user.username, true) } throws RuntimeException("db error")

        // When
        val result = repository.addFavorite(user)

        // Then
        result.isFailure shouldBe true
    }

    @Test
    fun `removeFavorite calls dao setFavorite with isFavorite false and returns success`() = runTest {
        // Given
        val username = "user1"
        coJustRun { dao.setFavorite(username, false) }

        // When
        val result = repository.removeFavorite(username)

        // Then
        coVerify { dao.setFavorite(username, false) }
        result.isSuccess shouldBe true
    }

    @Test
    fun `removeFavorite returns failure when dao throws`() = runTest {
        // Given
        val username = "user1"
        coEvery { dao.setFavorite(username, false) } throws RuntimeException("db error")

        // When
        val result = repository.removeFavorite(username)

        // Then
        result.isFailure shouldBe true
    }

    @Test
    fun `isFavorite emits true when user is favorite`() = runTest {
        // Given
        val username = "user1"
        every { dao.isFavorite(username) } returns flowOf(true)

        // When / Then
        repository.isFavorite(username).test {
            expectMostRecentItem() shouldBe true
        }
    }

    @Test
    fun `isFavorite emits false when user is not favorite`() = runTest {
        // Given
        val username = "user1"
        every { dao.isFavorite(username) } returns flowOf(false)

        // When / Then
        repository.isFavorite(username).test {
            expectMostRecentItem() shouldBe false
        }
    }

    @Test
    fun `getFavorites emits mapped UserModel list from dao`() = runTest {
        // Given
        val entities = listOf(
            UserEntity(id = 1, username = "user1", avatarUrl = "avatar1", url = "https://github.com/user1", isFavorite = true),
            UserEntity(id = 2, username = "user2", avatarUrl = "avatar2", url = "https://github.com/user2", isFavorite = true),
        )
        every { dao.getFavorites() } returns flowOf(entities)

        // When / Then
        repository.getFavorites().test {
            expectMostRecentItem() shouldBe entities.map { it.toUserModel() }
        }
    }

    @Test
    fun `getFavorites emits empty list when no favorites`() = runTest {
        // Given
        every { dao.getFavorites() } returns flowOf(emptyList())

        // When / Then
        repository.getFavorites().test {
            expectMostRecentItem() shouldBe emptyList()
        }
    }

    @Test
    fun `getFavorites emits multiple updates as favorites change`() = runTest {
        // Given
        val entity = UserEntity(id = 1, username = "user1", avatarUrl = "avatar1", url = "https://github.com/user1", isFavorite = true)
        every { dao.getFavorites() } returns flowOf(emptyList(), listOf(entity))

        // When / Then
        repository.getFavorites().test {
            awaitItem() shouldBe emptyList()
            awaitItem() shouldBe listOf(entity.toUserModel())
            awaitComplete()
        }
    }
}
