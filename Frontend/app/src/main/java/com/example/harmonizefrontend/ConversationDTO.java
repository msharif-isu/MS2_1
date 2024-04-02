package com.example.harmonizefrontend;

import java.util.List;

/**
 * DTO for conversation data incoming/outgoing from the server
 */
public class ConversationDTO {

    private String type;
    private Data data;

    static class Data {
        private int id;
        private List<Member> members;

        /**
         * Constructor for the conversation data
         * @param id
         * @param members
         */
        Data(int id, List<Member> members) {
            this.id = id;
            this.members = members;
        }
    }

    /**
     * Constructor for the conversation DTO
     * @param type
     * @param data
     */
    ConversationDTO(String type, Data data) {
        this.type = type;
        this.data = data;
    }




}
