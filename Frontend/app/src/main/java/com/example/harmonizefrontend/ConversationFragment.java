package com.example.harmonizefrontend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass that allows users to see a specific conversation with another user.
 * Use the {@link ConversationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationFragment extends Fragment implements WebSocketListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private ChatListAdapter chatListAdapter;
    private RecyclerView recyclerView;

    private EditText writeMsg;
    private Button sendBtn;

    private List<MessageDTO> list;

    private String username, password, JWTtoken;

    private RequestQueue mQueue;

    private Member currentMember = UserSession.getInstance().getCurrentUser(); // For testing purposes
    private Member secondMember = new Member(2, "jon", "jon", "jon", ""); // For testing purposes
    private ConversationDTO convo = new ConversationDTO(
            "harmonize.DTOs.ConversationDTO",
            new ConversationDTO.Data(
                    1,
                    Arrays.asList(
                            UserSession.getInstance().getCurrentUser(),
                            secondMember)

            )
            ); // For testing purposes, later on we can have multiple conversations


    /**
     * Default constructor for the ConversationFragment
     */
    public ConversationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConversationFragment newInstance(String param1, String param2) {
        ConversationFragment fragment = new ConversationFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            username = navBar.username;
            password = navBar.password;
            JWTtoken = navBar.jwtToken;
            mQueue = navBar.mQueue;
        }
        else {
            Log.e("msg", "navBar is null, JWT token not set");
        }

        // Connect to websocket
        String serverURL = "ws://coms-309-032.class.las.iastate.edu:8080/chats?username=" + username + "&password=" + password;
        Log.e("msg", "Before websocket connection");
        WebSocketManager.getInstance().connectWebSocket(serverURL);
        WebSocketManager.getInstance().setWebSocketListener(this);


    }

    /**
     * Called when the fragment is created, runs all interface related code
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_activity, container, false);

        writeMsg = view.findViewById(R.id.edit_message);
        sendBtn = view.findViewById(R.id.button_send);
        recyclerView = view.findViewById(R.id.recycler);
        list = getMessages();
        chatListAdapter = new ChatListAdapter(list);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        sendBtn.setOnClickListener(new View.OnClickListener() {
            Gson gson = new Gson();
            @Override
            public void onClick(View v) {
                if (writeMsg.getText().toString() != null) {
                    MessageDTO newMsg = new MessageDTO(
                            "harmonize.DTOs.MessageDTO",
                            new MessageDTO.Data(
                                    1,
                                    System.currentTimeMillis(),
                                    currentMember,
                                    convo),
                            writeMsg.getText().toString(
                            )
                    );

                    list.add(newMsg);
                    chatListAdapter.notifyItemChanged(chatListAdapter.getItemCount() + 1);
                    writeMsg.setText("");
                    String returnMsg = gson.toJson(newMsg);
                    Log.e("msg", "Before sending message");
                    WebSocketManager.getInstance().sendMessage(returnMsg);
                }

            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Gets generated messages, used for testing purposes
     * @return
     */
    private List<MessageDTO> getMessages() {
        List<MessageDTO> list = new ArrayList<>();
//        list.add(new MessageDTO(
//                "harmonize.DTOs.MessageDTO",
//                new MessageDTO.Data(1,
//                        System.currentTimeMillis(),
//                        currentMember,
//                        convo),
//                "Hello!"
//                )
//        );

        list.add(new MessageDTO(
                "harmonize.DTOs.MessageDTO",
                new MessageDTO.Data(2,
                        System.currentTimeMillis(),
                        secondMember,
                        convo),
                "Hi! How are you?"
                )
        );

        return list;
    }


    /**
     * Called when the websocket is opened
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.e("msg", "Websocket opened");

    }

    /**
     * Called when a WebSocket message is received.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {

        this.getActivity().runOnUiThread( () -> {
            Log.e("msg", "Recieved message in onWebSocketMessage, updating UI");
            Gson gson = new Gson();
            MessageDTO messageDTO = gson.fromJson(message, MessageDTO.class);
            list.add(messageDTO);
            chatListAdapter.notifyItemChanged(chatListAdapter.getItemCount() + 1);
            Log.e("msg", "Updated UI with incoming message");
        });

    }

    /**
     * Called when the WebSocket connection is closed.
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.e("msg", "Websocket closed");

    }

    /**
     * Called when an error occurs in the WebSocket communication.
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("msg", "Websocket error");

    }
}