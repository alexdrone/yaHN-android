package com.example.alexusbergo.yahn;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

/** Download a JSON resource on a background thread. */
abstract public class DownloadTask<T> extends AsyncTask<String, Void, T> {

  /** Manages the completion of a JSONDownloadTask. */
  public static abstract class Delegate<T> {

    /** The download task has been successful. */
    public abstract  void onSuccess(@NonNull T object);

    /** An exception has been raised while attempting to download the desired JSON. */
    public abstract void onError(@NonNull Exception exception);
  }

  /** The exception raised (if applicable). */
  public Exception getException() {
    return exception;
  }

  /** Store for the possible exception. */
  private Exception exception;

  /** The result object. */
  private T object;

  /** The task completion delegate. */
  private Delegate<T> delegate;

  public DownloadTask() {
  }

  public DownloadTask(@NonNull String url, @NonNull Delegate<T> delegate) {
    this.delegate = delegate;
    this.execute(url);
  }

  /** Parse the raw string response into the desired representation. */
  abstract public T parseResponse(String response) throws Exception;

  /** Invoked on the background thread. */
  public T doInBackground(String... urls) {
    try {
      String response = NetworkService.downloadUrl(urls[0]);
      this.object = parseResponse(response);
      return this.object;

    } catch (Exception e) {
      this.exception = e;
      return null;
    }
  }

  /** Invoked on the UI thread after the background computation finishes. */
  public void onPostExecute(T object) {
    if (object != null)
      delegate.onSuccess(this.object);
    else
      delegate.onError(this.exception);
  }
}