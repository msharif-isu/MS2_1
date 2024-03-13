package com.example.harmonizefrontend;

public class Message {
    private int typeMsg; // 0 for sent, 1 for received
    private String message;
        private long sentAt;
    private User sender;

    Message(int typeMsg, String message, User sender) {
        this.typeMsg = typeMsg;
        this.message = message;
        this.sender = sender;
    }

    protected int getTypeMsg() {
        return typeMsg;
    }

    protected String getMessage() {
        return message;
    }

    protected long getSentAt() {
        return sentAt;
    }

    protected User getUser() {
        return sender;
    }
}
