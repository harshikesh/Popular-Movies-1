package com.example.harshikesh.nanodegree_02_spotifier.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.harshikesh.nanodegree_02_spotifier.R;

public class MainActivity extends AppCompatActivity {

  private static String TAG = MainActivity.class.getSimpleName();

  public boolean mTwoPane = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.container, new MainFragment())
          .commit();
    }
    if (findViewById(R.id.detail_container) != null) {
      Log.d(TAG, "TwoPane mode");
      mTwoPane = true;
      if (savedInstanceState == null) {
        // Todo: select a movie and initialize Detail tabs view
      }
    } else {
      mTwoPane = false;
      getSupportActionBar().setElevation(0f);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
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
