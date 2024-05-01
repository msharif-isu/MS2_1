package messaging.conversations.Groupchats;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.harmonizefrontend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Connections.VolleyCallBack;
import UserInfo.Member;
import UserInfo.UserSession;

public class GroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Member> friends = new ArrayList<>();
//    private ClickListener clickListener;

    private RequestQueue mQueue = UserSession.getInstance().getmQueue();

    private ArrayList<Member> gcList = new ArrayList<>();



    public GroupListAdapter(ArrayList<Member> friendsList) {
        this.friends = friendsList;
//        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.e("convo", "Creating viewholder");
        RecyclerView.ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_to_groupchat, parent, false);
        viewHolder = new GroupViewHolder(view);
        Log.e("convo", "viewholder created");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Member friend = friends.get(position);


        final int index = holder.getAdapterPosition(); // Find alternative to getAdapterPosition
        GroupViewHolder viewHolder = (GroupViewHolder) holder;

        String name = friend.getFirstName() + " " + friend.getLastName();
        viewHolder.friendName.setText(name);
        Log.e("Groupchat", name);
        viewHolder.friendUsername.setText(friend.getUsername());

        makeImageRequest(friend.getid(), viewHolder.friendPfp);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("GroupChats", "Clicked");
                    if (gcList.contains(friend)) {
                        gcList.remove(friend);
                        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        gcList.add(friend);
                        viewHolder.itemView.setBackgroundColor(Color.rgb(200, 120, 106));
                    }

                    ArrayList<Integer> ids = new ArrayList<>();
                    for (Member friend : gcList) {
                        ids.add(friend.getid());
                        //Log.e("Report", "Ids: " + String.valueOf(ids));
                    }
                    Log.e("Report", "Ids: " + String.valueOf(ids));
                    UserSession.getInstance().setSelectedFriendsIds(ids);
            }
        });

    }


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
