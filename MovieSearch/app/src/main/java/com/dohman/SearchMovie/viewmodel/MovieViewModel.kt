package com.dohman.SearchMovie.viewmodel

import com.dohman.SearchMovie.MovieApi
import com.dohman.SearchMovie.MovieRepository
import com.dohman.SearchMovie.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieViewModel (): BaseViewModel<MovieState>() {

    private val movieRepository = MovieRepository

    fun getPopMovieList() {
        movieRepository.popMoviesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            statePublisher.onNext(MovieState.PopMovieRecieved(it.results))
                        },
                        {
                            statePublisher.onNext(MovieState.Error(it))
                        }
                ).addTo(disposable)
    }

    fun getSearchMovieList(input: String) {
        movieRepository.getSearchMoviesObservable(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            statePublisher.onNext(MovieState.SearchMovieRecieved(it.results))
                        },
                        {
                            statePublisher.onNext(MovieState.Error(it))
                        }
                ).addTo(disposable)
    }

}