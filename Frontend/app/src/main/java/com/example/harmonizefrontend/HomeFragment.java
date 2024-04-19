package com.example.harmonizefrontend;

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

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import Connections.WebSocketListener;
import Connections.WebSocketManagerFeed;

/**
 * A fragment that displays the main feed, showing music news for the usser.
 * Uses a WebSocket connection to receive feed data from the server.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements WebSocketListener {
    private RecyclerView feedRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FeedAdapter feedAdapter;
    private List<FeedDTO> feedItems;
    private WebSocket webSocket;
    private static final String WEB_SOCKET_URL = "ws://coms-309-032.class.las.iastate.edu:8080/feed";
    private String username, password, JWTtoken;
    private RequestQueue mQueue;
    private static final int LIMIT = 5;
    private int offset = 0;
    private int feedSize;
    private boolean isLoading = false;
    private boolean hasMore = true;
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
            JWTtoken = navBar.jwtToken;
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
        feedAdapter = new FeedAdapter(feedItems);
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

        return view;
    }

    /**
     * Loads more feed items when the user scrolls to the end of the list.
     */
    private void loadMoreItems() {

        isLoading = true;
        offset += LIMIT;
        FeedData data = new FeedData(LIMIT, offset);
        FeedRequest request = new FeedRequest(FeedRequest.RequestType.FEED_ITEMS, data);
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

            if (messageType.equals("com.fasterxml.jackson.databind.node.ObjectNode")) {

                feedSize = jsonObject.getAsJsonObject("data").get("size").getAsInt();
                loadInitialItems();

            } else if (messageType.equals("harmonize.DTOs.FeedDTO")) {

                FeedDTO feedDTO = gson.fromJson(jsonObject, FeedDTO.class);
                updateFeedItems(feedDTO);

            } else {

                Log.e("WebSocket", "Unknown message type: " + messageType);

            }
        });
    }

    /**
     * Loads the initial set of feed items.
     */
    public void loadInitialItems() {

        isLoading = true;
        offset = 0;
        FeedData data = new FeedData(LIMIT, offset);
        FeedRequest request = new FeedRequest(FeedRequest.RequestType.FEED_ITEMS, data);
        sendWebSocketRequest(request);

    }

    /**
     * Updates the feed items with the received FeedDTO data.
     *
     * @param feedDTO The FeedDTO object containing the feed data.
     */
    public void updateFeedItems(FeedDTO feedDTO) {

        if (offset == 0) {

            feedItems.clear();
            feedAdapter.notifyDataSetChanged();

        } else {

            if (feedDTO.getData() != null && feedDTO.getData().getItem() != null) {

                int insertIndex = feedItems.size();
                feedItems.add(feedDTO);
                feedAdapter.notifyItemInserted(insertIndex);
            }
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
        FeedData data = new FeedData(LIMIT, offset);
        FeedRequest request = new FeedRequest(FeedRequest.RequestType.REFRESH_FEED, data);
        sendWebSocketRequest(request);

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