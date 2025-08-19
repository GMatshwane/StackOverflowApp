package com.gordon.stackoverflow.data

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides network-related dependencies for the Stack Overflow API.
 * This singleton object configures HTTP client, Retrofit instance, and provides repository instances.
 */
object NetworkModule {
    /** Base URL for Stack Exchange API endpoints */
    private const val BASE_URL = "https://api.stackexchange.com/"

    /**
     * HTTP logging interceptor configured to log request and response bodies.
     * Used for debugging and monitoring network requests in development.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Configured OkHttpClient with logging interceptor.
     * Provides the underlying HTTP client for network requests with request/response logging.
     */
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    /**
     * Configured Retrofit instance for Stack Overflow API communication.
     * Includes base URL, HTTP client, and GSON converter for JSON serialization/deserialization.
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Stack Overflow API service instance created by Retrofit.
     * Provides access to all Stack Overflow API endpoints defined in [StackOverflowApiService].
     */
    val apiService: StackOverflowApiService = retrofit.create(StackOverflowApiService::class.java)

    /**
     * Provides a configured StackOverflowRepository instance.
     *
     * @param context Android context required for network availability checks and system services.
     * @return A new instance of [StackOverflowRepository] configured with the API service and context.
     */
    fun provideRepository(context: Context): StackOverflowRepository {
        return StackOverflowRepository(apiService, context)
    }
}