package Conversations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.harmonizefrontend.R;
import com.example.harmonizefrontend.navBar;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import Connections.VolleyCallBack;
import DTO.MessageDTO;
import UserInfo.UserSession;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportMessageFragment extends Fragment {

    private Button backBtn, sendBtn;
    private EditText inputReport;

    private MessageDTO reportedMessage;

    private RequestQueue mQueue;

    private String URL = "http://coms-309-032.class.las.iastate.edu:8080";




    public ReportMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportMessageFragment newInstance(String param1, String param2) {
        ReportMessageFragment fragment = new ReportMessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get mQueue from navbar
        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            mQueue = navBar.getQueue();
        }
        else {
            Log.e("Reporting", "Error getting mQueue from navbar");
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_message, container, false);

        backBtn = view.findViewById(R.id.Back_button);
        sendBtn = view.findViewById(R.id.button_send);
        inputReport = view.findViewById(R.id.edit_message);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go back to the conversation fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(ReportMessageFragment.this);
                transaction.commit();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!inputReport.getText().toString().equals("")) {
                    sendReport(new VolleyCallBack() {

                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Report sent", Toast.LENGTH_LONG).show();
                            // TODO: Tell user report was sent and close the fragment after an interval
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.remove(ReportMessageFragment.this);
                            transaction.commit();
                        }
                    });

                }
                else {
                    Toast.makeText(getContext(), "Please enter a report", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;


    }

    private void sendReport(final VolleyCallBack callBackDetails) {
        Log.e("Reporting", "Inside sendReport");

        JSONObject jsonBody = new JSONObject();
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("id", reportedMessage.getData().getDataId());
            messageObject.put("text", reportedMessage.getText());
            jsonBody.put("message", messageObject);
            jsonBody.put("reportText", inputReport.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL + "/users/reports",
                jsonBody, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("Reporting", "Message reported");

                            callBackDetails.onSuccess();

                        }
                        catch (Exception e) {
                            Log.e("Reporting", "Error reporting message");
                            e.printStackTrace();
                        }
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

                        Log.e("Reporting", error.toString());
                        Log.e("Reporting", "body: " + body);
                        // Gets: com.android.volley.ParseError: org.json.JSONException: Value First of type java.lang.String cannot be converted to JSONObject
                        // The backend still takes the request and updates the details
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                Log.e("Reporting", "JWT: " + UserSession.getInstance().getJwtToken());
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
        mQueue.add(jsonObjReq);

    };


}