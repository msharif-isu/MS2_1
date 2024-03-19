package com.example.harmonizefrontend;

public class MessageDTO {
    private String type;
    private Data data;
    private String text;

    public static class Data {
        private int id;
        private long unixTime;
        private Member sender;
        private ConversationDTO convo;

        Data(int id, long unixTime, Member sender, ConversationDTO convo) {
            this.id = id;
            this.unixTime = unixTime;
            this.sender = sender;
            this.convo = convo;
        }
    }

    MessageDTO(String type, Data data, String text) {
        this.type = type;
        this.data = data;
        this.text = text;
    }







}
