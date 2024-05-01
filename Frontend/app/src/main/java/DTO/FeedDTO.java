package DTO;

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
        private User user;
        private Song song;
        private Post post;

        public Item(int id, String type, User user, Song song) {
            this.id = id;
            this.type = type;
            this.user = user;
            this.song = song;
        }

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public User getUser() {
            return user;
        }

        public Song getSong() {
            return song;
        }
        public Post getPost() { return post; }
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

        public Song(String id, String artistId) {
            this.id = id;
            this.artistId = artistId;
        }

        public String getId() {
            return id;
        }

        public String getArtistId() {
            return artistId;
        }
    }

    public static class Post {
        private int id;
        private String time;
        private User poster;
        private String post;

        public Post(int id, String time, User poster, String post) {
            this.id = id;
            this.time = time;
            this.poster = poster;
            this.post = post;
        }

        public int getId() { return id; }
        public String getTime() { return time; }
        public User getPoster() { return poster; }
        public String getPost() { return post; }

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
