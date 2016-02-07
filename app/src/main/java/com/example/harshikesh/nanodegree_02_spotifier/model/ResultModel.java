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

  private ResultModel(Parcel in) {
  }

  public static final Parcelable.Creator<MovieResutModel> CREATOR =
      new Parcelable.Creator<MovieResutModel>() {

        @Override public MovieResutModel createFromParcel(Parcel source) {
          return new MovieResutModel(source);
        }

        @Override public MovieResutModel[] newArray(int size) {
          return new MovieResutModel[size];
        }
      };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }
}
