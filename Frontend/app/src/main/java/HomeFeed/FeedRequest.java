package HomeFeed;

public class FeedRequest {
    private String type;
    private FeedData data;

    public enum RequestType {

        FEED_ITEMS(3),
        REFRESH_FEED(4);

        private int value;

        RequestType(int value) {

            this.value = value;

        }

        public int getValue() {

            return value;

        }

    }

    public FeedRequest(RequestType requestType, int limit, int offset) {

        if (requestType == RequestType.FEED_ITEMS) {

            this.type = "harmonize.DTOs.FeedDTO";

        } else if (requestType == RequestType.REFRESH_FEED) {

            this.type = "com.fasterxml.jackson.databind.node.ObjectNode";

        } else {

            throw new IllegalArgumentException("Invalid request type: " + requestType);

        }

        this.data = new FeedData(requestType, limit, offset);

    }

    public static class FeedData {
        private int requestType;
        private Page page;

        public FeedData(RequestType requestType, int limit, int offset) {
            this.requestType = requestType.getValue();
            this.page = new Page(limit, offset);
        }

        public int getRequestType() {
            return requestType;
        }

        public Page getPage() {
            return page;
        }
    }

    public static class Page {
        private int limit;
        private int offset;

        public Page(int limit, int offset) {
            this.limit = limit;
            this.offset = offset;
        }

        public int getLimit() {
            return limit;
        }

        public int getOffset() {
            return offset;
        }
    }

    public String getType() {
        return type;
    }

    public FeedData getData() {
        return data;
    }
}
