package Reports;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.harmonizefrontend.R;


public class ReportViewHolder extends RecyclerView.ViewHolder {
    TextView reportedName, reportedMessage, reportedMessageDate, reportedReason;
    ImageView reportedImage;

    public ReportViewHolder(View itemView) {
        super(itemView);
        reportedName = itemView.findViewById(R.id.ReportedName);
        reportedMessage = itemView.findViewById(R.id.ReportedMessage);
        reportedMessageDate = itemView.findViewById(R.id.ReportedMessageDate);
        reportedReason = itemView.findViewById(R.id.ReportReason);
        reportedImage = itemView.findViewById(R.id.profile_Picture);
    }


}
