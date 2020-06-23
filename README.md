# PopularMovies
Poject 1 and 2 at Udacity Android Basics nanodegree program where I need to create movieEntries app.

At that stage the app should retrieve movieEntries data from themoviedb.org using their api and show the movieEntries' posters in the grid.
User could refresh the page and retrieve popular or top rated movieEntries by selecting appropriate option from the menu.
Clicking on the movieEntry poster, it should open the movieEntry details screen that contains movieEntry details.

In addition sharing functionality was implemented and the app saves state in case of screen configuration changes or when user switches between apps.
And retrieve the saved state and restore the views without pulling data from the internet.

## Pre-requisites
* Android target SDK v30 (min SDK v21)
* Android Build Tools v29.0.3
* Themoviedb API key

## Getting started
The app uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio.
To be able to run the app successfully, you need to:
1. Get themoviedb API key from themoviedb.org
2. Create apikey.properties file in the root directory of the project
3. Add the following line to newly created apikey.properties: THEMOVIEDB_API_KEY="YOUR_API_KEY"
4. Rebuild the project
