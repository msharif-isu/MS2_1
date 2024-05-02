package com.example.harmonizefrontend;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

import Connections.VolleySingleton;
import UserInfo.User;
import UserInfo.UserSession;

/**
 * This fragment will display all recommended friends to the user. Users will have the option to add them as friends.
 */
public class FindFragment extends Fragment {
    private ArrayList<User> userList;
    private RequestQueue mQueue;

    public FindFragment() {
        // Required empty public constructor
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

        String url = UserSession.getInstance().getURL() + "/users/friends/recommended";
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
                        JSONObject userObject = userJson.getJSONObject("user");
                        int id = userObject.getInt("id");
                        String username = userObject.getString("username");

                        String artistName = "";
                        if (userJson.has("artist")) {
                            JSONObject artistObject = userJson.getJSONObject("artist");
                            artistName = artistObject.getString("name");
                        }

                        userList.add(new User(id, username, artistName));
                    }

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
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
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
                ImageView profileImageView = userItemView.findViewById(R.id.profileImageView);
                TextView usernameTextView = userItemView.findViewById(R.id.usernameTextView);
                TextView sharedInterestsTextView = userItemView.findViewById(R.id.sharedInterestsTextView);
                ImageButton addFriendButton = userItemView.findViewById(R.id.addFriendButton);

                ImageRequest imageRequest = new ImageRequest(
                        UserSession.getInstance().getURL() + "/users/icons/" + user.getId(),
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                // Display the image in the ImageView

                                profileImageView.setImageBitmap(response);
                                Log.d("Image", "Got the image");
                            }
                        },
                        0, // Width, set to 0 to get the original width
                        0, // Height, set to 0 to get the original height
                        ImageView.ScaleType.FIT_XY, // ScaleType
                        Bitmap.Config.RGB_565, // Bitmap config

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error == null || error.networkResponse == null) {
                                    return;
                                }
                                String body = "";
                                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                                try {
                                    body = new String(error.networkResponse.data,"UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    // exception
                                }
                                Log.e("Image", body);
                                Log.e("Image", statusCode);
                                if (statusCode.equals("404")) {
                                    profileImageView.setImageResource(R.drawable.ic_launcher_foreground);
                                }
                            }
                        }

                )

                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", UserSession.getInstance().getJwtToken());
                        return headers;
                    }

                };

                // Adding request to request queue
                mQueue.add(imageRequest);



                //Replace .getProfileImageUrl with correct method
                //Glide.with(requireContext()).load(user.getProfileImageUrl()).into(profileImageView);

                String sharedInterests = "You both like: " + user.getArtistName();
                sharedInterestsTextView.setText(sharedInterests);
                usernameTextView.setText(user.getUsername());

                addFriendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFriendButton.setVisibility(View.INVISIBLE);
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
        String url = UserSession.getInstance().getURL() + "/users/friends/" + userId;

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    /**
                     * on response, create a toast (a small notification)
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Friend added", Toast.LENGTH_SHORT).show();
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
        mQueue.add(stringRequest);
    }

    private void makeImageRequest(int id, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(
                UserSession.getInstance().getURL() + "/users/icons/" + id,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView

                        imageView.setImageBitmap(response);
                        Log.d("Image", response.toString());
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body = "";
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                        Log.e("Image", body);
                        Log.e("Image", statusCode);
                        if (statusCode.equals("404")) {
                            imageView.setImageResource(R.drawable.ic_launcher_foreground);
                        }
                    }
                }

        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }

        };

        // Adding request to request queue
        mQueue.add(imageRequest);
    }
}