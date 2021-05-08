package com.company;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdviserViewGradesController {
    public buttonFunctions loadScreen;
    public UserObject currentUser;
    public Button logoutButton, meetingsButton, calendarButton, profileButton, homeButton, gradesButton;
    public Label userNameLabel, notifLabel;
    public ListView<String> studentsListView;
    public TableView<GradesObject> gradeTable;
    public TableColumn<GradesObject, String> courseColumn;
    public TableColumn<GradesObject, String> gradeColumn;
    public ObservableList<UserObject> studentList = FXCollections.observableArrayList();

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
        MySQLObject sql = new MySQLObject();
        studentList = sql.getStudents(currentUser.getUser_id());
        studentsListView.setPlaceholder(new Label("You have no advisees yet."));
        for(int i = 0; i < studentList.size(); i++) {
            studentsListView.getItems().add(studentList.get(i).getFirstName()+" "+studentList.get(i).getLastName());
        }
        studentsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                ObservableList<GradesObject> gradesList = FXCollections.observableArrayList();
                int chosenIndex = studentsListView.getSelectionModel().getSelectedIndex();
                int user_id = studentList.get(chosenIndex).getUser_id();
                String courseNames = sql.getCourseNamesOrGrades(user_id, "names");
                String courseGrades = sql.getCourseNamesOrGrades(user_id, "grades");
                String[] arrayNames = courseNames.split(", ", -2);
                String[] arrayGrades = courseGrades.split(", ", -2);
                List<String> listNames = Arrays.asList(arrayNames);
                List<String> listGrades = Arrays.asList(arrayGrades);
                for(int i = 0; i<listNames.size(); i++){
                    gradesList.add(new GradesObject(listNames.get(i), listGrades.get(i)));
                }
                courseColumn.setCellValueFactory(new PropertyValueFactory<>("course_names"));
                gradeColumn.setCellValueFactory(new PropertyValueFactory<>("course_grades"));
                gradeTable.setItems(gradesList);
            }
        });
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

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }

    public void gradesButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(gradesButton);
    }

}
