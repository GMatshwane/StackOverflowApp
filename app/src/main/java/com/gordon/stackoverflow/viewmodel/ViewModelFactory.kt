package com.gordon.stackoverflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gordon.stackoverflow.data.StackOverflowRepository

/**
 * Factory class for creating ViewModel instances with dependency injection.
 *
 * This factory provides ViewModels with the required StackOverflowRepository dependency,
 * following the recommended pattern for ViewModel creation with constructor parameters.
 * It supports creation of SearchViewModel and DetailViewModel instances.
 *
 * @property repository The StackOverflowRepository instance to inject into ViewModels.
 */
class ViewModelFactory(private val repository: StackOverflowRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}