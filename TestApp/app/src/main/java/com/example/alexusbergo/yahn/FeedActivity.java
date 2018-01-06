package com.example.alexusbergo.yahn;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexusbergo.testapp.R;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements Collection.Observer {

  public Collection getCollection() {
    return collection;
  }

  private Collection collection;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ApplicationContext appContet = ApplicationContext.defaultContext;
    this.collection = appContet.getHackerNewsService().getTopStories();

    setContentView(R.layout.activity_counter);

    RecyclerView stories = (RecyclerView) findViewById(R.id.stories);
    stories.setAdapter(new FeedAdapter());
    stories.setLayoutManager(new LinearLayoutManager(this));
    stories.getRecycledViewPool().setMaxRecycledViews(0, 0);

    this.collection.addObserver(this);
    this.collection.fetch();
    onStateChange();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.getCollection().removeObserver(this);
  }


  @Override
  public void onCollectionChange(@NonNull Collection collection) {
    onStateChange();
  }

  @SuppressLint("DefaultLocale")
  protected void onStateChange() {
    RecyclerView stories = (RecyclerView) findViewById(R.id.stories);
    stories.getAdapter().notifyDataSetChanged();
  }

  // Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
  public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    /**
     * Provide a direct reference to each of the views within a data item
     * Used to cache the views within the item layout for fast access
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
      public TextView titleTextView;
      public TextView urlTextView;

      public ViewHolder(View itemView) {
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.entry_title);
        urlTextView = (TextView) itemView.findViewById(R.id.entry_url);
      }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      Entry entry = getCollection().getResults().get(position);
      holder.titleTextView.setText(entry.getTitle());
      holder.urlTextView.setText(entry.getUrl());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      Context context = parent.getContext();
      LayoutInflater inflater = LayoutInflater.from(context);
      // Inflate the custom layout.
      View contactView = inflater.inflate(R.layout.entry_item, parent, false);
      // Return a new holder instance.
      ViewHolder viewHolder = new ViewHolder(contactView);
      return viewHolder;
    }

    @Override
    public int getItemCount() {
      return getCollection().getResults().size();
    }
  }

}
