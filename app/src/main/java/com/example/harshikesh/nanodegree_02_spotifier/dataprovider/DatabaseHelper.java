package com.example.harshikesh.nanodegree_02_spotifier.dataprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by harshikesh.kumar on 07/02/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

  public static final String ID = "_id";
  public static final String MOVIE_ID = "id";
  public static final String TITLE = "title";
  public static final String OVERVIEW = "overview";
  public static final String BACKDROP_IMAGE = "backdrop_image";
  public static final String AVERAGE_RATING = "average_rating";
  public static final String RELEASE_DATE = "release_date";
  public static final String POSTER_IMAGE = "poster_image";

  private SQLiteDatabase db;
  static final String DATABASE_NAME = "Movies";
  static final String MOVIES_TABLE_NAME = "FavoriteMovies";
  static final int DATABASE_VERSION = 1;
  static final String CREATE_MOVIE_TABLE =
      " CREATE TABLE " + MOVIES_TABLE_NAME + "("+
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MOVIE_ID +" TEXT NOT NULL, " +
            TITLE +" TEXT NOT NULL, " +
            OVERVIEW +" TEXT NOT NULL, " +
            BACKDROP_IMAGE +" TEXT, " +
            AVERAGE_RATING +" TEXT, " +
            RELEASE_DATE +" TEXT, " +
            POSTER_IMAGE +" TEXT);";


  DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_MOVIE_TABLE);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE_NAME);
    onCreate(db);
  }
}
