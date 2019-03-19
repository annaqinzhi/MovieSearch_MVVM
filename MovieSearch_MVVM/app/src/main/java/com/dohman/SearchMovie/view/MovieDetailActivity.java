package com.dohman.SearchMovie.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dohman.SearchMovie.R;
import com.dohman.SearchMovie.model.Movie;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView image;

    private TextView title, date, rating, overview, language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();

        image = findViewById(R.id.imageMain);

        title = findViewById(R.id.textTitle);

        date = findViewById(R.id.textYear);

        rating = findViewById(R.id.textRating);

        overview = findViewById(R.id.textPlot);

        language=findViewById(R.id.textLanguage);

        Movie movie = (Movie) getIntent().getExtras().getSerializable("MOVIE_DETAILS");


        if (movie != null) {
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + movie.getPosterPath()).into(image);
            title.setText(movie.getTitle());
            date.setText(movie.getReleaseDate());
            rating.setText(movie.getVoteAverage());
            overview.setText(movie.getOverview());
            if(movie.getOriginal_language().equals("en")){
                language.setText("English");
            } else if (movie.getOriginal_language().equals("zh")){
                language.setText("Chinese");
            } else if (movie.getOriginal_language().equals("ja")){
                language.setText("Japanese");
            } else {
                language.setText(movie.getOriginal_language());
            }
        }
    }
}
