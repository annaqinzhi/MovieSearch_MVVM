package com.dohman.SearchMovie.viewmodel

import com.dohman.SearchMovie.model.Movie

sealed class MovieState {
    class Error(val error: Throwable) : MovieState()
    class PopMovieRecieved(val movieList: List<Movie>) : MovieState()
    class SearchMovieRecieved(val movieList: List<Movie>) : MovieState()
}