package com.tiqndt.yogaadminapp;

public class ClassInstance {
    private int id;
    private String date;
    private String teacherName;
    private String comments;
    private int courseId;

    // Constructors
    public ClassInstance() {
    }

    public ClassInstance(String date, String teacherName, String comments, int courseId) {
        this.date = date;
        this.teacherName = teacherName;
        this.comments = comments;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}