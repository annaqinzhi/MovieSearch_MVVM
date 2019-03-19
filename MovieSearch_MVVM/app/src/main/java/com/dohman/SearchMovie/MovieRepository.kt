package com.dohman.SearchMovie

import com.dohman.SearchMovie.model.SearchResults

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MovieRepository {

    private val base_URL = "https://api.themoviedb.org"
    val api = "bc0d9d234a1124140f2ca26988c9ae27"

    val popMoviesObservable: Observable<SearchResults>
        get() {

            val retrofit = Retrofit.Builder()
                    .baseUrl(base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            val movieApi = retrofit.create(MovieApi::class.java)

            return movieApi.popMovies

        }

    fun getSearchMoviesObservable(inputText: String): Observable<SearchResults> {

        val retrofit = Retrofit.Builder()
                .baseUrl(base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val movieApi = retrofit.create(MovieApi::class.java)

        return movieApi.getSearchMovies(api, inputText)

    }

}
