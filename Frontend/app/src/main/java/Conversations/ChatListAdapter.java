package Conversations;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.harmonizefrontend.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import DTO.MessageDTO;
import UserInfo.UserSession;

/**
 * Adapter for the chat recycler view
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MessageDTO> messageList;

    private ClickListener clickListener;

    public ChatListAdapter(List<MessageDTO> messageList, ClickListener clickListener) {
        this.messageList = messageList;
        this.clickListener = clickListener;
    }

    /**
     * Returns the view type of the item at position for the purposes of view recycling.
     * @param position position to query
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 0 is sent 1 is recieved
        String senderUsername = messageList.get(position).getData().getDataSender().getUsername();
        String currentUsername = UserSession.getInstance().getCurrentUser().getUsername();
        Log.e("msg", "current username: " + currentUsername);

        if (senderUsername.equals(currentUsername)) {
            return 0;
        }
        else {
            return 1;
        }
    }


    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
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

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageDTO message = messageList.get(position);

        Date dateUnix = new Date(message.getData().getDataUnixTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String date = dateFormat.format(dateUnix);
        String time = timeFormat.format(dateUnix);


        final int index = holder.getAdapterPosition();
        if (getItemViewType(position) == 0) { // Sent message
            MessageSentViewHolder sentViewHolder = (MessageSentViewHolder) holder;
            sentViewHolder.messageText.setText(message.getText());
            sentViewHolder.dateText.setText(date);
            sentViewHolder.timeText.setText(time);


//            sentViewHolder.timeText.setText(message.getSentAt()); // Assume Message has getTime()
        } else { // Received message
            MessageRecieveViewHolder recieveViewHolder = (MessageRecieveViewHolder) holder;
            recieveViewHolder.messageText.setText(message.getText());
            recieveViewHolder.nameText.setText(message.getData().getDataSender().getUsername());
            recieveViewHolder.dateText.setText(date);
            recieveViewHolder.timeText.setText(time);

            recieveViewHolder.View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.click(index);
                }
            });
//            recieveViewHolder.timeText.setText(message.getSentAt());
//            recieveViewHolder.pfpImage.setImageURI(message.getUser().profileURL);

        }

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return
     */
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
