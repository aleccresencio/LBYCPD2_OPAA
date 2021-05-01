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

public class AdviserHomeScreenController {
    public UserObject currentUser;
    public Button logoutButton, meetingsButton, calendarButton, profileButton, homeButton, gradesButton;
    public Label userNameLabel;
    public ListView<String> notifView;
    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
        userNameLabel.setText("Welcome "+ currentUser.getFirstName()+" "+currentUser.getLastName()+"!");
        ArrayList<String> notifList = new ArrayList<>();
        MySQLObject sql = new MySQLObject();
        notifList = sql.studentNotifs(currentUser.getUser_id());
        for(int i = 0; i < notifList.size(); i++) {
            notifView.getItems().add(notifList.get(i));
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

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }

    public void gradesButton(ActionEvent actionEvent) {
    }
}
