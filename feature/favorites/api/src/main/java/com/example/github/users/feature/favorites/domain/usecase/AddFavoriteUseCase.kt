package com.example.github.users.feature.favorites.domain.usecase

import com.example.github.users.core.common.model.UserModel

interface AddFavoriteUseCase {
    suspend operator fun invoke(user: UserModel): Result<Unit>
}
