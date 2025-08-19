package com.gordon.stackoverflow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gordon.stackoverflow.data.Answer
import com.gordon.stackoverflow.data.NetworkModule
import com.gordon.stackoverflow.viewmodel.DetailViewModel
import com.gordon.stackoverflow.viewmodel.ViewModelFactory
import com.gordon.stackoverflowapp.R

/**
 * A composable that displays the detailed view of a Stack Overflow question and its answers.
 *
 * This screen provides a comprehensive view of a single Stack Overflow question including:
 * - Question title, body, tags, and metadata (creation date, view count)
 * - Question author information with avatar and reputation
 * - List of answers with filtering options (by votes, activity, or oldest)
 * - Answer voting, acceptance status, and author details
 * - Navigation back to the previous screen
 * - Error handling and network status dialogs
 *
 * The screen uses a LazyColumn for efficient scrolling through potentially long question
 * bodies and multiple answers. It supports three filter modes for answers:
 * - "Votes": Sorts answers by score in descending order
 * - "Active": Sorts answers by creation date in descending order (most recent first)
 * - "Oldest": Sorts answers by creation date in ascending order (oldest first)
 *
 * Visual features:
 * - Top app bar with back navigation
 * - Question details with formatted metadata and user information
 * - Scrollable tags display with Stack Overflow orange styling
 * - Filter chips for answer sorting with Stack Overflow theming
 * - Answer cards with acceptance indicators and vote counts
 * - User avatars loaded asynchronously with Coil
 * - Error cards for network or loading errors
 * - Network dialog for connectivity issues
 *
 * @param questionId The unique identifier of the Stack Overflow question to display.
 *                   Used to fetch question details and associated answers from the API.
 * @param navController Navigation controller for handling back navigation and screen transitions.
 *                      Used to navigate back to the previous screen when the back button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(questionId: Int, navController: NavController) {
    val context = LocalContext.current
    val repository = NetworkModule.provideRepository(context)
    val factory = ViewModelFactory(repository)
    val detailViewModel: DetailViewModel = viewModel(factory = factory)

    val question by detailViewModel.question.collectAsState()
    val answers by detailViewModel.answers.collectAsState()
    val isLoading by detailViewModel.isLoading.collectAsState()
    val errorMessage by detailViewModel.errorMessage.collectAsState()
    val showNetworkDialog by detailViewModel.showNetworkDialog.collectAsState()

    LaunchedEffect(questionId) {
        detailViewModel.loadQuestion(questionId)
        detailViewModel.loadAnswers(questionId)
    }

    val filterOptions = listOf("Votes", "Active", "Oldest")
    var selectedFilter by rememberSaveable { mutableStateOf("Votes") }
    val answerListState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.more_info)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            state = answerListState
        ) {
            // Question details as items
            item {
                Text(
                    text = question?.title ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                if (question != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            buildAnnotatedString {
                                append(stringResource(R.string.asked_formatted) + " ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(formatDate(question!!.creation_date))
                                }
                            },
                            fontSize = 12.sp,
                            color = colorResource(R.color.stackoverflow_gray),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            buildAnnotatedString {
                                append(stringResource(R.string.viewed_formatted) + " ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("${question!!.view_count} ${stringResource(R.string.times)}")
                                }
                            },
                            fontSize = 12.sp,
                            color = colorResource(R.color.stackoverflow_light_gray),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 5.dp),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )

                    // Question body
                    question!!.body?.let { body ->
                        Text(
                            text = stripHtml(body),
                            fontSize = 14.sp,
                            color = Color.Black,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // Tags
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        question!!.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(colorResource(R.color.stackoverflow_orange), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = tag, color = Color.White, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }

                    Text(buildAnnotatedString {
                        append(stringResource(R.string.asked_formatted) + " ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(dateFormated(question!!.creation_date))
                        }
                    }, fontSize = 12.sp, color = Color.Gray)

                    // User info
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val avatar = question!!.owner.profile_image
                        if (avatar != null) {
                            AsyncImage(
                                model = avatar,
                                contentDescription = stringResource(R.string.user_avatar),
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                question!!.owner.display_name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                            question!!.owner.reputation?.let {
                                Text(
                                    "%,d".format(it),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    // Answers section
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            "${answers.size} " + stringResource(R.string.answers),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                            filterOptions.forEach { filter ->
                                FilterChip(
                                    selected = selectedFilter == filter,
                                    onClick = { selectedFilter = filter },
                                    label = {
                                        Text(
                                            when(filter) {
                                                "Votes" -> stringResource(R.string.votes)
                                                "Active" -> stringResource(R.string.active)
                                                "Oldest" -> stringResource(R.string.oldest)
                                                else -> filter
                                            },
                                            fontSize = 12.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = colorResource(R.color.stackoverflow_orange),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color.Transparent,
                                        labelColor = colorResource(R.color.stackoverflow_gray)
                                    ),
                                    modifier = Modifier
                                        .padding(start = 2.dp)
                                        .height(26.dp)
                                        .defaultMinSize(minHeight = 0.dp, minWidth = 0.dp)
                                )
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )
                }
            }

            // Error message
            item {
                errorMessage?.let { message ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Answers list
            items(
                when (selectedFilter) {
                    "Votes" -> answers.sortedByDescending { it.score }
                    "Active" -> answers.sortedByDescending { it.creation_date }
                    "Oldest" -> answers.sortedBy { it.creation_date }
                    else -> answers
                }
            ) { answer ->
                AnswerItem(answer = answer)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showNetworkDialog) {
        NoNetworkDialog(onDismiss = { detailViewModel.dismissNetworkDialog() })
    }
}

/**
 * A composable that displays a single answer item in a card layout.
 *
 * This component renders a comprehensive view of a Stack Overflow answer with the following elements:
 * - Vote count and score display on the left side
 * - Accepted answer indicator (green checkmark) when the answer is accepted
 * - Answer body text with HTML stripped and proper formatting
 * - Answer metadata including creation date
 * - Answer author information with avatar, name, and reputation
 *
 * Visual styling and behavior:
 * - Uses a Card with elevated appearance (2dp elevation)
 * - Accepted answers have a light green background tint
 * - Vote count is prominently displayed with "Votes" label
 * - Accepted indicator shows a green circle with white checkmark
 * - Author avatars are loaded asynchronously and displayed as circles
 * - Reputation is formatted with thousand separators
 * - Answer body supports rich text with proper line height for readability
 *
 * The layout uses a Row structure with:
 * - Left column: Vote count and acceptance indicator (40dp width)
 * - Right column: Answer content, metadata, and author info (weighted to fill remaining space)
 *
 * @param answer The [Answer] object containing all the data to display including
 *               body content, score, creation date, author information, and acceptance status.
 *               This data is used to populate all visual elements of the answer card.
 */
@Composable
fun AnswerItem(answer: Answer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (answer.is_accepted) Color(0xFFF0F8F0) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Vote count and accepted indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(40.dp)
            ) {
                if (answer.is_accepted) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(colorResource(R.color.stackoverflow_green), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.accepted_answer),
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    answer.score.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.stackoverflow_gray)
                )
                Text(stringResource(R.string.votes_label), fontSize = 10.sp, color = colorResource(R.color.stackoverflow_gray))
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stripHtml(answer.body),
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        buildAnnotatedString {
                            append(stringResource(R.string.asked_formatted) + " ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(dateFormated(answer.creation_date))
                            }
                        },
                        fontSize = 12.sp,
                        color = colorResource(R.color.stackoverflow_gray)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    // User info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = answer.owner.profile_image
                                ?: "https://www.gravatar.com/avatar/placeholder",
                            contentDescription = stringResource(R.string.user_avatar),
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(verticalArrangement = Arrangement.Top) {
                            Text(
                                answer.owner.display_name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier
                            )
                            Text(
                                text = "%,d".format(answer.owner.reputation),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}