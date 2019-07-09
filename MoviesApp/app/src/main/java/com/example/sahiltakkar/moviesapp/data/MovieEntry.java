package com.example.sahiltakkar.moviesapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;


@TypeConverters(DbBitmapUtitlity.class)
@Entity(tableName = "movies")
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int priority;


    private int id;
    private String title;
    private Bitmap poster;



    public MovieEntry(String title,Bitmap poster,int id){

        this.poster=poster;
        this.title=title;
        this.id=id;
    }
    @Ignore
    public MovieEntry(String title,Bitmap poster,int id,int prioriy){

        this.poster=poster;

        this.title=title;
        this.id=id;
        this.priority=prioriy;
    }

    public String getTitle(){
        return title;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public int getPriority(){ return priority; }

    public int getId() {
        return id;
    }

    public void setPoster(Bitmap poster) {
         this.poster=poster;
    }

    public void setId(int id) {
        this.id=id;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setPriority(int priority){
        this.priority=priority;
    }
}

