package com.tyme.github.users.feature.users.domain.usecase

import androidx.paging.PagingData
import com.tyme.github.users.core.common.models.UserModel
import com.tyme.github.users.feature.users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPagingUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<PagingData<UserModel>> {
        return userRepository.getUserPaging()
    }
}
