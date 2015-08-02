package com.example.samplelist;

import java.util.ArrayList;

/**
 * FileName: Movie.java
 * <p/>
 * This is simple bean class that is used to get the information that need to be shown in the news feed.
 */

public class  Movie {
    private String title, thumbnailUrl;
    private String year;
    private String rating;
    private String genre;
    private String url;
    private String description;

    public Movie() {
    }

    public Movie(String name, String thumbnailUrl, String year, String rating,
                 String genre, String url, String description) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
        this.url = url;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String string) {
        this.rating = string;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}