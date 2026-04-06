package com.tyme.github.users.feature.users.domain.usecase

import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.feature.users.api.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    operator fun invoke(): Flow<List<UserModel>> = repository.getFavorites()
}
