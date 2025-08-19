package com.gordon.stackoverflow.data

/**
 * Represents the response from a Stack Overflow search query.
 *
 * @property items List of questions returned by the search.
 * @property has_more Indicates if more results are available.
 * @property quota_max Maximum number of requests allowed.
 * @property quota_remaining Number of requests remaining.
 */
data class SearchResponse(
    val items: List<Question>,
    val has_more: Boolean,
    val quota_max: Int,
    val quota_remaining: Int
)

/**
 * Represents the response containing answers for a Stack Overflow question.
 *
 * @property items List of answers returned.
 * @property has_more Indicates if more results are available.
 * @property quota_max Maximum number of requests allowed.
 * @property quota_remaining Number of requests remaining.
 */
data class AnswersResponse(
    val items: List<Answer>,
    val has_more: Boolean,
    val quota_max: Int,
    val quota_remaining: Int
)

/**
 * Represents a Stack Overflow question.
 *
 * @property question_id Unique identifier for the question.
 * @property title Title of the question.
 * @property body Body content of the question (may be null).
 * @property score Score/votes for the question.
 * @property answer_count Number of answers for the question.
 * @property view_count Number of views for the question.
 * @property creation_date Creation date as a Unix timestamp.
 * @property last_activity_date Last activity date as a Unix timestamp.
 * @property owner Owner/user who posted the question.
 * @property tags List of tags associated with the question.
 * @property is_answered Indicates if the question has been answered.
 * @property accepted_answer_id ID of the accepted answer (may be null).
 */
data class Question(
    val question_id: Int,
    val title: String,
    val body: String? = null,
    val score: Int,
    val answer_count: Int,
    val view_count: Int,
    val creation_date: Long,
    val last_activity_date: Long,
    val owner: Owner,
    val tags: List<String>,
    val is_answered: Boolean,
    val accepted_answer_id: Int? = null
)

/**
 * Represents an answer to a Stack Overflow question.
 *
 * @property answer_id Unique identifier for the answer.
 * @property question_id ID of the question this answer belongs to.
 * @property body Body content of the answer.
 * @property score Score/votes for the answer.
 * @property creation_date Creation date as a Unix timestamp.
 * @property owner Owner/user who posted the answer.
 * @property is_accepted Indicates if this answer is accepted.
 */
data class Answer(
    val answer_id: Int,
    val question_id: Int,
    val body: String,
    val score: Int,
    val creation_date: Long,
    val owner: Owner,
    val is_accepted: Boolean
)

/**
 * Represents a Stack Overflow user/owner.
 *
 * @property user_id Unique identifier for the user (may be null).
 * @property display_name Display name of the user.
 * @property profile_image URL to the user's profile image (may be null).
 * @property reputation Reputation score of the user (may be null).
 */
data class Owner(
    val user_id: Int? = null,
    val display_name: String,
    val profile_image: String? = null,
    val reputation: Int? = null
)