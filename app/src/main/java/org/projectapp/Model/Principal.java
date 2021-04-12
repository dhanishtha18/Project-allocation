package org.projectapp.Model;

public class Principal {
  private String id,name,login_id, password, email, phone,qualifiactions,expirence,profile_url,token;

  public Principal() {
  }

  public Principal(String id, String name, String login_id, String password, String email, String phone, String qualifiactions, String expirence, String profile_url, String token) {
    this.id = id;
    this.name = name;
    this.login_id = login_id;
    this.password = password;
    this.email = email;
    this.phone = phone;
    this.qualifiactions = qualifiactions;
    this.expirence = expirence;
    this.profile_url = profile_url;
    this.token = token;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getQualifiactions() {
    return qualifiactions;
  }

  public void setQualifiactions(String qualifiactions) {
    this.qualifiactions = qualifiactions;
  }

  public String getExpirence() {
    return expirence;
  }

  public void setExpirence(String expirence) {
    this.expirence = expirence;
  }

  public String getProfile_url() {
    return profile_url;
  }

  public void setProfile_url(String profile_url) {
    this.profile_url = profile_url;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
