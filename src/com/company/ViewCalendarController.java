package com.company;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.io.IOException;

public class ViewCalendarController {
    public buttonFunctions loadScreen;
    public UserObject currentUser;
    public Button logoutButton, meetingsButton, calendarButton, profileButton, homeButton;
    public DatePicker calendarInterface;

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
        calendarInterface.show();
    }

    public void meetingsButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentMeetingButton(meetingsButton, currentUser);
    }

    public void calendarButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.calendarButton(calendarButton, currentUser);
    }

    public void profileButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.profileButton(profileButton, currentUser);
    }

    public void homeButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentHomeButton(homeButton, currentUser);
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }

    public void gradesButton(ActionEvent actionEvent) {

    }
}
