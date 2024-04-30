package com.example.harmonizefrontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleyCallBack;
import Friends.FriendsListAdapter;
import UserInfo.Member;
import UserInfo.UserSession;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeeFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeeFriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendsListAdapter friendsListAdapter;
    private ArrayList<Member> friends = new ArrayList<>();
    private RequestQueue mQueue = UserSession.getInstance().getmQueue();

    private View view;


    public SeeFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_friends_list, container, false);
        Log.d("Friends", String.valueOf(UserSession.getInstance().getCurrentUser().getid()));
        recyclerView = view.findViewById(R.id.recycler);
        getFriends(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                Log.e("Friends", "Number of friends: " + friends.size());
                int newItemPosition;
                for (int i = 0; i < friends.size(); i++) {
                    newItemPosition = i;
                    friendsListAdapter.notifyItemInserted(newItemPosition);
                    recyclerView.scrollToPosition(newItemPosition);
                }
            }
        });
        friendsListAdapter = new FriendsListAdapter(friends);
        recyclerView.setAdapter(friendsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;

    }

    private void getFriends(VolleyCallBack volleyCallBack) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UserSession.getInstance().getURL() + "/users/friends",
                null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject friendObject = response.getJSONObject(i);
                            Member friend = parseFriend(friendObject);
                            Log.e("Friends", "Incoming friend id: " + friend.getid());
                            friends.add(friend);
                        }
                        volleyCallBack.onSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("Friends", error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };
        mQueue.add(jsonArrayRequest);
    }

    private Member parseFriend(JSONObject friendObject) throws JSONException {
        Gson gson = new Gson();

        return gson.fromJson(friendObject.toString(), Member.class);
    }
}