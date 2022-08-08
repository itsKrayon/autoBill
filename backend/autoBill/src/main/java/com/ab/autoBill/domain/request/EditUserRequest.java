package com.ab.autoBill.domain.request;


/**
 * @author Keyan
 */
public class EditUserRequest {
    private int id;
    private String username;
    private String password;

    public EditUserRequest(){}

    public EditUserRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
