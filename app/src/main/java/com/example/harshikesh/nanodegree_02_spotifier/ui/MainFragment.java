package com.example.harshikesh.nanodegree_02_spotifier.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.adapter.GridRecyclerAdapter;
import com.example.harshikesh.nanodegree_02_spotifier.interfaces.ApiConstants;
import com.example.harshikesh.nanodegree_02_spotifier.model.MovieResutModel;
import com.example.harshikesh.nanodegree_02_spotifier.model.ResultModel;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class MainFragment extends Fragment{

    private static String TAG=MainFragment.class.getSimpleName();
    GridRecyclerAdapter gridAdapter;
    private ResultModel mResultModel;
    RecyclerView recList;
    private Context mContext;
    private boolean mTwoPane = false;
    private GridLayoutManager glm ;
    public MainFragment()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get preference
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = pref.getString(getString(R.string.sort_by_key), getString(R.string.sort_by_default));
        String url= ApiConstants.DISCOVER_BASE_URL+sort+ApiConstants.API_KEY;
        FetchMovieTask movietask = new FetchMovieTask(url);
        movietask.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_main,container,false);


         recList = (RecyclerView) view.findViewById(R.id.recyclerview);
        recList.setHasFixedSize(true);

        if (getActivity().findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
        }
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            glm = new GridLayoutManager(mContext,2);
        }
        else{
            glm = new GridLayoutManager(mContext,3);
        }
        glm.setOrientation(GridLayoutManager.VERTICAL);

        recList.setLayoutManager(glm);
        gridAdapter= new GridRecyclerAdapter(mContext,mResultModel);
        recList.setAdapter(gridAdapter);
        gridAdapter.setOnItemClickListener(new GridRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MovieResutModel model = mResultModel.getResults().get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("detail_model", model);
                if(mTwoPane)
                {
                    instantiateDetailFragment(bundle);
                }else {
                    Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

   private void instantiateDetailFragment(Bundle bundle)
    {
        MovieDetailFragment fragment= new MovieDetailFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.support.design.R.anim.abc_fade_in, android.support.design.R.anim.abc_fade_out);
        ft.replace(R.id.detail_container,fragment);
        fragment.setArguments(bundle);
        ft.commit();

    }


    class FetchMovieTask extends AsyncTask<Void,Void,ResultModel>
    {

        private  String TAG=FetchMovieTask.class.getSimpleName();
        BufferedReader bReader;
        String url;
        FetchMovieTask(String lUrl)
        {
            url=lUrl;
        }
        @Override
        protected ResultModel doInBackground(Void... params) {


            Log.d(TAG,url);
            HttpURLConnection urlConnection = null;
            StringBuffer buffer=new StringBuffer();
            try {
                URL u = new URL(url);
                urlConnection = (HttpURLConnection) u.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream iStream = urlConnection.getInputStream();
                if (iStream == null) {
                    return null;
                }
                bReader = new BufferedReader(new InputStreamReader(iStream));
                String line;
                while ((line = bReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                Log.d(TAG,"result :"+ buffer);
                Gson gson = new Gson();
                mResultModel=gson.fromJson(buffer.toString(), ResultModel.class);
                return  mResultModel;
            }catch (IOException e)
            {
                Log.d(TAG,e.toString());
            }
            finally {
                if(urlConnection!=null) {
                    urlConnection.disconnect();
                }
                if(bReader!=null)
                {
                    try {
                        bReader.close();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResultModel model) {
            super.onPostExecute(model);
            if(model!=null)
            {
                 gridAdapter.setData(model);
                gridAdapter.notifyDataSetChanged();
                if(mTwoPane) {
                    Log.d(TAG, "Two pane mode");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("detail_model", mResultModel.getResults().get(0));
                    instantiateDetailFragment(bundle);
                }
            }
        }
    }
}
