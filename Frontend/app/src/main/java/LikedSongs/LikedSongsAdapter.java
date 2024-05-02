package LikedSongs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.harmonizefrontend.R;
import com.example.harmonizefrontend.Track;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import UserInfo.UserSession;

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

                int adapterPosition = holder.getBindingAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {

                    // Make the DELETE request to remove the liked song
                    String url = UserSession.getInstance().getURL() + "/users/songs/" + likedSong.getTrackId();

                    StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Song successfully removed from liked songs
                                    likedSongs.remove(adapterPosition);
                                    notifyItemRemoved(adapterPosition);
                                    Toast.makeText(holder.itemView.getContext(), "Song removed from liked songs", Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    Toast.makeText(holder.itemView.getContext(), "Failed to remove song from liked songs", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", UserSession.getInstance().getJwtToken());
                            return headers;
                        }
                    };

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
        private ImageView albumCoverImageView;
        private TextView songTitleTextView;
        private TextView artistNameTextView;
        private TextView albumNameTextView;
        ImageButton removeButton;

        LikedSongViewHolder(@NonNull View itemView) {
            super(itemView);
            albumCoverImageView = itemView.findViewById(R.id.albumCoverImageView);
            songTitleTextView = itemView.findViewById(R.id.songTitleTextView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
            albumNameTextView = itemView.findViewById(R.id.albumNameTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }

        void bind(Track likedSong) {
            songTitleTextView.setText(likedSong.getTrackName());
            artistNameTextView.setText(likedSong.getArtistName());
            albumNameTextView.setText(likedSong.getAlbumName());

            // Load the album cover image using Glide
            Glide.with(itemView)
                    .load(likedSong.getAlbumCoverLink())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(albumCoverImageView);
        }
    }
}
