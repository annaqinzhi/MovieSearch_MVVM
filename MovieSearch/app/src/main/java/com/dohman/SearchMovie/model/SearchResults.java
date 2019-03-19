package com.dohman.SearchMovie.model;

import com.dohman.SearchMovie.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResults implements Serializable {

    @SerializedName("page")
    private String page;
    @SerializedName("total_results")
    private String totalResults;
    @SerializedName("total_pages")
    private String totalPages;
    @SerializedName("results")
    private List<Movie> results;

    public SearchResults(String page, String totalResults, String totalPages, List<Movie> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
