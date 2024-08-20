# MovieHunt - Your Ultimate Movie Companion

**Description:**
MovieHunt is an Android application built with Kotlin and Jetpack Compose, designed to help users discover and manage their favorite movies and TV shows. The project follows Clean Architecture principles and is structured in a feature-driven approach.
This app that helps you discover and explore movies. Search for your favorite movies, save them for later, and stay updated with the latest releases.

ScreenShots                | --
:-------------------------:|:-------------------------:
![](https://github.com/user-attachments/assets/ea5910e7-d02b-4b52-94fe-726c6121cf19)  |  ![](https://github.com/user-attachments/assets/5a35b691-a28f-47cf-89a3-8515d728b335)
![](https://github.com/user-attachments/assets/d2cb8589-0e36-4f35-af8f-f4517587a53f)  |  ![](https://github.com/user-attachments/assets/b62b9708-7df1-4129-9533-3e1ad4baa5e2)


**Features:**
* Search for movies by title
* View detailed movie information including cast, overview, and ratings
* Bookmark your favorite movies or Tv Series for easy access
* Explore upcoming and trending movies


**Installation:**
1. Clone the repository: `git clone https://github.com/Minsamin/moviehunt.git`

**Usage:**
* Open the app and explore the home screen to discover movies
* Use the search bar to find specific movies
* Tap on a movie to view its details
* View cast information.
* Bookmark movies or Tv Series by clicking the bookmark icon

**Technologies:**
* Jetpack Compose: Modern toolkit for building native Android UI and Navigation.
* Kotlin: Official programming language for Android development.
* Dagger Hilt: Dependency injection library for Android.
* Room: SQLite object mapping library.
* Retrofit: Type-safe HTTP client for Android.
* Paging 3: Library for pagination in Android apps.
* Coroutines: Kotlin library for asynchronous programming.
* Coil: Fast, lightweight image loading library for Android.
* DataStore: Jetpack's replacement for SharedPreferences.
* [Other technologies used]

# Project Structure

The project is organized in a feature-driven manner, with each feature encapsulated within its own module. The structure follows the Clean Architecture approach, which separates the project into multiple layers:

- **Common**: Shared utilities, navigation, and base classes.
- **Features**:
  - **Bookmark**: Handles the bookmarking of movies and shows.
  - **Cast**: Manages the display of cast information.
  - **Details**: Fetches and displays detailed information about movies and shows.
  - **Home**: The main screen where users can browse popular movies and shows.
  - **Search**: Handles searching for movies and TV shows.
  - **Settings**: Manages application settings.

# Directory Structure

```plaintext
com/
└── rpimx/
    └── moviehunt/
        ├── common/
        │   ├── data/
        │   ├── di/
        │   ├── domain/
        │   ├── navigation/
        │   ├── presentation/
        │   └── utils/
        └── features/
            ├── bookmark/
            │   ├── data/
            │   ├── di/
            │   ├── domain/
            │   └── presentation/
            ├── cast/
            │   ├── data/
            │   ├── di/
            │   ├── domain/
            │   └── presentation/
            ├── details/
            │   ├── data/
            │   └── presentation/
            │       └── common/
            ├── home/
            │   ├── data/
            │   │   ├── paging/
            │   │   ├── remote/
            │   │   └── repository/
            │   ├── domain/
            │   └── presentation/
            ├── search/
            │   ├── data/
            │   ├── di/
            │   ├── domain/
            │   └── presentation/
            └── settings/
                ├── domain/
                └── presentation/
        └── ui/
```

Overview

https://github.com/user-attachments/assets/7efb0606-5519-4cfd-b2be-a1524c15f896

![MovieHuntAndroidTestResult](https://github.com/user-attachments/assets/b6c45d00-af6d-492a-8ff6-9fe3324d38c1)
![MovieHuntUITestResult](https://github.com/user-attachments/assets/095b9c82-279f-49d7-b400-00c2278d6ded)

**Contributing:**
* Contributions are welcome! If you find any issues or have suggestions for improvements, feel free to create an issue or submit a pull request.

**Contact:**
* If you have any questions, feel free to reach out at saminali500@gmail.com.
