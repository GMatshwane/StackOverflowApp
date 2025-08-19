package com.gordon.stackoverflow.viewmodel

import com.gordon.stackoverflow.data.NetworkResult
import com.gordon.stackoverflow.data.Question
import com.gordon.stackoverflow.data.Owner
import com.gordon.stackoverflow.data.StackOverflowRepository
import com.gordon.stackoverflow.data.Answer
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {
    private lateinit var repository: StackOverflowRepository
    private lateinit var viewModel: DetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val sampleQuestion = Question(
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

    private val sampleAnswers = listOf(
        Answer(
            answer_id = 1,
            question_id = 1,
            body = "Answer body",
            score = 5,
            creation_date = 123L,
            owner = Owner(display_name = "User"),
            is_accepted = true
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = DetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadAnswers_success() = runTest {
        coEvery { repository.getAnswers(1) } returns NetworkResult.Success(sampleAnswers)
        viewModel.loadAnswers(1)
        testScheduler.advanceUntilIdle()
        assertEquals(sampleAnswers, viewModel.answers.value)
        assertNull(viewModel.errorMessage.value)
        assertTrue(!viewModel.isLoading.value)
    }

    @Test
    fun loadAnswers_error() = runTest {
        coEvery { repository.getAnswers(1) } returns NetworkResult.Error("Not found")
        viewModel.loadAnswers(1)
        testScheduler.advanceUntilIdle()
        assertEquals(emptyList<Answer>(), viewModel.answers.value)
        assertEquals("Not found", viewModel.errorMessage.value)
        assertTrue(!viewModel.isLoading.value)
    }
}
