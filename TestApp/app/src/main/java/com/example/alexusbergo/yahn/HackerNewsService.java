package com.example.alexusbergo.yahn;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HackerNewsService extends NetworkService implements Collection.NetworkDelegate {

  private static final String REST_BASE = "https://hacker-news.firebaseio.com/v0/";
  private static String API_STORIES() { return REST_BASE + "beststories.json?print=pretty"; }
  private static String API_ENTRY(long id) { return REST_BASE + "item/"+id+".json?print=pretty"; }

  public Collection getTopStories() {
    return topStories;
  }

  /** Returns the currently fetched top stories.*/
  private Collection topStories = new Collection(this);

  @Override
  public void fetchCollection(@NonNull final Collection collection,
                              @NonNull final Callback<List<Entry>, Collection.FetchError> callback){
    assertIsOnMainThread();

    if (collection == this.topStories) {
      final List<Entry> result = new ArrayList<Entry>();
      final List<Integer> ids = new ArrayList<Integer>();

      // Parse the json response for a single item.
      final DownloadTask.Delegate<JSONObject> itemHandler = new DownloadTask.Delegate<JSONObject>(){
        @Override
        public void onSuccess(@NonNull JSONObject object) {
          assertIsOnMainThread();
          Entry entry = new Entry(object);
          result.add(entry);
          // Updates the progress.
          callback.onProgress(result, (float)result.size()/(float)ids.size());
          // If we fetched all of the ids we invoke the callback.
          if (ids.size() == result.size()) {
            callback.onResponse(result);
          }
        }
        @Override
        public void onError(@NonNull Exception exception) {
          assertIsOnMainThread();
          callback.onError(Collection.FetchError.NETWORK_REQUEST_FAILED);
        }
      };

      // Parse the ids from the list response.
      final DownloadTask.Delegate<JSONArray> listHanlder = new DownloadTask.Delegate<JSONArray>() {
        @Override
        public void onSuccess(@NonNull JSONArray object) {
          assertIsOnMainThread();
          try {
            for (int i = 0; i < object.length(); i++)
              ids.add(object.getInt(i));
            for (int i = 0; i < object.length(); i++)
              new JSONObjectDownloadTask(API_ENTRY(object.getLong(i)), itemHandler);
          } catch (Exception e) { /* No-op: skip */ }
        }
        @Override
        public void onError(@NonNull Exception exception) {
          assertIsOnMainThread();
          callback.onError(Collection.FetchError.NETWORK_REQUEST_FAILED);
        }
      };

      // Starts downloading the stories.
      new JSONArrayDownloadTask(API_STORIES(), listHanlder);

    } else {
      // This collection is not being exposed by the service.
      callback.onError(Collection.FetchError.UNHANDLED_COLLECTION);
    }
  }

}
