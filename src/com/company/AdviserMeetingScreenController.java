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

public class AdviserMeetingScreenController {
    public buttonFunctions loadScreen;
    public UserObject currentUser;
    public Button viewRequestsButton, logoutButton, meetingsButton, calendarButton, profileButton, homeButton, gradesButton, setMeetingButton, removeButton;
    public Label userNameLabel, notifLabel;
    public ListView<String> meetingsListView;

    public void transferCurrentUser(UserObject currentUser) {
        ArrayList<String> meetingsList = new ArrayList<String>();
        this.currentUser = currentUser;
        MySQLObject loginObject = new MySQLObject();
        meetingsList = loginObject.checkMeetings(currentUser.getUser_id(), currentUser.getDivision());
        for(int i = 0; i < meetingsList.size(); i++) {
            meetingsListView.getItems().add(meetingsList.get(i));
        }
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

    public void setMeeting(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) setMeetingButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adviserSetMeetingScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdviserSetMeetingScreenController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }

    public void gradesButton(ActionEvent actionEvent) {
    }

    public void removeButton(ActionEvent actionEvent) {
        String selected = meetingsListView.getSelectionModel().getSelectedItem();
        int selectedIndex = meetingsListView.getSelectionModel().getSelectedIndex();
        if(selected.startsWith("You set a")){
            String meetingSched = selected.substring(selected.length() - 17);
            meetingSched = meetingSched.substring(0,8)+" "+meetingSched.substring(12);
            MySQLObject sql = new MySQLObject();
            sql.removeMeeting(currentUser.getUser_id(), meetingSched);
            meetingsListView.getItems().remove(selectedIndex);
            notifLabel.setVisible(true);
            notifLabel.setText("Successfully removed meeting!");
        }else{
            notifLabel.setVisible(true);
            notifLabel.setText("Cannot cancel a meeting set by a student");
        }
    }

    public void viewRequestsButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) viewRequestsButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adviserViewRequestsScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdviserViewRequestsController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }
}
