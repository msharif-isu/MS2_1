package com.example.harmonizefrontend;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MessageDTO> messageList;

    protected ChatListAdapter(List<MessageDTO> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        // 0 is sent 1 is recieved
        String senderUsername = messageList.get(position).getData().getDataSender().getUsername();
        String currentUsername = UserSession.getInstance().getCurrentUser().getUsername();
        Log.e("msg", "current username: " + currentUsername);

        if (senderUsername.equals(currentUsername)) {
            return 0;
        }
        else {
            return 1;
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.e("msg", "Creating viewHolder");
        RecyclerView.ViewHolder text;
        if (viewType == 0) { // sent message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
            text = new MessageSentViewHolder(view);
        } else { // Recieve Message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_recieve, parent, false);
            text = new MessageRecieveViewHolder(view);
        }

        if (text == null) {
            Log.e("msg", "text msg is null");
        }
        return text;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageDTO message = messageList.get(position);

        Date dateUnix = new Date(message.getData().getDataUnixTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String date = dateFormat.format(dateUnix);
        String time = timeFormat.format(dateUnix);




        if (getItemViewType(position) == 0) { // Sent message
            MessageSentViewHolder sentViewHolder = (MessageSentViewHolder) holder;
            sentViewHolder.messageText.setText(message.getText());
            sentViewHolder.dateText.setText(date);
            sentViewHolder.timeText.setText(time);

//            sentViewHolder.timeText.setText(message.getSentAt()); // Assume Message has getTime()
        } else { // Received message
            MessageRecieveViewHolder recieveViewHolder = (MessageRecieveViewHolder) holder;
            recieveViewHolder.messageText.setText(message.getText());
            recieveViewHolder.nameText.setText(message.getData().getDataSender().getUsername());
            recieveViewHolder.dateText.setText(date);
            recieveViewHolder.timeText.setText(time);
//            recieveViewHolder.timeText.setText(message.getSentAt());
//            recieveViewHolder.pfpImage.setImageURI(message.getUser().profileURL);

        }

    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
