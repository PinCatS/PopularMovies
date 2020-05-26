# PopularMovies
Poject 1 at Udacity Android Basics nanodegree program where I need to create movies app (stage 1).

At that stage the app should retrieve movies data from themoviedb.org using their api and show the movies' posters in the grid.
User could refresh the page and retrieve popular or top rated movies by selecting appropriate option from the menu.
Clicking on the movie poster, it should open the movie details screen that contains movie details.

In addition sharing functionality was implemented and the app saves state in case of screen configuration changes or when user switches between apps.
And retrieve the saved state and restore the views without pulling data from the internet.

## Pre-requisites
* Android SDK v29 (min SDK v21)
* Android Build Tools v29.0.3
* Themoviedb API key

## Getting started
The app uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio.
To be able to run the app successfully, you need to:
1. Get themoviedb API key from themoviedb.org
2. Create apikey.properties file in the root directory of the project
3. Add the following line to newly created apikey.properties: THEMOVIEDB_API_KEY="YOUR_API_KEY"
4. Rebuild the project
