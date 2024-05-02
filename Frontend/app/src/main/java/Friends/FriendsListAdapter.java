package Friends;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.harmonizefrontend.ClickListener;
import com.example.harmonizefrontend.MessageFragment;
import com.example.harmonizefrontend.R;
import com.example.harmonizefrontend.navBar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleyCallBack;
import DTO.ConversationDTO;
import DTO.MessageDTO;
import UserInfo.Member;
import UserInfo.User;
import UserInfo.UserSession;

public class FriendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Member> friends = new ArrayList<>();
//    private ClickListener clickListener;

    private RequestQueue mQueue = UserSession.getInstance().getmQueue();
    private ImageView friendPfp;



    public FriendsListAdapter(ArrayList<Member> friendsList) {
        this.friends = friendsList;
//        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.e("convo", "Creating viewholder");
        RecyclerView.ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current_friend, parent, false);
        viewHolder = new FriendsViewHolder(view);
        Log.e("convo", "viewholder created");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Member friend = friends.get(position);


        final int index = holder.getAdapterPosition(); // Find alternative to getAdapterPosition
        FriendsViewHolder viewHolder = (FriendsViewHolder) holder;


//        if (conversation.get)
        viewHolder.friendName.setText(friend.getUsername());


        makeImageRequest(friend.getid(), viewHolder.friendPfp);

        viewHolder.removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Implement remove friend functionality
                int friendId = friend.getid();
                removeFriend(friendId, new VolleyCallBack() {


                    @Override
                    public void onSuccess() {
                        Log.e("Friends", "removed properly");
                        friends.remove(position);
                        notifyItemRemoved(position);

                    }
                });

            }
        });

        viewHolder.message.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // TODO: Allow ability to create new conversations
                int friendId = friend.getid();
                int userId = UserSession.getInstance().getCurrentUser().getid();
                List<Integer> ids = new ArrayList<>();
                ids.add(friendId);
                ids.add(userId);
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray jsonArray = new JSONArray(ids);
                    jsonObject.put("memberIds", jsonArray);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                createGroupchat(jsonObject, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
//                        ((navBar) getActivity()).loadFragment(new MessageFragment());
                    }
                });


            }

        });

    }

    private void createGroupchat(JSONObject jsonObject, VolleyCallBack volleyCallBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                UserSession.getInstance().getURL() + "/users/conversations",
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Groupchat", "Created groupchat");
                        ConversationDTO convo = null;
                        try {
                            convo = parseConversation(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

//                        int convoId = convo.getDataId();
//                        int messageNum = convo.getMessageList().size();
//                        Log.e("msg", "Conversation Id: " + convoId);
//                        Log.e("msg", "num of messages: " + messageNum);
                        UserSession.getInstance().setcurrentConversation(convo);
                        volleyCallBack.onSuccess();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }

                        String body = "";
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                        Log.e("Groupchat", body);
                        //do stuff with the body...
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }

        };
        mQueue.add(jsonObjectRequest);
    }

    private ConversationDTO parseConversation(JSONObject response) throws JSONException {
        Gson gson = new Gson();
        ConversationDTO.Data data = gson.fromJson(response.toString(), ConversationDTO.Data.class);
        ConversationDTO convo = new ConversationDTO("harmonize.DTOs.ConversationDTO", data);
        convo.ArrayListInitializer();
        return convo;
    };

    private void removeFriend(int friendId, VolleyCallBack volleyCallBack) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                UserSession.getInstance().getURL() + "/users/friends/" + String.valueOf(friendId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Friends", "Friend removed");
                        volleyCallBack.onSuccess();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders () throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };
        mQueue.add(stringRequest);
    };

    private void createConversation(Integer[] ids, VolleyCallBack volleyCallBack) throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("memberIds", ids);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                UserSession.getInstance().getURL() + "/users/conversations",
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        )

        {

                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };
        mQueue.add(jsonObjectRequest);
    };

    @Override
    public int getItemCount() {
        return friends.size();
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public Member getItem(int index) {
        return friends.get(index);
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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        mQueue.add(imageRequest);
    }
};
