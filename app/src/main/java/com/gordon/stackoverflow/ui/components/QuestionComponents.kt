package com.gordon.stackoverflow.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gordon.stackoverflow.data.Question
import com.gordon.stackoverflow.ui.dateFormated
import com.gordon.stackoverflow.ui.stripHtml
import com.gordon.stackoverflowapp.R

@Composable
fun QuestionList(
    questions: List<Question>,
    onQuestionClick: (Question) -> Unit
) {
    if (questions.isEmpty()) {
        EmptySearchResult()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(questions) { question ->
                Column {
                    QuestionItem(
                        question = question,
                        onQuestionClick = { onQuestionClick(question) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = colorResource(R.color.stackoverflow_gray)
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionItem(question: Question, onQuestionClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onQuestionClick() }
            .padding(horizontal = 8.dp, vertical = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Always reserve space for the accepted answer indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            ) {
                if (question.accepted_answer_id != null) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = stringResource(R.string.accepted_answer),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.question_prefix) + question.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.stackoverflow_title),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                question.body?.let { body ->
                    Text(
                        text = stripHtml(body),
                        fontSize = 12.sp,
                        color = colorResource(R.color.stackoverflow_gray),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    buildAnnotatedString {
                        append("${stringResource(R.string.asked)} ${dateFormated(question.creation_date)} ${stringResource(R.string.by)} ")
                        withStyle(style = SpanStyle(
                            color = colorResource(R.color.stackoverflow_title),
                            fontWeight = FontWeight.Bold
                        )
                        ) {
                            append(question.owner.display_name)
                        }
                    },
                    fontSize = 10.sp,
                    color = colorResource(R.color.stackoverflow_gray)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(end = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.answers_count, question.answer_count),
                    fontSize = 11.sp,
                    color = colorResource(R.color.stackoverflow_orange),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = stringResource(R.string.votes_count, question.score),
                    fontSize = 11.sp,
                    color = colorResource(R.color.stackoverflow_gray),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = stringResource(R.string.views_count, question.view_count),
                    fontSize = 11.sp,
                    color = colorResource(R.color.stackoverflow_gray)
                )
            }
            // Arrow icon
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = stringResource(R.string.go_to_details),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
