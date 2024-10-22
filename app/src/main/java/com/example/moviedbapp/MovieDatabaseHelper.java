package com.example.moviedbapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MOVIES = "movies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_POSTER = "poster";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_STUDIO = "studio";
    private static final String COLUMN_DESCRIPTION = "description";

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_YEAR + " TEXT,"
                + COLUMN_POSTER + " TEXT,"
                + COLUMN_RATING + " TEXT,"
                + COLUMN_STUDIO + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, movie.getTitle());
        values.put(COLUMN_YEAR, movie.getYear());
        values.put(COLUMN_POSTER, movie.getPoster());
        values.put(COLUMN_RATING, movie.getRating());
        values.put(COLUMN_STUDIO, movie.getStudio());
        values.put(COLUMN_DESCRIPTION, movie.getDescription());

        long result = db.insert(TABLE_MOVIES, null, values);
        if (result != -1) {
            Log.d("MovieDatabaseHelper", "Movie added: " + movie.getTitle());
        } else {
            Log.d("MovieDatabaseHelper", "Failed to add movie: " + movie.getTitle());
        }
        db.close();
    }

    public List<Movie> getAllFavorites() {
        List<Movie> movieList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MOVIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                movie.setYear(cursor.getString(cursor.getColumnIndex(COLUMN_YEAR)));
                movie.setPoster(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER)));
                movie.setRating(cursor.getString(cursor.getColumnIndex(COLUMN_RATING)));
                movie.setStudio(cursor.getString(cursor.getColumnIndex(COLUMN_STUDIO)));
                movie.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));

                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movieList;
    }

    public void removeMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_TITLE + " = ?";
        String[] whereArgs = { movie.getTitle() };
        int rowsDeleted = db.delete(TABLE_MOVIES, whereClause, whereArgs);
        if (rowsDeleted > 0) {
            Log.d("MovieDatabaseHelper", "Movie removed: " + movie.getTitle());
        } else {
            Log.d("MovieDatabaseHelper", "Failed to remove movie: " + movie.getTitle());
        }
        db.close();
    }
}
