package messaging.conversations.Groupchats;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.harmonizefrontend.R;

public class GroupViewHolder extends RecyclerView.ViewHolder{

    protected TextView friendUsername, friendName;
    protected ImageView friendPfp;

    public GroupViewHolder(View itemView) {
        super(itemView);
        friendUsername = itemView.findViewById(R.id.username);
        friendName = itemView.findViewById(R.id.name);
        friendPfp = itemView.findViewById(R.id.profile_Picture);
    }

    public TextView getFriendName() {
        return friendName;
    }
}
