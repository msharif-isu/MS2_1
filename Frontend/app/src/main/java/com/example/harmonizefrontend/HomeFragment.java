package com.example.harmonizefrontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * Fragment that houses the main feed. Will display new music releases
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements WebSocketListener {
    private RecyclerView feedRecyclerView;
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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Executes when fragment is created. Runs any initiation code that is not related to the interface.
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
        WebSocketManager.getInstance().connectWebSocket(serverURL);
        WebSocketManager.getInstance().setWebSocketListener(this);
    }

    /**
     * Inflates the layout for this fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        feedRecyclerView = view.findViewById(R.id.feedRecyclerView);
        feedItems = new ArrayList<>();
        feedAdapter = new FeedAdapter(feedItems);
        feedRecyclerView.setAdapter(feedAdapter);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCountLoaded = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && hasMore && (visibleItemCount + firstVisibleItemPosition) >= totalItemCountLoaded) {
                    //loadMoreItems();
                }
            }
        });

        return view;
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

        Log.d("WebSocket", "WebSocket opened");

    }

    @Override
    public void onWebSocketMessage(String message) {

        getActivity().runOnUiThread(() -> {

            Log.d("WebSocket", "Received message: " + message);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
            String messageType = jsonObject.get("type").getAsString();

            if (messageType.equals("com.fasterxml.jackson.databind.node.ObjectNode")) {

                feedSize = jsonObject.getAsJsonObject("data").get("size").getAsInt();

            } else if (messageType.equals("harmonize.DTOs.FeedDTO")) {

                FeedDTO feedDTO = gson.fromJson(jsonObject, FeedDTO.class);
                //yada yada

            } else {

                Log.e("WebSocket", "Unknown message type: " + messageType);

            }
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d("WebSocket", "WebSocket closed");
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("WebSocket", "WebSocket error", ex);
    }

}