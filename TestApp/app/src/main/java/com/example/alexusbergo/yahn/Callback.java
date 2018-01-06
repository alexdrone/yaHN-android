package com.example.alexusbergo.yahn;

import java.util.List;

public abstract class Callback<T, E> {
  /** Called after the resource T has been fetched. */
  abstract public void onResponse(T object);

  /** Called if an error occurred while fetching the desired resource. */
  abstract public void onError(E error);

  /** The current download progress. */
  abstract public void onProgress(List<Entry> object, float progress);
}
