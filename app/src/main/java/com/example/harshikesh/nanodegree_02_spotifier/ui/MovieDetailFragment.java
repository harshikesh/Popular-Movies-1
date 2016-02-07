package com.example.harshikesh.nanodegree_02_spotifier.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.api.ApiManager;
import com.example.harshikesh.nanodegree_02_spotifier.dataprovider.UpdateDatabaseTask;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.AppConstants;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.Iupdate;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.MoviesInterface;
import com.example.harshikesh.nanodegree_02_spotifier.model.Language;
import com.example.harshikesh.nanodegree_02_spotifier.model.MovieResutModel;
import com.example.harshikesh.nanodegree_02_spotifier.model.MovieTrailerModel;
import com.example.harshikesh.nanodegree_02_spotifier.util.PaletteTransformation;
import com.squareup.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class MovieDetailFragment extends BaseFragment implements View.OnClickListener, Iupdate {

  private static String TAG = MovieDetailFragment.class.getSimpleName();

  private Bundle extras;
  private boolean mTwoPane;
  private SharedPreferences sharedpreferences;
  SharedPreferences.Editor editor;
  private MovieTrailerModel mTrailerModel;
  private int mPallColor;
  private Activity mActivity;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolBar;
  private int mStatusColor;
  @Bind(R.id.title) TextView tvTitle;
  @Bind(R.id.release) TextView tvReleaseDate;
  @Bind(R.id.rating) TextView rating;
  @Bind(R.id.poster_image) ImageView ivPoster;
  @Bind(R.id.background) ImageView backdrop;
  @Bind(R.id.plot_synopsis) TextView tvOverview;
  private MovieResutModel mMovieModel;
  @Bind(R.id.coordinate_layout) CoordinatorLayout coordinatorLayout;
  @Bind(R.id.favourite) FloatingActionButton favouriteButton;
  @Bind(R.id.share) FloatingActionButton shareButton;
  @Bind(R.id.trailers) FloatingActionButton trailerButton;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mActivity = getActivity();

    View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

    extras = this.getArguments();
    mMovieModel = (MovieResutModel) extras.getParcelable("detail_model");
    Log.d(TAG, mMovieModel.toString());
    ButterKnife.bind(this, rootView);

    mCollapsingToolBar.setTitle(mMovieModel.getTitle());
    mCollapsingToolBar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    tvTitle.setText(mMovieModel.getTitle());
    tvReleaseDate.setText(mMovieModel.getRelease_date().substring(0, 4));
    rating.setText("" + mMovieModel.getVote_average());

    final PaletteTransformation transformation = PaletteTransformation.getInstance();
    Picasso.with(getContext())
        .load(mMovieModel.getPoster_path())
        .fit()
        .placeholder(R.drawable.movie_placeholder)
        .error(R.drawable.error_placeholder)
        .into(ivPoster);
    Picasso.with(getContext())
        .load(mMovieModel.getBackdrop_path())
        .fit()
        .placeholder(R.drawable.movie_placeholder)
        .error(R.drawable.error_placeholder)
        .transform(transformation)
        .centerInside()
        .into(backdrop, new com.squareup.picasso.Callback() {
          @Override public void onSuccess() {
            Log.d(TAG, "Palette color mTwoPane : " + mTwoPane);
            if (!mTwoPane && isAdded()) {
              Bitmap bitmap = ((BitmapDrawable) backdrop.getDrawable()).getBitmap(); // Ew!
              Palette palette = transformation.extractPaletteAndRelease();
              if (palette != null) {
                mPallColor =
                    palette.getLightMutedColor(getResources().getColor(R.color.colorPrimaryDark));
                mStatusColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
                setActionbarAndStatusBarColors(mPallColor, mStatusColor);
              }
            }
          }

          @Override public void onError() {

          }
        });
    tvOverview.setText(mMovieModel.getOverview());

    sharedpreferences = getActivity().getSharedPreferences(AppConstants.LIKED_MOVIES_SHAREDPREF,
        Context.MODE_PRIVATE);
    editor = sharedpreferences.edit();
    if (sharedpreferences.getBoolean(mMovieModel.getId(), false)) {
      favouriteButton.setImageResource(R.drawable.heart_filled);
    } else {
      favouriteButton.setImageResource(R.drawable.heart_empty);
    }

    favouriteButton.setOnClickListener(this);
    shareButton.setOnClickListener(this);
    trailerButton.setOnClickListener(this);
    return rootView;
  }

  private void watchYoutubeTrailer(final MovieTrailerModel movieTrailerModel) {

    int vidSize = movieTrailerModel.getResults().size();
    String[] titles = new String[vidSize];
    for (int i = 0; i < vidSize; i++) {
      titles[i] = movieTrailerModel.getResults().get(i).getName();
    }
    AlertDialog dialog =
        new AlertDialog.Builder(getContext()).setTitle(R.string.movies_trailer_dialog)
            .setSingleChoiceItems(titles, -1, new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                watchYoutubeVideo(movieTrailerModel.getResults().get(which).getKey());
                dialog.dismiss();
              }
            })
            .create();
    dialog.show();
  }

  private void watchYoutubeVideo(String id) {
    try {
      if (isAdded()) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiManager.VIDEO_BASE_URL + id));
        startActivity(intent);
      }
    } catch (ActivityNotFoundException ex) {
      Log.d(TAG, " Activity not found to play youtube video " + ex);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  protected void setActionbarAndStatusBarColors(int actionBarColor, int statusBarColor) {
    Log.d(TAG, "actionbar colors " + actionBarColor + "status color" + statusBarColor);
    try {
      ((MovieDetailActivity) mActivity).getSupportActionBar()
          .setBackgroundDrawable(new ColorDrawable(actionBarColor));
      mCollapsingToolBar.setContentScrimColor(actionBarColor);
      Window window = getActivity().getWindow();
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(statusBarColor);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void onClick(View v) {
    final MoviesInterface movieinterface =
        ApiManager.getInstance().getRestAdapter().create(MoviesInterface.class);
    switch (v.getId()) {
      case R.id.favourite:

        if (sharedpreferences.getBoolean(mMovieModel.getId(), false)) {
          favouriteButton.setImageResource(R.drawable.heart_empty);
          editor.putBoolean(mMovieModel.getId(), false);
        } else {
          favouriteButton.setImageResource(R.drawable.heart_filled);
          editor.putBoolean(mMovieModel.getId(), true);
        }
        editor.commit();
        //Update database
        UpdateDatabaseTask updateTask = new UpdateDatabaseTask(mActivity, mMovieModel, this);
        updateTask.execute();
        break;
      case R.id.share:
        if (isAdded()) {
          if (isInternetAvailable()) {
            movieinterface.videos(mMovieModel.getId(), Language.LANGUAGE_EN.toString(),
                new Callback<MovieTrailerModel>() {
                  @Override
                  public void success(MovieTrailerModel movieTrailerModel, Response response) {
                    Log.d(TAG,
                        "Response retrofit" + movieTrailerModel + " \n" + response.toString());
                    if (movieTrailerModel != null) {
                      Intent intent_Share = new Intent();
                      intent_Share.setAction(Intent.ACTION_SEND);
                      intent_Share.setType("text/plain");
                      intent_Share.putExtra(Intent.EXTRA_TEXT,
                          ApiManager.VIDEO_BASE_URL + movieTrailerModel.getResults()
                              .get(0)
                              .getKey());
                      startActivity(Intent.createChooser(intent_Share, "Share via"));
                    }
                  }

                  @Override public void failure(RetrofitError error) {
                    Log.d(TAG, "Response retrofit error" + error);
                    showSnackbar(coordinatorLayout,
                        getResources().getString(R.string.something_went_wrong));
                  }
                });
          } else {
            showSnackbar(coordinatorLayout, getResources().getString(R.string.no_connection));
          }
        }
        break;
      case R.id.trailers:
        if (isInternetAvailable()) {
          movieinterface.videos(mMovieModel.getId(), Language.LANGUAGE_EN.toString(),
              new Callback<MovieTrailerModel>() {
                @Override
                public void success(MovieTrailerModel movieTrailerModel, Response response) {
                  Log.d(TAG, "Response retrofit" + movieTrailerModel + " \n" + response.toString());
                  watchYoutubeTrailer(movieTrailerModel);
                }

                @Override public void failure(RetrofitError error) {
                  Log.d(TAG, "Response retrofit error" + error);
                  showSnackbar(coordinatorLayout,
                      getResources().getString(R.string.something_went_wrong));
                }
              });
        } else {
          showSnackbar(coordinatorLayout, getResources().getString(R.string.no_connection));
        }
        break;
    }
  }

  @Override public void onSuccess(String val) {
    showSnackbar(coordinatorLayout, mMovieModel.getTitle() + " " + val);
  }

  @Override public void onFailure() {
    showSnackbar(coordinatorLayout,
        mMovieModel.getTitle() + " " + getResources().getString(R.string.something_went_wrong));
  }
}
