package com.tyme.github.users.data.repositories.users.mappers

import com.tyme.github.users.data.remote.responses.users.UserResponse
import com.tyme.github.users.data.storages.databases.entities.UserEntity
import com.tyme.github.users.domain.models.users.UserModel
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class UserMappersKtTest {

    @Test
    fun `UserResponse toUserEntity returns UserEntity`() {
        // Given
        val userResponse = UserResponse(
            id = 1,
            login = "login",
            avatarUrl = "avatarUrl",
            htmlUrl = "htmlUrl",
        )
        val expected = UserEntity(
            id = 1,
            username = "login",
            avatarUrl = "avatarUrl",
            url = "htmlUrl",
        )

        // When
        val result = userResponse.toUserEntity()

        // Then
        result shouldBe expected
    }

    @Test
    fun `UserResponse toUserEntity returns UserEntity with default values`() {
        // Given
        val userResponse = UserResponse()
        val expected = UserEntity(
            id = 0,
            username = "",
            avatarUrl = "",
            url = "",
        )

        // When
        val result = userResponse.toUserEntity()

        // Then
        result shouldBe expected
    }

    @Test
    fun `UserEntity toUserModel returns UserModel`() {
        // Given
        val userResponse = UserEntity(
            id = 1,
            username = "login",
            avatarUrl = "avatarUrl",
            url = "htmlUrl",
        )
        val expected = UserModel(
            id = 1,
            username = "login",
            avatarUrl = "avatarUrl",
            url = "htmlUrl",
        )

        // When
        val result = userResponse.toUserModel()

        // Then
        result shouldBe expected
    }
}