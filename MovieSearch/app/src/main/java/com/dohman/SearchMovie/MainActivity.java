package com.dohman.SearchMovie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String base_URL = "https://api.themoviedb.org";
    private GridView gridView;
    private SearchView searchView;

    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private MovieBaseAdapter movieBaseAdapter;
    private ProgressDialog progressBar;
    private Context context;
    private Disposable disposable;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        disposable = new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);

        getPopMoviesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());

    }

    public Observable<SearchResults> getPopMoviesObservable() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        NetWorkInterface netWorkInterface = retrofit.create(NetWorkInterface.class);

        return netWorkInterface.getPopMovies();

    }

    public Observable<SearchResults> getSearchMoviesObservable(String inputText) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        NetWorkInterface netWorkInterface = retrofit.create(NetWorkInterface.class);

        return netWorkInterface.getSearchMovies(netWorkInterface.api,inputText);

    }


    public Observer<SearchResults> getObserver() {
        return new Observer<SearchResults>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                //startProgressBar();
            }

            @Override
            public void onNext(SearchResults searchResults) {
                Log.d(TAG, "Movies in Observer is: " + searchResults.getPage());
                moviesList = searchResults.getResults();

                //movieBaseAdapter.notifyDataSetChanged();
                //updatedProgressBar()

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");

                movieBaseAdapter = new MovieBaseAdapter(MainActivity.this, R.layout.movie_list, moviesList);
                gridView.setAdapter(movieBaseAdapter);

                //progressBar.dismiss();
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, MovieDetailActivity.class);
        Movie movie = (Movie) parent.getItemAtPosition(position);
        intent.putExtra("MOVIE_DETAILS", movie);
        startActivity(intent);

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

                 getSearchMoviesObservable(newText)
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(getObserver());

                return true;
            }
        });

        return true;
    }

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
