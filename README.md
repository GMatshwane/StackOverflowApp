# StackOverflowApp

## Overview
StackOverflowApp is an Android application designed to allow users to search, view, and interact with questions and answers from Stack Overflow. The app provides a user-friendly interface for browsing recent questions, searching for specific topics, and viewing detailed answers. It is intended for developers and enthusiasts who want quick access to Stack Overflow content on their mobile devices.

## Architecture
The project follows a modular MVVM (Model-View-ViewModel) architecture:
- **Model**: Data classes representing Stack Overflow entities (questions, answers, users).
- **View**: Jetpack Compose UI components for rendering lists, cards, and details.
- **ViewModel**: Handles business logic, state management, and data fetching using Kotlin coroutines.
- **Repository**: Abstracts data access, providing a clean API for ViewModels to interact with remote data sources.

### Patterns Used
- **MVVM**: Separation of concerns between UI, business logic, and data.
- **Repository Pattern**: Centralizes data operations and abstracts API calls.
- **Dependency Injection**: ViewModelFactory is used for ViewModel instantiation.
- **Jetpack Compose**: Declarative UI framework for building native Android interfaces.
- **Coroutines**: Asynchronous programming for network and data operations.

## Stack Overflow API
The app uses the [Stack Exchange API](https://api.stackexchange.com/) to retrieve data from Stack Overflow.

### Endpoints Used
- **Recent Questions**: `GET /2.3/questions?order=desc&sort=activity&site=stackoverflow`
- **Search Questions**: `GET /2.3/search?order=desc&sort=activity&intitle={query}&site=stackoverflow`
- **Question Details**: `GET /2.3/questions/{ids}?order=desc&sort=activity&site=stackoverflow&filter=withbody`
- **Answers**: `GET /2.3/questions/{ids}/answers?order=desc&sort=activity&site=stackoverflow`

## Additional Features
- **Error Handling**: Displays error messages using custom ErrorCard components.
- **Splash Screen**: Custom splash and app icon support.
- **Back Button Confirmation**: Prompts user before closing the app from the search screen.

## Testing
The project includes comprehensive unit and UI tests:
- **ViewModel Tests**: Validate business logic and state management.
- **Repository Tests**: Ensure correct API interaction and error handling.
- **UI Component Tests**: Verify rendering and user interaction using Jetpack Compose testing APIs.

## Getting Started
1. Clone the repository.
2. Open in Android Studio.
3. Build and run the app on an emulator or device.
4. Run tests using `./gradlew testDebugUnitTest`.

## Notes
- The installation file can be found in the ``build-file`` directory in the root of the project.
- The app requires internet access to fetch data from Stack Overflow.
- API rate limits may apply.
- All static strings are managed via `strings.xml` for localization support.
- Colors and themes are defined in `colors.xml` and `themes.xml` for consistent styling.
- The app uses Material Design components for a modern look and feel.
- The app is designed to be accessible, with proper content descriptions and focus management for screen readers.
- The app is designed to be responsive and works on various screen sizes and orientations.
- The code is structured to facilitate easy navigation and understanding of the project components.
- The app follows best practices for Android development, including lifecycle management and state handling.
- 

