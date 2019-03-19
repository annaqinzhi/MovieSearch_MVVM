package com.dohman.apigrupparbete;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieBaseAdapter extends BaseAdapter {

    private List<MovieDetails> movieDetailsList;

    private int resource;

    private Context context;


    public MovieBaseAdapter(Context context, int resource, List<MovieDetails> movieDetails) {

        this.context = context;
        this.movieDetailsList = movieDetails;
        this.resource = resource;
    }

    @Override
    public int getCount() {

        int nMovies = 0;
        if (movieDetailsList != null)
            nMovies = movieDetailsList.size();

        return nMovies;
    }


    @Override
    public long getItemId(int position) {

        return 0;
    }

    class ViewHolder {

        TextView movieName;
        ImageView image;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.movieName = (TextView) convertView.findViewById(R.id.title);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        MovieDetails details = movieDetailsList.get(position);

        Glide.with(context).load("https://image.tmdb.org/t/p/w500/" + details.getImage()).into(holder.image);
        holder.movieName.setText(details.getTitle());

        return convertView;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return movieDetailsList.get(position);
    }

    /*public void setfilter(List<MovieDetails> listitem)
    {
        movieDetailsList=new ArrayList<>();
        movieDetailsList.addAll(listitem);
        notifyDataSetChanged();
    }*/

}