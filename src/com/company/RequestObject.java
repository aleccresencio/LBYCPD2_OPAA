package com.company;

public class RequestObject {
    private int requestId;
    private int fromId;
    private int toId;
    private String firstName;
    private String lastName;
    private String date;
    private String time;

    public RequestObject(int requestId, int fromId, int toId, String firstName, String lastName, String date, String time){
        this.requestId = requestId;
        this.fromId = fromId;
        this.toId = toId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.time = time;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
