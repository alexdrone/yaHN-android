package com.example.alexusbergo.yahn;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Collection {

  public interface Observer {
    /** The collection passed as argument changed its status. */
    public void onCollectionChange(@NonNull Collection collection);
  }

  public interface NetworkDelegate {
    /** Performs the actual fetch request for this collection. */
    public void fetchCollection(@NonNull Collection collection,
                                @NonNull Callback<List<Entry>, FetchError> callback);
  }

  /** The current state of the network request associated to this collection. */
  public enum FetchStatus {
    FETCHED, FETCHING, NOT_FETCHED, DISPOSED
  }

  /** Error for the last network request (if applicable). */
  public enum FetchError {
    NONE, NETWORK_REQUEST_FAILED, UNSPECIFIED, UNHANDLED_COLLECTION;
  }

  public List<Entry> getResults() {
    return this.results;
  }

  public FetchError getError() {
    return error;
  }

  public FetchStatus getStatus() {
    return status;
  }

  public float getProgress() {
    return progress;
  }

  /**
   * Fetches the remote collection.
   * Note: - No-op if 'networkDelegate' is null.
   */
  public void fetch() {
    if (this.networkDelegate == null
        || this.getStatus() == FetchStatus.FETCHING
        || this.getStatus() == FetchStatus.DISPOSED)
      return;

    this.status = FetchStatus.FETCHING;
    this.notifyObservers();

    final Collection collection = this;
    this.networkDelegate.fetchCollection(this, new Callback<List<Entry>, FetchError>() {
      @Override
      public void onResponse(List<Entry> object) {
        collection.results = object;
        collection.status = FetchStatus.FETCHED;
        collection.error = FetchError.NONE;
        collection.notifyObservers();
      }

      @Override
      public void onError(FetchError error) {
        collection.results = new ArrayList<Entry>();
        collection.status = FetchStatus.NOT_FETCHED;
        collection.error = error;
        collection.notifyObservers();
      }

      @Override
      public void onProgress(List<Entry> object, float progress) {
        collection.results = object;
        collection.progress = progress;
        collection.notifyObservers();
      }
    });
  }

  public void dispose() {
    this.status = FetchStatus.DISPOSED;
    this.results = new ArrayList<Entry>();
    this.observers = new ArrayList<Observer>();
    this.networkDelegate = null;
  }

  private void notifyObservers() {
    for (Observer obj: this.observers)
      obj.onCollectionChange(this);
  }

  /** Registers an observer for this collection. */
  public void addObserver(Observer observer) {
    if (observer == null)
      return;
    if (this.observers.contains(observer))
      return;
    this.observers.add(observer);
    if (this.status == FetchStatus.NOT_FETCHED)
      this.fetch();
  }

  /** Deregister the object passed as argument as an observer for this collection.*/
  public void removeObserver(Observer observer) {
    if (observer == null)
      return;
    this.observers.remove(observer);
  }

  Collection(@NonNull NetworkDelegate networkDelegate) {
    this.networkDelegate = networkDelegate;
  }

  /**
   * The buffer with the current network response.
   * Note: This collection is not thread safe.
   */
  @NonNull
  private List<Entry> results = new ArrayList<>();

  /** Error for the last network request (if applicable). */
  private FetchError error = FetchError.NONE;

  /** The current state of the network request associated to this collection. */
  private FetchStatus status;

  /** The collection's observers currently registered. */
  private List<Observer> observers = new LinkedList<>();

  /** The delegate that is in charge of performing the network request. */
  private NetworkDelegate networkDelegate;

  /** The download progress. */
  private float progress;

  @Override
  public String toString() {
    return
        "Collection {results=" + results.size() + ", error=" + error + ", status=" + status +
        ", progress=" + progress +'}';
  }
}
