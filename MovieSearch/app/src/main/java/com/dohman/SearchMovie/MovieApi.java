package com.dohman.SearchMovie;

import com.dohman.SearchMovie.model.SearchResults;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("/3/movie/popular?api_key=bc0d9d234a1124140f2ca26988c9ae27")
    Observable<SearchResults> getPopMovies ();

    @GET("/3/search/movie")
    Observable<SearchResults> getSearchMovies (
            @Query("api_key") String api,
            @Query("query") String inputText);
}
