package com.example.harshikesh.nanodegree_02_spotifier.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.AppConstants;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.MoviesInterface;
import com.example.harshikesh.nanodegree_02_spotifier.model.Language;
import com.example.harshikesh.nanodegree_02_spotifier.model.MovieResutModel;
import com.example.harshikesh.nanodegree_02_spotifier.model.ResultModel;
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

  public MainFragment() {
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
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
    if (isInternetAvailable()) {
      //Get preference
      SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
      String moviesFilter =
          pref.getString(getString(R.string.sort_by_key), getString(R.string.sort_by_default));
      if (!mMoviesFilter.equalsIgnoreCase(moviesFilter) || mResultModel == null) {
        mMoviesFilter = moviesFilter;
        fetchMovie(true);
      }
    } else {
      if (recList != null) {
        showSnackbar(recList, getResources().getString(R.string.no_connection));
      }
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(PARCELABLE_KEY, mResultModel);
  }

  /**
   * @param refresh whether the adapter should be refreshed.
   */
  void fetchMovie(boolean refresh) {
    if (refresh) {
      mResultModel = null;
      mPage = 1;
    }
    mApiManager = ApiManager.getInstance();
    iMovieInterface = mApiManager.getRestAdapter().create(MoviesInterface.class);

    if (mMoviesFilter.equals(AppConstants.HIGHEST_RATED)) {
      iMovieInterface.topRated(mPage, Language.LANGUAGE_EN.toString(), this);
    } else {
      iMovieInterface.popular(mPage, Language.LANGUAGE_EN.toString(), this);
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
            if(isInternetAvailable()) {
              mPage++;
              fetchMovie(false);
            }else
            {
              showSnackbar(recList,getResources().getString(R.string.no_connection));
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

  @Override public void success(ResultModel resultModel, Response response) {
    loadMovies(resultModel);
  }

  @Override public void failure(RetrofitError error) {
    Log.d(TAG, "MainFragment Retrofit failure" + error);
  }
}
