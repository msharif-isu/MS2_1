package com.example.harmonizefrontend;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Connections.WebSocketListener;
import Connections.WebSocketManagerChat;
import DTO.MessageDTO;
import UserInfo.UserSession;
import messaging.ClickListener;
import messaging.chat.ReportMessageFragment;
import messaging.conversations.ConversationListAdapter;

import DTO.ConversationDTO;

/**
 * A simple {@link Fragment} subclass that allows users to see a specific conversation with another user.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationsFragment extends Fragment implements WebSocketListener {

    private String username, password, JWTtoken;
    private ConversationListAdapter conversationListAdapter;
    private RecyclerView recyclerView;
    private ClickListener clickListener;
    private List<ConversationDTO> conversations;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    public static ConversationsFragment newInstance() {
        return new ConversationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            username = navBar.username;
            password = navBar.password;
            JWTtoken = navBar.jwtToken;
        }
        else {
            Log.e("msg", "navBar is null, JWT token not set");
        }

        clickListener = new ClickListener() {
            @Override
            public void click(int index) {
                // TODO: Open the chat fragment AND GIVE IT THE CORRECT CONVERSATION DATA
                ((navBar) getActivity()).loadFragment(new MessageFragment());
            }
        };

        String serverURL = "ws://coms-309-032.class.las.iastate.edu:8080/chats?username=" + username + "&password=" + password;
        Log.e("msg", "Before websocket connection");
        WebSocketManagerChat.getInstance().connectWebSocket(serverURL);
        WebSocketManagerChat.getInstance().setWebSocketListener(this);

    }

    /**
     * Called when the fragment is created, runs all interface related code
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_list, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        conversations = getConversations();
        conversationListAdapter = new ConversationListAdapter(conversations, clickListener);
        recyclerView.setAdapter(conversationListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private List<ConversationDTO> getConversations() {
//        String convo1 = "{\"type\":\"harmonize.DTOs.ConversationDTO\",\"data\":{\"id\":2,\"members\":[{\"id\":3,\"firstName\":\"John\",\"lastName\":\"Smith\",\"username\":\"john\",\"bio\":\"\",\"roles\":[{\"id\":3,\"name\":\"USER\"}]},{\"id\":5,\"firstName\":\"Manas\",\"lastName\":\"Mathur\",\"username\":\"manasmathur2023\",\"bio\":\"\",\"roles\":[{\"id\":3,\"name\":\"USER\"}]}]}}";
//        String convo2 = "{\"type\":\"harmonize.DTOs.ConversationDTO\",\"data\":{\"id\":3,\"members\":[{\"id\":4,\"firstName\":\"Tim\",\"lastName\":\"Brown\",\"username\":\"tim\",\"bio\":\"\",\"roles\":[{\"id\":3,\"name\":\"USER\"}]},{\"id\":5,\"firstName\":\"Manas\",\"lastName\":\"Mathur\",\"username\":\"manasmathur2023\",\"bio\":\"\",\"roles\":[{\"id\":3,\"name\":\"USER\"}]}]}}";
//        Gson gson = new Gson();
//
//        ConversationDTO conversationDTO1 = gson.fromJson(convo1, ConversationDTO.class);
//        ConversationDTO conversationDTO2 = gson.fromJson(convo2, ConversationDTO.class);
//        conversationDTO1.ArrayListInitializer();
//        conversationDTO2.ArrayListInitializer();
//
//        UserSession.getInstance().addConversation(conversationDTO1);
//        UserSession.getInstance().addConversation(conversationDTO2);

//        return UserSession.getInstance().getConversations();
        return new ArrayList<ConversationDTO>();
    }





    /**
     * Called when the WebSocket connection is successfully opened.
     *
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.e("msg", "Websocket opened");

    }

    /**
     * Called when a WebSocket message is received.
     *
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) throws JSONException {
        Log.e("msg", "Recieved Message: " + message);
        Gson gson = new Gson();

        JSONObject jsonObject = new JSONObject(message);
        String type = jsonObject.getString("type");
        Log.e("msg", "Type: " + type);

        if (type.equals("harmonize.DTOs.ConversationDTO")) {
            ConversationDTO conversationDTO = gson.fromJson(message, ConversationDTO.class);
            // Must initialize the arraylist because gson sets it to null
            conversationDTO.ArrayListInitializer();
            Log.e("msg", "New Conversation created");
            UserSession.getInstance().addConversation(conversationDTO);

            this.getActivity().runOnUiThread( () -> {
            Log.e("msg", "Recieved message in onWebSocketMessage, updating UI");
            conversations.add(conversationDTO);
            conversationListAdapter.notifyItemChanged(conversationListAdapter.getItemCount() + 1);
            Log.e("msg", "Updated UI with incoming message");
        });
        }
        else if (type.equals("harmonize.DTOs.MessageDTO")) {
            MessageDTO messageDTO = gson.fromJson(message, MessageDTO.class);
            int conversationId = messageDTO.getData().getDataConversation().getDataId();
            Log.e("msg", "New Message for conversationid :"+ conversationId);
            UserSession.getInstance().getConversation(conversationId).addMessage(messageDTO);
        }
        else {
            Log.e("msg", "Unknown type");
        }


    }

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.e("msg", "Websocket closed");

        // TODO: Consider possibility of adding duplicate data after it closes and reopens

    }
    /**
     * Called when an error occurs in the WebSocket communication.
     *
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("msg", "Websocket error: " + ex.toString());

    }
}
