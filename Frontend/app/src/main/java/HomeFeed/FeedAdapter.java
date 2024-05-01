package HomeFeed;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.harmonizefrontend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.FeedDTO;
import UserInfo.UserSession;

/**
 * This adapter is responsible for creating and binding views for each item in the RecyclerView.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private List<FeedDTO> feedItems;
    private OnAddTrackListener onAddTrackListener;

    public interface OnAddTrackListener {
        void onAddTrack(FeedDTO feedItem);
    }

    /**
     * Constructor of the FeedAdapter class. Takes a List<FeedItem> parameter and assigns it to the feedItems instance variable.
     * @param feedItems
     */
    public FeedAdapter(List<FeedDTO> feedItems, OnAddTrackListener onAddTrackListener) {
        this.feedItems = feedItems;
        this.onAddTrackListener = onAddTrackListener;
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
        holder.bind(feedItem);

        // Set click listener for the add button
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddTrackListener != null) {
                    onAddTrackListener.onAddTrack(feedItem);
                }
            }
        });
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
        private ImageView albumCoverImageView;
        private TextView feedItemTypeTextView;
        private TextView artistNameTextView;
        private TextView albumNameTextView;
        private TextView trackNameTextView;
        private ImageButton addButton;

        private RequestQueue requestQueue;

        FeedViewHolder(@NonNull View itemView) {

            super(itemView);

            albumCoverImageView = itemView.findViewById(R.id.albumCoverImageView);
            feedItemTypeTextView = itemView.findViewById(R.id.feedItemTypeTextView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
            albumNameTextView = itemView.findViewById(R.id.albumNameTextView);
            trackNameTextView = itemView.findViewById(R.id.trackNameTextView);
            addButton = itemView.findViewById(R.id.addButton);

            requestQueue = Volley.newRequestQueue(itemView.getContext());
        }

        void bind(FeedDTO feedItem) {
            feedItemTypeTextView.setText(feedItem.getData().getItem().getType().replaceAll("_", " "));

            // Make API request to retrieve track information
            String trackUrl = "http://coms-309-032.class.las.iastate.edu:8080/music/songs/" + feedItem.getData().getItem().getSong().getId();
            JsonObjectRequest trackRequest = new JsonObjectRequest(Request.Method.GET, trackUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Parse the JSON response and update views with track information
                                String trackName = response.getString("title");
                                JSONObject artist = response.getJSONObject("artist");
                                String artistName = artist.getString("name");
                                JSONObject albumObject = response.getJSONObject("album");
                                String albumName = albumObject.getString("name");
                                String albumImage = albumObject.getString("imageUrl");

                                trackNameTextView.setText(trackName);
                                artistNameTextView.setText(artistName);
                                albumNameTextView.setText(albumName);

                                // Load album cover image using Glide
                                Glide.with(itemView)
                                        .load(albumImage)
                                        .placeholder(R.drawable.placeholder_image)
                                        .error(R.drawable.error_image)
                                        .into(albumCoverImageView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing error
                                trackNameTextView.setText("Track not found");
                                artistNameTextView.setText("Artist not found");
                                albumNameTextView.setText("Album not found");
                                albumCoverImageView.setImageResource(R.drawable.error_image);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    Log.d("SearchFragment", "JWTtoken: " + UserSession.getInstance().getJwtToken());
                    headers.put("Authorization", UserSession.getInstance().getJwtToken());
                    return headers;
                }
            };

            // Add the request to the RequestQueue
            requestQueue.add(trackRequest);
        }
    }

}
