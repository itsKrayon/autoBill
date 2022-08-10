package com.ab.autoBill.domain;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Keyan
 */
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String username;
    @Column
    private String password;

    /**
     * 用户头像图片
     */
    @Column
    private String userImg = "默认值";
    @Column
    private boolean isValid;


    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Authority> authorities = new HashSet<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String user_img) {
        this.userImg = userImg;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}


