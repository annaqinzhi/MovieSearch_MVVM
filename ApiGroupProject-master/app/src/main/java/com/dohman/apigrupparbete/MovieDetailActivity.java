package com.dohman.apigrupparbete;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

        MovieDetails details = (MovieDetails) getIntent().getExtras().getSerializable("MOVIE_DETAILS");


        if (details != null) {
            //Showing image from the movie db api into imageview using glide library
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + details.getImage()).into(image);
            title.setText(details.getTitle());
            date.setText(details.getRelease_year());
            rating.setText(Double.toString(details.getRating()));
            overview.setText(details.getPlot());
            if(details.getLanguage().equals("en")){
            language.setText("English");
            } else if (details.getLanguage().equals("zh")){
                language.setText("Chinese");
            } else if (details.getLanguage().equals("ja")){
                language.setText("Japanese");
            } else {
                language.setText(details.getLanguage());
            }
        }
    }
}
