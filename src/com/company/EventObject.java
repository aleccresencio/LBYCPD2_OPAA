package com.company;

import javafx.event.Event;

public class EventObject {
    private int event_id = 0;
    private String time;
    private String eventTitle;

    EventObject(int event_id, String time, String eventTitle){
        this.event_id = event_id;
        this.time = time;
        this.eventTitle = eventTitle;
    }

    EventObject(String time, String eventTitle){
        this.time = time;
        this.eventTitle = eventTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public int getEvent_id(){
        return event_id;
    }
}
