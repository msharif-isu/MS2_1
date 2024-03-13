package com.example.harmonizefrontend;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<MessageViewHolder>{
    private List<Message> messageList;

    private ChatListAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    private int getMsgType(int msgNum) {
        return messageList.get(msgNum).getTypeMsg();
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 0) { // sent message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
        } else { // Recieve Message
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_recieve, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final MessageViewHolder holder, final int position) {
        final int index = holder.getAdapterPosition();
        holder.messageText.setText(messageList.get(position).getMessage());
        holder.nameText.setText(messageList.get(position).getUser().getUsername());
//        holder.pfpImage.setImageURI((URI) messageList.get(position).getUser().getPfp());
//        holder.timeText.setText((String) messageList.get(position).getSentAt());
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
