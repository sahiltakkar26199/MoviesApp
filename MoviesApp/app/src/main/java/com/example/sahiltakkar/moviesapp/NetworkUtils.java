package com.example.sahiltakkar.moviesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.sahiltakkar.moviesapp.data.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();


    public static String fetchLatestMovies(String url) {

        Uri uri = Uri.parse(url);
        URL builtUrl = null;
        try {
            builtUrl = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String jsonResponse = null;

        Log.v("MainActivity","BuiltUri: "+url);
        jsonResponse = getResponseFromHttpUrl(builtUrl);



        return jsonResponse;
    }



    public static ArrayList<MovieEntry> parseJSONResponse(String jsonResponse) {
        ArrayList<MovieEntry> list = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(jsonResponse);
            JSONArray array = reader.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject movie = array.getJSONObject(i);
                String poster = movie.getString("poster_path");
                Bitmap posterBitmap = getBitMapPoster(poster);
                String title = movie.getString("original_title");
      //          String releaseDate = movie.getString("release_date");
    //            String info = movie.getString("overview");
                int id=movie.getInt("id");
                MovieEntry w = new MovieEntry(title, posterBitmap,id);
                list.add(w);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with parsing JSON results");
        }
        return list;
    }

    private static String getResponseFromHttpUrl(URL url) {
        String JsonResponse = "";
        if (url == null) {
            return "";
        }

        HttpsURLConnection urlConnection = null;
        InputStream in = null;

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            in = urlConnection.getInputStream();
            JsonResponse = readInputStream(in);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results", e);
        } finally {
            urlConnection.disconnect();
        }
        return JsonResponse;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader
                    = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static Bitmap getBitMapPoster(String src) {
        try {
            URL url = new URL("https://image.tmdb.org/t/p/w500/"+src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem parsing image");
            return null;
        }
    }


    public static Movie parseMovieDetailsResponse(String response) {


        Movie movie=null;
        List<String> genres=new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(response);

            JSONArray array = reader.getJSONArray("genres");
            for (int i = 0; i < array.length(); i++) {
                JSONObject genreObject = array.getJSONObject(i);
                String genre = genreObject.getString("name");
                genres.add(genre);
            }

            String posterPath=reader.getString("backdrop_path");
            Bitmap poster=getBitMapPoster(posterPath);
            String homepage=reader.getString("homepage");
            int id=reader.getInt("id");
            String language=reader.getString("original_language");
            String overview=reader.getString("overview");
            String tagline=reader.getString("tagline");
            String releaseDate=reader.getString("release_date");
            String title=reader.getString("original_title");

            movie=new Movie(id,poster,genres,homepage,overview,language,tagline,releaseDate,title);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with parsing JSON results");
        }
        return movie;

    }


    public static List<MovieReview> parseReviewsResponse(String response){



       List<MovieReview> list=new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(response);
            JSONArray result=reader.getJSONArray("results");
            for(int i=0 ; i<result.length() ; i++){
              JSONObject review=result.getJSONObject(i);
              String content=review.getString("content");
              String author=review.getString("author");
              list.add(new MovieReview(author,content));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with parsing JSON results");
        }

        return list;
    }



    public static List<String> parseVideoResponse(String response){

        List<String> list=new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(response);
            JSONArray result=reader.getJSONArray("results");
            for(int i=0 ; i<result.length() ; i++){
                JSONObject video=result.getJSONObject(i);
                String key=video.getString("key");
                list.add(key);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with parsing JSON results");
        }

        return list;
    }


}

