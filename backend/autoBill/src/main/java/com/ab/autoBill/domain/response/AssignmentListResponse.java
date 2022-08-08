package com.ab.autoBill.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class AssignmentListResponse {
    private Long id;

    private Long assignmentId;

    private String title;

    private String description;

    private int status;//已提交未批改=0，未提交=1，已提交已批改=2

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date submitDatetime;//提交作业的时间

    private int isExpired;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deadline ;

    private String comment;

    private String file;

    public AssignmentListResponse() {
    }

    public AssignmentListResponse(Long id, String title, String description, int status, Date submitDatetime, int isExpired, Date deadline, String comment) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.submitDatetime = submitDatetime;
        this.isExpired = isExpired;
        this.deadline = deadline;
        this.comment = comment;
    }

    public AssignmentListResponse(Long id,String file,Long assignmentId, String title, String description, int status, Date submitDatetime, int isExpired, Date deadline, String comment) {
        this.id = id;
        this.file = file;
        this.assignmentId = assignmentId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.submitDatetime = submitDatetime;
        this.isExpired = isExpired;
        this.deadline = deadline;
        this.comment = comment;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getSubmitDatetime() {
        return submitDatetime;
    }

    public void setSubmitDatetime(Date submitDatetime) {
        this.submitDatetime = submitDatetime;
    }

    public int getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(int isExpired) {
        this.isExpired = isExpired;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }
}
