package com.tyme.github.users.data.remote.services

import com.tyme.github.users.data.remote.responses.users.UserDetailsResponse
import com.tyme.github.users.data.remote.responses.users.UserResponse
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