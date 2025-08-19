package com.gordon.stackoverflow.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for Stack Overflow endpoints.
 */
interface StackOverflowApiService {
    /**
     * Searches Stack Overflow questions using advanced search.
     *
     * @param pageSize Number of results per page (default: 20)
     * @param order Sort order (default: "desc")
     * @param sort Sort by field (default: "activity")
     * @param title Title or keywords to search for
     * @param site Site to search (default: "stackoverflow")
     * @param filter Response filter (default: "withbody")
     * @return [Response] containing [SearchResponse] with matching questions
     */
    @GET("2.2/search/advanced")
    suspend fun searchQuestions(
        @Query("pagesize") pageSize: Int = 20,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "activity",
        @Query("title") title: String,
        @Query("site") site: String = "stackoverflow",
        @Query("filter") filter: String = "withbody"
    ): Response<SearchResponse>

    /**
     * Gets answers for a specific question.
     *
     * @param questionId The ID of the question
     * @param order Sort order (default: "desc")
     * @param sort Sort by field (default: "activity")
     * @param site Site to search (default: "stackoverflow")
     * @param filter Response filter (default: "withbody")
     * @return [Response] containing [AnswersResponse] with answers for the question
     */
    @GET("2.2/questions/{questionId}/answers")
    suspend fun getAnswers(
        @Path("questionId") questionId: Int,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "activity",
        @Query("site") site: String = "stackoverflow",
        @Query("filter") filter: String = "withbody"
    ): Response<AnswersResponse>

    /**
     * Gets recent questions from Stack Overflow.
     *
     * @param pageSize Number of results per page (default: 20)
     * @param order Sort order (default: "desc")
     * @param sort Sort by field (default: "activity")
     * @param site Site to search (default: "stackoverflow")
     * @param filter Response filter (default: "withbody")
     * @return [Response] containing [SearchResponse] with recent questions
     */
    @GET("2.2/questions")
    suspend fun getRecentQuestions(
        @Query("pagesize") pageSize: Int = 20,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "activity",
        @Query("site") site: String = "stackoverflow",
        @Query("filter") filter: String = "withbody"
    ): Response<SearchResponse>

    /**
     * Gets a question by its ID.
     *
     * @param questionId The ID of the question
     * @param site Site to search (default: "stackoverflow")
     * @param filter Response filter (default: "withbody")
     * @return [Response] containing [SearchResponse] with the question details
     */
    @GET("2.2/questions/{questionId}")
    suspend fun getQuestionById(
        @Path("questionId") questionId: Int,
        @Query("site") site: String = "stackoverflow",
        @Query("filter") filter: String = "withbody"
    ): Response<SearchResponse>
}