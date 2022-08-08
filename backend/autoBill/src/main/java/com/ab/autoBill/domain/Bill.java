package com.ab.autoBill.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Keyan
 */
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String transType;

    @Column
    private String transParty;

    @Column
    private String goods;
    @Column
    private int amount;

    @Column
    private boolean isSpending;

    @Column
    private String payMethod;

    @Column
    private String currState;

    @Column
    private String remark;

    @Column
    private String month;


    @Column
    private String type;


    @Column
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private Date transTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Bill() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransParty() {
        return transParty;
    }

    public void setTransParty(String transParty) {
        this.transParty = transParty;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isSpending() {
        return isSpending;
    }

    public void setSpending(boolean spending) {
        isSpending = spending;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getCurrState() {
        return currState;
    }

    public void setCurrState(String currState) {
        this.currState = currState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
