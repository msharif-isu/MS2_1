package Friends;

import static android.view.View.GONE;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.harmonizefrontend.R;

public class FriendsViewHolder extends RecyclerView.ViewHolder{

    protected TextView friendName;
    protected ImageView friendPfp;
    protected ImageButton message;
    protected ImageButton removeFriend;

    public FriendsViewHolder(View itemView) {
        super(itemView);
        friendName = itemView.findViewById(R.id.FriendName);
        message = itemView.findViewById(R.id.messageFriend);
        removeFriend = itemView.findViewById(R.id.deleteFriend);
        friendPfp = itemView.findViewById(R.id.profile_Picture);
    }

    public TextView getFriendName() {
        return friendName;
    }
}
