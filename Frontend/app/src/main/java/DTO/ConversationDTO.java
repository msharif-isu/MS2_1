package DTO;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import UserInfo.Member;
import UserInfo.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO for conversation data incoming/outgoing from the server
 */
public class ConversationDTO{

    private String type;
    private Data data;

    private ArrayList<MessageDTO> messageList;

    public static class Data {
        private int id;
        @SerializedName("members")
        private List<Member> members;

        /**
         * Constructor for the conversation data
         * @param id
         * @param members
         */
        public Data(int id, List<Member> members) {
            this.id = id;
            this.members = members;

        }

        public int getDataId() {
            return id;
        }

        public List<Member> getMembers() {
            return members;
        }

    }

    /**
     * Constructor for the conversation DTO
     * @param type
     * @param data
     */
    public ConversationDTO(String type, Data data) {
        this.type = type;
        this.data = data;
    }

    public void addMessage(MessageDTO message) {
        Log.e("msg", "Adding message to conversation");
        messageList.add(message);
    }

    public ArrayList<MessageDTO> getMessageList() {
        return messageList;
    }

    public int getDataId() {
        return this.data.getDataId();
    }

    /**
     * Initializes the members list because gson automatically sets it to null
     */
    public void ArrayListInitializer() {
        messageList = new ArrayList<MessageDTO>();
//        this.data.ArrayListInitializer();

    }

    public ArrayList<Member> getFriends() {
        ArrayList<Member> friends = new ArrayList<>();
        for (Member member : this.data.members) {
            if (!Objects.equals(member.getUsername(), UserSession.getInstance().getCurrentUser().getUsername())) {
                friends.add(member);
            }
        }

        return friends;
//        return this.data.getMembers().get(0);
    }




}
