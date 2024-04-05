package com.example.harmonizefrontend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import UserInfo.UserSession;

public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private SearchResultsAdapter searchResultsAdapter;
    private List<Track> searchResults;
    private String username, password, JWTtoken;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        searchResults = new ArrayList<>();
        searchResultsAdapter = new SearchResultsAdapter(searchResults);
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mQueue = Volley.newRequestQueue(getContext());

        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            username = navBar.username;
            password = navBar.password;
            JWTtoken = navBar.jwtToken;
            mQueue = navBar.mQueue;
        } else {
            Log.e("msg", "navBar is null, JWT token not set");
        }

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
            requestBody.put("limit", "9");
            requestBody.put("offset", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, requestBody,
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
                                String artistName = trackObject.getJSONArray("artists").getJSONObject(0).getString("name");
                                // Parse other track details as needed
                                Track track = new Track(trackName, artistName);
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
                JWTtoken = UserSession.getInstance().getJwtToken();
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", JWTtoken);
                return headers;
            }
        };

        mQueue.add(jsonObjectRequest);
    }

}
