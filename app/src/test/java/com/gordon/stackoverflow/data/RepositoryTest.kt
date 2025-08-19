package com.gordon.stackoverflow.data

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Network
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class RepositoryTest {
    private lateinit var repository: StackOverflowRepository
    private val mockApiService: StackOverflowApiService = mockk(relaxed = true)
    private val mockContext: android.content.Context = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = StackOverflowRepository(mockApiService, mockContext)
    }

    @Test
    fun searchQuestions_returnsSuccess() = runTest {
        val sampleQuestions = listOf(Question(
            question_id = 1,
            title = "Title",
            body = "Body",
            creation_date = 123L,
            last_activity_date = 123L,
            owner = Owner(display_name = "User"),
            answer_count = 1,
            score = 2,
            view_count = 3,
            accepted_answer_id = null,
            tags = listOf("kotlin"),
            is_answered = false
        ))
        val mockConnectivityManager: ConnectivityManager = mockk(relaxed = true)
        val mockNetwork: Network = mockk(relaxed = true)
        val mockNetworkCapabilities: NetworkCapabilities = mockk(relaxed = true)
        coEvery { mockContext.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        coEvery { mockConnectivityManager.activeNetwork } returns mockNetwork
        coEvery { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        coEvery { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        coEvery { mockApiService.searchQuestions(title = any()) } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns SearchResponse(sampleQuestions, true, 10000, 9999)
        }
        repository = StackOverflowRepository(mockApiService, mockContext)
        val result = repository.searchQuestions("test")
        assertTrue(result is NetworkResult.Success)
        assertEquals(sampleQuestions, (result as NetworkResult.Success).data)
    }

    @Test
    fun searchQuestions_returnsError() = runTest {
        val mockConnectivityManager: ConnectivityManager = mockk(relaxed = true)
        val mockNetwork: Network = mockk(relaxed = true)
        val mockNetworkCapabilities: NetworkCapabilities = mockk(relaxed = true)
        coEvery { mockContext.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) } returns mockConnectivityManager
        coEvery { mockConnectivityManager.activeNetwork } returns mockNetwork
        coEvery { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        coEvery { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        coEvery { mockApiService.searchQuestions(title = any()) } returns mockk {
            every { isSuccessful } returns false
            every { message() } returns "Search failed"
        }
        repository = StackOverflowRepository(mockApiService, mockContext)
        val result = repository.searchQuestions("test")
        assertTrue(result is NetworkResult.Error)
        assertEquals("Search failed: Search failed", (result as NetworkResult.Error).message)
    }
}
