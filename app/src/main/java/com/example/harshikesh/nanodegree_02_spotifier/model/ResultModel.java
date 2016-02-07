package com.example.harshikesh.nanodegree_02_spotifier.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class ResultModel implements Parcelable {

  ArrayList<MovieResutModel> results = new ArrayList<MovieResutModel>();

  public ArrayList<MovieResutModel> getResults() {
    return results;
  }

  public void setResults(ArrayList<MovieResutModel> results) {
    this.results = results;
  }

  public ResultModel () {
  }

  public static final Parcelable.Creator<ResultModel> CREATOR = new Parcelable.Creator<ResultModel>() {
    public ResultModel createFromParcel(Parcel in) {
      return new ResultModel(in);
    }

    public ResultModel[] newArray(int size) {
      return new ResultModel[size];
    }
  };


  private ResultModel(Parcel in) {
    in.readTypedList(results,MovieResutModel.CREATOR);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(results);
  }
}
