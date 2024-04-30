package messaging.conversations;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.harmonizefrontend.R;

public class ConversationViewHolder  extends RecyclerView.ViewHolder{

    protected TextView friendName, lastMessage, lastMessageTime;
    protected ImageView friendPfp;
    protected ImageButton delete;

    public ConversationViewHolder(View itemView) {
        super(itemView);
        friendName = itemView.findViewById(R.id.FriendName);
        lastMessage = itemView.findViewById(R.id.lastMessage);
        lastMessageTime = itemView.findViewById(R.id.lastMessageDate);
        friendPfp = itemView.findViewById(R.id.profile_Picture);
        delete = itemView.findViewById(R.id.delete);
        itemView.findViewById(R.id.delete).setVisibility(View.GONE);
    }

    public TextView getFriendName() {
        return friendName;
    }

    public TextView getLastMessage() {
        return lastMessage;
    }

    public TextView getLastMessageTime() {
        return lastMessageTime;
    }
}
