package com.example.sahiltakkar.moviesapp;

import android.graphics.Bitmap;

import java.util.List;

public class Movie {

private int id;
private Bitmap poster;
private List<String> genre;
private String homepage;
private String overView;
private String language;
private String tagline;
private String date;
private String title;

Movie(int id , Bitmap poster , List<String> genre , String homepage , String overView , String language , String tagline,String date,String title){
    this.id=id;
    this.poster=poster;
    this.genre=genre;
    this.homepage=homepage;
    this.overView=overView;
    this.language=language;
    this.tagline=tagline;
    this.date=date;
    this.title=title;
}

    public String getTitle() {
        return title;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getLanguage() {
        return language;
    }

    public int getId() {
        return id;
    }

    public String getOverView() {
        return overView;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDate() {
        return date;
    }
}
