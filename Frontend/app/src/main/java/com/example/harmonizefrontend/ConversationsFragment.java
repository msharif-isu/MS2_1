package com.example.harmonizefrontend;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleyCallBack;
import Connections.WebSocketListener;
import Connections.WebSocketManagerChat;
import DTO.MessageDTO;
import UserInfo.UserSession;
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
    private Map<Integer, ConversationDTO> listConversations;


    private RequestQueue mQueue = UserSession.getInstance().getmQueue();

    private ArrayList<ConversationDTO> selectedConversations = new ArrayList<>();

    private View view;
    public ConversationsFragment() {
        // Required empty public constructor
    }

    public static ConversationsFragment newInstance() {
        return new ConversationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listConversations = new HashMap<>();

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
                ConversationDTO currentConvo = conversationListAdapter.getItem(index);
                int convoId = currentConvo.getDataId();
                int messageNum = currentConvo.getMessageList().size();
                Log.e("msg", "Conversation Id: " + convoId);
                Log.e("msg", "num of messages: " + messageNum);
                UserSession.getInstance().setcurrentConversation(currentConvo);
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
        view = inflater.inflate(R.layout.fragment_conversation_list, container, false);

        recyclerView = view.findViewById(R.id.recycler);
//        view.findViewById(R.id.deleteConvo).setVisibility(View.GONE);
        conversations = getConversations();
        conversationListAdapter = new ConversationListAdapter(conversations, clickListener);
        recyclerView.setAdapter(conversationListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;


    }

    private List<ConversationDTO> getConversations() {
        List<ConversationDTO> conversations = new ArrayList<ConversationDTO>();
        conversations = UserSession.getInstance().getConversations();

        for (ConversationDTO convo : conversations) {
            int id = convo.getDataId();
            if (!listConversations.containsKey(id)) {
                listConversations.put(id, convo);
            }
        }

        return conversations;
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
                if (!listConversations.containsKey(conversationDTO.getDataId())) {
                    conversations.add(conversationDTO);
                    int newItemPosition = conversations.size() - 1;
                    conversationListAdapter.notifyItemChanged(newItemPosition);
                    recyclerView.scrollToPosition(newItemPosition);
                    Log.e("msg", "Updated UI with incoming message");
                }
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

    private void deleteConversation(int id, final VolleyCallBack callback) {
        String url = UserSession.getInstance().getURL() + "/users/conversations/" + id;
        Log.d("JWT", "Delete Convo JWT: " + UserSession.getInstance().getJwtToken());
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("msg", "Conversation deleted");
                            callback.onSuccess();
                        }
                        catch (Exception e) {
                            Log.e("msg", "Error deleting conversation");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Conversation", "Conversation id: " + id + " not deleted");
                        Log.e("Conversation", error.toString());

                    }
                }

        )
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };
        mQueue.add(stringRequest);
        }


    }
