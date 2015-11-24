package com.example.harshikesh.nanodegree_02_spotifier.model;

import java.util.ArrayList;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class ResultModel {

   ArrayList<MovieResutModel> results = new ArrayList<MovieResutModel>();

    public ArrayList<MovieResutModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieResutModel> results) {
        this.results = results;
    }
}
