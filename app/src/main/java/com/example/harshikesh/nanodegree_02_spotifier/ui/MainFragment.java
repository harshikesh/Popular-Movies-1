package com.example.harshikesh.nanodegree_02_spotifier.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.adapter.GridRecyclerAdapter;
import com.example.harshikesh.nanodegree_02_spotifier.api.ApiManager;
import com.example.harshikesh.nanodegree_02_spotifier.dataprovider.DatabaseHelper;
import com.example.harshikesh.nanodegree_02_spotifier.dataprovider.MoviesContentProvider;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.AppConstants;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.MoviesInterface;
import com.example.harshikesh.nanodegree_02_spotifier.model.Language;
import com.example.harshikesh.nanodegree_02_spotifier.model.MovieResutModel;
import com.example.harshikesh.nanodegree_02_spotifier.model.ResultModel;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class MainFragment extends BaseFragment implements Callback<ResultModel> {

  private static final String PARCELABLE_KEY = "parcelable_key";
  private static String TAG = MainFragment.class.getSimpleName();
  private GridRecyclerAdapter gridAdapter;
  private ResultModel mResultModel;
  private RecyclerView recList;
  private Context mContext;
  private boolean mTwoPane = false;
  private GridLayoutManager glm;
  private ApiManager mApiManager;
  private MoviesInterface iMovieInterface;
  private int mPage = 1;
  private String mMoviesFilter = AppConstants.MOST_POPULAR;
  private MainActivity mActivity;
  private SharedPreferences pref;

  public MainFragment() {
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = (MainActivity) getActivity();
    pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
    mMoviesFilter = pref.getString(getString(R.string.sort_by_key),
        getString(R.string.sort_by_default));
    if (savedInstanceState == null) {
      Log.d(TAG,"first time fetching");
      fetchMovie(true);
    } else {
      mResultModel = (ResultModel) savedInstanceState.getParcelable(PARCELABLE_KEY);
    }
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
  }

  @Override public void onResume() {
    super.onResume();
    //Get preference
    String moviesFilter =
        pref.getString(getString(R.string.sort_by_key), getString(R.string.sort_by_default));
    setActionBarTitle(moviesFilter);
    if (!mMoviesFilter.equalsIgnoreCase(moviesFilter) || mResultModel == null) {
      mMoviesFilter = moviesFilter;
      fetchMovie(true);
    }
  }

  private void setActionBarTitle(String filter)
  {
    //On Screen rotation the title will not change.
    if (filter.equals(AppConstants.HIGHEST_RATED)) {
      mActivity.getSupportActionBar().setTitle(getResources().getString(R.string.top_rated_movies));
    } else if (filter.equals(AppConstants.MOST_POPULAR)) {
      mActivity.getSupportActionBar().setTitle(getResources().getString(R.string.most_popular_movies));
    } else if (filter.equals(AppConstants.FAVORITE)) {
      mActivity.getSupportActionBar().setTitle(getResources().getString(R.string.my_favorite));
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(PARCELABLE_KEY, mResultModel);
  }

  /**
   * @param refresh whether the adapter should be refreshed.
   */
  private void fetchMovie(boolean refresh) {
    if (refresh) {
      mResultModel = null;
      mPage = 1;
    }
    mApiManager = ApiManager.getInstance();
    iMovieInterface = mApiManager.getRestAdapter().create(MoviesInterface.class);
    if (mMoviesFilter.equals(AppConstants.HIGHEST_RATED)) {
      iMovieInterface.topRated(mPage, Language.LANGUAGE_EN.toString(), this);
    } else if (mMoviesFilter.equals(AppConstants.MOST_POPULAR)) {
      iMovieInterface.popular(mPage, Language.LANGUAGE_EN.toString(), this);
    } else if (mMoviesFilter.equals(AppConstants.FAVORITE)) {
      new LoadMoviesTask().execute();
    }
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_main, container, false);
    recList = (RecyclerView) view.findViewById(R.id.recyclerview);
    recList.setHasFixedSize(true);
    recList.setOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
          int visibleItemCount = recyclerView.getChildCount();
          int totalItemCount = recyclerView.getLayoutManager().getItemCount();
          int pastVisibleItem =
              ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
          if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
            if (!mMoviesFilter.equals(AppConstants.FAVORITE)) {
              if (isInternetAvailable()) {
                mPage++;
                fetchMovie(false);
              } else {
                showSnackbar(recList, getResources().getString(R.string.no_connection));
              }
            } else {
              showSnackbar(recList, getResources().getString(R.string.thats_all));
            }
          }
        }
      }
    });

    if (getActivity().findViewById(R.id.detail_container) != null) {
      mTwoPane = true;
    }
    if (getActivity().getResources().getConfiguration().orientation
        == Configuration.ORIENTATION_PORTRAIT) {
      glm = new GridLayoutManager(mContext, 2);
    } else {
      glm = new GridLayoutManager(mContext, 3);
    }
    glm.setOrientation(GridLayoutManager.VERTICAL);

    recList.setLayoutManager(glm);
    gridAdapter = new GridRecyclerAdapter(mContext, mResultModel);
    recList.setAdapter(gridAdapter);
    gridAdapter.setOnItemClickListener(new GridRecyclerAdapter.OnItemClickListener() {
      @Override public void onItemClick(View view, int position) {
        if (mResultModel != null) {
          MovieResutModel model = mResultModel.getResults().get(position);
          Bundle bundle = new Bundle();
          bundle.putParcelable("detail_model", model);
          if (mTwoPane) {
            instantiateDetailFragment(bundle);
          } else {
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
          }
        }
      }
    });
    return view;
  }

  private void instantiateDetailFragment(Bundle bundle) {
    MovieDetailFragment fragment = new MovieDetailFragment();
    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    ft.setCustomAnimations(android.support.design.R.anim.abc_fade_in,
        android.support.design.R.anim.abc_fade_out);
    ft.replace(R.id.detail_container, fragment);
    fragment.setArguments(bundle);
    ft.commit();
  }

  private int loadFavoriteMovies() {
    // Retrieve movie records
    mResultModel = null;
    ResultModel resultModel = new ResultModel();
    ArrayList<MovieResutModel> favsModel = new ArrayList<>();
    MoviesContentProvider provider = new MoviesContentProvider(getContext());

    Cursor c =
        provider.query(Uri.parse(MoviesContentProvider.CONTENT_URI.toString()), null, null, null,
            "title");
    if (c != null) {
      if (c.moveToFirst()) {
        do {
          favsModel.add(new MovieResutModel(c.getString(c.getColumnIndex(DatabaseHelper.MOVIE_ID)),
              c.getString(c.getColumnIndex(DatabaseHelper.TITLE)),
              c.getString(c.getColumnIndex(DatabaseHelper.OVERVIEW)),
              String.valueOf(c.getLong(c.getColumnIndex(DatabaseHelper.RELEASE_DATE))),
              c.getString(c.getColumnIndex(DatabaseHelper.AVERAGE_RATING)),
              c.getString(c.getColumnIndex(DatabaseHelper.POSTER_IMAGE)),
              c.getString(c.getColumnIndex(DatabaseHelper.BACKDROP_IMAGE))));
        } while (c.moveToNext());
      }
      c.close();
    }
    resultModel.setResults(favsModel);
    mResultModel = resultModel;
    return favsModel.size();
  }

  private void loadMovies(ResultModel resultModel) {
    if (mResultModel == null) {
      mResultModel = resultModel;
    } else {
      mResultModel.getResults().addAll(resultModel.getResults());
    }
    gridAdapter.setData(mResultModel);
    gridAdapter.notifyDataSetChanged();
    if (mTwoPane) {
      Log.d(TAG, "Two pane mode");
      Bundle bundle = new Bundle();
      bundle.putParcelable("detail_model", mResultModel.getResults().get(0));
      instantiateDetailFragment(bundle);
    }
  }

  class LoadMoviesTask extends AsyncTask<Void, Integer, Integer> {
    @Override protected Integer doInBackground(Void... params) {
      Integer size = loadFavoriteMovies();
      return size;
    }

    @Override protected void onPostExecute(Integer size) {
      super.onPostExecute(size);
      Log.d(TAG, "Favorite movie size : " + size);
      if (size > 0) {
        gridAdapter.setData(mResultModel);
        gridAdapter.notifyDataSetChanged();
      } else {
        mResultModel = null;
        gridAdapter.setData(mResultModel);
        gridAdapter.notifyDataSetChanged();
        showSnackbar(recList, getResources().getString(R.string.no_fav_movie));
        Log.d(TAG, "No favourite items");
      }
    }
  }

  @Override public void success(ResultModel resultModel, Response response) {
    loadMovies(resultModel);
  }

  @Override public void failure(RetrofitError error) {
    mResultModel = null;
    gridAdapter.setData(null);
    gridAdapter.notifyDataSetChanged();
    showSnackbar(recList, getResources().getString(R.string.unable_to_reach));
    Log.d(TAG, "MainFragment Retrofit failure" + error);
  }
}
