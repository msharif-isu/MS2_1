package messaging.conversations;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.harmonizefrontend.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleyCallBack;
import DTO.ConversationDTO;
import UserInfo.UserSession;

import com.example.harmonizefrontend.ClickListener;

public class ConversationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ConversationDTO> conversationList;
    private ClickListener clickListener;

    private RequestQueue mQueue = UserSession.getInstance().getmQueue();

    private String URL = "http://coms-309-032.cs.iastate.edu:8080";
    private Bitmap friendPic;
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

        String friendUsername = conversation.getFriend().getUsername();

        String lastMessage = "";
        String lastMessageDateTime = "";


        try {
            lastMessage = conversation.getMessageList().get(-1).getText();

            Date dateUnix = new Date(conversation.getMessageList().get(-1).getData().getDataUnixTime());
            String lastMessageDate = dateFormat.format(dateUnix);
            String lastMessageTime = timeFormat.format(dateUnix);
            lastMessageDateTime = lastMessageDate + " " + lastMessageTime;
        }
        catch (Exception e) {
            Log.e("convo", "Error, likely no messages between user");

        }



        final int index = holder.getAdapterPosition();
        ConversationViewHolder viewHolder = (ConversationViewHolder) holder;
        viewHolder.friendName.setText(friendUsername);
        viewHolder.lastMessage.setText(lastMessage);
        viewHolder.lastMessageTime.setText(lastMessageDateTime);

        requestFriendImage(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                viewHolder.friendPfp.setImageBitmap(friendPic);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.click(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
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
                URL + "/users/1/image", // Do change
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        if (response == null) {
                            // TODO
                        }
                        else {
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
    };
}
