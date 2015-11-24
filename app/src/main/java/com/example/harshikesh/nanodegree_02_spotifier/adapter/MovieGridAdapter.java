package com.example.harshikesh.nanodegree_02_spotifier.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.model.ResultModel;
import com.squareup.picasso.Picasso;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class MovieGridAdapter extends BaseAdapter {

    private static String TAG= MovieGridAdapter.class.getSimpleName();

    private Context mContext;
    private ResultModel mMovieResutModel= new ResultModel();

    public MovieGridAdapter(Context context,ResultModel result)
    {
        mContext=context;
        mMovieResutModel=result;
    }
    @Override
    public int getCount() {
        if(mMovieResutModel!=null) {
            Log.d(TAG,mMovieResutModel.getResults().size()+"");
            return mMovieResutModel.getResults().size();
        }
        return 0;
    }

    public void setmMovieResutModel(ResultModel movieResutModel) {
        this.mMovieResutModel = movieResutModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.grid_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.img_grid);
            convertView.setTag(viewHolder);
        }
        viewHolder=(ViewHolder)convertView.getTag();

        if(viewHolder!=null) {
           // Picasso.with(mContext).cancelRequest(viewHolder.iconView);
            if (mMovieResutModel != null) {

                Picasso.with(mContext).load(mMovieResutModel.getResults().get(position).getPoster_path()).into(viewHolder.iconView);
            }
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        public ImageView iconView;

    }

}
