package com.example.harmonizefrontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    //private ArrayList<User> userList;
    private int id;
    private String username;
    private RequestQueue mQueue;
    private String jwtToken;
    private static final String TAG = "Idk what this should be";
    public FindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        jwtToken = "Bear " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqaW0iLCJleHAiOjE3MDkzMjkxMTd9.4qXILCSharyAZS6fRciVjXBuZhwsX1Nmp6jt8h-J9nsHBxXLF8NQTCUkOUMm7BD8V9i-FDmdzIGnu0GeE162tg";

    }

    @Override
//    An inflater takes an XML layout file as an input and builds the view object from it at runtime.
//    A ViewGroup is a View that can contain other views. (In our case, we are using a LinearView)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        LinearLayout containerLayout = rootView.findViewById(R.id.container);

        // Make API request to fetch recommended friends list
        fetchUserList();

        return rootView;
    }

    private void fetchUserList() {


        //String url = "http://coms-309-032.class.las.iastate.edu:8080/users/friends/recommended";
        String url = "10:48.48.244:8080/users/friends/recommended";

//        Request a string response from the url.
//        'Request.Method.GET' means that this is a GET request.
//        'url' is the URL to which the request is made.
//        'null' is the request body. This is a GET request, so there is no request body.
//        'Response.Listener<JSONArray>()' is a listener that is triggered when the response is successfully received. It expects a JSONArray as a response.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //userList = new ArrayList<User>();
                String jsonString = response.toString();
                Log.d(TAG, jsonString);
                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject userJson = response.getJSONObject(i);

                        int id = userJson.getInt("id");
                        String username = userJson.getString("username");
                        //userList.add(new User(id, username));

                    }

                    //Populate the user items
                    //populateUserItems();

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        // the comma here is used to separate the parameters being passed to the constructor.
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", jwtToken);
                return params;
            }
        };

        mQueue.add(jsonArrayRequest);

    }

//    private void populateUserItems() {
//
//        if (userList != null) {
//
//            LinearLayout containerLayout = getView().findViewById(R.id.container);
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//
//            for (User user: userList) {
//
//                View userItemView = inflater.inflate(R.layout.user_item, containerLayout, false);
//
//                // This binds the user information to the user item view
//                TextView usernameTextView = userItemView.findViewById(R.id.usernameTextView);
//                Button addFriendButton = userItemView.findViewById(R.id.addFriendButton);
//
//                // idk how to do getUsername()
//                usernameTextView.setText(user.getUsername());
//
//                addFriendButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        // idk how to do getId()
//                        //addFriend(user.getId());
//
//                    }
//                });
//
//                containerLayout.addView(userItemView);
//
//            }
//
//        }
//
//    }

//    private void addFriend(int userId) {
//
//        // Makes API requests to add friend
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        String url = "http://coms-309-032.class.las.iastate.edu:8080/users/friends/add/" + userId;
//
//        // Request a string response
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//
//                // Update UI with the response
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
//
//            }
//
//        });
//
//        // Add the request to the requestQueue
//        queue.add(stringRequest);
//
//    }
}