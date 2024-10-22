package com.example.moviedbapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedbapp.MovieDetailsActivity;
import com.example.moviedbapp.R;
import com.example.moviedbapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieList;
    private OnItemClickListener listener;
    private boolean isFavoritesList;

    public interface OnItemClickListener {
        void onAddToFavoritesClick(Movie movie);
        void onRemoveFromFavoritesClick(Movie movie);
    }

    public MovieAdapter(Context context, List<Movie> movieList, OnItemClickListener listener, boolean isFavoritesList) {
        this.context = context;
        this.movieList = movieList != null ? movieList : new ArrayList<>();
        this.listener = listener;
        this.isFavoritesList = isFavoritesList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(movie.getYear());

        Picasso.get().load(movie.getPoster()).placeholder(R.drawable.placeholder_image).into(holder.posterImageView);

        if (isFavoritesList) {
            holder.addToFavoritesButton.setText("Remove from Favorites");
            holder.addToFavoritesButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveFromFavoritesClick(movie);
                    movieList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, movieList.size());
                }
            });
        } else {
            holder.addToFavoritesButton.setText("Add to Favorites");
            holder.addToFavoritesButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToFavoritesClick(movie);
                }
            });
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("movie", movie);
            context.startActivity(intent);
        });
        holder.posterImageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("movie", movie);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImageView;
        TextView titleTextView;
        TextView yearTextView;
        Button addToFavoritesButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            addToFavoritesButton = itemView.findViewById(R.id.addToFavoritesButton);
        }
    }
}
