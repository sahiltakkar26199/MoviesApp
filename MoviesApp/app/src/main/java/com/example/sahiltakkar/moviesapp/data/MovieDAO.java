package com.example.sahiltakkar.moviesapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.sahiltakkar.moviesapp.Movie;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface MovieDAO {

    @Query("SELECT * FROM movies ORDER BY priority")
    LiveData<List<MovieEntry>> loadAllMovies();

    @Insert
    void insertMovie(MovieEntry movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry movieEntry);

    @Delete
    void deleteTask(MovieEntry movieEntry);

    @Query("SELECT * FROM movies WHERE id=:id")
    MovieEntry loadMovieById(int id);

}
