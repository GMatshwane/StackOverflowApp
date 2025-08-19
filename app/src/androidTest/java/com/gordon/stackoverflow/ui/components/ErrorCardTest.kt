package com.gordon.stackoverflow.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test

class ErrorCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorCard_showsErrorMessage_whenMessageProvided() {
        val errorMessage = "Something went wrong"
        composeTestRule.setContent {
            ErrorCard(message = errorMessage)
        }
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun errorCard_isNotDisplayed_whenMessageIsNull() {
        composeTestRule.setContent {
            ErrorCard(message = null)
        }
        composeTestRule.onNodeWithText("Something went wrong").assertDoesNotExist()
    }
}

