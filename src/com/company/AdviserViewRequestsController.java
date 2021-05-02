package com.company;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AdviserViewRequestsController {
    public buttonFunctions loadScreen;
    public UserObject currentUser;
    public Button acceptButton, declineButton, logoutButton, meetingsButton, calendarButton, profileButton, homeButton, gradesButton;
    public Label userNameLabel, notifLabel;
    public TableView<RequestObject> requestTable;
    public TableColumn<RequestObject, Integer> requestId;
    public TableColumn<RequestObject, String> firstName;
    public TableColumn<RequestObject, String> lastName;
    public TableColumn<RequestObject, String> date;
    public TableColumn<RequestObject, String> time;

    public void transferCurrentUser(UserObject currentUser) {
        ObservableList<RequestObject> requestsList = FXCollections.observableArrayList();
        this.currentUser = currentUser;
        MySQLObject sql = new MySQLObject();
        requestsList = sql.checkRequests(currentUser.getUser_id());
        requestId.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        requestTable.setItems(requestsList);
    }

    public void meetingsButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.adviserMeetingsButton(meetingsButton, currentUser);
    }

    public void calendarButton(ActionEvent actionEvent) {
    }

    public void profileButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.profileButton(profileButton, currentUser);
    }

    public void homeButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.adviserHomeButton(homeButton, currentUser);
    }

    public void acceptButton(ActionEvent actionEvent) throws IOException {
        RequestObject selected = requestTable.getSelectionModel().getSelectedItem();
        int selectedIndex = requestTable.getSelectionModel().getSelectedIndex();
        MySQLObject sql = new MySQLObject();
        sql.acceptRequest(selected.getRequestId(), selected.getFromId(), selected.getToId(), selected.getDate(), selected.getTime());
        requestTable.getItems().remove(selectedIndex);
        notifLabel.setVisible(true);
        notifLabel.setText("Meeting accepted!");
    }

    public void declineButton(ActionEvent actionEvent) throws IOException {
        RequestObject selected = requestTable.getSelectionModel().getSelectedItem();
        int selectedIndex = requestTable.getSelectionModel().getSelectedIndex();
        MySQLObject sql = new MySQLObject();
        sql.declineRequest(selected.getRequestId(), selected.getFromId(), selected.getToId(), selected.getDate(), selected.getTime());
        requestTable.getItems().remove(selectedIndex);
        notifLabel.setVisible(true);
        notifLabel.setText("Meeting declined!");
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }

    public void gradesButton(ActionEvent actionEvent) {
    }
}
