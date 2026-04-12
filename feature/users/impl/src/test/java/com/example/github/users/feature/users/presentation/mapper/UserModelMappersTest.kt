package com.example.github.users.feature.users.presentation.mapper

import com.example.github.users.core.common.model.UserModel
import com.example.github.users.core.ui.component.UserListItem
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class UserModelMappersTest {

    @Test
    fun `toUserListItem maps all fields correctly`() {
        val model = UserModel(
            id = 1,
            username = "user1",
            avatarUrl = "https://avatar.url",
            url = "https://github.com/user1",
            isFavorite = true,
        )

        val result = model.toUserListItem()

        result shouldBe UserListItem(
            id = 1,
            username = "user1",
            avatarUrl = "https://avatar.url",
            url = "https://github.com/user1",
            isFavorite = true,
        )
    }

    @Test
    fun `toUserModel maps all fields correctly`() {
        val item = UserListItem(
            id = 1,
            username = "user1",
            avatarUrl = "https://avatar.url",
            url = "https://github.com/user1",
            isFavorite = true,
        )

        val result = item.toUserModel()

        result shouldBe UserModel(
            id = 1,
            username = "user1",
            avatarUrl = "https://avatar.url",
            url = "https://github.com/user1",
            isFavorite = true,
        )
    }
}
