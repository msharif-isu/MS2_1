package messaging.conversations;

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

import DTO.ConversationDTO;
import com.example.harmonizefrontend.ClickListener;

public class ConversationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ConversationDTO> conversationList;
    private ClickListener clickListener;

    public ConversationListAdapter(List<ConversationDTO> conversationList, ClickListener clickListener) {
        this.conversationList = conversationList;
        this.clickListener = clickListener;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.e("convo", "Creating viewholder");
        RecyclerView.ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_convo, parent, false);
        viewHolder = new ConversationViewHolder(view);
        Log.e("convo", "viewholder created");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConversationDTO conversation = conversationList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String friendUsername = conversation.getFriend().getUsername();

        String lastMessage = "";
        String lastMessageDateTime = "";
        try {
            lastMessage = conversation.getMessageList().get(-1).getText();

            Date dateUnix = new Date(conversation.getMessageList().get(-1).getData().getDataUnixTime());
            String lastMessageDate = dateFormat.format(dateUnix);
            String lastMessageTime = timeFormat.format(dateUnix);
            lastMessageDateTime = lastMessageDate + " " + lastMessageTime;
        }
        catch (Exception e) {
            Log.e("convo", "Error, likely no messages between user");

        }


        final int index = holder.getAdapterPosition();
        ConversationViewHolder viewHolder = (ConversationViewHolder) holder;
        viewHolder.friendName.setText(friendUsername);
        viewHolder.lastMessage.setText(lastMessage);
        viewHolder.lastMessageTime.setText(lastMessageDateTime);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.click(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ConversationDTO getItem(int index) {
        return conversationList.get(index);
    }
}
