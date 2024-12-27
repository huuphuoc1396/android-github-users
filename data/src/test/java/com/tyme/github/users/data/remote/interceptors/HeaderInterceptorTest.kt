package com.tyme.github.users.data.remote.interceptors

import com.tyme.github.users.data.remote.services.UserService
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class HeaderInterceptorTest {

    private val mockWebServer = MockWebServer()

    private val headerInterceptor = HeaderInterceptor()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .build()

    private val userService: UserService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(UserService::class.java)


    @Test
    fun `intercept should add Accept and Content-Type headers to request`() = runTest {

        // Given
        val testDataJson = "{\"login\":\"test\"}"
        val successResponse = MockResponse().setBody(testDataJson)

        mockWebServer.enqueue(successResponse)
        userService.getUserDetails(username = "test")

        // When
        val request = mockWebServer.takeRequest()

        // Then
        val accept = request.getHeader("Accept")
        val contentType = request.getHeader("Content-Type")

        accept shouldBe "application/json"
        contentType shouldBe "application/json"
    }
}