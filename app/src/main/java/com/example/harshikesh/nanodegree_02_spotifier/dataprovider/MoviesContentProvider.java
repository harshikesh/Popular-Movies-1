package com.example.harshikesh.nanodegree_02_spotifier.dataprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;

/**
 * Created by harshikesh.kumar on 07/02/16.
 */
public class MoviesContentProvider extends ContentProvider {

  private static final String TAG = MoviesContentProvider.class.getSimpleName();

  static final String PROVIDER_NAME = "com.harshikesh.FavoriteMoviesProvider";
  public static final String PROVIDER_URL = "content://" + PROVIDER_NAME + "/movies";
  public static final Uri CONTENT_URI = Uri.parse(PROVIDER_URL);
  private static HashMap<String, String> MOVIES_PROJECTION_MAP;
  private Context mContext;
  private SQLiteDatabase db;
  private int count = 0;

  static final int MOVIES = 1;
  static final int MOVIE_ID = 2;

  static final UriMatcher uriMatcher;

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(PROVIDER_NAME, "movies", MOVIES);
    uriMatcher.addURI(PROVIDER_NAME, "movies/#", MOVIE_ID);
  }

  public MoviesContentProvider() {

  }
  public  MoviesContentProvider(Context context)
  {
    mContext = context;
    DatabaseHelper dbhelper = new DatabaseHelper(context);
    db = dbhelper.getWritableDatabase();
  }


  @Override public boolean onCreate() {
    if(mContext == null)
    mContext = getContext();
    DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    db = dbHelper.getWritableDatabase();
    return (db != null);
  }

  @Nullable @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(DatabaseHelper.MOVIES_TABLE_NAME);

    switch (uriMatcher.match(uri)) {
      case MOVIES:
        qb.setProjectionMap(MOVIES_PROJECTION_MAP);
        break;

      case MOVIE_ID:
        qb.appendWhere(DatabaseHelper.ID + "=" + uri.getPathSegments().get(1));
        break;

      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    new DatabaseHelper(mContext);

    if (sortOrder == null || sortOrder == "") {
      sortOrder = DatabaseHelper.TITLE;
    }
    return db.query(DatabaseHelper.MOVIES_TABLE_NAME,null,selection,selectionArgs,null,null,sortOrder);
   // return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
  }

  @Nullable @Override public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      /**
       * Get all movie records
       */
      case MOVIES:
        return "vnd.android.cursor.dir/vnd.harshikesh.movies";

      /**
       * Get a movie record
       */
      case MOVIE_ID:
        return "vnd.android.cursor.item/vnd.harshikesh.movies";

      default:
        throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
  }

  @Nullable @Override public Uri insert(Uri uri, ContentValues values) {

    //Adding new movie
    long rowID = 0;
    try {
      rowID = db.insert(DatabaseHelper.MOVIES_TABLE_NAME, "", values);
    }catch (Exception e)
    {
      e.printStackTrace();
    }
    count = (int)rowID;
    if (rowID > 0) {
      Uri contenturi = ContentUris.withAppendedId(CONTENT_URI, rowID);
      mContext.getContentResolver().notifyChange(contenturi, null);
      return contenturi;
    }else {
      Log.d(TAG,"Failed to add new movie ");
      throw new SQLException("Failed to add new movie Exception occured" + uri);
    }
  }

  @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
    int count = 0;

    switch (uriMatcher.match(uri)) {
      case MOVIES:
        count = db.delete(DatabaseHelper.MOVIES_TABLE_NAME, selection, selectionArgs);
        break;

      case MOVIE_ID:
        String id = uri.getPathSegments().get(1);
        count = db.delete(DatabaseHelper.MOVIES_TABLE_NAME, DatabaseHelper.ID + " = " + id +
            (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;

      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    mContext.getContentResolver().notifyChange(uri, null);
    return count;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

    switch (uriMatcher.match(uri)) {
      case MOVIES:
        count = db.update(DatabaseHelper.MOVIES_TABLE_NAME, values, selection, selectionArgs);
        break;

      case MOVIE_ID:
        count = db.update(DatabaseHelper.MOVIES_TABLE_NAME, values, DatabaseHelper.ID + " = " + uri.getPathSegments().get(1) +
            (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;

      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    if (count == 0) {
      insert(uri, values);
    }
    mContext.getContentResolver().notifyChange(uri, null);
    return count;
  }
}
