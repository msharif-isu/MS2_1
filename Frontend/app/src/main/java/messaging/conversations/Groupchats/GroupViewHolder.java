package messaging.conversations.Groupchats;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.harmonizefrontend.R;

public class GroupViewHolder extends RecyclerView.ViewHolder{

    protected TextView friendName;
    protected ImageView friendPfp;
    protected ImageButton message;
    protected ImageButton removeFriend;

    public GroupViewHolder(View itemView) {
        super(itemView);
        friendName = itemView.findViewById(R.id.FriendName);
        friendPfp = itemView.findViewById(R.id.profile_Picture);
        message = itemView.findViewById(R.id.messageFriend);
        removeFriend = itemView.findViewById(R.id.deleteFriend);
    }

    public TextView getFriendName() {
        return friendName;
    }
}
