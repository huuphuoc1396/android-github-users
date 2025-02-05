package com.tyme.github.users.ui.features.userdetails.mappers

import com.tyme.github.users.domain.models.users.UserDetailsModel
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.ui.features.userdetails.models.UserDetailUiState
import com.tyme.github.users.ui.features.userdetails.models.UserDetailsDestination
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class UserDetailsMapperKtTest {

    @Test
    fun `toUserDetailsUiState returns UserDetailUiState`() {
        // Given
        val userDetailsModel = UserDetailsModel(
            username = "username",
            avatarUrl = "avatarUrl",
            country = "country",
            followers = 1,
            following = 1,
            url = "url",
        )

        // When
        val result = userDetailsModel.toUserDetailsUiState()

        // Then
        val expected = UserDetailUiState(
            username = "username",
            avatarUrl = "avatarUrl",
            country = "country",
            followers = "1+",
            following = "1+",
            url = "url",
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toUserDetailsDestination returns UserDetailsDestination`() {
        // Given
        val userModel = UserModel(
            username = "username",
            avatarUrl = "avatarUrl",
            url = "url",
        )

        // When
        val result = userModel.toUserDetailsDestination()

        // Then
        val expected = UserDetailsDestination(
            username = "username",
            avatarUrl = "avatarUrl",
            url = "url",
        )
        assertEquals(expected, result)
    }
}