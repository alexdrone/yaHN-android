package com.example.alexusbergo.yahn;

import android.support.annotation.NonNull;

import org.json.JSONArray;

public final class JSONArrayDownloadTask extends DownloadTask<JSONArray> {

  public JSONArrayDownloadTask(@NonNull String url, @NonNull Delegate<JSONArray> delegate) {
    super(url, delegate);
  }

  @Override
  public JSONArray parseResponse(String response) throws Exception {
    JSONArray array = new JSONArray(response);
    return array;
  }
}