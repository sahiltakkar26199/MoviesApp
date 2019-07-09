package com.example.sahiltakkar.moviesapp.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sahiltakkar.moviesapp.Movie;
import com.example.sahiltakkar.moviesapp.R;
import com.example.sahiltakkar.moviesapp.data.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {


    private  AdapterOnItemClickListener listener;
    private Context mContext;
    private static List<String> videosList = new ArrayList<>();
    private String url;


    public  void setVideosList(List<String> list){
        videosList=list;
    }

    public  List<String> getVideosList() {
        return videosList;
    }


    public VideosAdapter(List<String> videosList,Context context,AdapterOnItemClickListener listener){
        this.videosList=videosList;
        this.mContext=context;
        this.listener=listener;
    }

    public interface AdapterOnItemClickListener {
        void onListItemClick(String key);
    }


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.videos_list_item,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final String currentVideo = videosList.get(position);


            holder.button.setText("video "+position);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://www.youtube.com/watch?v="+currentVideo;
                Intent intent2=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent2);

            }
        });




    }


    @Override
    public int getItemCount() {
        if(videosList==null){
            return 0;
        }else{
            return videosList.size();
        }
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Button button;

        public VideoViewHolder(View itemView) {
        super(itemView);
        button=itemView.findViewById(R.id.button_view);
            itemView.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
            Log.v("VideoAdapter","Item is clckedd");
            int clickedPosition=getAdapterPosition();
            String key=videosList.get(clickedPosition);
            listener.onListItemClick(key);

        }
    }

}



/*

    public VideosAdapter(@NonNull Context context, List<String> list) {
        super(context, 0 , list);
        mContext = context;
        videosList = list;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.videos_list_item,parent,false);

        final String currentVideo = videosList.get(position);

        Button button=listItem.findViewById(R.id.button_view);
        if(button!=null)
        button.setText("video");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="https://www.youtube.com/watch?v="+currentVideo;
               Intent intent2=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent2);

            }
        });
        return listItem;
    }





}

*/
