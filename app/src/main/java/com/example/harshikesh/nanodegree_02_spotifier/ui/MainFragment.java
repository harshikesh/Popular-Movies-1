package com.example.harshikesh.nanodegree_02_spotifier.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.adapter.MovieGridAdapter;
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
    MovieGridAdapter mMovieGridAdapter;
    private ResultModel mResultModel;
    private  GridView gridview;
    private Context mContext;

    public MainFragment()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FetchMovieTask movietask=new FetchMovieTask();
        movietask.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_main,container,false);

        mMovieGridAdapter= new MovieGridAdapter(getContext(),mResultModel);

        gridview= (GridView)view.findViewById(R.id.grid_movie);
        gridview.setAdapter(mMovieGridAdapter);
        gridview.setOnItemClickListener(onItemCLickListener);
        return view;
    }

    AdapterView.OnItemClickListener onItemCLickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

           MovieResutModel model=mResultModel.getResults().get(position);
            Intent intent= new Intent(getContext(),MovieDetailActivity.class);
            intent.putExtra("detail_model",model);
            startActivity(intent);
        }
    };

    class FetchMovieTask extends AsyncTask<Void,Void,ResultModel>
    {

        private  String TAG=FetchMovieTask.class.getSimpleName();
        BufferedReader bReader;
        @Override
        protected ResultModel doInBackground(Void... params) {

            //Get preference
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort = pref.getString(getString(R.string.sort_by_key), getString(R.string.sort_by_key));

            String url= ApiConstants.DISCOVER_BASE_URL+sort+ApiConstants.API_KEY;
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
                mMovieGridAdapter=null;
                mMovieGridAdapter = new MovieGridAdapter(mContext,model);
                gridview.setAdapter(mMovieGridAdapter);
                mMovieGridAdapter.notifyDataSetChanged();
            }
        }
    }
}
