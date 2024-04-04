package com.example.harmonizefrontend;

public class FeedDTO {
    private String type;
    private Data data;

    public static class Data {
        private int index;
        private Item item;

        public Data(int index, Item item) {
            this.index = index;
            this.item = item;
        }

        public int getIndex() {
            return index;
        }

        public Item getItem() {
            return item;
        }
    }

    public static class Item {
        private int id;
        private String type;
        private String expiration;
        private User user;
        private Song song;

        public Item(int id, String type, String expiration, User user, Song song) {
            this.id = id;
            this.type = type;
            this.expiration = expiration;
            this.user = user;
            this.song = song;
        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getExpiration() {
            return expiration;
        }

        public User getUser() {
            return user;
        }

        public Song getSong() {
            return song;
        }
    }

    public static class User {
        private int id;

        public User(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static class Song {
        private String id;
        private String artistId;
        private String title;
        private String artist;

        public Song(String id, String artistId, String title, String artist) {
            this.id = id;
            this.artistId = artistId;
            this.title = title;
            this.artist = artist;
        }

        public String getId() {
            return id;
        }

        public String getArtistId() {
            return artistId;
        }

        public String getTitle() {
            return title;
        }

        public String getArtist() {
            return artist;
        }
    }

    public FeedDTO(String type, Data data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Data getData() {
        return data;
    }
}
