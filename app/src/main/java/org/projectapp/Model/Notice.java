package org.projectapp.Model;

public class Notice {
    public String title;
    public String url;
    public String uploadedby;
    public String date;
    public String type;
    public String id;

    public Notice() {
    }

    public Notice(String title, String url, String uploadedby, String date, String type, String id) {
        this.title = title;
        this.url = url;
        this.uploadedby = uploadedby;
        this.date = date;
        this.type = type;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploadedby() {
        return uploadedby;
    }

    public void setUploadedby(String uploadedby) {
        this.uploadedby = uploadedby;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
