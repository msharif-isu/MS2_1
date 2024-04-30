package com.example.harmonizefrontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import Connections.VolleySingleton;
import UserInfo.User;
import UserInfo.UserSession;

/**
 * This fragment will display all recommended friends to the user. Users will have the option to add them as friends.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<User> userList;
    private int id;
    private String username;
    private String artistName;
    private RequestQueue mQueue;
    private static final String TAG = FindFragment.class.getSimpleName();
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

    /**
     * Executes when fragment is created. Runs any initiation code that is not related to the interface.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = new ArrayList<>();
        mQueue = VolleySingleton.getInstance(getActivity()).getRequestQueue();

    }


    /**
     * Inflates the layout for this fragment.
     * An inflater takes an XML layout file as an input and builds the view object from it at runtime.
     * A ViewGroup is a View that can contain other views. (In our case, we are using a LinearView)
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

        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        LinearLayout containerLayout = rootView.findViewById(R.id.container);

        // Make API request to fetch recommended friends list
        fetchUserList();

        return rootView;
    }

    /**
     * Creates a json array request for recommended friends of the user.
     */
    private void fetchUserList() {

        String url = "http://coms-309-032.class.las.iastate.edu:8080/users/friends/recommended";
        //String url = "10:48.48.244:8080/users/friends/recommended";

//        Request a string response from the url.
//        'Request.Method.GET' means that this is a GET request.
//        'url' is the URL to which the request is made.
//        'null' is the request body. This is a GET request, so there is no request body.
//        'Response.Listener<JSONArray>()' is a listener that is triggered when the response is successfully received. It expects a JSONArray as a response.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            /**
             * on response, take the response and add to userList.
             * @param response
             */
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject userJson = response.getJSONObject(i);

                        int id = userJson.getInt("id");
                        String username = userJson.getString("username");
                        String artistName = "";

                        JSONObject artistJson = userJson.getJSONObject("artist");
                        if (artistJson.has("name")) {

                            artistName = artistJson.getString("name");

                        }

                        userList.add(new User(id, username, artistName));
                    }

                    //Populate the user items
                    populateUserItems();

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        // the comma here is used to separate the parameters being passed to the constructor.
        }, new Response.ErrorListener() {
            /**
             * On error response, execute this.
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        }) {
            /**
             * Checks authorization
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UserSession.getInstance().getJwtToken());
                return params;
            }
        };

        mQueue.add(jsonArrayRequest);

    }

    /**
     * Fills the fragment with user_items that are populated with user data.
     */
    private void populateUserItems() {

        if (userList != null) {

            LinearLayout containerLayout = requireView().findViewById(R.id.container);
            LayoutInflater inflater = LayoutInflater.from(getContext());

            for (User user: userList) {

                View userItemView = inflater.inflate(R.layout.user_item, containerLayout, false);

                // This binds the user information to the user item view
                TextView usernameTextView = userItemView.findViewById(R.id.usernameTextView);
                Button addFriendButton = userItemView.findViewById(R.id.addFriendButton);

                usernameTextView.setText(user.getUsername());

                addFriendButton.setOnClickListener(new View.OnClickListener() {
                    /**
                     * on click, send a friend request to the user
                     * @param v The view that was clicked.
                     */
                    @Override
                    public void onClick(View v) {

                        v.setVisibility(View.INVISIBLE);
                        addFriend(user.getId());

                    }
                });

                containerLayout.addView(userItemView);

            }

        }

    }

    /**
     * Create an API request that will send a user a friend request.
     * @param userId
     */
    private void addFriend(int userId) {

        // Makes API requests to add friend
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "http://coms-309-032.class.las.iastate.edu:8080/users/friends/" + userId;

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    /**
                     * on response, create a toast (a small notification)
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * on error response, create a toast saying "request failed"
                     * @param error
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_SHORT).show();

                    }
                }) {
            /**
             * checks authorization
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UserSession.getInstance().getJwtToken());
                return params;
            }
        };

        // Add the request to the requestQueue
        queue.add(stringRequest);

    }
}