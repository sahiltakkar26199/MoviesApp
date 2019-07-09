package com.example.sahiltakkar.moviesapp;

public class MovieReview {

    private String author;
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public MovieReview(String author , String content){
        this.author=author;
        this.content=content;
    }

}
