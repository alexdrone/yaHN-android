package com.example.alexusbergo.yahn;

import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class NetworkService {

  /**
   * Given a URL, sets up a connection and gets the HTTP response body from the server.
   * If the network request is successful, it returns the response body in String form. Otherwise,
   * it will throw an IOException.
   */
   public static String downloadUrl(@NonNull String urlString) {
    InputStream stream = null;
    HttpsURLConnection connection = null;
    String result = null;
    try {
      URL url = new URL(urlString);
      connection = (HttpsURLConnection) url.openConnection();
      connection.setReadTimeout(3000);
      connection.setConnectTimeout(3000);
      connection.setRequestMethod("GET");
      connection.setDoInput(true);
      connection.connect();
      int responseCode = connection.getResponseCode();
      if (responseCode != HttpsURLConnection.HTTP_OK)
        throw new IOException("HTTP error code: " + responseCode);
      stream = connection.getInputStream();
      if (stream != null)
        result = readStream(stream);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (stream != null)
        try {
          stream.close();
        } catch (Exception e) { /* log warning. */ }
      if (connection != null)
        connection.disconnect();
    }
    return result;
  }

  private static long copy(InputStream input, OutputStream output) throws IOException {
    byte[] buffer = new byte[1024*4];
    long count = 0;
    int n = 0;
    while (-1 != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }

  /**
   * Converts the contents of an InputStream to a String.
   */
  private static String readStream(InputStream input) {
    try {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy(input, output);
      return output.toString();
    } catch (IOException e) {
      return "";
    }
  }

  public static void assertIsOnMainThread() {
    if (Looper.getMainLooper().getThread() != Thread.currentThread())
      throw new AssertionError("Expected to run on the main thread.");
  }
}
