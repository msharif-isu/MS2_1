package com.example.harmonizefrontend;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connections.VolleyCallBack;
import Reports.Report;
import Reports.ReportedListAdapter;
import UserInfo.Member;
import UserInfo.UserSession;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;


public class SeeReportsFragment extends Fragment {

    private ReportedListAdapter reportedListAdapter;
    private RecyclerView recyclerView;
    private ClickListener clickListener;
    private List<Report> reportList;

    private RequestQueue mQueue;

//    private String URL = "http://coms-309-032.class.las.iastate.edu:8080";


    public SeeReportsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navBar navBar = (navBar) getActivity();
        if (navBar != null) {
            mQueue = navBar.getQueue();
        }

        clickListener = new ClickListener() {
            @Override
            public void click(int index) {
                Report currentReport = reportedListAdapter.getItem(index);
                Log.e("report", "Report: " + currentReport.getReportText());
                // TODO: DELETE THAT MEAN USER

                // REMOVE REPORT FROM LIST
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_reports, container, false);

        recyclerView = rootView.findViewById(R.id.recycler);
        reportList = new ArrayList<>();
        reportedListAdapter = new ReportedListAdapter(reportList, clickListener);
        recyclerView.setAdapter(reportedListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getReports(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                // I have no clue if this would work tbh
                Log.e("report", "Report List size: " + reportList.size());
                int newItemPosition;
                for (int i = 0; i < reportList.size(); i++) {
                    newItemPosition = i;
                    reportedListAdapter.notifyItemInserted(newItemPosition);
                    recyclerView.scrollToPosition(newItemPosition);
                }

//                int newItemPosition = list.size() - 1;
//                chatListAdapter.notifyItemInserted(newItemPosition);
//                recyclerView.scrollToPosition(newItemPosition);
//                reportedListAdapter.notifyItemRangeInserted(reportedListAdapter.getItemCount(), reportList.size());

            }
        });

        return rootView;
    }


    private void getReports(VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(
                Request.Method.GET,
                UserSession.getInstance().getURL() + "/moderators/reports",
                null,
                response -> {
                    try {
                        Log.e("report", "Accessed all reports");
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject reportObject = response.getJSONObject(i);
                            Report report = parseReport(reportObject);
                            reportList.add(report);
                        }
                        volleyCallBack.onSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("report", error.toString())
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", UserSession.getInstance().getJwtToken());
                return headers;
            }
        };
        mQueue.add(jsonArrayReq);
    }

    private Report parseReport(JSONObject reportObject) throws JSONException {
        Gson gson = new Gson();
        JSONObject JSONreportedMember = reportObject.getJSONObject("reported");
        Log.e("report", "ReportedMember: " + JSONreportedMember.toString());
        Member reportedMember = gson.fromJson(JSONreportedMember.toString(), Member.class);

        JSONObject reportedMessage = reportObject.getJSONObject("message");
        String reportedTextMessage = reportedMessage.getString("text");
        String reportedReason = reportObject.getString("reportText");

        String reportedTime = reportedMessage.getString("time");

        return new Report(
                reportedMember,
                reportedTextMessage,
                reportedReason,
                reportedTime
        );
    }



}
