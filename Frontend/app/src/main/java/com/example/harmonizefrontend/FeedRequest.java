package com.example.harmonizefrontend;

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

    public FeedRequest(RequestType requestType, FeedData data) {

        if (requestType == RequestType.FEED_ITEMS) {

            this.type = "harmonize.DTOs.FeedDTO";

        } else if (requestType == RequestType.REFRESH_FEED) {

            this.type = "com.fasterxml.jackson.databind.node.ObjectNode";

        } else {

            throw new IllegalArgumentException("Invalid request type: " + requestType);

        }

        this.data = data;

    }

    public String getType() {

        return type;

    }

    public FeedData getData() {

        return data;

    }
}
