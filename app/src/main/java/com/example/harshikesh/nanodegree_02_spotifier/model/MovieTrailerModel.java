package com.example.harshikesh.nanodegree_02_spotifier.model;

import java.util.ArrayList;

/**
 * Created by harshikesh.kumar on 01/02/16.
 */
//{"id":9647,
//    "results":
//    [{"id":"533ec667c3a368544800174e",
//    "iso_639_1":"en",
//    "key":"J11NIQJdO5Q",
//    "name":"Scrooged (1988)",
//    "site":"YouTube",
//    "size":360,
//    "type":"Trailer"}]
//    }

public class MovieTrailerModel {

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ArrayList<TrailerModel> getResults() {
    return results;
  }

  public void setResults(ArrayList<TrailerModel> results) {
    this.results = results;
  }

  String id="id";
  ArrayList<TrailerModel> results;

}
