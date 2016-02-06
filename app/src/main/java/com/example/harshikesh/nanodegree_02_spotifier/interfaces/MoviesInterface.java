package com.example.harshikesh.nanodegree_02_spotifier.interfaces;

import com.example.harshikesh.nanodegree_02_spotifier.model.MovieTrailerModel;
import com.example.harshikesh.nanodegree_02_spotifier.model.ResultModel;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by harshikesh.kumar on 01/02/15.
 */
public interface MoviesInterface {

    /**
     * Get the videos (trailers, teasers, clips, etc...) for a specific movie id.
     *
     * @param tmdbId   TMDb id.
     * @param language <em>Optional.</em> ISO 639-1 code.
     */
    @GET("/movie/{id}/videos")
    void videos(
            @Path("id") String tmdbId,
            @Query("language") String language,
            Callback<MovieTrailerModel> videosCallback
    );


   /**
     * Get the list of popular movies on The Movie Database. This list refreshes every day.
     *
     * @param page     <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     */
    @GET("/movie/popular")
    void popular(
            @Query("page") Integer page,
            @Query("language") String language,
            Callback<ResultModel> results
    );

    /**
     * Get the list of top rated movies.
     *
     * @param page     <em>Optional.</em> Minimum value is 1, expected value is an integer.
     * @param language <em>Optional.</em> ISO 639-1 code.
     */
    @GET("/movie/top_rated")
    void topRated(
            @Query("page") Integer page,
            @Query("language") String language,
            Callback<ResultModel> results
    );

}
