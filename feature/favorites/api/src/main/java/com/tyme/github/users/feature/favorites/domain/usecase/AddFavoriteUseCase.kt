package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.core.common.models.UserModel

interface AddFavoriteUseCase {
    suspend operator fun invoke(user: UserModel): Result<Unit>
}
