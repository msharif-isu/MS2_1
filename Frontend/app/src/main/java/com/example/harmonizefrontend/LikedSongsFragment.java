package com.example.harmonizefrontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import LikedSongs.LikedSongsAdapter;
import UserInfo.UserSession;

public class LikedSongsFragment extends Fragment {
    private RecyclerView likedSongsRecyclerView;
    private LikedSongsAdapter likedSongsAdapter;
    private List<Track> likedSongs;
    private String username, password, JWTtoken;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked_songs, container, false);

        likedSongsRecyclerView = view.findViewById(R.id.likedSongsRecyclerView);
        likedSongs = new ArrayList<>();
        likedSongsAdapter = new LikedSongsAdapter(likedSongs);
        likedSongsRecyclerView.setAdapter(likedSongsAdapter);
        likedSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchLikedSongs();

        return view;
    }

    private void fetchLikedSongs() {
        String url = "http://coms-309-032.class.las.iastate.edu:8080/users/songs";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        likedSongs.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject songObject = response.getJSONObject(i);
                                String id = songObject.getString("id");
                                String artistId = songObject.getString("artistid");
                                String title = songObject.getString("title");
                                String artist = songObject.getString("artist");

                                Track likedSong = new Track(id, artistId, title, artist);
                                likedSongs.add(likedSong);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        likedSongsAdapter.notifyDataSetChanged();
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
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }
}
