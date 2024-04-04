package Reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import com.example.harmonizefrontend.ClickListener;

import com.example.harmonizefrontend.R;


public class ReportedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Report> reportList;

    private ClickListener clickListener;

    public ReportedListAdapter(List<Report> reportList, ClickListener clickListener) {
        this.reportList = reportList;
        this.clickListener = clickListener;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Report report = reportList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String reportedUsername = report.getReportedUsername();
        String reportedMessage = report.getReportText();
        String reportedReason = report.getReportReason();
        String time = report.getTime();
        // TODO: Convert ISO 8601 to proper time

        final int index = holder.getAdapterPosition();
        ReportViewHolder viewHolder = (ReportViewHolder) holder;
        viewHolder.reportedName.setText(reportedUsername);
        viewHolder.reportedMessage.setText(reportedMessage);
        viewHolder.reportedMessageDate.setText(time);
        viewHolder.reportedReason.setText(reportedReason);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.click(index);
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
        return reportList.size();
    }

    public Report getItem(int index) {
        return reportList.get(index);
    }
}
