package Conversations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.harmonizefrontend.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Connections.VolleyCallBack;
import DTO.MessageDTO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportMessageFragment extends Fragment {

    Button backBtn, sendBtn;
    EditText inputReport;

    MessageDTO reportedMessage;




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




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_message, container, false);

        backBtn = view.findViewById(R.id.Back_button);
        sendBtn = view.findViewById(R.id.button_send);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go back to the conversation fragment
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!inputReport.getText().toString().isEmpty()) {
                    sendReport(new VolleyCallBack() {

                        @Override
                        public void onSuccess() {
                            // TODO: Tell user report was sent and close the fragment after an interval
                        }
                    });

                }
                else {
                    Toast.makeText(getContext(), "Please enter a report", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;


    }

    private void sendReport(final VolleyCallBack callBackDetails) {
//
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("messageId", reportedMessage.getData().getDataId());
//            jsonBody.put("report", inputReport.getText().toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
//                Request.Method.PUT,
//                URL + "/users",
//                jsonBody, // Pass null as the request body since it's a GET request
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.e("JWT", "Updating user details!");
//                            username = response.getString("username");
//                            firstName = response.getString("firstName");
//                            lastName = response.getString("lastName");
//                            bio = response.getString("bio");
//                            callBackDetails.onSuccess();
//
//                        }
//                        catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("JWT", error.toString());
//                        // Gets: com.android.volley.ParseError: org.json.JSONException: Value First of type java.lang.String cannot be converted to JSONObject
//                        // The backend still takes the request and updates the details
//                    }
//                }
//        )
//
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", jwtToken);
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("param1", "value1");
////                params.put("param2", "value2");
//                return params;
//            }
//        };
//        mQueue.add(jsonObjReq);
//
    };


}