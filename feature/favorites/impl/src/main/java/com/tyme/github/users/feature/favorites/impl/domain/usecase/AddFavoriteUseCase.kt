package com.tyme.github.users.feature.favorites.impl.domain.usecase

import com.tyme.github.users.feature.favorites.api.model.FavoriteUser
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(user: FavoriteUser) = repository.addFavorite(user)
}
