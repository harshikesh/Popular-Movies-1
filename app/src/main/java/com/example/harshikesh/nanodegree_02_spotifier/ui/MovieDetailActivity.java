package com.example.harshikesh.nanodegree_02_spotifier.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.harshikesh.nanodegree_02_spotifier.R;

/**
 * Created by harshikesh.kumar on 22/11/15.
 */
public class MovieDetailActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    if (savedInstanceState == null) {

      MovieDetailFragment fragment = new MovieDetailFragment();
      FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.detail_container, fragment);
      fragment.setArguments(getIntent().getExtras());
      ft.commit();
    }
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    if (id == R.id.action_settings) {
      Intent i = new Intent(this, SettingsActivity.class);
      startActivity(i);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
