package com.gordon.stackoverflow.ui.components

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

/**
 * A composable that displays an error message in a styled card.
 *
 * This component renders error messages in a visually distinct card with error-themed colors
 * from the Material Design color scheme. The card is only displayed when a non-null error
 * message is provided, making it safe to use with nullable error states.
 *
 * The component features:
 * - Error-themed background color (errorContainer) for visual distinction
 * - Error-colored text for high contrast and visibility
 * - Full width layout with consistent padding
 * - Conditional rendering (only shows when message is not null)
 *
 * This is commonly used to display network errors, validation errors, or any other
 * error messages that need to be presented to the user in a consistent manner
 * throughout the Stack Overflow app.
 *
 * @param message The error message to display. If null, the card will not be rendered.
 *                This allows for safe integration with nullable error state management
 *                where errors may or may not be present.
 */
@Composable
fun ErrorCard(message: String?) {
    message?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Text(
                text = it,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
