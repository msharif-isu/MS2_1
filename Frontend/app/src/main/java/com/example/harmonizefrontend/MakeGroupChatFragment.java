package com.example.harmonizefrontend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleyCallBack;
import DTO.ConversationDTO;
import Friends.FriendsListAdapter;
import UserInfo.Member;
import UserInfo.UserSession;
import messaging.conversations.Groupchats.GroupListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MakeGroupChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MakeGroupChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private GroupListAdapter GroupListAdapter;
    private ArrayList<Member> friends = new ArrayList<>();
    private ImageButton backBtn, confirmBtn;
    private RequestQueue mQueue = UserSession.getInstance().getmQueue();

    private View view;


    public MakeGroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_to_groupchat, container, false);
        Log.d("Friends", String.valueOf(UserSession.getInstance().getCurrentUser().getid()));
        recyclerView = view.findViewById(R.id.recycler);
        backBtn = view.findViewById(R.id.back_button);
        confirmBtn = view.findViewById(R.id.createChat);
        getFriends(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                Log.e("Friends", "Number of friends: " + friends.size());
                int newItemPosition;
                for (int i = 0; i < friends.size(); i++) {
                    newItemPosition = i;
                    GroupListAdapter.notifyItemInserted(newItemPosition);
                    recyclerView.scrollToPosition(newItemPosition);
                }
            }
        });
        GroupListAdapter = new GroupListAdapter(friends);
        recyclerView.setAdapter(GroupListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((navBar) getActivity()).loadFragment(new ConversationsFragment());
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> ids = new ArrayList<>();
                ids.add(UserSession.getInstance().getCurrentUser().getid());
                ids.addAll(UserSession.getInstance().getSelectedFriendsIds());
                Log.e("Groupchat", String.valueOf(ids.size()));
                if (ids.size() > 2) {
                    Log.e("Groupchat", String.valueOf(ids));
                    JSONObject jsonObject = new JSONObject();
                    try {
                        JSONArray jsonArray = new JSONArray(ids);
                        jsonObject.put("memberIds", jsonArray);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    createGroupchat(jsonObject, new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            ((navBar) getActivity()).loadFragment(new MessageFragment());
                        }
                    });

                }
            }
        });
        return view;

    }

    private void createGroupchat(JSONObject jsonObject, VolleyCallBack volleyCallBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                UserSession.getInstance().getURL() + "/users/conversations",
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Groupchat", "Created groupchat");
                        ConversationDTO convo = null;
                        try {
                            convo = parseConversation(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

//                        int convoId = convo.getDataId();
//                        int messageNum = convo.getMessageList().size();
//                        Log.e("msg", "Conversation Id: " + convoId);
//                        Log.e("msg", "num of messages: " + messageNum);
                        UserSession.getInstance().setcurrentConversation(convo);
                        volleyCallBack.onSuccess();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            return;
                        }

                        String body = "";
                        //get status code here
                        final String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            // exception
                        }
                        Log.e("Groupchat", body);
                        //do stuff with the body...
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }

        };
        mQueue.add(jsonObjectRequest);
    }

    private ConversationDTO parseConversation(JSONObject response) throws JSONException {
        Gson gson = new Gson();
        ConversationDTO.Data data = gson.fromJson(response.toString(), ConversationDTO.Data.class);
        ConversationDTO convo = new ConversationDTO("harmonize.DTOs.ConversationDTO", data);
        convo.ArrayListInitializer();
        return convo;
    };

    private void getFriends(VolleyCallBack volleyCallBack) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                UserSession.getInstance().getURL() + "/users/friends",
                null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject friendObject = response.getJSONObject(i);
                            Member friend = parseFriend(friendObject);
                            Log.e("Friends", "Incoming friend id: " + friend.getid());
                            friends.add(friend);
                        }
                        volleyCallBack.onSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("Friends", error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };
        mQueue.add(jsonArrayRequest);
    }

    private Member parseFriend(JSONObject friendObject) throws JSONException {
        Gson gson = new Gson();

        return gson.fromJson(friendObject.toString(), Member.class);
    }
}