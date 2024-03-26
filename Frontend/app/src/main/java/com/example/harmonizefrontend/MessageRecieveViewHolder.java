package com.example.harmonizefrontend;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MessageRecieveViewHolder extends RecyclerView.ViewHolder{
    TextView messageText, timeText, nameText, dateText;
    ImageView pfpImage;
//    android.view.View View;

    MessageRecieveViewHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_other);
        timeText = itemView.findViewById(R.id.text_timestamp_other);
        nameText = itemView.findViewById(R.id.text_user_other);
        dateText = itemView.findViewById(R.id.text_date_other);
        pfpImage = itemView.findViewById(R.id.image_profile_other);
//        itemView = View;
    }

}