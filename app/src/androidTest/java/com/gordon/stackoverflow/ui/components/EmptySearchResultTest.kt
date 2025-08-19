package com.gordon.stackoverflow.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test

class EmptySearchResultTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptySearchResult_showsIconAndMessage() {
        composeTestRule.setContent {
            EmptySearchResult()
        }
        composeTestRule.onNodeWithContentDescription("No results").assertIsDisplayed()
        composeTestRule.onNodeWithText("No results found").assertIsDisplayed()
    }
}

