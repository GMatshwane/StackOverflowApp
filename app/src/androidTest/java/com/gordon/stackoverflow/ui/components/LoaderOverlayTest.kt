package com.gordon.stackoverflow.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class LoaderOverlayTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loaderOverlay_showsLoadingMessageAndIndicator_whenLoading() {
        composeTestRule.setContent {
            LoaderOverlay(isLoading = true)
        }
        composeTestRule.onNodeWithText("Loading ...").assertIsDisplayed()
    }

    @Test
    fun loaderOverlay_isNotDisplayed_whenNotLoading() {
        composeTestRule.setContent {
            LoaderOverlay(isLoading = false)
        }
        composeTestRule.onNodeWithText("Loading ...").assertDoesNotExist()
    }
}
