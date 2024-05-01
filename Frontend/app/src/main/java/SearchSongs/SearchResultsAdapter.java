package SearchSongs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.harmonizefrontend.R;
import com.example.harmonizefrontend.Track;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder> {
    private List<Track> searchResults;
    private OnAddTrackListener onAddTrackListener;

    public SearchResultsAdapter(List<Track> searchResults) {
        this.searchResults = searchResults;
    }

    public interface OnAddTrackListener {
        void onAddTrack(Track track);
    }

    public void setOnAddTrackListener(OnAddTrackListener listener) {
        this.onAddTrackListener = listener;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Track track = searchResults.get(position);
        holder.bind(track, onAddTrackListener);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView trackNameTextView;
        private TextView artistNameTextView;
        private ImageView albumCoverImageView;
        private ImageButton addButton;

        SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            trackNameTextView = itemView.findViewById(R.id.trackNameTextView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
            albumCoverImageView = itemView.findViewById(R.id.albumCoverImageView);
            addButton = itemView.findViewById(R.id.addButton);
        }

        void bind(Track track, OnAddTrackListener listener) {
            trackNameTextView.setText(track.getTrackName());
            artistNameTextView.setText(track.getArtistName());

            addButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddTrack(track);
                }
            });

            Glide.with(itemView)
                    .load(track.getAlbumCoverLink())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(albumCoverImageView);
        }
    }
}
