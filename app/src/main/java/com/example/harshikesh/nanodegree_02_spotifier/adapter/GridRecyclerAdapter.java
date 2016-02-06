package com.example.harshikesh.nanodegree_02_spotifier.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.harshikesh.nanodegree_02_spotifier.R;
import com.example.harshikesh.nanodegree_02_spotifier.model.ResultModel;
import com.squareup.picasso.Picasso;

/**
 * Created by harshikesh.kumar on 07/12/15.
 */
public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> {

  private Context mContext;
  private ResultModel mMovieResutModel;
  OnItemClickListener mItemClickListener;

  public GridRecyclerAdapter(Context context, ResultModel result) {
    mContext = context;
    mMovieResutModel = result;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

    View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.grid_item, viewGroup, false);

    return new ViewHolder(itemView);
  }

  @Override public void onBindViewHolder(ViewHolder viewHolder, int position) {

    if (mMovieResutModel != null) {

      Picasso.with(mContext)
          .load(mMovieResutModel.getResults().get(position).getPoster_path())
          .fit()
          .placeholder(R.drawable.movie_placeholder)
          .error(R.drawable.error_placeholder)
          .centerInside()
          .into(viewHolder.iconView);
      viewHolder.ratingtextview.setText(
          mMovieResutModel.getResults().get(position).getVote_average() + "");
      viewHolder.titletextview.setText(mMovieResutModel.getResults().get(position).getTitle());
    }
  }

  @Override public int getItemCount() {
    if (mMovieResutModel != null) {
      return mMovieResutModel.getResults().size();
    }
    return 0;
  }

  public interface OnItemClickListener {
    public void onItemClick(View view, int position);
  }

  public void setOnItemClickListener(final OnItemClickListener ItemClickListener) {
    mItemClickListener = ItemClickListener;
  }

  public void setData(ResultModel resultModel) {
    mMovieResutModel = resultModel;
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView iconView;
    public TextView titletextview;
    public TextView ratingtextview;

    public ViewHolder(View v) {
      super(v);
      iconView = (ImageView) v.findViewById(R.id.img_grid);
      titletextview = (TextView) v.findViewById(R.id.title_text_view);
      ratingtextview = (TextView) v.findViewById(R.id.rating_text_view);
      iconView.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
      if (mItemClickListener != null) {
        mItemClickListener.onItemClick(v, getPosition());
      }
    }
  }
}
