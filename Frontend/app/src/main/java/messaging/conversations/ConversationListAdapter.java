package messaging.conversations;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.harmonizefrontend.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleyCallBack;
import DTO.ConversationDTO;
import DTO.MessageDTO;
import PictureData.SharedViewModel;
import UserInfo.Member;
import UserInfo.User;
import UserInfo.UserSession;




import com.example.harmonizefrontend.ClickListener;

public class ConversationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ConversationDTO> conversationList;
    private ClickListener clickListener;

    private RequestQueue mQueue = UserSession.getInstance().getmQueue();
    private Bitmap friendPic;
    private Boolean isSelected = false;
    ArrayList<ConversationDTO> selectedConversations = new ArrayList<>();



    public ConversationListAdapter(List<ConversationDTO> conversationList, ClickListener clickListener) {
        this.conversationList = conversationList;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.e("convo", "Creating viewholder");
        RecyclerView.ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_convo, parent, false);
        viewHolder = new ConversationViewHolder(view);
        Log.e("convo", "viewholder created");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConversationDTO conversation = conversationList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String lastMessage = "";
        String lastMessageDateTime = "";
        String friendUsername = "";


        try {
            MessageDTO message = conversation.getMessageList().get(conversation.getMessageList().size() - 1);
            lastMessage = message.getText();
            Log.e("Time", String.valueOf(message.getData().getDataUnixTime()));
            Date dateUnix = new Date(message.getData().getDataUnixTime());

            String lastMessageDate = dateFormat.format(dateUnix);
            String lastMessageTime = timeFormat.format(dateUnix);
            lastMessageDateTime = lastMessageDate + " " + lastMessageTime;
        } catch (Exception e) {
            Log.e("convo", "Error, likely no messages between user");

        }

        final int index = holder.getAdapterPosition(); // Find alternative to getAdapterPosition
        ConversationViewHolder viewHolder = (ConversationViewHolder) holder;
            ArrayList<Member> friends = conversation.getFriends();
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < friends.size() - 1; i++) { // Iterate until last friend
                String tempName = friends.get(i).getUsername();
                if (!tempName.equals(UserSession.getInstance().getCurrentUser().getUsername())) {
                    name.append(friends.get(i).getUsername()).append(", ");
                }
            }
            if (!friends.get(friends.size() - 1).getUsername().equals(UserSession.getInstance().getCurrentUser().getUsername())) {
                name.append(friends.get(friends.size() - 1).getUsername());
            }

//        if (conversation.get)
        viewHolder.friendName.setText(name);
        viewHolder.lastMessage.setText(lastMessage);
        viewHolder.lastMessageTime.setText(lastMessageDateTime);

        requestFriendImage(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                viewHolder.friendPfp.setImageBitmap(friendPic);
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int convoId = conversation.getDataId();
                deleteConversation(convoId, new VolleyCallBack() {

                    @Override
                    public void onSuccess() {
                        conversationList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    if (selectedConversations.contains(conversation)) {
                        selectedConversations.remove(conversation);
                        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                        viewHolder.delete.setVisibility(View.GONE);
                    } else {
                        selectedConversations.add(conversation);
                        viewHolder.itemView.setBackgroundColor(Color.rgb(200, 120, 106));
                        viewHolder.delete.setVisibility(View.VISIBLE);
                    }

                    if (selectedConversations.size() == 0) {
                        isSelected = false;
                    }
//                    UserSession.getInstance().setSelectedconversations(selectedConversations);
                } else {
                    clickListener.click(index);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isSelected = true;

                if (selectedConversations.contains(conversation)) {
                    selectedConversations.remove(conversation);
                    viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    viewHolder.delete.setVisibility(View.GONE);
                } else {
                    selectedConversations.add(conversation);
                    viewHolder.itemView.setBackgroundColor(Color.rgb(200, 120, 106));
                    viewHolder.delete.setVisibility(View.VISIBLE);
                }

                if (selectedConversations.size() == 0) {
                    isSelected = false;
                    selectedConversations.clear();
                }
//                UserSession.getInstance().setSelectedconversations(selectedConversations);
                return true;
            }
        });
    }

    private void deleteConversation(int convoId, VolleyCallBack volleyCallBack) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                UserSession.getInstance().getURL() + "/users/conversations/" + String.valueOf(convoId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Conversations", response);
                        volleyCallBack.onSuccess();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Conversations", error.toString());
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
        mQueue.add(stringRequest);
    };

    @Override
    public int getItemCount() {
        return conversationList.size();
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

    public ConversationDTO getItem(int index) {
        return conversationList.get(index);
    }

    private void requestFriendImage(final VolleyCallBack callBack) {


        ImageRequest imageRequest = new ImageRequest(
                UserSession.getInstance().getURL() + "/users/1/image", // Do change
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        if (response == null) {
                            // TODO
                        } else {
                            // TODO
                            friendPic = response;
                        }
                        callBack.onSuccess();
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                    }
                }

        );

        // Adding request to request queue
        mQueue.add(imageRequest);
    }
};
