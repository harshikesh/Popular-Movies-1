package com.example.harshikesh.nanodegree_02_spotifier.dataprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.Iupdate;
import com.example.harshikesh.nanodegree_02_spotifier.model.MovieResutModel;

/**
 * Created by harshikesh.kumar on 07/02/16.
 */
public class UpdateDatabaseTask extends AsyncTask<Void, Void, Void> {

  private Context mContext;
  private MovieResutModel mMovieModel;
  private Iupdate mUpdateListener;
  private String inserted = "added to favorite";
  private String deleted = "removed from favorite";

  public UpdateDatabaseTask(Context context, MovieResutModel resutModel, Iupdate updateListener) {
    mUpdateListener = updateListener;
    mContext = context;
    mMovieModel = resutModel;
  }

  @Override protected Void doInBackground(Void... params) {
    saveFavorite();
    return null;
  }

  private void saveFavorite() {
    Cursor cursor = mContext.getContentResolver()
        .query(MoviesContentProvider.CONTENT_URI, null,
            DatabaseHelper.MOVIE_ID + "=" + String.valueOf(mMovieModel.getId()), null, null);
    MoviesContentProvider provider = new MoviesContentProvider(mContext);
    //If our cursor is not null and the count is 0 it's a new favorite, else removed it.
    if (cursor != null) {
      if (cursor.getCount() == 0) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.MOVIE_ID, mMovieModel.getId());
        values.put(DatabaseHelper.TITLE, mMovieModel.getTitle());
        values.put(DatabaseHelper.BACKDROP_IMAGE, mMovieModel.getBackdrop_path());
        values.put(DatabaseHelper.AVERAGE_RATING, String.valueOf(mMovieModel.getVote_average()));
        values.put(DatabaseHelper.RELEASE_DATE, mMovieModel.getRelease_date());
        values.put(DatabaseHelper.POSTER_IMAGE, mMovieModel.getPoster_path());
        values.put(DatabaseHelper.OVERVIEW, mMovieModel.getOverview());

        int isInserted = provider.update(MoviesContentProvider.CONTENT_URI, values,
            DatabaseHelper.MOVIE_ID + "=" + mMovieModel.getId(), null);
        if (isInserted > 0) {
          mUpdateListener.onSuccess(inserted);
        } else {
          mUpdateListener.onFailure();
        }
      } else {
        int isDeleted = provider.delete(MoviesContentProvider.CONTENT_URI,
            DatabaseHelper.MOVIE_ID + "=" + mMovieModel.getId(), null);
        if (isDeleted > 0) {
          mUpdateListener.onSuccess(deleted);
        } else {
          mUpdateListener.onFailure();
        }
      }
      cursor.close();
    }
  }
}
