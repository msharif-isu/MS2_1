package HomeFeed;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.harmonizefrontend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.FeedDTO;
import UserInfo.UserSession;

/**
 * This adapter is responsible for creating and binding views for each item in the RecyclerView.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_POST = 1;
    private List<FeedDTO> feedItems;
    private static OnAddTrackListener onAddTrackListener;

    private RequestQueue mQueue = UserSession.getInstance().getmQueue();

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

    public int getItemViewType(int position) {
        FeedDTO feedItem = feedItems.get(position);
        if (feedItem.getData().getItem().getType().equals("POST")) {
            return VIEW_TYPE_POST;
        } else {
            return VIEW_TYPE_ITEM;
        }
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_POST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_item, parent, false);
            return new PostViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
            return new FeedViewHolder(view);
        }
    }

    /**
     * This method is called by the RecyclerView to bind data to the views of a view holder at a specific position.
     * It retrieves the FeedItem object at the given position from the feedItems list.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedDTO feedItem = feedItems.get(position);
        holder.bind(feedItem);
    }

    /**
     * Returns total number of items in the feedItems list.
     * @return
     */
    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    // ViewHolder class
    abstract static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bind(FeedDTO feedItem);
    }

    public static class PostViewHolder extends ViewHolder {
        private ImageView profileImageView;
        private TextView usernameTextView;
        private TextView postTextView;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
        }

        void bind(FeedDTO feedItem) {
            int userId = feedItem.getData().getItem().getUser().getId();

            // Make API request to retrieve user information
            String userUrl = UserSession.getInstance().getURL() + "/users/id/" + userId;
            JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, userUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Parse the JSON response and update views with user information
                                String username = response.getString("username");
                                //String profileImageUrl = response.getString("profileImageUrl");

                                usernameTextView.setText(username);

                                // Load profile image using Glide
//                                Glide.with(itemView.getContext())
//                                        .load(profileImageUrl)
//                                        .into(profileImageView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing error
                                usernameTextView.setText("User not found");
                                profileImageView.setImageResource(R.drawable.placeholder_image);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            // Handle network error
                            usernameTextView.setText("Failed to load user");
                            profileImageView.setImageResource(R.drawable.placeholder_image);
                        }
                    }) {
                @Override
                public HashMap<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", UserSession.getInstance().getJwtToken());
                    return headers;
                }
            };

            // Add the request to the RequestQueue
            Volley.newRequestQueue(itemView.getContext()).add(userRequest);

            postTextView.setText(feedItem.getData().getItem().getPost().getPost());
        }
    }

    /**
     * This constructor takes the inflated view item as a parameter and initializes the views.
     * This class represents a single item view in the RecyclerView and holds references to the views within that item view.
     */
    public static class FeedViewHolder extends ViewHolder {
        private TextView feedItemTypeTextView;
        private TextView artistNameTextView;
        private TextView albumNameTextView;
        private TextView trackNameTextView;
        private ImageButton addButton;
        private ImageButton albumButton;
        private WebView playSongView;

        private RequestQueue requestQueue;

        FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            feedItemTypeTextView = itemView.findViewById(R.id.feedItemTypeTextView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
            albumNameTextView = itemView.findViewById(R.id.albumNameTextView);
            trackNameTextView = itemView.findViewById(R.id.trackNameTextView);
            addButton = itemView.findViewById(R.id.addButton);
            albumButton = itemView.findViewById(R.id.albumCoverImageButton);

            playSongView = itemView.findViewById(R.id.webView);

            playSongView.getSettings().setJavaScriptEnabled(true);

            requestQueue = Volley.newRequestQueue(itemView.getContext());
        }

        void bind(FeedDTO feedItem) {
            feedItemTypeTextView.setText(feedItem.getData().getItem().getType().replaceAll("_", " "));

            // Make API request to retrieve track information
            String trackUrl = UserSession.getInstance().getURL() + "/music/songs/" + feedItem.getData().getItem().getSong().getId();
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
                                        .into(albumButton);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing error
                                trackNameTextView.setText("Track not found");
                                artistNameTextView.setText("Artist not found");
                                albumNameTextView.setText("Album not found");
                                albumButton.setImageResource(R.drawable.error_image);
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
            Volley.newRequestQueue(itemView.getContext()).add(trackRequest);

            // Set click listener for the add button
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddTrackListener != null) {
                        onAddTrackListener.onAddTrack(feedItem);
                        addButton.setImageResource(R.drawable.green_checkbox);
                    }
                }
            });
            albumButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playSongView.getVisibility() == View.GONE) {
                        // Expand the WebView and load the URL
                        playSongView.setVisibility(View.VISIBLE);
                        playSongView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        playSongView.requestLayout();
                        playSongView.loadUrl("https://open.spotify.com/embed/track/" + feedItem.getData().getItem().getSong().getId() + "?utm_source=generator&theme=0");
                    } else {
                        // Collapse the WebView
                        playSongView.setVisibility(View.GONE);
                        playSongView.getLayoutParams().height = 0;
                        playSongView.requestLayout();
                    }
                }
            });
        }
    }

    private void makeImageRequest(int id, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(
                UserSession.getInstance().getURL() + "/users/icons/" + id,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView

                        imageView.setImageBitmap(response);
                        Log.d("Image", response.toString());
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body = "";
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                        Log.e("Image", body);
                        Log.e("Image", statusCode);
                        if (statusCode.equals("404")) {
                            imageView.setImageResource(R.drawable.ic_launcher_foreground);
                        }
                    }
                }

        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        mQueue.add(imageRequest);
    }

}
