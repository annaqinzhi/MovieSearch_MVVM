package com.dohman.apigrupparbete;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String DOWNLOADINGMESSAGE = "HÄMTAR FILMER ..." ;
    private static final int DELAY_MILLISEC = 600;
    private static final String URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=bc0d9d234a1124140f2ca26988c9ae27";
    private GridView gridView;
    private SearchView searchView;
    private Handler mHandler;

    private ArrayList<MovieDetails> movieList;
    private MovieBaseAdapter movieArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);

        mHandler = new Handler();

        //Executing AsyncTask, passing api as parameter
        new CheckConnectionStatus(this).execute(URL_POPULAR);

    }

    //This method is invoked whenever we click over any item of list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Moving to MovieDetailsActivity from MainActivity. Sending the MovieDetails object from one activity to another activity
        Intent intent = new Intent(this, MovieDetailActivity.class);
        MovieDetails details = (MovieDetails) parent.getItemAtPosition(position);
        intent.putExtra("MOVIE_DETAILS", details);

        startActivity(intent);

    }

    //AsyncTask to process network request
    class CheckConnectionStatus extends AsyncTask<String, Integer, String> {

        private ProgressDialog progressBar;
        private Context context;

        public CheckConnectionStatus(Context context) {

            this.context = context;
            startProgressBar();
        }

        //This method will run on UIThread and it will execute before doInBackground
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //This method will run on background thread and after completion it will return result to onPostExecute
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                //As we are passing just one parameter to AsyncTask, so used param[0] to get value at 0th position that is URL
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //Getting inputstream from connection, that is response which we got from server
                InputStream inputStream = urlConnection.getInputStream();
                //Reading the response
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                bufferedReader.close();
                //Returning the response message to onPostExecute method

                JSONObject jsonObject = null;

                if (s != null) {
                    try {

                        //Parent JSON Object. Json object start at { and end at }
                        jsonObject = new JSONObject(s);

                        movieList = new ArrayList<>();

                        //JSON Array of parent JSON object. Json array starts from [ and end at ]
                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        //Reading JSON object inside Json array
                        for (int i = 0; i < jsonArray.length(); i++) {

                            //Reading JSON object at 'i'th position of JSON Array
                            JSONObject object = jsonArray.getJSONObject(i);
                            MovieDetails movieDetails = new MovieDetails();
                            movieDetails.setTitle(object.getString("original_title"));
                            movieDetails.setRating(object.getDouble("vote_average"));
                            movieDetails.setPlot(object.getString("overview"));
                            movieDetails.setRelease_year(object.getString("release_date"));
                            movieDetails.setImage(object.getString("poster_path"));
                            movieDetails.setLanguage(object.getString("original_language"));
                            //movieDetails.setImagePath("https://image.tmdb.org/t/p/w500/" + movieDetails.getImageString());

                            movieList.add(movieDetails);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                return s;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage(), e);
            }
            return null;
        }

        //This method runs on UIThread and it will execute when doInBackground is completed
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Creating custom array adapter instance and setting context of MainActivity, List item layout file and movie list.
            movieArrayAdapter = new MovieBaseAdapter(MainActivity.this, R.layout.movie_list, movieList);

            //Setting adapter to listview
            gridView.setAdapter(movieArrayAdapter);
            progressBar.dismiss();

        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(Integer ... progress) {

            super.onProgressUpdate(progress);
            progressBar.setProgress(0);
            progressBar.setProgress(progress[0]);
        }


        /**
         *
         */
        private  void startProgressBar() {
            progressBar = new ProgressDialog(context);
            progressBar.setCancelable(true);
            progressBar.setMessage(DOWNLOADINGMESSAGE);
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();

            //progressBarStatus = 0;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);


        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);

        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                final String new_text = newText;
                //mQueryString = searchTerm;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Put your call to the server here (with mQueryString)
                        new CheckConnectionStatus(MainActivity.this).execute("https://api.themoviedb.org/3/search/movie?api_key=bc0d9d234a1124140f2ca26988c9ae27&query=" + new_text);

                    }
                }, DELAY_MILLISEC);




                return true;
            }
        });

        return true;
    }


    //for changeing the text color of searchview
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));


                }
            }
        }
    }
}