package DTO;

import UserInfo.Member;

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
        public Data(int id, long unixTime, Member sender, ConversationDTO convo) {
            this.id = id;
            this.unixTime = unixTime;
            this.sender = sender;
            this.convo = convo;
        }

        /**
         * Getters
         */
        public int getDataId() {
            return id;
        }

        public long getDataUnixTime() {
            return unixTime;
        }

        public Member getDataSender() {
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
    public MessageDTO(String type, Data data, String text) {
        this.type = type;
        this.data = data;
        this.text = text;
    }

    // Getters
    protected String getType() {
        return type;
    }

    public Data getData() {
        return data;
    }

    public String getText() {
        return text;
    }







}
