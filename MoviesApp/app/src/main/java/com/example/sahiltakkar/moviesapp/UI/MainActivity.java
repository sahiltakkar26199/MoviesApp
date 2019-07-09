
package com.example.sahiltakkar.moviesapp.UI;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahiltakkar.moviesapp.Movie;
import com.example.sahiltakkar.moviesapp.NetworkUtils;
import com.example.sahiltakkar.moviesapp.R;
import com.example.sahiltakkar.moviesapp.data.AppDatabase;
import com.example.sahiltakkar.moviesapp.data.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.AdapterOnItemClickListener,LoaderManager.LoaderCallbacks<List<MovieEntry>>{

    private static final String upcomingMoviesUrl=" https://api.themoviedb.org/3/movie/upcoming" +
            "?api_key=c92b11b90288f849997d288fac94714f&language=en-US&page=1";

    private static final String popularMoviesUrl="https://api.themoviedb.org/3/movie/popular?" +
            "api_key=c92b11b90288f849997d288fac94714f&language=en-US&page=1";

    private static final String topRatedUrl="https://api.themoviedb.org/3/movie/top_rated?api_key=c92b11b90288f849997d288fac94714f&language=en-US&page=1";

    private static  int LOADER_ID=1;

    private ProgressBar mProgressBar;
    private TextView mEmptyTextView;

    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    private Display display;
    DisplayMetrics outMetrics;

    private List<MovieEntry> list;

    private ActionBar toolbar;
    private AppDatabase mDb;
    private static CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar=(ProgressBar)findViewById(R.id.progress_bar);
        mEmptyTextView=(TextView)findViewById(R.id.empty_view);
        toolbar=getSupportActionBar();
        BottomNavigationView navigation=(BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Popular");
        display = getWindowManager().getDefaultDisplay();
        outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth/200);

        recyclerView=(RecyclerView)findViewById(R.id.my_recycler_view);
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        list=new ArrayList<>();



        mAdapter=new MoviesAdapter(list,this,this,false,MainActivity.this);

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager
                (getApplicationContext(),columns,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
                mEmptyTextView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
        }else{
            mEmptyTextView.setVisibility(View.INVISIBLE);
            getLoaderManager().initLoader(LOADER_ID,null,this);
        }

        coordinatorLayout=findViewById(R.id.coordinate_layout);


    }

 private void callLoader(){
     ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
     NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
     boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
     if (!isConnected) {
         mEmptyTextView.setVisibility(View.VISIBLE);
         mProgressBar.setVisibility(View.INVISIBLE);
     }else{
         mEmptyTextView.setVisibility(View.INVISIBLE);
         mProgressBar.setVisibility(View.VISIBLE);
         getLoaderManager().restartLoader(LOADER_ID,null,this);
     }
        }

    private void displayNoConnection() {

    }

    @Override
    public Loader<List<MovieEntry>> onCreateLoader(int id, Bundle args) {
        Log.v("MainActivity","Loader has started");
        return new AsyncTaskLoader<List<MovieEntry>>(this) {
            List<MovieEntry> result;
            String response="";
            @Override
            public List<MovieEntry> loadInBackground() {
                Log.v("MainActivity","Background work has started");
                if(toolbar.getTitle().equals("Upcoming")) {
                    response=NetworkUtils.fetchLatestMovies(upcomingMoviesUrl);
                }else if(toolbar.getTitle().equals("Popular")){
                    response=NetworkUtils.fetchLatestMovies(popularMoviesUrl);
                }else if(toolbar.getTitle().equals("Top Rated")){
                    response=NetworkUtils.fetchLatestMovies(topRatedUrl);
                }
                result=NetworkUtils.parseJSONResponse(response);
                return result;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        }    ;




    }

    @Override
    public void onLoadFinished(Loader<List<MovieEntry>> loader, List<MovieEntry> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mAdapter.setList(data);
        mAdapter.notifyDataSetChanged();
      //  mAdapter=new MoviesAdapter(data);
       recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieEntry>> loader) {

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.popular_movies:
                    recyclerView.setVisibility(View.INVISIBLE);
                    toolbar.setTitle("Popular");
                    callLoader();
                    return true;
                case R.id.top_rated:
                    recyclerView.setVisibility(View.INVISIBLE);
                    toolbar.setTitle("Top Rated");
                    callLoader();
                    return true;
                case R.id.upcoming_movies:
                    recyclerView.setVisibility(View.INVISIBLE);
                    toolbar.setTitle("Upcoming");
                    callLoader();
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                recyclerView.setVisibility(View.INVISIBLE);
                callLoader();
                return true;
            case R.id.favourites:
                Intent intent=new Intent(this,FavouritesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadeout,R.anim.move);
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(Integer id,View view) {
        Intent intent=new Intent(this,MovieDetailsActivity.class);

      //  createCircularReveal(coordinatorLayout);
            presentActivity(id,coordinatorLayout);
    //    overridePendingTransition(R.anim.fadeout,R.anim.zoomin);
    }

    public void presentActivity(Integer id,View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(MovieDetailsActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra("ID",id.toString());
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

        public Context gettContext(){
        return getApplicationContext();
        }

        public static void showSnackBar(String str){
            Snackbar snackbar=Snackbar.make(coordinatorLayout,str,Snackbar.LENGTH_LONG);
            snackbar.show();

        }


/*

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createCircularReveal(final View view) {

        // to get the center of FAB
        int centerX = (int) coordinatorLayout.getX() + coordinatorLayout.getWidth() / 2;
        int centerY = (int) coordinatorLayout.getY();
        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());
        // starts the effect at centerX, center Y and covers final radius
        Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                centerX, centerY, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        revealAnimator.start();

    }


    hides the circular view



    private void hideCircularReveal(final View myView) {

        // get the center for the clipping circle
        int cx = (int) coordinatorLayout.getX() + coordinatorLayout.getWidth() / 2;
        int cy = (int) coordinatorLayout.getY();


        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(myView.getWidth(), myView.getHeight());

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                myView.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        // start the animation
        anim.start();
    }
*/
}


