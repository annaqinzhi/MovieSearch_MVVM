package com.dohman.apigrupparbete;

import java.io.Serializable;

/**
 * Created by anandsingh on 28/12/16.
 */

public class MovieDetails  implements Serializable{

    String title;
    String release_year;
    String plot;
    String language;
    String image;
    String genre;
    String country;
    String runtime;
    String producer;
    double rating;

    public MovieDetails(String title, String release_year, String plot, String language, String image, String genre, String country, String runtime, String producer, double rating) {
        this.title = title;
        this.release_year = release_year;
        this.plot = plot;
        this.language = language;
        this.image = image;
        this.genre = genre;
        this.country = country;
        this.runtime = runtime;
        this.producer = producer;
        this.rating = rating;
    }

    public MovieDetails() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_year() {
        return release_year;
    }

    public void setRelease_year(String release_year) {
        this.release_year = release_year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
