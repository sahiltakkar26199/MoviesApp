package com.example.sahiltakkar.moviesapp.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.content.AsyncTaskLoader;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.sahiltakkar.moviesapp.Movie;
import com.example.sahiltakkar.moviesapp.MovieReview;
import com.example.sahiltakkar.moviesapp.NetworkUtils;
import com.example.sahiltakkar.moviesapp.UI.MainActivity;
import com.example.sahiltakkar.moviesapp.UI.MovieDetailsActivity;

import java.util.List;


public class MovieDetailsViewModel extends ViewModel {


    private  Movie MovieDetails;
    private Integer id;
    private String url;
    private List movieReviews;

    private List<String> videos;
    private String videosUrl;

    public Movie getMovieDetails() {
        return MovieDetails;
    }

    public List<MovieReview> getMovieReviews(){return movieReviews;}

    private String reviewsUrl;

    public List<String> getVideos() {
        return videos;
    }

    public MovieDetailsViewModel(Integer id){
        this.id=id;
        url="https://api.themoviedb.org/3/movie/"+id.toString()
                +"?api_key=c92b11b90288f849997d288fac94714f&language=en-US";
       reviewsUrl="https://api.themoviedb.org/3/movie/" +id.toString()+
               "/reviews?api_key=c92b11b90288f849997d288fac94714f&language=en-US&page=1";
       videosUrl="https://api.themoviedb.org/3/movie/"+id.toString()
               +"/videos?api_key=c92b11b90288f849997d288fac94714f&language=en-US";
        new MovieAsyncTask().execute();
        new getReviewsAsyncTask().execute();
        new getVideosAsyncTask().execute();

    }

    private class MovieAsyncTask extends AsyncTask<String,Void,Movie>{

        @Override
        protected Movie doInBackground(String... strings) {
            String response=NetworkUtils.fetchLatestMovies(url);
            Movie result=NetworkUtils.parseMovieDetailsResponse(response);

            return result;
        }


        @Override
        protected void onPostExecute(Movie movie) {
            MovieDetails=movie;
            MovieDetailsActivity.updateUi(movie);
        }
    }

    private class getReviewsAsyncTask extends AsyncTask<String,Void,List<MovieReview>>{

        @Override
        protected List<MovieReview> doInBackground(String... strings) {
            String response=NetworkUtils.fetchLatestMovies(reviewsUrl);
            return NetworkUtils.parseReviewsResponse(response);
        }

        @Override
        protected void onPostExecute(List<MovieReview> strings) {
            movieReviews=strings;
            MovieDetailsActivity.updateReviewsUI(movieReviews);
        }
    }


    private class getVideosAsyncTask extends AsyncTask<String,Void,List<String>>{

        @Override
        protected List<String> doInBackground(String... strings) {
            String response=NetworkUtils.fetchLatestMovies(videosUrl);
            return NetworkUtils.parseVideoResponse(response);
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            videos=strings;
            MovieDetailsActivity.updateVideosUI(videos);
        }
    }

}
