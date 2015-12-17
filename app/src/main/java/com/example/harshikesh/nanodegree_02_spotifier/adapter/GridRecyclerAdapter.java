package com.example.harshikesh.nanodegree_02_spotifier.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.model.ResultModel;
import com.squareup.picasso.Picasso;

/**
 * Created by harshikesh.kumar on 07/12/15.
 */
public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ResultModel mMovieResutModel= new ResultModel();
    OnItemClickListener mItemClickListener;

    public GridRecyclerAdapter(Context context,ResultModel result)
    {
        mContext=context;
        mMovieResutModel=result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.grid_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // Picasso.with(mContext).cancelRequest(viewHolder.iconView);
            if (mMovieResutModel != null) {

                Picasso.with(mContext).load(mMovieResutModel.getResults().get(position).getPoster_path()).fit()
                       .centerInside()
                        .into(viewHolder.iconView);
        }
    }

    @Override
    public int getItemCount() {
        if(mMovieResutModel!=null) {
            Log.d("Movie Size: ", mMovieResutModel.getResults().size() + "");
            return mMovieResutModel.getResults().size();
        }
        return 0;
    }


        public interface OnItemClickListener {
            public void onItemClick(View view , int position);
        }

        public void setOnItemClickListener(final OnItemClickListener ItemClickListener) {
            mItemClickListener = ItemClickListener;
        }

    public void setData(ResultModel resultModel)
    {
        mMovieResutModel=resultModel;
    }


    public class ViewHolder  extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public ImageView iconView;

        public ViewHolder(View v)
        {
            super(v);
            iconView=(ImageView)v.findViewById(R.id.img_grid);
            iconView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
