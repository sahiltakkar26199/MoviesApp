package com.example.sahiltakkar.moviesapp.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.sahiltakkar.moviesapp.data.AppDatabase;
import com.example.sahiltakkar.moviesapp.data.MovieEntry;

public class AddMovieByIdViewModel extends ViewModel {

    private MovieEntry movie;

    public MovieEntry getMovie() {
        return movie;
    }

    public AddMovieByIdViewModel(AppDatabase database , int id){
        movie=database.movieDAO().loadMovieById(id);
    }
}
