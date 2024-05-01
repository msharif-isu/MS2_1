package UserInfo;

import android.util.Log;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DTO.ConversationDTO;
import DTO.MessageDTO;

/**
 * Class which holds data about the current user using the application
 */
public class UserSession {

    private static UserSession instance;
    private Member currentUser;
    private List<String> roleList;

    private String jwtToken, password;

    private Map<Integer, ConversationDTO> conversations;

    private MessageDTO messagedReportedTemp;

    private ConversationDTO currentConversation;
    private RequestQueue mQueue;

    private ArrayList<Integer> selectedFriendsIds = new ArrayList<>();


    private String URL = "http://coms-309-032.class.las.iastate.edu:8080";

    private UserSession() {
        conversations = new java.util.HashMap<>();
    }

    /**
     * Get the instance of the user session
     * @return
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public List<String> getRoles() {
        return roleList;
    }

    public void setRoles(List<String> roles) {
        this.roleList = roles;
    }

    public Member getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Member currentUser) {
        this.currentUser = currentUser;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setPassword(String password) {
        this.password = password;
        }

    public String getPassword() {
        return password;
    }

    public void addConversation(ConversationDTO conversation) {
        conversations.put(conversation.getDataId(), conversation);
    }

    public void removeConversation(ConversationDTO conversation) {
        if (conversations.containsKey(conversation.getDataId())) {
            conversations.remove(conversation.getDataId());
        }
        else {
            Log.e("Conversation", "Error removing conversation, cannot find key");
        }
    }

    public List<ConversationDTO> getConversations() {
        List<ConversationDTO> convos = new ArrayList<>();
        for (Integer key : conversations.keySet()) {
            convos.add(conversations.get(key));
        }
        return convos;
    }

    public ConversationDTO getConversation(int id) {
        return conversations.get(id);
    }

    public void setcurrentConversation(ConversationDTO convo) {
        currentConversation = convo;
    }

    public ConversationDTO getCurrentConversation() {
        return currentConversation;
    }

    public void setReportedMessage(MessageDTO messageDTO) {
        this.messagedReportedTemp = messageDTO;
    }

    public MessageDTO getReportedMessage() {
        return messagedReportedTemp;
    }

    public void clearReportedMessage(MessageDTO messageDTO) {
        this.messagedReportedTemp = null;
    }

    public void setmQueue(RequestQueue mQueue) {
        this.mQueue = mQueue;
    }

    public RequestQueue getmQueue() {
        return mQueue;
    }

    public String getURL() {
        return URL;
    }

    public void setSelectedFriendsIds(ArrayList<Integer> list) {
        selectedFriendsIds = list;
    }

    public ArrayList<Integer> getSelectedFriendsIds() {
        return selectedFriendsIds;
    }

}
