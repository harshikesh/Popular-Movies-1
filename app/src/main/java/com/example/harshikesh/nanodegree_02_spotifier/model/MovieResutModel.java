package com.example.harshikesh.nanodegree_02_spotifier.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.harshikesh.nanodegree_02_spotifier.api.ApiManager;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class MovieResutModel implements Parcelable {

  boolean adult;
  String id;

  @Override public int describeContents() {
    return 0;
  }

  public MovieResutModel(Parcel in) {
    backdrop_path = in.readString();
    original_language = in.readString();
    original_title = in.readString();
    overview = in.readString();
    release_date = in.readString();
    poster_path = in.readString();
    title = in.readString();
    video = in.readString();
    id = in.readString();
    popularity = in.readFloat();
    vote_average = in.readFloat();
    vote_count = in.readLong();
  }

  @Override public void writeToParcel(Parcel dest, int flags) {

    dest.writeString(backdrop_path);
    dest.writeString(original_language);
    dest.writeString(original_title);
    dest.writeString(overview);
    dest.writeString(release_date);
    dest.writeString(poster_path);
    dest.writeString(title);
    dest.writeString(video);
    dest.writeString(id);
    dest.writeFloat(popularity);
    dest.writeFloat(vote_average);
    dest.writeFloat(vote_count);
  }

  String backdrop_path;//":"/kvXLZqY0Ngl1XSw7EaMQO0C1CCj.jpg",
  String[] genre_ids;//
  String original_language;//":"en",
  String original_title;//":"Ant-Man",
  String overview;
      //":"Armed with the astonishing ability to shrink in scale but increase in strength, con-man Scott Lang must embrace his inner-hero and help his mentor, Dr. Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a heist that will save the world.",
  String release_date;//":"2015-07-17",
  String poster_path;//":"/7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg",
  float popularity;//":56.44978,
  String title;//":"Ant-Man",
  String video;//":false,
  float vote_average;//":7.0,
  long vote_count;//":1593

  public boolean isAdult() {
    return adult;
  }

  public void setAdult(boolean adult) {
    this.adult = adult;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBackdrop_path() {
    return ApiManager.IMAGE_BASE_URL_BACKDROP + backdrop_path;
  }

  public void setBackdrop_path(String backdrop_path) {
    this.backdrop_path = backdrop_path;
  }

  public String[] getGenre_ids() {
    return genre_ids;
  }

  public void setGenre_ids(String[] genre_ids) {
    this.genre_ids = genre_ids;
  }

  public String getOriginal_language() {
    return original_language;
  }

  public void setOriginal_language(String original_language) {
    this.original_language = original_language;
  }

  public String getOriginal_title() {
    return original_title;
  }

  public void setOriginal_title(String original_title) {
    this.original_title = original_title;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getRelease_date() {
    return release_date;
  }

  public void setRelease_date(String release_date) {
    this.release_date = release_date;
  }

  public String getPoster_path() {
    return ApiManager.IMAGE_BASE_URL + poster_path;
  }

  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }

  public float getPopularity() {
    return popularity;
  }

  public void setPopularity(float popularity) {
    this.popularity = popularity;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getVideo() {
    return video;
  }

  public void setVideo(String video) {
    this.video = video;
  }

  public float getVote_average() {
    return vote_average;
  }

  public void setVote_average(float vote_average) {
    this.vote_average = vote_average;
  }

  public long getVote_count() {
    return vote_count;
  }

  public void setVote_count(long vote_count) {
    this.vote_count = vote_count;
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

  @Override public String toString() {
    String string = "id :"
        + id
        + "\n"
        + "original lang :"
        + original_language
        + "\n"
        + "title:"
        + title
        + "\n"
        + "overview :"
        + overview
        + "\n"
        + "release date : "
        + release_date
        + "\n"
        + "popularity : "
        + popularity
        + "\n"
        + "vote average : "
        + vote_average
        + "\n"
        + "vote count : "
        + vote_count
        + "\n";

    return string;
  }
}
