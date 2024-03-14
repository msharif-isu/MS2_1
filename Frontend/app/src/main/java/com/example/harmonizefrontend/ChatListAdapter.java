package com.example.harmonizefrontend;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Message> messageList;

    protected ChatListAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        // 0 is sent 1 is recieved
        return messageList.get(position).getTypeMsg();
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
        Message message = messageList.get(position);

        if (getItemViewType(position) == 0) { // Sent message
            MessageSentViewHolder sentViewHolder = (MessageSentViewHolder) holder;
            sentViewHolder.messageText.setText(message.getMessage());
//            sentViewHolder.timeText.setText(message.getSentAt()); // Assume Message has getTime()
        } else { // Received message
            MessageRecieveViewHolder recieveViewHolder = (MessageRecieveViewHolder) holder;
            recieveViewHolder.messageText.setText(message.getMessage());
            recieveViewHolder.nameText.setText(message.getUser().getUsername());
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
