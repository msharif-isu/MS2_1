package com.example.harmonizefrontend;

import androidx.appcompat.app.AppCompatActivity;

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

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment implements WebSocketListener {

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

    private List<Message> list;

    private String password, JWTtoken;

    private RequestQueue mQueue;


    public MessagesFragment() {
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
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            password = navBar.password;
            JWTtoken = navBar.jwtToken;
            mQueue = navBar.mQueue;
        }
        else {
            Log.e("msg", "navBar is null, JWT token not set");
        }

        // Connect to websocket
        String serverURL = "ws://coms-309-032.class.las.iastate.edu:8080/chats?password=" + password;

        WebSocketManager.getInstance().connectWebSocket(serverURL, JWTtoken);
        WebSocketManager.getInstance().setWebSocketListener(this);


    }

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
            @Override
            public void onClick(View v) {
                if (writeMsg.getText().toString() != null) {
                    Message newmsg = new Message(0, writeMsg.getText().toString(), new User("Manasm", "blank"));
                    list.add(newmsg);
                    chatListAdapter.notifyItemChanged(chatListAdapter.getItemCount() + 1);
                    writeMsg.setText("");
                }

            }
        });

        // Inflate the layout for this fragment
        return view;

    }



    private List<Message> getMessages() {
        List<Message> list = new ArrayList<>();
        list.add(new Message(
                1,
                "Hello this is Mayank!",
                new User(
                        "MayankM",
                        "BlankURL")));
        list.add(new Message(
                0,
                "Hello this is Manas!",
                new User(
                        "ManasM",
                        "BlankURL")));
        return list;
    }

        @Override
        public void onWebSocketOpen(ServerHandshake handshakedata) {

        }

        @Override
        public void onWebSocketMessage(String message) {

        // Why does it not like runOnUiThread? Likely import statement issue?
            runOnUiThread(() -> {
                Log.e("msg", "onWebSocketOpen connected to server")
            })



        }

        @Override
        public void onWebSocketClose(int code, String reason, boolean remote) {

        }

        @Override
        public void onWebSocketError(Exception ex) {

        }

}