package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdviserSetMeetingScreenController {
    public UserObject currentUser;
    public buttonFunctions loadScreen;
    public Button logoutButton, meetingsButton, calendarButton, profileButton, homeButton, confirmButton, gradesButton;
    public Label userNameLabel, notifLabel;
    public TextField monthField, dayField, yearField, hourField, minuteField;
    public ListView<String> meetingsListView;

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
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

    public void confirmButton(ActionEvent actionEvent) {
        String meetingSched = monthField.getText()+"/"+dayField.getText()+"/"+yearField.getText()+" "+hourField.getText()+":"+minuteField.getText();
        MySQLObject sql = new MySQLObject();
        sql.setMeeting(currentUser.getUser_id(), meetingSched);
        //clears textfields
        monthField.clear();
        dayField.clear();
        yearField.clear();
        hourField.clear();
        minuteField.clear();
        notifLabel.setText("Succesfully set a meeting!");
        notifLabel.setVisible(true);
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }

    public void gradesButton(ActionEvent actionEvent) {
    }
}
