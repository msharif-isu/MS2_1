package com.example.harmonizefrontend;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageSentViewHolder extends RecyclerView.ViewHolder{
    TextView messageText, timeText;

//    android.view.View View;

    MessageSentViewHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_other);
        timeText = itemView.findViewById(R.id.text_timestamp_other);
//        itemView = View;
    }

}