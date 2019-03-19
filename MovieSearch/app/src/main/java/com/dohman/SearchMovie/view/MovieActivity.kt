package com.dohman.SearchMovie.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.dohman.SearchMovie.BuildConfig
import com.dohman.SearchMovie.addTo
import com.dohman.SearchMovie.model.Movie
import com.dohman.SearchMovie.viewmodel.MovieState
import com.dohman.SearchMovie.viewmodel.MovieViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MovieActivity : BaseActivity<MovieViewModel>(MovieViewModel::class.java), AdapterView.OnItemClickListener {

    private var gridView: GridView? = null
    private var movieBaseAdapter: MovieBaseAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.dohman.SearchMovie.R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        gridView = findViewById<View>(com.dohman.SearchMovie.R.id.gridview) as GridView
        gridView!!.onItemClickListener = this

        viewModel.getState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { state ->
                            when (state) {
                                is MovieState.PopMovieRecieved -> {
                                    showMovieList(state.movieList)
                                }
                                is MovieState.SearchMovieRecieved -> {
                                    showMovieList(state.movieList)
                                }
                            }
                        },
                        {
                            if (BuildConfig.DEBUG) Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                        }
                )
                .addTo(disposable)

        viewModel.getPopMovieList()

    }

    fun showMovieList(movies: List<Movie>) {
        movieBaseAdapter = MovieBaseAdapter(this, com.dohman.SearchMovie.R.layout.movie_list, movies)
        gridView?.setAdapter(movieBaseAdapter)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

        val intent = Intent(this, MovieDetailActivity::class.java)
        val movie = parent.getItemAtPosition(position) as Movie
        intent.putExtra("MOVIE_DETAILS", movie)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.dohman.SearchMovie.R.menu.menu, menu)
        val myActionMenuItem = menu.findItem(com.dohman.SearchMovie.R.id.action_search)
        var searchView = myActionMenuItem.actionView as SearchView

        changeSearchViewTextColor(searchView)
       // val searchEditText = searchView.findViewById(com.dohman.SearchMovie.R.id.search_src_text) as EditText
       // searchEditText.setHintTextColor(Color.WHITE)


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {

                if (!searchView!!.isIconified) {
                    searchView!!.isIconified = true
                }
                myActionMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                var newText = newText.toLowerCase()

                viewModel.getState()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { state ->
                                    when (state) {
                                        is MovieState.PopMovieRecieved -> {
                                            showMovieList(state.movieList)
                                        }
                                        is MovieState.SearchMovieRecieved -> {
                                            showMovieList(state.movieList)
                                        }
                                    }
                                },
                                {
                                    if (BuildConfig.DEBUG) Toast.makeText(this@MovieActivity, "Error", Toast.LENGTH_LONG).show()
                                }
                        )
                        .addTo(disposable)

                viewModel.getSearchMovieList(newText)

                return true
            }
        })
        return true
    }

    private fun changeSearchViewTextColor(view: View?) {
        if (view != null) {
            if (view is TextView) {
                view.setTextColor(Color.WHITE)
                return
            } else if (view is ViewGroup) {
                val viewGroup = view as ViewGroup?
                for (i in 0 until viewGroup!!.childCount) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i))
                }
            }
        }
    }
}

