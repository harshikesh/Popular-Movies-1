package com.example.harshikesh.nanodegree_02_spotifier.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.AppConstants;
import com.example.harshikesh.nanodegree_02_spotifier.model.MovieResutModel;
import com.squareup.picasso.Picasso;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class MovieDetailFragment extends Fragment {

    private static String TAG=MovieDetailFragment.class.getSimpleName();

    private Bundle extras;
    private boolean mTwoPane;
    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_detail, container, false);

            extras = this.getArguments();
        final MovieResutModel model=extras.getParcelable("detail_model");

        Log.d(TAG,model.toString());

        TextView tvTitle = (TextView) rootView.findViewById(R.id.title);
        TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.release);
        TextView rating = (TextView) rootView.findViewById(R.id.rating);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.poster_image);
        ImageView backdrop = (ImageView) rootView.findViewById(R.id.background);
        final FloatingActionButton favourite = (FloatingActionButton) rootView.findViewById(R.id.favourite);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(model.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        TextView tvOverview = (TextView) rootView.findViewById(R.id.plot_synopsis);

        tvTitle.setText(model.getTitle());
        tvReleaseDate.setText(model.getRelease_date().substring(0,4));
        rating.setText("" + model.getVote_average());
        Picasso.with(getContext()).load(model.getPoster_path()).into(ivPoster);
        Picasso.with(getContext()).load(model.getBackdrop_path()).fit()
                .centerInside().into(backdrop);
        tvOverview.setText(model.getOverview());

        sharedpreferences = getActivity().getSharedPreferences(AppConstants.LIKED_MOVIES_SHAREDPREF, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        if (sharedpreferences.getBoolean(model.getId(), false)) {
            favourite.setImageResource(R.drawable.heart_filled);
        } else {
            favourite.setImageResource(R.drawable.heart_empty);
        }
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        return rootView;

    }
}
