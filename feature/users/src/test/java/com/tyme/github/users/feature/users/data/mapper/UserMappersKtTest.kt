package com.tyme.github.users.feature.users.data.mapper

import com.tyme.github.users.feature.users.data.local.UserEntity
import com.tyme.github.users.feature.users.data.remote.dto.UserResponse
import com.tyme.github.users.feature.users.domain.model.UserModel
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
        val userEntity = UserEntity(
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
        val result = userEntity.toUserModel()

        // Then
        result shouldBe expected
    }
}
