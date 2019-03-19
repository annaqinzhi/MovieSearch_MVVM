package com.dohman.SearchMovie.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dohman.SearchMovie.R;
import com.dohman.SearchMovie.model.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MovieBaseAdapter extends android.widget.BaseAdapter {

    private List<Movie> movies;

    private int resource;

    private Context context;


    public MovieBaseAdapter(Context context, int resource, List<Movie> movies) {

        this.context = context;
        this.movies = movies;
        this.resource = resource;
    }

    @Override
    public int getCount() {

        int nMovies = 0;
        if (movies != null)
            nMovies = movies.size();

        return nMovies;
    }


    @Override
    public long getItemId(int position) {

        return 0;
    }

    class ViewHolder {

        ImageView image;
        TextView movieName;
        TextView pop;

    }


    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view == null) {

            view = LayoutInflater.from(context).inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.movieName = (TextView) view.findViewById(R.id.title);
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.pop = (TextView) view.findViewById(R.id.pop);
            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }

        Movie movie = movies.get(position);

        Glide.with(context).load("https://image.tmdb.org/t/p/w500/" + movie.getPosterPath()).into(holder.image);
        holder.movieName.setText(movie.getTitle());
        holder.pop.setText(movie.getVoteAverage());


        return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

}