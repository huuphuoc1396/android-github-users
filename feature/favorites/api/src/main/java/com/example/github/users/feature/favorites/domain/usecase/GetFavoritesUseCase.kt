package com.example.github.users.feature.favorites.domain.usecase

import com.example.github.users.core.common.model.UserModel
import kotlinx.coroutines.flow.Flow

interface GetFavoritesUseCase {
    operator fun invoke(): Flow<List<UserModel>>
}
