package messaging.chat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.harmonizefrontend.R;

/**
 * View holder for the sent message in conversations
 */
public class MessageSentViewHolder extends RecyclerView.ViewHolder{
    protected TextView messageText, timeText, dateText;

//    android.view.View View;

    /**
     * Constructor for the sent message view holder
     * @param itemView
     */
    MessageSentViewHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_other);
        timeText = itemView.findViewById(R.id.text_timestamp_other);
        dateText = itemView.findViewById(R.id.text_date_other);
//        itemView = View;
    }



    // getters
    public TextView getMessageText() {
        return messageText;
    }

    public TextView getTimeText() {
        return timeText;
    }

    public TextView getDateText() {
        return dateText;
    }

}