package com.example.harmonizefrontend;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageSentViewHolder extends RecyclerView.ViewHolder{
    TextView messageText, timeText, dateText;

//    android.view.View View;

    MessageSentViewHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_other);
        timeText = itemView.findViewById(R.id.text_timestamp_other);
        dateText = itemView.findViewById(R.id.text_date_other);
//        itemView = View;
    }

}