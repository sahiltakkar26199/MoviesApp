package com.example.sahiltakkar.moviesapp.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.sahiltakkar.moviesapp.data.AppDatabase;
import com.example.sahiltakkar.moviesapp.data.MovieEntry;

import java.util.List;


public class MyViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntry>> list;;

    public MyViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db=AppDatabase.getsInstance(this.getApplication());
        list=db.movieDAO().loadAllMovies();
    }

    public LiveData<List<MovieEntry>> getList(){
        return list;
    }
}
