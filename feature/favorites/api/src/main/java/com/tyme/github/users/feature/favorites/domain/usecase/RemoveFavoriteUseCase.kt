package com.tyme.github.users.feature.favorites.domain.usecase

interface RemoveFavoriteUseCase {
    suspend operator fun invoke(username: String): Result<Unit>
}
