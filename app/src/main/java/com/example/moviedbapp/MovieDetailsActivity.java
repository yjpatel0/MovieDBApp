package com.example.moviedbapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviedbapp.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView moviePoster;
    private TextView movieTitle, movieYear, movieDescription;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieYear = findViewById(R.id.movieYear);
        movieDescription = findViewById(R.id.movieDescription);
        backButton = findViewById(R.id.backButton);

        // Retrieve the movie object from the intent
        Movie movie = getIntent().getParcelableExtra("movie");

        if (movie != null) {
            Glide.with(this)
                    .load(movie.getPoster())
                    .into(moviePoster);
            movieTitle.setText(movie.getTitle());
            movieYear.setText(movie.getYear());
            movieDescription.setText(movie.getDescription()); // Use actual description from Movie object
        }

        backButton.setOnClickListener(v -> finish());
    }
}
