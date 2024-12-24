package com.tyme.github.users.data.repositories.users.mappers

import com.tyme.github.users.data.remote.responses.users.UserDetailsResponse
import com.tyme.github.users.domain.models.users.UserDetailsModel
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class UserDetailsMappersKtTest {

    @Test
    fun `toUserDetailsModel returns UserDetailsModel`() {
        // Given
        val userDetailsResponse = UserDetailsResponse(
            login = "username",
            avatarUrl = "avatarUrl",
            location = "country",
            followers = 10,
            following = 20,
            htmlUrl = "url"
        )

        // When
        val result = userDetailsResponse.toUserDetailsModel()

        // Then
        val expected = UserDetailsModel(
            username = "username",
            avatarUrl = "avatarUrl",
            country = "country",
            followers = 10,
            following = 20,
            url = "url"
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toUserDetailsModel returns UserDetailsModel with default values`() {
        // Given
        val userDetailsResponse = UserDetailsResponse(
            login = null,
            avatarUrl = null,
            location = null,
            followers = null,
            following = null,
            htmlUrl = null
        )

        // When
        val result = userDetailsResponse.toUserDetailsModel()

        // Then
        val expected = UserDetailsModel(
            username = "",
            avatarUrl = "",
            country = "",
            followers = 0,
            following = 0,
            url = ""
        )
        assertEquals(expected, result)
    }
}