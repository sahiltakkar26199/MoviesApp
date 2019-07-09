package com.example.sahiltakkar.moviesapp.UI;



import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sahiltakkar.moviesapp.Movie;
import com.example.sahiltakkar.moviesapp.R;
import com.example.sahiltakkar.moviesapp.data.AppDatabase;
import com.example.sahiltakkar.moviesapp.data.MovieEntry;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private static List<MovieEntry> list;
    private static AdapterOnItemClickListener listener;
    private  Context mContext;
    private AppDatabase mDb;
    private  static boolean isFavouritesLayout;
    private static int prioriy=1;
    private Activity activity;

    public MoviesAdapter(List<MovieEntry> moviesList, AdapterOnItemClickListener listener,Context context,boolean bool) {
        this.list = moviesList;
        this.listener = listener;
        this.mContext=context;
        mDb=AppDatabase.getsInstance(context);
        isFavouritesLayout=bool;
    }

    public MoviesAdapter(List<MovieEntry> moviesList, AdapterOnItemClickListener listener,Context context,boolean bool,Activity activity) {
        this.list = moviesList;
        this.listener = listener;
        this.mContext=context;
        mDb=AppDatabase.getsInstance(context);
        isFavouritesLayout=bool;
        this.activity=activity;
    }


    public MoviesAdapter(List<MovieEntry> moviesList) {
        this.list = moviesList;

    }


    public interface AdapterOnItemClickListener {
        void onListItemClick(Integer id,View view);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(isFavouritesLayout){
            view=inflater.inflate(R.layout.list_item_favourites,parent,false);
        }else {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MovieEntry currentMovie = list.get(position);
        holder.poster.setImageBitmap(currentMovie.getPoster());
        holder.nameOfMovie.setText(currentMovie.getTitle());

        if(isFavouritesLayout){
         holder.deleteButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.fadeout);
                 holder.deleteButton.startAnimation(myAnim);

                 mDb.movieDAO().deleteTask(currentMovie);
                 FavouritesActivity.showSnackBar("Movie deleted from favourites");

             }
         });
        }else {

            MovieEntry movie=mDb.movieDAO().loadMovieById(currentMovie.getId());
            if(movie==null){
                holder.addButton.setVisibility(View.VISIBLE);
            }else{
                holder.addButton.setVisibility(View.GONE);
            }

            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isPresent = false;
                    int id = currentMovie.getId();

//                    AddMovieByIdViewModelFactory viewModelFactory=new AddMovieByIdViewModelFactory(mDb,id);
//                    AddMovieByIdViewModel viewModel= ViewModelProviders.of(activity,viewModelFactory).get(AddMovieByIdViewModel.class);


                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.fadein);
                        holder.addButton.setVisibility(View.GONE);
                        holder.addButton.startAnimation(myAnim);
                        mDb.movieDAO().insertMovie(currentMovie);
                        MainActivity.showSnackBar("Movie added to favourites");



                    //    MainActivity.showSnackBar("Movie already added ");



                }
            });
        }


    }

    private void changeImageIcon(Movie movie){
        if(movie==null){

        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private TextView nameOfMovie;
        private ImageView addButton;
        private ImageView deleteButton;
        private CoordinatorLayout coordinatorLayout;


        public MyViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.poster);
            nameOfMovie = (TextView) view.findViewById(R.id.name);
            if(isFavouritesLayout){
                deleteButton=(ImageView)view.findViewById(R.id.deleteButton);
            }else {
                addButton = (ImageView) view.findViewById(R.id.addButton);
            }
            coordinatorLayout=view.findViewById(R.id.coordinate_layout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int clickedPosition=getAdapterPosition();
            MovieEntry movie=list.get(clickedPosition);
            listener.onListItemClick(movie.getId(),v);

        }

    }


    public  void setList(List<MovieEntry> list) {
        this.list = list;
    }

    public  List<MovieEntry> getList() {
        return this.list;
    }



}