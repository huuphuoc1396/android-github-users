package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import javax.inject.Inject

internal class AddFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoriteRepository,
) : AddFavoriteUseCase {
    override suspend operator fun invoke(user: UserModel): Result<Unit> = repository.addFavorite(user)
}
