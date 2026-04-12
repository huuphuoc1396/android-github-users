package com.example.github.users.core.network.interceptors

import io.kotest.matchers.shouldBe
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test

internal class HeaderInterceptorTest {

    private val mockWebServer = MockWebServer()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .build()

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `intercept should add Accept and Content-Type headers to request`() {
        // Given
        mockWebServer.enqueue(MockResponse().setBody("{}"))

        // When
        okHttpClient.newCall(
            Request.Builder()
                .url(mockWebServer.url("/"))
                .build()
        ).execute()

        val request = mockWebServer.takeRequest()

        // Then
        request.getHeader("Accept") shouldBe "application/json"
        request.getHeader("Content-Type") shouldBe "application/json"
    }
}
