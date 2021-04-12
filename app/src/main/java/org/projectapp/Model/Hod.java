package org.projectapp.Model;

import java.util.ArrayList;

public class Hod {
    private String name,login_id,password,department,email,phone, id,status,account,search,profile_url,qualification,experience,token;
    private ArrayList<String> subjects;
    public Hod() {
    }

    public Hod(String name, String login_id, String password, String department, String email, String phone, String id, String status, String account, String search, String profile_url, String qualification, String experience, String token, ArrayList<String> subjects) {
        this.name = name;
        this.login_id = login_id;
        this.password = password;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.status = status;
        this.account = account;
        this.search = search;
        this.profile_url = profile_url;
        this.qualification = qualification;
        this.experience = experience;
        this.token = token;
        this.subjects = subjects;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }
}
