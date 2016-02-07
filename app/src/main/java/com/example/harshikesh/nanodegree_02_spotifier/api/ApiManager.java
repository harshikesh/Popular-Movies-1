package com.example.harshikesh.nanodegree_02_spotifier.api;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by harshikesh.kumar on 20/11/15.
 */
public class ApiManager {

    //Key to get access TMDB
    public static String KEY="26cff5f3b8b7abdc8cae461878fe1835";

    //Base Url for TMDB
    public static String IMAGE_BASE_URL="http://image.tmdb.org/t/p/w185";
    public static String IMAGE_BASE_URL_BACKDROP="http://image.tmdb.org/t/p/w780";

    public static String VIDEO_BASE_URL="https://www.youtube.com/watch?v=";

    private  static ApiManager mApiManager;
    private ApiManager() {
    }

    public static ApiManager getInstance(){
        if(mApiManager == null) {
            mApiManager =  new ApiManager();
        }
       return mApiManager;
    }

    public final static String API_BASE_ENDPOINT = "https://api.themoviedb.org/3";

    public static final String PARAM_API_KEY = "api_key";

    private RestAdapter mRestAdapter;
    public RestAdapter getRestAdapter() {
        if (mRestAdapter == null) {
            RestAdapter.Builder builder = new RestAdapter.Builder();

            builder.setEndpoint(API_BASE_ENDPOINT);
            builder.setConverter(new GsonConverter(ApiHelper.getGsonBuilder().create()));
            builder.setRequestInterceptor(new RequestInterceptor() {
                public void intercept(RequestFacade requestFacade) {
                    requestFacade.addQueryParam(PARAM_API_KEY, KEY);
                }
            });
            mRestAdapter = builder.build();
        }

        return mRestAdapter;
    }
}
