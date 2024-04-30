package LikedSongs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.harmonizefrontend.R;
import com.example.harmonizefrontend.Track;

import java.util.List;

public class LikedSongsAdapter extends RecyclerView.Adapter<LikedSongsAdapter.LikedSongViewHolder> {
    private List<Track> likedSongs;

    public LikedSongsAdapter(List<Track> likedSongs) {
        this.likedSongs = likedSongs;
    }

    @NonNull
    @Override
    public LikedSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liked_song, parent, false);
        return new LikedSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedSongViewHolder holder, int position) {
        Track likedSong = likedSongs.get(position);
        holder.bind(likedSong);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {

                    // Make the DELETE request to remove the liked song
                    String url = "http://coms-309-032.class.las.iastate.edu:8080/users/songs/" + likedSong.getTrackId();

                    StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Song successfully removed from liked songs
                                    likedSongs.remove(adapterPosition);
                                    notifyItemRemoved(adapterPosition);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle the error
                                    error.printStackTrace();
                                }
                            });

                    Volley.newRequestQueue(holder.itemView.getContext()).add(deleteRequest);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return likedSongs.size();
    }

    static class LikedSongViewHolder extends RecyclerView.ViewHolder {
        private TextView songTitleTextView;
        private TextView artistNameTextView;
        Button removeButton;

        LikedSongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitleTextView = itemView.findViewById(R.id.songTitleTextView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }

        void bind(Track likedSong) {
            songTitleTextView.setText(likedSong.getTrackName());
            artistNameTextView.setText(likedSong.getArtistName());
        }
    }
}
