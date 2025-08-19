package com.gordon.stackoverflow.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.gordon.stackoverflowapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class for managing Stack Overflow API operations.
 *
 * This class serves as the data layer for the Stack Overflow app, handling network requests,
 * network connectivity checks, and providing a clean API for the presentation layer.
 * All network operations are performed on the IO dispatcher for optimal performance.
 *
 * @property apiService The Retrofit API service for Stack Overflow endpoints.
 * @property context Android context used for network connectivity checks and string resources.
 */
class StackOverflowRepository(
    private val apiService: StackOverflowApiService,
    private val context: Context
) {
    /**
     * Checks if the device has an active internet connection.
     *
     * This method uses the ConnectivityManager to verify that the device has an active
     * network connection with internet capability.
     *
     * @return true if internet connection is available, false otherwise.
     */
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Searches Stack Overflow questions based on a query string.
     *
     * Performs a search for Stack Overflow questions using the provided query string.
     * The search is performed on the IO dispatcher and includes network connectivity checks.
     *
     * @param query The search query string to look for in question titles.
     * @return [NetworkResult] containing a list of [Question] objects on success,
     *         or an error message on failure.
     */
    suspend fun searchQuestions(query: String): NetworkResult<List<Question>> {
        return withContext(Dispatchers.IO) {
            try {
                if (!isNetworkAvailable()) {
                    return@withContext NetworkResult.Error(context.getString(R.string.no_internet_connection))
                }

                val response = apiService.searchQuestions(title = query)
                if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->
                        NetworkResult.Success(searchResponse.items)
                    } ?: NetworkResult.Error(context.getString(R.string.empty_response))
                } else {
                    NetworkResult.Error(context.getString(R.string.search_failed, response.message()))
                }
            } catch (e: Exception) {
                NetworkResult.Error(context.getString(R.string.network_error, e.localizedMessage))
            }
        }
    }

    /**
     * Retrieves answers for a specific Stack Overflow question.
     *
     * Fetches all answers associated with a given question ID from the Stack Overflow API.
     * The operation is performed on the IO dispatcher and includes network connectivity checks.
     *
     * @param questionId The unique identifier of the question to get answers for.
     * @return [NetworkResult] containing a list of [Answer] objects on success,
     *         or an error message on failure.
     */
    suspend fun getAnswers(questionId: Int): NetworkResult<List<Answer>> {
        return withContext(Dispatchers.IO) {
            try {
                if (!isNetworkAvailable()) {
                    return@withContext NetworkResult.Error(context.getString(R.string.no_internet_connection))
                }

                val response = apiService.getAnswers(questionId)
                if (response.isSuccessful) {
                    response.body()?.let { answersResponse ->
                        NetworkResult.Success(answersResponse.items)
                    } ?: NetworkResult.Error(context.getString(R.string.empty_response))
                } else {
                    NetworkResult.Error(context.getString(R.string.failed_to_load_answers, response.message()))
                }
            } catch (e: Exception) {
                NetworkResult.Error(context.getString(R.string.network_error, e.localizedMessage))
            }
        }
    }

    /**
     * Fetches recent Stack Overflow questions.
     *
     * Retrieves a list of the most recent questions from Stack Overflow, sorted by activity.
     * This is typically used to populate the main feed or home screen of the application.
     * The operation is performed on the IO dispatcher and includes network connectivity checks.
     *
     * @return [NetworkResult] containing a list of recent [Question] objects on success,
     *         or an error message on failure.
     */
    suspend fun fetchRecentQuestions(): NetworkResult<List<Question>> {
        return withContext(Dispatchers.IO) {
            try {
                if (!isNetworkAvailable()) {
                    return@withContext NetworkResult.Error(context.getString(R.string.no_internet_connection))
                }
                val response = apiService.getRecentQuestions()
                if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->
                        NetworkResult.Success(searchResponse.items)
                    } ?: NetworkResult.Error(context.getString(R.string.empty_response))
                } else {
                    NetworkResult.Error(context.getString(R.string.failed_to_load_questions, response.message()))
                }
            } catch (e: Exception) {
                NetworkResult.Error(context.getString(R.string.network_error, e.localizedMessage))
            }
        }
    }

    /**
     * Retrieves a specific Stack Overflow question by its ID.
     *
     * Fetches detailed information about a single question using its unique identifier.
     * This is typically used when navigating to a question's detail view.
     * The operation is performed on the IO dispatcher and includes network connectivity checks.
     *
     * @param questionId The unique identifier of the question to retrieve.
     * @return [NetworkResult] containing the [Question] object on success,
     *         or an error message on failure. Returns "Question not found" if the question
     *         doesn't exist or the response is empty.
     */
    suspend fun getQuestionById(questionId: Int): NetworkResult<Question> {
        return withContext(Dispatchers.IO) {
            try {
                if (!isNetworkAvailable()) {
                    return@withContext NetworkResult.Error(context.getString(R.string.no_internet_connection))
                }
                val response = apiService.getQuestionById(questionId)
                if (response.isSuccessful) {
                    val items = response.body()?.items
                    if (!items.isNullOrEmpty()) {
                        NetworkResult.Success(items[0])
                    } else {
                        NetworkResult.Error(context.getString(R.string.question_not_found))
                    }
                } else {
                    NetworkResult.Error(context.getString(R.string.failed_to_load_question, response.message()))
                }
            } catch (e: Exception) {
                NetworkResult.Error(context.getString(R.string.network_error, e.localizedMessage))
            }
        }
    }
}