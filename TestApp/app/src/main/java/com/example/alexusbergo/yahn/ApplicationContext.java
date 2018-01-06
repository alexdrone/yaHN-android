package com.example.alexusbergo.yahn;

public class ApplicationContext {

  public static ApplicationContext defaultContext = new ApplicationContext();

  public HackerNewsService getHackerNewsService() {
    return hackerNewsService;
  }

  /** The default HN service. */
  private final HackerNewsService hackerNewsService = new HackerNewsService();
}
