package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StudentRequestMeetingScreenController {
    public buttonFunctions loadScreen;
    public UserObject currentUser;
    public Button logoutButton, meetingsButton, calendarButton, profileButton, homeButton, confirmButton;
    public Label userNameLabel, notifLabel;
    public TextField monthField, dayField, yearField, hourField, minuteField;
    public DatePicker datePicker;
    public ComboBox<String> hourDropDown, minDropDown;

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
        hourDropDown.getItems().add("00");
        hourDropDown.getItems().add("01");
        hourDropDown.getItems().add("02");
        hourDropDown.getItems().add("03");
        hourDropDown.getItems().add("04");
        hourDropDown.getItems().add("05");
        hourDropDown.getItems().add("06");
        hourDropDown.getItems().add("07");
        hourDropDown.getItems().add("08");
        hourDropDown.getItems().add("09");
        for(int i=10; i<=24; i++){
            hourDropDown.getItems().add(String.valueOf(i));
        }
        minDropDown.getItems().add("00");
        minDropDown.getItems().add("01");
        minDropDown.getItems().add("02");
        minDropDown.getItems().add("03");
        minDropDown.getItems().add("04");
        minDropDown.getItems().add("05");
        minDropDown.getItems().add("06");
        minDropDown.getItems().add("07");
        minDropDown.getItems().add("08");
        minDropDown.getItems().add("09");
        for(int i=10; i<=59; i++){
            minDropDown.getItems().add(String.valueOf(i));
        }
        hourDropDown.setVisibleRowCount(5);
        minDropDown.setVisibleRowCount(5);
    }

    public void meetingsButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentMeetingButton(meetingsButton, currentUser);
    }

    public void calendarButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentCalendarButton(calendarButton, currentUser);
    }

    public void profileButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.profileButton(profileButton, currentUser);
    }

    public void homeButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentHomeButton(homeButton, currentUser);
    }

    String meetingDate;
    public void getDate(ActionEvent actionEvent){
        LocalDate myDate = datePicker.getValue();
        String formattedDate = myDate.format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        this.meetingDate = formattedDate;
    }

    public void confirmButton(ActionEvent actionEvent) {
        if(meetingDate == null){
            notifLabel.setText("Please pick a date for your meeting.");
            notifLabel.setVisible(true);
        }else if(hourDropDown.getSelectionModel().getSelectedItem()==null || minDropDown.getSelectionModel().getSelectedItem()==null){
            notifLabel.setText("Please pick a time for your meeting.");
            notifLabel.setVisible(true);
        }else {
            String requestSched = meetingDate+" "+hourDropDown.getSelectionModel().getSelectedItem()+":"+minDropDown.getSelectionModel().getSelectedItem();
            MySQLObject sql = new MySQLObject();
            String notif = sql.sendRequest(currentUser.getUser_id(), currentUser.getAdviser(), requestSched);
            //clears inputs
            datePicker.getEditor().clear();
            hourDropDown.getSelectionModel().clearSelection();
            minDropDown.getSelectionModel().clearSelection();
            notifLabel.setText(notif);
            notifLabel.setVisible(true);
        }
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }
}
