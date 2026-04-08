package com.tyme.github.users.feature.favorites.domain.usecase

import androidx.paging.PagingData
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.feature.favorites.api.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritePagingUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    operator fun invoke(): Flow<PagingData<UserModel>> = repository.getFavoritePaging()
}
