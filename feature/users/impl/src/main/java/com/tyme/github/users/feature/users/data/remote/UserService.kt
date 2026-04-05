package com.tyme.github.users.feature.users.data.remote

import com.tyme.github.users.feature.users.data.remote.dto.UserDetailsResponse
import com.tyme.github.users.feature.users.data.remote.dto.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface UserService {

    @GET("/users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int,
        @Query("since") since: Int,
    ): List<UserResponse>

    @GET("/users/{username}")
    suspend fun getUserDetails(
        @Path("username") username: String,
    ): UserDetailsResponse
}
