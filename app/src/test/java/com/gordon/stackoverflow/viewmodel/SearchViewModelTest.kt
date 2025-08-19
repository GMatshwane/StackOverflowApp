package com.gordon.stackoverflow.viewmodel

import com.gordon.stackoverflow.data.NetworkResult
import com.gordon.stackoverflow.data.Question
import com.gordon.stackoverflow.data.Owner
import com.gordon.stackoverflow.data.StackOverflowRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private lateinit var repository: StackOverflowRepository
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val sampleQuestions = listOf(
        Question(
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
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_fetchesRecentQuestions() = runTest(testDispatcher) {
        coEvery { repository.fetchRecentQuestions() } returns NetworkResult.Success(sampleQuestions)
        viewModel = SearchViewModel(repository)
        testScheduler.advanceUntilIdle()
        assertEquals(sampleQuestions, viewModel.searchResults.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun searchQuestions_success_updatesResults() = runTest(testDispatcher) {
        coEvery { repository.fetchRecentQuestions() } returns NetworkResult.Success(emptyList())
        coEvery { repository.searchQuestions(any()) } returns NetworkResult.Success(sampleQuestions)
        viewModel = SearchViewModel(repository)
        testScheduler.advanceUntilIdle() // Let initial load complete
        viewModel.searchQuestions("query")
        testScheduler.advanceUntilIdle()
        assertEquals(sampleQuestions, viewModel.searchResults.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun searchQuestions_error_setsErrorMessage() = runTest(testDispatcher) {
        coEvery { repository.fetchRecentQuestions() } returns NetworkResult.Success(emptyList())
        coEvery { repository.searchQuestions(any()) } returns NetworkResult.Error("error")
        viewModel = SearchViewModel(repository)
        testScheduler.advanceUntilIdle() // Let initial load complete
        viewModel.searchQuestions("query")
        testScheduler.advanceUntilIdle()
        assertEquals("error", viewModel.errorMessage.value)
        assertFalse(viewModel.isLoading.value)
        assertEquals(emptyList<Question>(), viewModel.searchResults.value)
    }

    @Test
    fun refreshQuestions_fetchesRecentQuestions() = runTest(testDispatcher) {
        coEvery { repository.fetchRecentQuestions() } returns NetworkResult.Success(sampleQuestions)
        viewModel = SearchViewModel(repository)
        testScheduler.advanceUntilIdle() // Let initial load complete
        viewModel.refreshQuestions()
        testScheduler.advanceUntilIdle()
        assertEquals(sampleQuestions, viewModel.searchResults.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun dismissNetworkDialog_setsDialogFalse() = runTest(testDispatcher) {
        coEvery { repository.fetchRecentQuestions() } returns NetworkResult.Success(emptyList())
        viewModel = SearchViewModel(repository)
        testScheduler.advanceUntilIdle()
        viewModel.dismissNetworkDialog()
        assertFalse(viewModel.showNetworkDialog.value)
    }

    @Test
    fun clearError_setsErrorNull() = runTest(testDispatcher) {
        coEvery { repository.fetchRecentQuestions() } returns NetworkResult.Success(emptyList())
        viewModel = SearchViewModel(repository)
        testScheduler.advanceUntilIdle()
        viewModel.clearError()
        assertNull(viewModel.errorMessage.value)
    }
}
