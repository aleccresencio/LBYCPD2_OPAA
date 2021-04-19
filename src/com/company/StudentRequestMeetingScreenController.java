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
import java.util.ArrayList;

public class StudentRequestMeetingScreenController {
    public buttonFunctions loadScreen;
    public UserObject currentUser;
    public Button logoutButton, meetingsButton, calendarButton, profileButton, homeButton, confirmButton;
    public Label userNameLabel;
    public TextField monthField, dayField, yearField, hourField, minuteField;
    public ListView<String> meetingsListView;

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
    }

    public void meetingsButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentMeetingButton(meetingsButton, currentUser);
    }

    public void calendarButton(ActionEvent actionEvent) {
    }

    public void profileButton(ActionEvent actionEvent) {
    }

    public void homeButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentHomeButton(homeButton, currentUser);
    }

    public void confirmButton(ActionEvent actionEvent) {
        String requestSched = monthField.getText()+"/"+dayField.getText()+"/"+yearField.getText()+" "+hourField.getText()+":"+minuteField.getText();
        MySQLObject sql = new MySQLObject();
        sql.sendRequest(currentUser.getUser_id(), currentUser.getAdviser(), requestSched);
        monthField.clear();
        dayField.clear();
        yearField.clear();
        hourField.clear();
        minuteField.clear();
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }
}
