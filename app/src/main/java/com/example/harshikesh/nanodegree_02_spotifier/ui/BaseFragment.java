package com.example.harshikesh.nanodegree_02_spotifier.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by harshikesh.kumar on 07/02/16.
 */
public class BaseFragment extends Fragment {


  protected boolean isInternetAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  protected void showSnackbar(View view, String msg) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
  }
}
