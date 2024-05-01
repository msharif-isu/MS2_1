package HomeFeed;

public class FeedRequest {
    private String type;
    private FeedData data;

    public FeedRequest(FeedEnum requestType, int limit, int offset) {

        if (requestType == FeedEnum.NEW_PAGE) {

            this.type = "harmonize.DTOs.FeedDTO";

        } else if (requestType == FeedEnum.REFRESH) {

            this.type = "harmonize.DTOs.FeedDTO";
            //this.type = "com.fasterxml.jackson.databind.node.ObjectNode";

        } else {

            throw new IllegalArgumentException("Invalid request type: " + requestType);

        }

        this.data = new FeedData(requestType, limit, offset);

    }

    public static class FeedData {
        private int requestType;
        private Page page;

        public FeedData(FeedEnum requestType, int limit, int offset) {
            this.requestType = requestType.ordinal();
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
