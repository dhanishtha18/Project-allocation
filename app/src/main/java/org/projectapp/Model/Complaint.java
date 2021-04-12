package org.projectapp.Model;

public class Complaint {
    private String sender;
    private String message;
    private String time;
    private String url;
    private String type;
    private String id;
    private String msgtype;
    private String name;

    public Complaint() {
    }

    public Complaint(String sender, String message, String time, String url, String type, String id, String msgtype, String name) {
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.url = url;
        this.type = type;
        this.id = id;
        this.msgtype = msgtype;
        this.name = name;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
