package com.gordon.stackoverflow.data

/**
 * A sealed class representing the different states of a network operation.
 * This class encapsulates the result of network requests and provides type-safe handling
 * of success, error, and loading states.
 *
 * @param T The type of data expected in the success case.
 */
sealed class NetworkResult<T> {
    /**
     * Represents a successful network operation.
     * Contains the data returned from the network request.
     *
     * @param data The successfully retrieved data of type [T].
     */
    data class Success<T>(val data: T) : NetworkResult<T>()

    /**
     * Represents a failed network operation.
     * Contains an error message describing what went wrong.
     *
     * @param message A descriptive error message explaining the failure.
     */
    data class Error<T>(val message: String) : NetworkResult<T>()

    /**
     * Represents an ongoing network operation.
     * Used to indicate that a network request is in progress.
     *
     * @param isLoading Boolean flag indicating if the operation is currently loading.
     *                  Defaults to true.
     */
    data class Loading<T>(val isLoading: Boolean = true) : NetworkResult<T>()
}