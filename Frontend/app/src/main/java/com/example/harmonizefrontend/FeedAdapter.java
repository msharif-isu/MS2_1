package com.example.harmonizefrontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This adapter is responsible for creating and binding views for each item in the RecyclerView.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<FeedDTO> feedItems;

    /**
     * Constructor of the FeedAdapter class. Takes a List<FeedItem> parameter and assigns it to the feedItems instance variable.
     * @param feedItems
     */
    public FeedAdapter(List<FeedDTO> feedItems) {
        this.feedItems = feedItems;
    }

    /**
     * This method is called by the RecyclerView when it needs to create a new view holder for an item.
     * It inflates the item_feed layout using the LayoutInflater and creates a new instance of the FeedViewHolder class, passing the inflated view as a parameter.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);

        return new FeedViewHolder(view);
    }

    /**
     * This method is called by the RecyclerView to bind data to the views of a view holder at a specific position.
     * It retrieves the FeedItem object at the given position from the feedItems list.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedDTO feedItem = feedItems.get(position);

        //Bind the data to the views
    }

    /**
     * Returns total number of items in the feedItems list.
     * @return
     */
    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    /**
     * This constructor takes the inflated view item as a parameter and initializes the views.
     * This class represents a single item view in the RecyclerView and holds references to the views within that item view.
     */
    static class FeedViewHolder extends RecyclerView.ViewHolder {
        // Declare views

        FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
        }

    }

}
