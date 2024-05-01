package com.example.harmonizefrontend;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.WebSocketListener;
import Connections.WebSocketManagerChat;
import DTO.SendDTO;
import UserInfo.Member;
import UserInfo.User;
import messaging.chat.ChatListAdapter;
import messaging.chat.ReportMessageFragment;
import DTO.ConversationDTO;
import DTO.MessageDTO;
import UserInfo.UserSession;


/**
 * A simple {@link Fragment} subclass that allows users to see a specific conversation with another user.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment implements WebSocketListener {

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

    private ClickListener clickListener;

    private EditText writeMsg;
    private Button sendBtn;
    private ImageView backBtn;
    private RequestQueue mQueue = UserSession.getInstance().getmQueue();

    private List<MessageDTO> list;

    private Map<Integer,MessageDTO> listMap;

    private String username, password, JWTtoken;

    private TextView friendUsername;

    private ImageView friendPfp;



    /**
     * Default constructor for the ConversationFragment
     */
    public MessageFragment() {
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
    public static MessageFragment newInstance(String param1, String param2) {
        return new MessageFragment();
    }

    /**
     * Called when the fragment is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listMap = new HashMap<>();

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
                MessageDTO reportedMessage = list.get(index);
                UserSession.getInstance().setReportedMessage(reportedMessage);
                Log.e("msg", "Reported Message: " + UserSession.getInstance().getReportedMessage().getData().getText());

                ((navBar) getActivity()).loadFragmentPopout(new ReportMessageFragment());
            }
        };

        String serverURL = "ws://coms-309-032.class.las.iastate.edu:8080/chats?username=" + username + "&password=" + password;
        Log.e("msg", "Before websocket connection");
        WebSocketManagerChat.getInstance().connectWebSocket(serverURL);
        WebSocketManagerChat.getInstance().setWebSocketListener(this);


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

        friendPfp = view.findViewById(R.id.profile_Picture);
        writeMsg = view.findViewById(R.id.edit_message);
        backBtn = view.findViewById(R.id.back_button);
        sendBtn = view.findViewById(R.id.button_send);
        recyclerView = view.findViewById(R.id.recycler);
        friendUsername = view.findViewById(R.id.friendUsername);
        list = getMessages();
        chatListAdapter = new ChatListAdapter(list, clickListener);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        StringBuilder tempNames = new StringBuilder();
        ConversationDTO tempConvo = UserSession.getInstance().getCurrentConversation();

        if (tempConvo.getFriends().size() > 1) {
            ArrayList<Member> friends = tempConvo.getFriends();
            for (int i = 0; i < friends.size() - 1; i++) { // Iterate until last friend
                String tempName = friends.get(i).getUsername();
                if (!tempName.equals(UserSession.getInstance().getCurrentUser().getUsername())) {
                    tempNames.append(friends.get(i).getUsername()).append(", ");
                }
            }
            tempNames.append(friends.get(friends.size() - 1).getUsername());
            friendPfp.setImageResource(R.drawable.baseline_groups_24);
        }
        else {
            tempNames = new StringBuilder(tempConvo.getFriends().get(tempConvo.getFriends().size() - 1).getUsername());
            makeImageRequest(tempConvo.getFriends().get(tempConvo.getFriends().size() - 1).getid(), friendPfp);
        }

        friendUsername.setText(tempNames.toString());


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((navBar) getActivity()).loadFragment(new ConversationsFragment());

                getActivity().findViewById(R.id.popout_frame_layout).setVisibility(View.GONE);

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            Gson gson = new Gson();
            @Override
            public void onClick(View v) {
                if (writeMsg.getText().toString() != null) {
                    SendDTO sentMessage = new SendDTO(
                            "harmonize.DTOs.MessageDTO",
                            new SendDTO.Data(
                                    new SendDTO.Data.Conversation(
                                            UserSession.getInstance().getCurrentConversation().getDataId()
                                    ),
                                    writeMsg.getText().toString()
                            )
                    );

                    Gson gson = new Gson();
                    String sentMessageString = gson.toJson(sentMessage);
                    Log.e("msg", sentMessageString);
                    writeMsg.setText("");
                    WebSocketManagerChat.getInstance().sendMessage(sentMessageString);
                }
            }
        });

//        chatListAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Gets generated messages, used for testing purposes
     * @return
     */
    private List<MessageDTO> getMessages() {
        List<MessageDTO> list = new ArrayList<>();
        list = UserSession.getInstance().getCurrentConversation().getMessageList();
        if (list.size() != 0) {
            for (MessageDTO message : list) {
                int id = message.getData().getDataId();
                if (!listMap.containsKey(id)) {
                    listMap.put(id, message);
                }
            }
        }

//        list.add(new MessageDTO(
//                "harmonize.DTOs.MessageDTO",
//                new MessageDTO.Data(2,
//                        System.currentTimeMillis(),
//                        secondMember,
//                        convo),
//                "Hi! How are you?"
//                )
//        );

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
        }
        else if (type.equals("harmonize.DTOs.MessageDTO")) {
            MessageDTO messageDTO = gson.fromJson(message, MessageDTO.class);
            int conversationId = messageDTO.getData().getDataConversation().getDataId();
            Log.e("msg", "New Message for conversationid :"+ conversationId);
            UserSession.getInstance().getConversation(conversationId).addMessage(messageDTO);

            this.getActivity().runOnUiThread( () -> {
                Log.e("msg", "Recieved message in onWebSocketMessage, updating UI");
                Log.e("msg", "Current conversationId: " + UserSession.getInstance().getCurrentConversation().getDataId());
                if (conversationId == UserSession.getInstance().getCurrentConversation().getDataId() && !listMap.containsKey(messageDTO.getData().getDataId())) {
                    list.add(messageDTO);
                    listMap.put(messageDTO.getData().getDataId(), messageDTO);
                    Log.e("msg", "Last message: " + list.get(list.size() - 1).toString());
                    int newItemPosition = list.size() - 1;
                    chatListAdapter.notifyItemInserted(newItemPosition);
                    recyclerView.scrollToPosition(newItemPosition);


//                    chatListAdapter.notifyItemChanged(chatListAdapter.getItemCount() + 1);
                }
                Log.e("msg", "Updated UI with incoming message");
            });
        }
        else {
            Log.e("msg", "Unknown type");
        }

//        this.getActivity().runOnUiThread( () -> {
//            Log.e("msg", "Recieved message in onWebSocketMessage, updating UI");
//            Gson gson = new Gson();
//            MessageDTO messageDTO = gson.fromJson(message, MessageDTO.class);
//            list.add(messageDTO);
//            chatListAdapter.notifyItemChanged(chatListAdapter.getItemCount() + 1);
//            Log.e("msg", "Updated UI with incoming message");
//        });

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

        // TODO: Consider possibility of adding duplicate data after it closes and reopens

    }

    /**
     * Called when an error occurs in the WebSocket communication.
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("msg", "Websocket error: " + ex.toString());

    }

    private void makeImageRequest(int id, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(
                UserSession.getInstance().getURL() + "/users/icons/" + id,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView

                        imageView.setImageBitmap(response);
                        Log.d("Image", response.toString());
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }
                        String body = "";
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                        Log.e("Image", body);
                        Log.e("Image", statusCode);
                        if (statusCode.equals("404")) {
                            imageView.setImageResource(R.drawable.ic_launcher_foreground);
                        }
                    }
                }

        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

        // Adding request to request queue
        mQueue.add(imageRequest);
    }


}