package com.example.harmonizefrontend;

import java.util.List;

public class ConversationDTO {

    private String type;
    private Data data;

    static class Data {
        private int id;
        private List<Member> members;

        Data(int id, List<Member> members) {
            this.id = id;
            this.members = members;
        }
    }

    ConversationDTO(String type, Data data) {
        this.type = type;
        this.data = data;
    }




}
