package com.tyme.github.users.feature.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow

interface IsFavoriteUseCase {
    operator fun invoke(username: String): Flow<Boolean>
}
