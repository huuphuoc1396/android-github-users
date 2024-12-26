package com.tyme.github.users.domain.usecases.users

import androidx.paging.PagingData
import com.tyme.github.users.domain.models.users.UserModel
import com.tyme.github.users.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPagingUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<PagingData<UserModel>> {
        return userRepository.getUserPaging()
    }
}