package com.example.sahiltakkar.moviesapp.UI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import com.example.sahiltakkar.moviesapp.R;
import com.example.sahiltakkar.moviesapp.ViewModels.MyViewModel;
import com.example.sahiltakkar.moviesapp.data.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity implements MoviesAdapter.AdapterOnItemClickListener {

    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    private Display display;
    DisplayMetrics outMetrics;

    private ArrayList<MovieEntry> list;
    private static CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        coordinatorLayout=findViewById(R.id.coordinate_layout_fav);
        display = getWindowManager().getDefaultDisplay();
        outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth/200);

        recyclerView=(RecyclerView)findViewById(R.id.my_recycler_view);

        list=new ArrayList<>();



        mAdapter=new MoviesAdapter(list,this,this,true);

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager
                (getApplicationContext(),columns,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);
        retriveData();


    }

    @Override
    public void onListItemClick(Integer id, View view) {
        Intent intent=new Intent(this,MovieDetailsActivity.class);
        intent.putExtra("ID",id.toString());
        startActivity(intent);
    }


    @Override
    protected void onResume() {

        super.onResume();

    }

    private void retriveData(){
        MyViewModel viewModel= ViewModelProviders.of(this).get(MyViewModel.class);
        LiveData<List<MovieEntry>> list=viewModel.getList();
        list.observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mAdapter.setList(movieEntries);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadeout,R.anim.fadein);
    }


    public static void showSnackBar(String str){
        Snackbar snackbar=Snackbar.make(coordinatorLayout,str,Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

