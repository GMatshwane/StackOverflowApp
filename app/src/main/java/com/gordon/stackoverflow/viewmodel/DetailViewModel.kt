package com.gordon.stackoverflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gordon.stackoverflow.data.Answer
import com.gordon.stackoverflow.data.NetworkResult
import com.gordon.stackoverflow.data.Question
import com.gordon.stackoverflow.data.StackOverflowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the details of a question and its answers in the StackOverflow app.
 * Handles fetching answers, managing loading states, and handling errors.
 */
class DetailViewModel(private val repository: StackOverflowRepository) : ViewModel() {
    /**
     * StateFlow to hold the list of answers for a question.
     * Initialized with an empty list.
     */
    private val _answers = MutableStateFlow<List<Answer>>(emptyList())
    val answers: StateFlow<List<Answer>> = _answers.asStateFlow()
    /**
     * StateFlow to indicate whether the app is currently loading data.
     * Initialized to false, indicating no loading state initially.
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    /**
     * StateFlow to hold any error messages that occur during data fetching.
     * Initialized to null, indicating no error initially.
     */
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    /**
     * StateFlow to control the visibility of a network error dialog.
     * Initialized to false, indicating the dialog is not shown initially.
     */
    private val _showNetworkDialog = MutableStateFlow(false)
    val showNetworkDialog: StateFlow<Boolean> = _showNetworkDialog.asStateFlow()
    /**
     * StateFlow to hold the question details.
     * Initialized with null, indicating no question is loaded initially.
     */
    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question.asStateFlow()

    /**
     * Loads the answers for a specific question by its ID.
     * Updates the answers state and handles loading and error states.
     *
     * @param questionId The ID of the question for which to load answers.
     * @param Unit
     */
    fun loadAnswers(questionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.getAnswers(questionId)) {
                is NetworkResult.Success -> {
                    _answers.value = result.data
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
     * Loads the details of a specific question by its ID.
     * Updates the question state and handles loading and error states.
     *
     * @param questionId The ID of the question to load.
     * @return Unit
     */
    fun loadQuestion(questionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            when (val result = repository.getQuestionById(questionId)) {
                is NetworkResult.Success -> _question.value = result.data
                is NetworkResult.Error -> _errorMessage.value = result.message
                else -> {}
            }
            _isLoading.value = false
        }
    }

    /**
     * Dismisses the network error dialog by setting its visibility to false.
     * This is typically called when the user acknowledges the dialog.
     *
     * @return Unit
     */
    fun dismissNetworkDialog() {
        _showNetworkDialog.value = false
    }

    /**
     * Clears the current error message by setting it to null.
     * This is typically called when the error has been acknowledged or resolved.
     *
     * @return Unit
     */
    fun clearError() {
        _errorMessage.value = null
    }
}