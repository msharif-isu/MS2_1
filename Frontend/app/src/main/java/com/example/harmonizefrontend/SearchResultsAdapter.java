package com.example.harmonizefrontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder> {
    private List<Track> searchResults;

    public SearchResultsAdapter(List<Track> searchResults) {
        this.searchResults = searchResults;
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
        holder.bind(track);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView trackNameTextView;
        private TextView artistNameTextView;

        SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            trackNameTextView = itemView.findViewById(R.id.trackNameTextView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
        }

        void bind(Track track) {
            trackNameTextView.setText(track.getTrackName());
            artistNameTextView.setText(track.getArtistName());
        }
    }
}
