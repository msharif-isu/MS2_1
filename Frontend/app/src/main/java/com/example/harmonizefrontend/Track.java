package com.example.harmonizefrontend;

public class Track {
    private String trackName;
    private String artistName;
    private String albumName;
    private String trackId;
    private String artistId;
    private String albumCoverLink;

    public Track(String trackName, String artistName, String trackId, String albumCoverLink, String albumName, String artistId) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.trackId = trackId;
        this.albumCoverLink = albumCoverLink;
        this.albumName = albumName;
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

    public String getAlbumCoverLink() { return albumCoverLink; }

    public void setAlbumCoverLink(String albumCoverLink) { this.albumCoverLink = albumCoverLink; }
}
