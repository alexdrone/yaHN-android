package com.example.alexusbergo.yahn;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public final class Entry implements Serializable {

  private final static long UNDEFINED_IDENTIFIER = -1;
  private final static String UNDEFINED_STRING = "undefined";

  private final static String TAG_ID = "id";
  private final static String TAG_BY = "by";
  private final static String TAG_TIME = "time";
  private final static String TAG_TEXT = "text";
  private final static String TAG_URL = "url";
  private final static String TAG_SCORE = "score";
  private final static String TAG_TITLE = "title";
  private final static String TAG_TYPE = "type";

  /**
   * The type of item. One of "job", "story", "comment", "poll", or "pollopt".
   */
  public enum Type {
    JOB, STORY, COMMENT, POLL, POLLOPT, UNDEFINED
  }

  /**
   * Returns the item's unique id.
   */
  @Contract(pure = true)
  public long getId() {
    return id;
  }

  /**
   * Set the item's unique id.
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Returns the username of the item's author.
   */
  @NonNull
  @Contract(pure = true)
  public String getBy() {
    return by;
  }

  /**
   * Sets the username of the item's author.
   */
  public void setBy(@NonNull String by) {
    this.by = by;
  }

  /**
   * Returns the creation date of the item.
   */
  @NonNull
  @Contract(pure = true)
  public Date getTime() {
    return time;
  }

  /**
   * Sets the creation date of the item.
   */
  public void setTime(@NonNull Date time) {
    this.time = time;
  }

  /**
   * Returns the comment, story or poll text. HTML.
   */
  @NonNull
  @Contract(pure = true)
  public String getText() {
    return text;
  }

  /**
   * Sets the comment, story or poll text. HTML.
   */
  public void setText(@NonNull String text) {
    this.text = text;
  }

  /**
   * Returns URL of the story.
   */
  @NonNull
  @Contract(pure = true)
  public String getUrl() {
    return url;
  }

  /**
   * Sets the URL of the story.
   */
  public void setUrl(@NonNull String url) {
    this.url = url;
  }

  /**
   * Returns the story's score, or the votes for a pollopt.
   */
  @Contract(pure = true)
  public int getScore() {
    return score;
  }

  /**
   * Sets the story's score, or the votes for a pollopt.
   */
  public void setScore(int score) {
    this.score = score;
  }

  /**
   * Returns the title of the story, poll or job.
   */
  @NonNull
  @Contract(pure = true)
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the story, poll or job.
   */
  public void setTitle(@NonNull String title) {
    this.title = title;
  }

  /**
   * Returns the  title of the story, poll or job.
   */
  @Contract(pure = true)
  public Type getType() {
    return type;
  }

  /**
   * Sets the title of the story, poll or job.
   */
  public void setType(Type type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Entry entry = (Entry) o;

    if (id != entry.id) return false;
    if (score != entry.score) return false;
    if (!by.equals(entry.by)) return false;
    if (!time.equals(entry.time)) return false;
    if (!text.equals(entry.text)) return false;
    if (!url.equals(entry.url)) return false;
    if (!title.equals(entry.title)) return false;
    return type == entry.type;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + by.hashCode();
    result = 31 * result + time.hashCode();
    result = 31 * result + text.hashCode();
    result = 31 * result + url.hashCode();
    result = 31 * result + title.hashCode();
    result = 31 * result + score;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }

  /**
   * The item's unique id.
   */
  private long id = UNDEFINED_IDENTIFIER;

  /**
   * The username of the item's author.
   */
  @NonNull
  private String by = UNDEFINED_STRING;

  /**
   * Creation date of the item.
   */
  @NonNull
  private Date time = new Date();

  /**
   * The comment, story or poll text. HTML.
   */
  @NonNull
  private String text = UNDEFINED_STRING;

  /**
   * The URL of the story.
   */
  @NonNull
  private String url = UNDEFINED_STRING;

  /**
   * The title of the story, poll or job.
   */
  @NonNull
  private String title = UNDEFINED_STRING;

  /**
   * The story's score, or the votes for a pollopt.
   */
  private int score = 0;

  /**
   * The type of item. One of "job", "story", "comment", "poll", or "pollopt".
   */
  private Type type = Type.UNDEFINED;

  public Entry(@NonNull JSONObject json) {
    try {
      if (json.has(TAG_ID))
        this.id = json.getLong(TAG_ID);
      if (json.has(TAG_BY))
        this.by = json.getString(TAG_BY);
      if (json.has(TAG_TIME))
        this.time = new Date(json.getLong(TAG_TIME)*1000);
      if (json.has(TAG_URL))
        this.url = json.getString(TAG_URL);
      if (json.has(TAG_SCORE))
        this.score = json.getInt(TAG_SCORE);
      if (json.has(TAG_TITLE))
        this.title = json.getString(TAG_TITLE);
      if (json.has(TAG_TEXT))
        this.text = json.getString(TAG_TEXT);
      if (json.has(TAG_TYPE)) {
        String type = json.getString(TAG_TYPE);
        if (type.equals("poll"))
          this.type = Type.POLL;
        else if (type.equals("comment"))
          this.type = Type.COMMENT;
        else if (type.equals("job"))
          this.type = Type.JOB;
        else if (type.equals("story"))
          this.type = Type.STORY;
        else if (type.equals("pollopt"))
          this.type = Type.POLLOPT;
      }

    } catch (JSONException e) {
      this.type = Type.UNDEFINED;
    }
  }
}
