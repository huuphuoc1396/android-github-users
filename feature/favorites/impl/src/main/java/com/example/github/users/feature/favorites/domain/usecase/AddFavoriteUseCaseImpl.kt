package com.example.github.users.feature.favorites.domain.usecase

import com.example.github.users.core.common.model.UserModel
import com.example.github.users.feature.favorites.domain.repository.FavoriteRepository
import javax.inject.Inject

internal class AddFavoriteUseCaseImpl @Inject constructor(
    private val repository: FavoriteRepository,
) : AddFavoriteUseCase {
    override suspend operator fun invoke(user: UserModel): Result<Unit> = repository.addFavorite(user)
}
