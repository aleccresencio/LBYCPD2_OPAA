package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AdviserViewRequestsController {
    public buttonFunctions loadScreen;
    public UserObject currentUser;
    public Button acceptButton, declineButton, logoutButton, meetingsButton, calendarButton, profileButton, homeButton, gradesButton;
    public Label userNameLabel, notifLabel;
    public ListView<String> requestsListView;

    public void transferCurrentUser(UserObject currentUser) {
        ArrayList<String> requestsList = new ArrayList<String>();
        this.currentUser = currentUser;
        MySQLObject loginObject = new MySQLObject();
        requestsList = loginObject.checkMeetings(currentUser.getUser_id(), currentUser.getDivision());
        for(int i = 0; i < requestsList.size(); i++) {
            requestsListView.getItems().add(requestsList.get(i));
        }
    }

    public void meetingsButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.adviserMeetingsButton(meetingsButton, currentUser);
    }

    public void calendarButton(ActionEvent actionEvent) {
    }

    public void profileButton(ActionEvent actionEvent) {
    }

    public void homeButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.adviserHomeButton(homeButton, currentUser);
    }

    public void acceptButton(ActionEvent actionEvent) throws IOException {
    }

    public void declineButton(ActionEvent actionEvent) throws IOException {
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }

    public void gradesButton(ActionEvent actionEvent) {
    }
}
