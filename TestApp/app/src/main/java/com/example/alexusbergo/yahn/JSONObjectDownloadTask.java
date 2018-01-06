package com.example.alexusbergo.yahn;


import android.support.annotation.NonNull;

import org.json.JSONObject;

public final class JSONObjectDownloadTask extends DownloadTask<JSONObject> {

  public JSONObjectDownloadTask(@NonNull String url, @NonNull Delegate<JSONObject> delegate) {
    super(url, delegate);
  }

    @Override
  public JSONObject parseResponse(String response) throws Exception {
      return new JSONObject(response);
  }
}
