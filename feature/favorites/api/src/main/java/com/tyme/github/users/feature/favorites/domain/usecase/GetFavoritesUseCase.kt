package com.tyme.github.users.feature.favorites.domain.usecase

import com.tyme.github.users.core.common.models.UserModel
import kotlinx.coroutines.flow.Flow

interface GetFavoritesUseCase {
    operator fun invoke(): Flow<List<UserModel>>
}
