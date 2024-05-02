package Reports;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.harmonizefrontend.ClickListener;

import com.example.harmonizefrontend.R;

import org.json.JSONObject;

import Connections.VolleyCallBack;
import DTO.MessageDTO;
import UserInfo.User;
import UserInfo.UserSession;


public class ReportedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Report> reportList;


    private int reportedId;

    private RequestQueue mQueue = UserSession.getInstance().getmQueue();

    public ReportedListAdapter(List<Report> reportList, ClickListener clickListener) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        viewHolder = new ReportViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Report report = reportList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String reportedUsername = report.getReportedUsername();
        String reportedMessage = report.getReportText();
        String reportedReason = report.getReportReason();
        String lastMessageDateTime = "";
        long time = Long.valueOf(report.getTime());

        try {
            Date dateUnix = new Date(time);
            String lastMessageDate = dateFormat.format(dateUnix);
            String lastMessageTime = timeFormat.format(dateUnix);
            lastMessageDateTime = lastMessageDate + " " + lastMessageTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        final int index = holder.getAdapterPosition();
        ReportViewHolder viewHolder = (ReportViewHolder) holder;
        viewHolder.reportedName.setText(reportedUsername);
        viewHolder.reportedMessage.setText(reportedMessage);
        viewHolder.reportedMessageDate.setText(lastMessageDateTime);
        viewHolder.reportedReason.setText(reportedReason);

        getReportedUser(reportedUsername, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                Log.e("Reported", "user found");
                makeImageRequest(reportedId, viewHolder.reportedImage);
            }
        });





        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(reportedId, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        reportList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
            }
        });

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return reportList.size()    ;
    }

    public Report getItem(int index) {
        return reportList.get(index);
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

    private void getReportedUser(String username, VolleyCallBack callBack) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                UserSession.getInstance().getURL() + "/users/username/" + username,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            reportedId = response.getInt("id");
                            callBack.onSuccess();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JWT", error.toString());
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
        };
        mQueue.add(jsonObjReq);
    }

    private void deleteUser(int id, VolleyCallBack volleyCallBack) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                UserSession.getInstance().getURL() + "/moderators/users/" + String.valueOf(id),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("Report", response);
                        volleyCallBack.onSuccess();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
        };
        mQueue.add(stringRequest);
    }
}
