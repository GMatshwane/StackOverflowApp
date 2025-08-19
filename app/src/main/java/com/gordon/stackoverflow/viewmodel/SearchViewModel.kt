package com.gordon.stackoverflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gordon.stackoverflow.data.NetworkResult
import com.gordon.stackoverflow.data.Question
import com.gordon.stackoverflow.data.StackOverflowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing search functionality in the StackOverflow app.
 * Handles fetching recent questions, searching for specific queries,
 * and managing loading states and error messages.
 */
class SearchViewModel(private val repository: StackOverflowRepository) : ViewModel() {
    /**
     * StateFlow to hold the list of search results.
     * Initialized with an empty list.
     */
    private val _searchResults = MutableStateFlow<List<Question>>(emptyList())
    /**
     * Exposes the search results as a StateFlow.
     * This allows the UI to observe changes in the search results.
     */
    val searchResults: StateFlow<List<Question>> = _searchResults.asStateFlow()
    /**
     * StateFlow to indicate whether the app is currently loading data.
     * Initialized to false, indicating no loading state initially.
     */
    private val _isLoading = MutableStateFlow(false)
    /**
     * Exposes the loading state as a StateFlow.
     * This allows the UI to observe changes in the loading state,
     * enabling it to show or hide loading indicators as necessary.
     */
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    /**
     * StateFlow to hold any error messages that occur during data fetching or searching.
     * Initialized to null, indicating no error initially.
     */
    private val _errorMessage = MutableStateFlow<String?>(null)
    /**
     * Exposes the error message as a StateFlow.
     * This allows the UI to observe changes in the error message,
     * enabling it to display error messages to the user when necessary.
     */
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    /**
     * StateFlow to control the visibility of a network error dialog.
     * Initialized to false, indicating the dialog is not shown initially.
     */
    private val _showNetworkDialog = MutableStateFlow(false)
    /**
     * Exposes the network dialog visibility state as a StateFlow.
     * This allows the UI to observe changes in the dialog visibility,
     * enabling it to show or hide the dialog based on network errors.
     */
    val showNetworkDialog: StateFlow<Boolean> = _showNetworkDialog.asStateFlow()
    /**
     * Initializes the ViewModel by fetching recent questions from the repository.
     * This is done in a coroutine to avoid blocking the main thread.
     * The loading state is set to true while fetching data,
     * and it is set to false once the data is fetched or an error occurs.
     */
    init {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.fetchRecentQuestions()) {
                is NetworkResult.Success -> _searchResults.value = result.data
                is NetworkResult.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    /**
     * Searches for questions based on the provided query string.
     * If the query is blank, it returns immediately without performing a search.
     * It updates the loading state and error message accordingly.
     * If the search is successful, it updates the search results.
     * If there is an error, it checks if the error message contains "internet" to
     * show a network dialog, or sets the error message to be displayed.
     */
    fun searchQuestions(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.searchQuestions(query)) {
                is NetworkResult.Success -> {
                    _searchResults.value = result.data
                }
                is NetworkResult.Error -> {
                    if (result.message.contains("internet", ignoreCase = true)) {
                        _showNetworkDialog.value = true
                    } else {
                        _errorMessage.value = result.message
                    }
                }
                else -> {}
            }
            _isLoading.value = false
        }
    }
    /**
     * Refreshes the list of questions by fetching recent questions from the repository.
     * It updates the loading state and error message accordingly.
     * If the fetch is successful, it updates the search results.
     * If there is an error, it sets the error message to be displayed.
     */
    fun refreshQuestions() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            when (val result = repository.fetchRecentQuestions()) {
                is NetworkResult.Success -> _searchResults.value = result.data
                is NetworkResult.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }
    /**
     * Dismisses the network error dialog by setting its visibility state to false.
     * This is typically called when the user acknowledges the dialog.
     */
    fun dismissNetworkDialog() {
        _showNetworkDialog.value = false
    }
    /**
     * Clears the current error message by setting it to null.
     * This is typically called when the error has been acknowledged or resolved.
     */
    fun clearError() {
        _errorMessage.value = null
    }
}