# PopularMovies
Poject 1 and 2 at Udacity Android Developer nanodegree program where I need to create movieEntries app.

The PopularMovie app lists popular or top rated movies retrieved from themoviedb.org, lets user see the details of the movies (title, synopsis, rating, trailers and reviews) and mark them as favorites that can be listed offline.

* Designed UI for portrait and landscape orientations. Got experience working with Constraint layout, RecyclerView, PagerView2
* Used Android Architecture Components (LiveData, ModelView, Room and DataBinding).
* Followed recommended android app architecture guide where UI is drived from a Model and the Model communicates with a Repository that hides data source from the Model and serves as a mediator between database and network data sources.
* Retrieved data from network and parsed json data working with themoviedb API. Stored and retrieved data from SQLite.
* Enjoyed much! Looking forward to learn more and do cool apps:)

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
