package com.example.harmonizefrontend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleySingleton;
import SearchSongs.SearchResultsAdapter;

import UserInfo.UserSession;

public class SearchFragment extends Fragment implements SearchResultsAdapter.OnAddTrackListener {

    private EditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private SearchResultsAdapter searchResultsAdapter;
    private List<Track> searchResults;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        searchResults = new ArrayList<>();
        searchResultsAdapter = new SearchResultsAdapter(searchResults);
        searchResultsAdapter.setOnAddTrackListener(this);
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mQueue = VolleySingleton.getInstance(getActivity()).getRequestQueue();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                } else {
                    searchResults.clear();
                    performSearch(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    private void performSearch(String query) {
        String url = "http://coms-309-032.class.las.iastate.edu:8080/music";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("q", query);
            requestBody.put("type", "track");
            requestBody.put("limit", "10");
            requestBody.put("offset", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject tracksObject = response.getJSONObject("tracks");
                            JSONArray itemsArray = tracksObject.getJSONArray("items");
                            searchResults.clear();
                            for (int i = 0; i < itemsArray.length(); i++) {
                                JSONObject trackObject = itemsArray.getJSONObject(i);
                                String trackName = trackObject.getString("name");
                                String trackId = trackObject.getString("id");

                                JSONArray artistsArray = trackObject.getJSONArray("artists");
                                String artistName = artistsArray.getJSONObject(0).getString("name");

                                String albumCoverLink = "";
                                if (trackObject.has("album")) {
                                    JSONObject albumObject = trackObject.getJSONObject("album");
                                    if (albumObject.has("images")) {
                                        JSONArray imagesArray = albumObject.getJSONArray("images");
                                        if (imagesArray.length() > 0) {
                                            albumCoverLink = imagesArray.getJSONObject(0).getString("url");
                                        }
                                    }
                                }

                                Track track = new Track(trackName, artistName, trackId, albumCoverLink, null, null);
                                searchResults.add(track);
                            }
                            searchResultsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

        mQueue.add(jsonObjectRequest);
    }

    @Override
    public void onAddTrack(Track track) {
        addTrackToLikedSongs(track);
    }

    private void addTrackToLikedSongs(Track track) {
        String url = "http://coms-309-032.class.las.iastate.edu:8080/users/songs/" + track.getTrackId();

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

}
