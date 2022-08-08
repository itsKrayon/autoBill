package com.ab.autoBill.domain.request;

public class AssignmentRequest {
    private String id;

    private String title;

    private String description;

    private String deadline; //作业的截止日期，具体到秒

    private String sectionId;//关联的具体课程ID


    public AssignmentRequest() {
    }

    public AssignmentRequest(String title, String description, String deadline, String sectionId) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.sectionId = sectionId;
    }

    public AssignmentRequest(String id, String title, String description, String deadline, String sectionId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.sectionId = sectionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
