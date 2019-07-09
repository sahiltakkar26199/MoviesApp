package com.example.sahiltakkar.moviesapp.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieaDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private int id;

    public MovieaDetailViewModelFactory(int id){
        this.id=id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new MovieDetailsViewModel(id);
    }
}
