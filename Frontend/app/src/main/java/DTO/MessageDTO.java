package DTO;

import UserInfo.Member;

/**
 * DTO for message data incoming/outgoing from the server
 */
public class MessageDTO {
    private String type;
    private Data data;


    public static class Data {
        private int id;
        private long time;
        private Member sender;
        private ConversationDTO.Data conversation;

        private String text;

        /**
         * Constructor for the message data
         * @param id
         * @param unixTime
         * @param sender
         * @param convo
         */
        public Data(int id, long unixTime, Member sender, ConversationDTO.Data conversation, String text) {
            this.id = id;
            this.time = unixTime;
            this.sender = sender;
            this.conversation = conversation;
            this.text = text;
        }

        /**
         * Getters
         */
        public int getDataId() {
            return id;
        }

        public long getDataUnixTime() {
            return time;
        }

        public Member getDataSender() {
            return sender;
        }

        public ConversationDTO.Data getDataConversation() {
            return conversation;
        }

        public String getText() {
            return text;
        }
    }


    /**
     * Constructor for the message DTO
     * @param type
     * @param data
     */
    public MessageDTO(String type, Data data) {
        this.type = type;
        this.data = data;
    }

    // Getters
    public String getType() {
        return type;
    }

    public Data getData() {
        return data;
    }

    public String getText() {
        return this.getData().getText();
    }

}
