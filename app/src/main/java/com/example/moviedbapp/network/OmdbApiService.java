package com.example.moviedbapp.network;

import com.example.moviedbapp.model.Movie;
import com.example.moviedbapp.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbApiService {
    @GET("/")
    Call<MovieResponse> searchMovies(@Query("s") String query, @Query("apikey") String apiKey);

    @GET("/")
    Call<Movie> getMovieDetails(@Query("t") String title, @Query("apikey") String apiKey);
}
