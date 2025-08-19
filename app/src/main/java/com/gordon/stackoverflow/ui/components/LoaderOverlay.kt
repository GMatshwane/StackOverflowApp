package com.gordon.stackoverflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gordon.stackoverflowapp.R

/**
 * A composable that displays a loading overlay with a progress indicator and loading text.
 *
 * This component renders a semi-transparent overlay that covers the entire screen when
 * loading operations are in progress. It provides visual feedback to users indicating
 * that the app is processing a request or performing background work.
 *
 * The overlay features:
 * - Semi-transparent black background (60% opacity) that dims the underlying content
 * - Centered circular progress indicator with Stack Overflow brand colors
 * - Localized loading text with ellipsis animation
 * - Conditional rendering based on loading state
 *
 * The component uses the app's color scheme for consistency:
 * - Progress indicator: Stack Overflow title color with white track
 * - Loading text: White color for contrast against dark overlay
 *
 * This is commonly used during network requests, data loading operations, or any
 * asynchronous tasks that require user feedback in the Stack Overflow app.
 *
 * @param isLoading Boolean flag that determines whether the loading overlay should be displayed.
 *                  When true, the overlay is rendered with progress indicator and loading text.
 *                  When false, no overlay is displayed, allowing normal UI interaction.
 */
@Composable
fun LoaderOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(R.color.stackoverflow_title),
                    trackColor = colorResource(R.color.white),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                     text = stringResource(R.string.loading) + " ...",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
