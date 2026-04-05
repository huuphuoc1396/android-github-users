package com.tyme.github.users.feature.favorites.impl.domain.usecase

import com.tyme.github.users.feature.users.api.model.UserModel
import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(user: UserModel) = repository.addFavorite(user)
}
