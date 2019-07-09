package com.example.sahiltakkar.moviesapp.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.sahiltakkar.moviesapp.ViewModels.AddMovieByIdViewModel;
import com.example.sahiltakkar.moviesapp.data.AppDatabase;

public class AddMovieByIdViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private  AppDatabase mDb;
    private  int mMovieId;

    public AddMovieByIdViewModelFactory(AppDatabase database,int id){
        mDb=database;
        mMovieId=id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new AddMovieByIdViewModel(mDb,mMovieId);
    }
}
