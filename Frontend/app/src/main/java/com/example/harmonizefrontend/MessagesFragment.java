//package com.example.harmonizefrontend;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link MessagesFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class MessagesFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private ArrayList<User> friendList;
//    private int id;
//    private String username;
//    private RequestQueue mQueue;
//    private String jwtToken;
//    private static final String TAG = FindFragment.class.getSimpleName();
//
//    public MessagesFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment MessagesFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static MessagesFragment newInstance(String param1, String param2) {
//        MessagesFragment fragment = new MessagesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        friendList = new ArrayList<>();
//        mQueue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
//        jwtToken = "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MSIsImV4cCI6MTcwOTQwOTIwNn0.60NflM9v-M-yWIQhuG7646xYY8pe9rZ4Uk9VE_PvMUtZszhNx_7GjdnwxhtnaIodNjx-jh7RC9pi_wO05ixe4Q";
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
//        LinearLayout containerLayout = rootView.findViewById(R.id.messagesContainer);
//
//        return rootView;
//    }
//
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//        super.onViewCreated(view, savedInstanceState);
//
//        fetchFriendList();
//
//    }
//
//    public void fetchFriendList() {
//
//        String url = "http://coms-309-032.class.las.iastate.edu:8080/users/friends";
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                String jsonString = response.toString();
//                Log.d("will", jsonString);
//                try {
//
//                    for (int i = 0; i < response.length(); i++) {
//
//                        JSONObject userJson = response.getJSONObject(i);
//
//                        int id = userJson.getInt("id");
//                        String username = userJson.getString("username");
//                        friendList.add(new User(id, username));
//
//                    }
//
//                    //Populate the user items
//                    populateFriendItems();
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//
//                }
//            }
//            // the comma here is used to separate the parameters being passed to the constructor.
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                error.printStackTrace();
//
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                //params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("Authorization", jwtToken);
//                return params;
//            }
//        };
//
//        mQueue.add(jsonArrayRequest);
//
//    }
//
//    private void populateFriendItems() {
//
//        if (friendList != null) {
//
//            LinearLayout containerLayout = requireView().findViewById(R.id.messagesContainer);
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//
//            for (User friend: friendList) {
//
//                View friendItemView = inflater.inflate(R.layout.friend_item, containerLayout, false);
//
//                // This binds the friend information to the friend item view
//                TextView friendUsernameTextView = friendItemView.findViewById(R.id.friendUsernameTextView);
//                Button removeFriendButton = friendItemView.findViewById(R.id.removeFriendButton);
//
//                friendUsernameTextView.setText(friend.getUsername());
//
//                removeFriendButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        v.setVisibility(View.INVISIBLE);
//                        removeFriend(friend.getId());
//
//                    }
//                });
//
//                containerLayout.addView(friendItemView);
//
//            }
//
//        }
//
//    }
//
//    private void removeFriend(int userId) {
//
//        // Makes API requests to remove friend
//        RequestQueue queue = Volley.newRequestQueue(requireContext());
//        String url = "http://coms-309-032.class.las.iastate.edu:8080/users/friends/" + userId;
//
//        // Request a string response
//        StringRequest stringRequest = new StringRequest(Request.Method.DELETE,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_SHORT).show();
//
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                //params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("Authorization", jwtToken);
//                return params;
//            }
//        };
//
//        // Add the request to the requestQueue
//        queue.add(stringRequest);
//
//    }
//
//}