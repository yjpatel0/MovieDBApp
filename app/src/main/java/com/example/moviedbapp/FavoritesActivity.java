package com.example.moviedbapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedbapp.adapter.MovieAdapter;
import com.example.moviedbapp.model.Movie;
import com.example.moviedbapp.model.MovieDatabaseHelper;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MovieDatabaseHelper movieDatabaseHelper;
    private Button backButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerViewFavorites);
        backButton = findViewById(R.id.backButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieDatabaseHelper = new MovieDatabaseHelper(this);

        List<Movie> favoriteMovies = movieDatabaseHelper.getAllFavorites();
        if (favoriteMovies.isEmpty()) {
            Log.d("FavoritesActivity", "No favorite movies found.");
            Toast.makeText(this, "No favorite movies found", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("FavoritesActivity", "Favorite movies loaded: " + favoriteMovies.size());
        }

        movieAdapter = new MovieAdapter(this, favoriteMovies, this, true);
        recyclerView.setAdapter(movieAdapter);

        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onAddToFavoritesClick(Movie movie) {
        // Not used in FavoritesActivity
    }

    @Override
    public void onRemoveFromFavoritesClick(Movie movie) {
        movieDatabaseHelper.removeMovie(movie);
        Toast.makeText(this, movie.getTitle() + " removed from favorites", Toast.LENGTH_SHORT).show();
    }
}
