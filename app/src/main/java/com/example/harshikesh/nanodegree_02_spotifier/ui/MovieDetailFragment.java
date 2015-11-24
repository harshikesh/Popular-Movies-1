package com.example.harshikesh.nanodegree_02_spotifier.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.model.MovieResutModel;
import com.squareup.picasso.Picasso;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class MovieDetailFragment extends android.support.v4.app.Fragment {

    private static String TAG=MovieDetailFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_detail, container, false);


        Intent i = getActivity().getIntent();
        MovieResutModel model= (MovieResutModel)i.getSerializableExtra("detail_model");
        Log.d(TAG,model.toString());

        TextView tvTitle = (TextView) rootView.findViewById(R.id.title);
        TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.release);
        TextView rating = (TextView) rootView.findViewById(R.id.rating);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.poster_image);
        TextView tvTime = (TextView) rootView.findViewById(R.id.total_time);
        Button favourite = (Button) rootView.findViewById(R.id.favourite);
        TextView tvOverview = (TextView) rootView.findViewById(R.id.plot_synopsis);

        tvTitle.setText(model.getTitle());
        tvReleaseDate.setText(model.getRelease_date());
        rating.setText(""+model.getVote_average()+"/10");
        Picasso.with(getContext()).load(model.getPoster_path()).into(ivPoster);
        favourite.setText("Mark as favourite");
        tvTime.setText("120 min");
        tvOverview.setText(model.getOverview());
        return rootView;

    }
}
