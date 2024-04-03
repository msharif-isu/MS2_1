package DTO;

public abstract class AbstractDTO {

    private String type;

    private Data Data;

    public AbstractDTO(String type, Data Data) {
        this.type = type;
        this.Data = Data;
    }

    public abstract class Data {
        private int id;

        public Data(int id) {
            this.id = id;
        }


    }
}
