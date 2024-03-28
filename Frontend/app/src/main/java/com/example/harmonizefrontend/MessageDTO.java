package com.example.harmonizefrontend;

/**
 * DTO for message data incoming/outgoing from the server
 */
public class MessageDTO {
    private String type;
    private Data data;
    private String text;

    public static class Data {
        private int id;
        private long unixTime;
        private Member sender;
        private ConversationDTO convo;

        /**
         * Constructor for the message data
         * @param id
         * @param unixTime
         * @param sender
         * @param convo
         */
        Data(int id, long unixTime, Member sender, ConversationDTO convo) {
            this.id = id;
            this.unixTime = unixTime;
            this.sender = sender;
            this.convo = convo;
        }

        /**
         * Getters
         */
        protected int getDataId() {
            return id;
        }

        protected long getDataUnixTime() {
            return unixTime;
        }

        protected Member getDataSender() {
            return sender;
        }

        protected ConversationDTO getDataConversationDTO() {
            return convo;
        }
    }


    /**
     * Constructor for the message DTO
     * @param type
     * @param data
     * @param text
     */
    MessageDTO(String type, Data data, String text) {
        this.type = type;
        this.data = data;
        this.text = text;
    }

    // Getters
    protected String getType() {
        return type;
    }

    protected Data getData() {
        return data;
    }

    protected String getText() {
        return text;
    }







}
