package com.example.harmonizefrontend;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Connections.WebSocketListener;
import Connections.WebSocketManagerFeed;
import HomeFeed.FeedAdapter;
import DTO.FeedDTO;
import HomeFeed.FeedEnum;
import HomeFeed.FeedRequest;

import UserInfo.UserSession;

/**
 * A fragment that displays the main feed, showing music news for the user.
 * Uses a WebSocket connection to receive feed data from the server.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements WebSocketListener, FeedAdapter.OnAddTrackListener {
    private RecyclerView feedRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FeedAdapter feedAdapter;
    private List<FeedDTO> feedItems;
    private WebSocket webSocket;
    private static final String WEB_SOCKET_URL = "wss://coms-309-032.class.las.iastate.edu:8443/feed";
    private String username, password;
    private RequestQueue mQueue;
    private static final int LIMIT = 10;
    private int offset = 0;
    private int feedSize;
    private boolean isLoading = false;
    private boolean hasMore = true;

    private FloatingActionButton createPostFab;
    private AlertDialog postInputDialog;
    /**
     * required empty constructor
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is created.
     * Initializes the WebSocket connection and sets the WebSocket listener.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            username = navBar.username;
            password = navBar.password;
            mQueue = navBar.mQueue;
        } else {
            Log.e("msg", "navBar is null, JWT token not set");
        }

        // Connect to WebSocket
        String serverURL = WEB_SOCKET_URL + "?username=" + username + "&password=" + password;
        Log.e("msg", "Before WebSocket connection");
        WebSocketManagerFeed.getInstance().connectWebSocket(serverURL);
        WebSocketManagerFeed.getInstance().setWebSocketListener(this);
    }

    /**
     * Called to inflate the layout for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        feedRecyclerView = view.findViewById(R.id.feedRecyclerView);
        feedItems = new ArrayList<>();
        feedAdapter = new FeedAdapter(feedItems, this);
        feedRecyclerView.setAdapter(feedAdapter);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
            }
        });

        feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCountLoaded = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && hasMore && (visibleItemCount + firstVisibleItemPosition) >= totalItemCountLoaded) {
                    loadMoreItems();
                }
            }
        });

        createPostFab = view.findViewById(R.id.createPostFab);
        createPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostInputDialog();
            }
        });

        return view;
    }

    /**
     * Loads more feed items when the user scrolls to the end of the list.
     */
    private void loadMoreItems() {

        isLoading = true;
        offset += LIMIT;
        FeedRequest request = new FeedRequest(FeedEnum.NEW_PAGE, LIMIT, offset);
        sendWebSocketRequest(request);

    }

    /**
     * Sends a WebSocket request to fetch feed items.
     *
     * @param request
     */
    private void sendWebSocketRequest(FeedRequest request) {

        Gson gson = new Gson();
        String requestJson = gson.toJson(request);
        WebSocketManagerFeed.getInstance().sendMessage(requestJson);

    }

    /**
     * Called when the WebSocket connection is opened.
     *
     * @param handshakedata The handshake data received from the server.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

        Log.d("WebSocket", "WebSocket opened");

    }

    /**
     * Called when a message is received from the WebSocket server.
     *
     * @param message The message received from the server.
     */
    @Override
    public void onWebSocketMessage(String message) {

        getActivity().runOnUiThread(() -> {

            Log.d("WebSocket", "Received message: " + message);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
            String messageType = jsonObject.get("type").getAsString();

            switch (messageType) {
                case "com.fasterxml.jackson.databind.node.ObjectNode":

                    feedSize = jsonObject.getAsJsonObject("data").get("size").getAsInt();
                    loadInitialItems();

                    break;
                case "harmonize.DTOs.FeedDTO":

                    FeedDTO feedDTO = gson.fromJson(jsonObject, FeedDTO.class);
                    updateFeedItems(feedDTO);

                    break;
                case "harmonize.Enum.FeedEnum":

                    String data = jsonObject.get("data").getAsString();
                    if (data.equals("NEW_POST")) {
                        Toast.makeText(getActivity(), "A friend just posted!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:

                    Log.e("WebSocket", "Unknown message type: " + messageType);

                    break;
            }
        });
    }

    /**
     * Loads the initial set of feed items.
     */
    public void loadInitialItems() {

        isLoading = true;
        offset = 0;
        FeedRequest request = new FeedRequest(FeedEnum.NEW_PAGE, LIMIT, offset);
        sendWebSocketRequest(request);

    }

    /**
     * Updates the feed items with the received FeedDTO data.
     *
     * @param feedDTO The FeedDTO object containing the feed data.
     */
    public void updateFeedItems(FeedDTO feedDTO) {
        if (feedDTO.getData() != null && feedDTO.getData().getItem() != null) {
            if (offset == 0) {
                feedItems.clear();
            }
            feedItems.add(feedDTO);
            feedAdapter.notifyDataSetChanged();
        }

        offset += 1;
        isLoading = false;
        hasMore = offset < feedSize;
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Refreshes the feed by clearing the existing items and fetching new ones.
     */
    private void refreshFeed() {

        isLoading = true;
        offset = 0;
        FeedRequest request = new FeedRequest(FeedEnum.REFRESH, LIMIT, offset);
        sendWebSocketRequest(request);

    }

    @Override
    public void onAddTrack(FeedDTO feedItem) {
        addTrackToLikedSongs(feedItem);
    }

    private void addTrackToLikedSongs(FeedDTO feedItem) {
        // Get the track ID from the feedItem
        String trackId = feedItem.getData().getItem().getSong().getId();

        // Make an API request to add the track to the user's liked songs
        String url = UserSession.getInstance().getURL() + "/users/songs/" + trackId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Track added successfully
                        Toast.makeText(getActivity(), "Track added to liked songs", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Toast.makeText(getActivity(), "Failed to add track to liked songs", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };

        mQueue.add(stringRequest);
    }

    private void showPostInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.layout_post_input, null);
        EditText postInputEditText = view.findViewById(R.id.postInputEditText);
        Button postButton = view.findViewById(R.id.postButton);

        builder.setView(view);
        postInputDialog = builder.create();

        postButton.setOnClickListener(v -> {
            String postText = postInputEditText.getText().toString().trim();
            if (!postText.isEmpty()) {
                createPost(postText);
                postInputDialog.dismiss();
            }
        });

        postInputDialog.show();
    }

    private void createPost(String postText) {
        String url = UserSession.getInstance().getURL() + "/users/posts";

        JSONObject postData = new JSONObject();
        try {
            postData.put("post", postText);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Posted successfully
                        Toast.makeText(getActivity(), "Successfully Posted!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Toast.makeText(getActivity(), "Failed to post.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };

        mQueue.add(jsonObjectRequest);
    }

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param code   The status code indicating the reason for closure.
     * @param reason The reason for the closure.
     * @param remote Whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d("WebSocket", "WebSocket closed");
    }

    /**
     * Called when an error occurs in the WebSocket communication.
     *
     * @param ex The exception representing the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("WebSocket", "WebSocket error", ex);
    }

}