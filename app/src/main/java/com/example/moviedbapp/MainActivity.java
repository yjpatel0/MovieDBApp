package com.example.moviedbapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedbapp.adapter.MovieAdapter;
import com.example.moviedbapp.model.Movie;
import com.example.moviedbapp.model.MovieDatabaseHelper;
import com.example.moviedbapp.model.MovieResponse;
import com.example.moviedbapp.network.OmdbApiService;
import com.example.moviedbapp.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {

    private EditText searchEditText;
    private Button searchButton;
    private Button favoritesButton;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MovieDatabaseHelper movieDatabaseHelper;
    private OmdbApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        favoritesButton = findViewById(R.id.favoritesButton);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieDatabaseHelper = new MovieDatabaseHelper(this);

        apiService = RetrofitClient.getClient("https://www.omdbapi.com/").create(OmdbApiService.class);

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString();
            if (!query.isEmpty()) {
                searchMovies(query);
            } else {
                Toast.makeText(MainActivity.this, "Please enter a movie name", Toast.LENGTH_SHORT).show();
            }
        });

        favoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }

    private void searchMovies(String query) {
        Call<MovieResponse> call = apiService.searchMovies(query, "dc722c3f");
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getSearch();
                    movieAdapter = new MovieAdapter(MainActivity.this, movies, MainActivity.this, false);
                    recyclerView.setAdapter(movieAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "No movies found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddToFavoritesClick(Movie movie) {
        fetchMovieDetails(movie.getTitle(), true);
    }

    @Override
    public void onRemoveFromFavoritesClick(Movie movie) {
        // Empty implementation since MainActivity does not deal with removing favorites
    }

    private void fetchMovieDetails(String title, boolean addToFavorites) {
        Call<Movie> call = apiService.getMovieDetails(title, "dc722c3f");
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Movie movie = response.body();
                    if (addToFavorites) {
                        movieDatabaseHelper.addMovie(movie);
                        Toast.makeText(MainActivity.this, movie.getTitle() + " added to favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle the movie details (e.g., show in a new activity)
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Movie details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
