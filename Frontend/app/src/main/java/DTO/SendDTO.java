package DTO;

//{
//        "type": "harmonize.DTOs.MessageDTO",
//        "data": {
//        "conversation": {
//        "id": 1
//        },
//        "text": "Hello, World!"
//        }
//        }


public class SendDTO {
    private String type;
    private Data data;

    public SendDTO(String type, Data data) {
        this.type = type;
        this.data = data;
    }

    public static class Data {
        private Conversation conversation;
        private String text;

        public static class Conversation {
            private int id;

            public Conversation(int id) {
                this.id = id;
            }
        }

        public Data(Conversation convo, String text) {
            this.conversation = convo;
            this.text = text;
        }

    }
}
