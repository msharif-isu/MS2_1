package com.example.harmonizefrontend;

public class Track {
    private String trackName;
    private String artistName;
    private String albumName;
    private String trackId;
    private String artistId;

    public Track(String trackName, String artistName, String albumName, String trackId, String artistId) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.trackId = trackId;
        this.artistId = artistId;
    }

    public Track(String trackName, String artistName){
        this.trackName = trackName;
        this.artistName = artistName;
    }

    public Track(String trackId, String artistId, String trackName, String artistName) {

        this.trackName = trackName;
        this.artistName = artistName;
        this.trackId = trackId;
        this.artistId = artistId;

    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}
