package com.tyme.github.users.data.remote.services

import com.tyme.github.users.data.remote.responses.users.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface UserService {

    @GET("/users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int = 10,
        @Query("since") since: Int = 100,
    ): List<UserResponse>
}