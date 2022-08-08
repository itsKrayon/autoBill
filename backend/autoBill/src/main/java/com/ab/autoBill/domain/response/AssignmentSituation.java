package com.ab.autoBill.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AssignmentSituation {
    private Long assignmentListId;
    private String studentName;
    int status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    Date submitTime;

    public AssignmentSituation(Long assignmentListId, String studentName, int status) {
        this.assignmentListId = assignmentListId;
        this.studentName = studentName;
        this.status = status;
    }

    public AssignmentSituation() {
    }

    public Long getAssignmentListId() {
        return assignmentListId;
    }

    public void setAssignmentListId(Long assignmentListId) {
        this.assignmentListId = assignmentListId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }
}
