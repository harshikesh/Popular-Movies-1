package com.example.harshikesh.nanodegree_02_spotifier.util;

import android.graphics.Bitmap;
import android.support.v4.util.Pools;
import android.support.v7.graphics.Palette;
import android.util.Log;
import com.squareup.picasso.Transformation;

/**
 * Created by harshikesh.kumar on 01/02/16.
 */
public final class PaletteTransformation implements Transformation {
  private static final Pools.Pool<PaletteTransformation> POOL = new Pools.SynchronizedPool<>(5);

  /**
   * Factory.
   */
  public static PaletteTransformation getInstance() {
    PaletteTransformation instance = POOL.acquire();
    return instance != null ? instance : new PaletteTransformation();
  }

  private Palette palette;

  private PaletteTransformation() {
  }

  public Palette extractPaletteAndRelease() {
    Palette palette = this.palette;
    if (palette == null) {
      Log.e("TRANSFORMATION", "Transformation was not run");
      return null;
    }
    this.palette = null;
    POOL.release(this);
    return palette;
  }

  @Override public Bitmap transform(Bitmap source) {
    if (palette != null) {
      throw new IllegalStateException("Instances may only be used once.");
    }
    palette =
        Palette.generate(source); //This is depracated, in the future favor ColorUtils instead.
    return source;
  }

  @Override public String key() {
    return "";
  }
}