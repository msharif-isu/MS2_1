package com.example.harmonizefrontend;

public class FeedData {
    private int limit;
    private int offset;

    public FeedData(int limit, int offset) {
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
