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
import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.api.ApiManager;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.AppConstants;
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
public class MovieDetailFragment extends BaseFragment {

  private static String TAG = MovieDetailFragment.class.getSimpleName();

  private Bundle extras;
  private boolean mTwoPane;
  private SharedPreferences sharedpreferences;
  SharedPreferences.Editor editor;
  private MovieTrailerModel mTrailerModel;
  private int mPallColor;
  private Activity mActivity;
  private CollapsingToolbarLayout mCollapsingToolBar;
  private int mStatusColor;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mActivity = getActivity();
    View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

    extras = this.getArguments();

    final MovieResutModel model = extras.getParcelable("detail_model");
    Log.d(TAG, model.toString());
    final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView.findViewById(
        R.id.coordinate_layout);

    mCollapsingToolBar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

    TextView tvTitle = (TextView) rootView.findViewById(R.id.title);
    TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.release);
    TextView rating = (TextView) rootView.findViewById(R.id.rating);
    ImageView ivPoster = (ImageView) rootView.findViewById(R.id.poster_image);
    final ImageView backdrop = (ImageView) rootView.findViewById(R.id.background);
    final FloatingActionButton favourite =
        (FloatingActionButton) rootView.findViewById(R.id.favourite);
    final FloatingActionButton share = (FloatingActionButton) rootView.findViewById(R.id.share);
    final FloatingActionButton trailers =
        (FloatingActionButton) rootView.findViewById(R.id.trailers);
    mCollapsingToolBar.setTitle(model.getTitle());
    mCollapsingToolBar.setExpandedTitleColor(
        getResources().getColor(android.R.color.transparent));

    TextView tvOverview = (TextView) rootView.findViewById(R.id.plot_synopsis);

    tvTitle.setText(model.getTitle());
    tvReleaseDate.setText(model.getRelease_date().substring(0, 4));
    rating.setText("" + model.getVote_average());
    final PaletteTransformation transformation = PaletteTransformation.getInstance();
    Picasso.with(getContext()).load(model.getPoster_path()).into(ivPoster);
    Picasso.with(getContext())
        .load(model.getBackdrop_path())
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
    tvOverview.setText(model.getOverview());

    sharedpreferences = getActivity().getSharedPreferences(AppConstants.LIKED_MOVIES_SHAREDPREF,
        Context.MODE_PRIVATE);
    editor = sharedpreferences.edit();
    if (sharedpreferences.getBoolean(model.getId(), false)) {
      favourite.setImageResource(R.drawable.heart_filled);
    } else {
      favourite.setImageResource(R.drawable.heart_empty);
    }

    final MoviesInterface movieinterface =
        ApiManager.getInstance().getRestAdapter().create(MoviesInterface.class);

    favourite.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

          if (sharedpreferences.getBoolean(model.getId(), false)) {
            favourite.setImageResource(R.drawable.heart_empty);
            editor.putBoolean(model.getId(), false);
          } else {
            favourite.setImageResource(R.drawable.heart_filled);
            editor.putBoolean(model.getId(), true);
          }
          editor.commit();

      }
    });
    share.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (isAdded()) {
          if (isInternetAvailable()) {
            movieinterface.videos(model.getId(), Language.LANGUAGE_EN.toString(), new Callback<MovieTrailerModel>() {
                  @Override public void success(MovieTrailerModel movieTrailerModel, Response response) {
                    Log.d(TAG, "Response retrofit" + movieTrailerModel + " \n" + response.toString());
                    if (movieTrailerModel != null) {
                      Intent intent_Share = new Intent();
                      intent_Share.setAction(Intent.ACTION_SEND);
                      intent_Share.setType("text/plain");
                      intent_Share.putExtra(Intent.EXTRA_TEXT,
                          ApiManager.VIDEO_BASE_URL + movieTrailerModel.getResults().get(0).getKey());
                      startActivity(Intent.createChooser(intent_Share, "Share via"));
                    }
                  }

                  @Override public void failure(RetrofitError error) {
                    Log.d(TAG, "Response retrofit error" + error);
                  }
                });
          } else {
            showSnackbar(coordinatorLayout, getResources().getString(R.string.no_connection));
          }
        }
      }
    });
    trailers.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
      if (isInternetAvailable()) {
        movieinterface.videos(model.getId(), Language.LANGUAGE_EN.toString(),
            new Callback<MovieTrailerModel>() {
              @Override
              public void success(MovieTrailerModel movieTrailerModel, Response response) {
                Log.d(TAG, "Response retrofit" + movieTrailerModel + " \n" + response.toString());
                watchYoutubeTrailer(movieTrailerModel);
              }

              @Override public void failure(RetrofitError error) {
                Log.d(TAG, "Response retrofit error" + error);
              }
            });
      } else {
        showSnackbar(coordinatorLayout, getResources().getString(R.string.no_connection));
      }
      }
    });
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
    Log.d(TAG, "actionbar colors "+actionBarColor + "status color" + statusBarColor );
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
}
