package com.example.github.users.feature.users.domain.usecase

import androidx.paging.PagingData
import com.example.github.users.core.common.model.UserModel
import com.example.github.users.feature.users.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetUserPagingUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<PagingData<UserModel>> {
        return userRepository.getUserPaging()
    }
}
