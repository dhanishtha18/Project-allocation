package org.projectapp.Model;

public class StudyResources {
    public String description;
    public String url;
    public String uploadedby;
    public String uploaderid;
    public String date;
    public String type;
    public String id;

    public StudyResources() {
    }

    public StudyResources(String description, String url, String uploadedby, String uploaderid, String date, String type, String id) {
        this.description = description;
        this.url = url;
        this.uploadedby = uploadedby;
        this.uploaderid = uploaderid;
        this.date = date;
        this.type = type;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUploaderid() {
        return uploaderid;
    }

    public void setUploaderid(String uploaderid) {
        this.uploaderid = uploaderid;
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
