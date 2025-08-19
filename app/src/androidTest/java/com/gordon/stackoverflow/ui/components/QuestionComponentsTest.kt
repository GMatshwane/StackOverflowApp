package com.gordon.stackoverflow.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import com.gordon.stackoverflow.data.Question
import com.gordon.stackoverflow.data.Owner

class QuestionComponentsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun sampleQuestion() = Question(
        question_id = 1,
        title = "Sample Question Title",
        body = "Sample question body.",
        creation_date = 1620000000,
        owner = Owner(display_name = "TestUser"),
        answer_count = 2,
        score = 5,
        view_count = 100,
        accepted_answer_id = 10,
        is_answered = false,
        last_activity_date = 12120000000,
        tags = listOf("kotlin", "android")
    )

    @Test
    fun questionItem_displaysAllStatsAndData() {
        composeTestRule.setContent {
            QuestionItem(question = sampleQuestion(), onQuestionClick = {})
        }
        composeTestRule.onNodeWithText("Q: Sample Question Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sample question body.").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 answers").assertIsDisplayed()
        composeTestRule.onNodeWithText("5 votes").assertIsDisplayed()
        composeTestRule.onNodeWithText("100 views").assertIsDisplayed()
        composeTestRule.onNodeWithText("TestUser", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Accepted Answer").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Go to details").assertIsDisplayed()
    }

    @Test
    fun questionItem_triggersClickCallback() {
        var clicked = false
        composeTestRule.setContent {
            QuestionItem(question = sampleQuestion(), onQuestionClick = { clicked = true })
        }
        composeTestRule.onNodeWithText("Q: Sample Question Title").performClick()
        assert(clicked)
    }

    @Test
    fun questionList_showsEmptySearchResult_whenEmpty() {
        composeTestRule.setContent {
            QuestionList(questions = emptyList(), onQuestionClick = {})
        }
        composeTestRule.onNodeWithText("No results found").assertIsDisplayed()
    }

    @Test
    fun questionList_displaysMultipleQuestions() {
        val questions = listOf(sampleQuestion(), sampleQuestion().copy(question_id = 2, title = "Another Title"))
        composeTestRule.setContent {
            QuestionList(questions = questions, onQuestionClick = {})
        }
        composeTestRule.onNodeWithText("Q: Sample Question Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Q: Another Title").assertIsDisplayed()
    }
}
