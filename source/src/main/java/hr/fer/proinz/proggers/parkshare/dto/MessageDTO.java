package hr.fer.proinz.proggers.parkshare.dto;

public class MessageDTO {
    private String msg;
    private String desc;

    public MessageDTO() {
    }

    public MessageDTO(String msg, String desc) {
        this.msg = msg;
        this.desc = desc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
