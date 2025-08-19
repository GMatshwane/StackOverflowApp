package com.gordon.stackoverflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gordon.stackoverflowapp.R

/**
 * A composable that displays an empty search result state.
 *
 * This component is shown when a search query returns no results, providing
 * visual feedback to the user with a search icon and explanatory text.
 * The component is centered on the screen and uses the app's color scheme
 * for consistent theming.
 *
 * The component displays:
 * - A search icon to indicate the search context
 * - A localized "No results found" message
 *
 * All visual elements use the app's defined color resources and styling
 * to maintain consistency with the overall design system.
 */
@Composable
fun EmptySearchResult() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "No results",
                tint = colorResource(R.color.stackoverflow_gray),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_results_found),
                color = colorResource(R.color.stackoverflow_gray),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}