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

import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that houses the main feed. Will display new music releases
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements WebSocketListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private RecyclerView feedRecyclerView;
    private FeedAdapter feedAdapter;
    private List<FeedItem> feedItems;

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Executes when fragment is created. Runs any initiation code that is not related to the interface.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // get necessary data (jwt token and crap)

        //connect to websocket
        // String url = "";
        //WebSocketManager.getInstance().connectWebSocket(url);
        //WebSocketManager.getInstance().setWebSocketListener(this);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedRecyclerView = view.findViewById(R.id.feedRecyclerView);
        feedItems = new ArrayList<>();
        feedAdapter = new FeedAdapter(feedItems);
        feedRecyclerView.setAdapter(feedAdapter);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void fetchFeedItem() {

        // get feed item stuff

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
            //FeedResponse feedResponse = gson.fromJson(message, FeedResponse.class);
            //updateFeedItems(feedResponse);

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

    /**
     *
     * @param feedResponse

    private void updateFeedItems(FeedResponse feedResponse) {
        List<FeedItem> newItems = feedResponse.getItems();
        int oldSize = feedItems.size();
        feedItems.addAll(newItems);
        feedAdapter.notifyItemRangeInserted(oldSize, newItems.size());
        currentPage = feedResponse.getNextPage();
        hasMore = feedResponse.isHasMore();
    }
    */
}