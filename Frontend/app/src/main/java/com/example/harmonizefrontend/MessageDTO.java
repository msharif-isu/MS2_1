package com.example.harmonizefrontend;

public class MessageDTO extends packetDTO.Data{

    private int id;
    private long unixTime;
    private Member sender;
    private ConversationDTO convo;
    private String text;

}
