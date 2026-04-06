package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(user: UserModel) = repository.addFavorite(user)
}
