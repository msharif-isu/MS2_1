package com.example.harmonizefrontend;

public class FeedRequest {

    private String type;
    private FeedData data;

    public FeedRequest(int requestType, FeedData data) {

        if (requestType == 3) {

            this.type = "harmonize.DTOs.FeedDTO";

        } else if (requestType == 4) {

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
