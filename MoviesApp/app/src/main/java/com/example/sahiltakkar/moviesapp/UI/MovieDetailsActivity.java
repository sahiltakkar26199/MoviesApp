package com.example.sahiltakkar.moviesapp.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.example.sahiltakkar.moviesapp.BlurBuilder;
import com.example.sahiltakkar.moviesapp.Movie;
import com.example.sahiltakkar.moviesapp.MovieReview;
import com.example.sahiltakkar.moviesapp.R;
import com.example.sahiltakkar.moviesapp.ViewModels.AddMovieByIdViewModel;
import com.example.sahiltakkar.moviesapp.ViewModels.AddMovieByIdViewModelFactory;
import com.example.sahiltakkar.moviesapp.ViewModels.MovieDetailsViewModel;
import com.example.sahiltakkar.moviesapp.ViewModels.MovieaDetailViewModelFactory;
import com.example.sahiltakkar.moviesapp.data.AppDatabase;
import com.example.sahiltakkar.moviesapp.data.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements VideosAdapter.AdapterOnItemClickListener {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    private static int revealX;
    private static int revealY;
    private static FrameLayout additonalLayout;
    Intent intent;
    private static RecyclerView videosRecyclerView;
    private static Integer movieId;
    private static ActionBar toolbar;
    private static ImageView posterView;
    private static TextView taglineTextView;
    private static TextView releaseDateTextView;
    private static TextView overviewTextView;
    private static TextView genreTextView;
    private static ImageView favouritesImageView;
    private static CoordinatorLayout movieDetailsView;
    private static TextView reviewsTextView;
    private static boolean isAddedToFavourites;
    private AppDatabase mDb;
    private static TextView OVERVIEW;
    private static TextView GENRE;
    private static TextView RELEASE_DATE;
    private static VideoView videoView;
    private static TextView TRAILER;
    private static RecyclerView recyclerView;
    private  static VideosAdapter videosAdaper;
    private static Intent intent2;
    private static Context context;
    private static FrameLayout  mContainerView;
    private static HorizontalScrollView horizontalScrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.movie_details_activity);
        intent = getIntent();
        movieId = Integer.valueOf(intent.getStringExtra("ID"));
        Log.v("MovieActivity", "Id is " + movieId.toString());
        movieDetailsView = findViewById(R.id.movie_details);
      //   videoView=findViewById(R.id.video_view);
        context=getApplicationContext();
        OVERVIEW = findViewById(R.id.overView_question);
        RELEASE_DATE = findViewById(R.id.release_date);
        GENRE = findViewById(R.id.genre);
        toolbar = getSupportActionBar();
        additonalLayout=findViewById(R.id.additonal_layout);
        reviewsTextView = findViewById(R.id.content);
        posterView = findViewById(R.id.alternate_poster);
        taglineTextView = findViewById(R.id.tagline);
        releaseDateTextView = findViewById(R.id.release_date_main);
        overviewTextView = findViewById(R.id.overView);
        genreTextView = findViewById(R.id.genre_ans);
        recyclerView=findViewById(R.id.recycler_view_videos);
        movieDetailsView.setVisibility(View.INVISIBLE);
        mContainerView = (FrameLayout) findViewById(R.id.frame_layout);
        TRAILER=findViewById(R.id.trailers_ques);
     //   overridePendingTransition(R.anim.fadeout,R.anim.fadein);
        videosRecyclerView=findViewById(R.id.recycler_view_videos);
        videosRecyclerView.setVisibility(View.INVISIBLE);
       // runFadeInAnimation();
        horizontalScrollView=findViewById(R.id.horizontal_scroll);
        horizontalScrollView.setVisibility(View.INVISIBLE);

        context=getApplicationContext();


            if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                    intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
                movieDetailsView.setVisibility(View.INVISIBLE);

                revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
                revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


                ViewTreeObserver viewTreeObserver = movieDetailsView.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            revealActivity(revealX, revealY);

                            movieDetailsView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                }
            } else {
                movieDetailsView.setVisibility(View.VISIBLE);
            }





            toolbar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            MovieaDetailViewModelFactory viewModelFactory = new MovieaDetailViewModelFactory(movieId);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            MovieDetailsViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel.class);

            Movie movie = viewModel.getMovieDetails();
            List<MovieReview> review = viewModel.getMovieReviews();
            if (movie != null) {
                movieDetailsView.setVisibility(View.VISIBLE);
                Bitmap originalBitmap = movie.getPoster();
                Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
                mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));

                updateUi(movie);
            }
            if (review != null) {
                updateReviewsUI(review);
            }
            List<String> videos = viewModel.getVideos();
            if (videos == null) {
                videosRecyclerView.setVisibility(View.INVISIBLE);
                videos = new ArrayList<>();
                videos.add("Hi");
                videos.add("Hello");
            }
            videosAdaper = new VideosAdapter(videos, this, this);

            if (videos != null) {
                videosRecyclerView.setVisibility(View.VISIBLE);
                videosAdaper.setVideosList(videos);
                videosAdaper.notifyDataSetChanged();
                horizontalScrollView.setVisibility(View.VISIBLE);
            }
            recyclerView.setAdapter(videosAdaper);


    }






    @Override
    public void onBackPressed() {
     //   runFadeOutAnimation();
        finish();
        overridePendingTransition(R.anim.fadeout,R.anim.fadein);


    }

    public static void updateUi(Movie movieDetails) {



        toolbar.setTitle(movieDetails.getTitle());
        posterView.setImageBitmap(movieDetails.getPoster());
        if (movieDetails.getTagline().isEmpty()) {
            taglineTextView.setVisibility(View.INVISIBLE);
        } else {
            taglineTextView.setText("\" " + movieDetails.getTagline() + " \"");
        }
        releaseDateTextView.setText(movieDetails.getDate());
        overviewTextView.setText(movieDetails.getOverView());
        setGenre(movieDetails.getGenre());
        RELEASE_DATE.setText("Release Date:");
        OVERVIEW.setText("OVERVIEW:");
        TRAILER.setText("TRAILERS:");
        GENRE.setText("GENRE:"); movieDetailsView.setVisibility(View.VISIBLE);
        Bitmap originalBitmap = movieDetails.getPoster();
        Bitmap blurredBitmap = BlurBuilder.blur( context, originalBitmap );
        BitmapDrawable dr=new BitmapDrawable(context.getResources(), blurredBitmap);
        dr.setAlpha(150);
        movieDetailsView.setBackground(dr);
        mContainerView.setBackgroundColor(getDominantColor(movieDetails.getPoster()));
        horizontalScrollView.setVisibility(View.INVISIBLE);
        movieDetailsView.setVisibility(View.VISIBLE);
    }
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    private static void setGenre(List<String> genre) {

        String output = "";
        for (int i = 0; i < genre.size(); i++) {
            output = output + genre.get(i) + ", ";
        }
        output = output.substring(0, output.length() - 2);
        genreTextView.setText(output);
    }



    public static void updateReviewsUI(List<MovieReview> movieReviews) {
        String output = "";
        String author = "";
        String content = "";
        if(movieReviews.size()==0){
            reviewsTextView.setText("Sorry, No Reviews");
        }
        for (int i = 0; i < movieReviews.size(); i++) {
            SpannableString s1 = new SpannableString(movieReviews.get(i).getAuthor() + ":  \n");
            s1.setSpan(new ForegroundColorSpan(Color.rgb(56, 223, 110)), 0, s1.length(), 0);
            s1.setSpan(new AbsoluteSizeSpan(80), 0, s1.length(), 0);
            SpannableString s2 = new SpannableString(movieReviews.get(i).getContent() + "\n\n\n");
            reviewsTextView.append(TextUtils.concat(s1, s2));

            // output=output+movieReviews.get(i).getAuthor()+": "+movieReviews.get(i).getContent()+"\n\n\n";
        }

    }


    public  static void updateVideosUI(List<String> videos){
        videosAdaper.setVideosList(videos);
        Log.v("MainActivity","size of list is "+String.valueOf(videos.size()));

        videosAdaper.notifyDataSetChanged();
        recyclerView.setAdapter(videosAdaper);
        horizontalScrollView.setVisibility(View.VISIBLE);
   /* recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        intent2=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM"));
        context.startActivity(intent2);

            //    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")));
        }
    });
*/
    }


    @Override
    public void onListItemClick(String key) {
        Log.v("MainActivity","Item is clckedd");
       String url="https://www.youtube.com/watch?v="+key;
        Intent intent2=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent2);
    }




    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(movieDetailsView.getWidth(),movieDetailsView.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(movieDetailsView, x, y, 0, finalRadius);
            circularReveal.setDuration(1200);

            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            circularReveal.start();

        } else {
            finish();
        }
    }

    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(movieDetailsView.getWidth(), movieDetailsView.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    movieDetailsView, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(1200);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    movieDetailsView.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }
}



