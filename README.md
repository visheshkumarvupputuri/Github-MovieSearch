# Movie-Search
This project helps in discovering movies and also in searching movies of user choice.

Intially, movies with ascending order of release dates are displayed. When the user searches for a movie, all the movies with matching name are displayed.

The application uses Retrofit for networking calls, kotlin language, and LiveData to create a reactive pattern.

LiveData helps in communication between the View and ViewModel, and also between the ViewModel and Model.

Glide is used to load images as it offers a robust caching mechanism.

To load all the content I have not used Paging library from Android JetPack but have used ScrollListener on Recycler View.
