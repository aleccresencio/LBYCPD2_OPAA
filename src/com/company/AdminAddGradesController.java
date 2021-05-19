package com.company;

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

public class AdminAddGradesController {
    public UserObject currentUser;
    public Button logoutButton, addGradeButton, backButton, removeButton;
    public Label userNameLabel, notifLabel;
    public TextField courseCodeField;
    public ComboBox<String> gradeDropDown;
    public TableView<GradesObject> gradeTable;
    public TableColumn<GradesObject, String> courseColumn;
    public TableColumn<GradesObject, String> gradeColumn;
    public int newUserId;

    public void transferCurrentUser(UserObject currentUser, int newUserId) {
        //gets the user id of the newly addes student
        this.newUserId = newUserId;
        this.currentUser = currentUser;
        MySQLObject sql = new MySQLObject();
        ArrayList<String> gradesList = new ArrayList<>();
        gradesList.add("4.0");
        gradesList.add("3.5");
        gradesList.add("3.0");
        gradesList.add("2.5");
        gradesList.add("2.0");
        gradesList.add("1.5");
        gradesList.add("1.0");
        gradesList.add("0.0");
        for(int i = 0; i<gradesList.size();i++) {
            gradeDropDown.getItems().add(gradesList.get(i));
        }
        gradeDropDown.setVisibleRowCount(3);
    }
    public ObservableList<GradesObject> gradesList = FXCollections.observableArrayList();
    public void addGradeButton(ActionEvent actionEvent) {
        if(gradeDropDown.getSelectionModel().getSelectedItem() == null){
            notifLabel.setVisible(true);
            notifLabel.setText("Please input a grade.");
        }else {
            String courseName = courseCodeField.getText().toUpperCase();
            String grade = gradeDropDown.getSelectionModel().getSelectedItem();
            if (courseCodeField.getText().isEmpty()) {
                notifLabel.setVisible(true);
                notifLabel.setText("Please specify the course name");
            } else if(!courseCodeField.getText().matches("[A-Za-z0-9]+") || courseCodeField.getText().length()>99){
                notifLabel.setVisible(true);
                notifLabel.setText("Invalid course code.");
            } else {
                boolean checker = true;
                for (int i = 0; i < gradesList.size(); i++) {
                    if (courseName.equals(gradesList.get(i).getCourse_names())) {
                        checker = false;
                    }
                }
                if (checker == false) {
                    notifLabel.setVisible(true);
                    notifLabel.setText("There is already a grade for " + courseName);
                } else if (checker == true) {
                    notifLabel.setVisible(false);
                    MySQLObject sql = new MySQLObject();
                    sql.addGrades(newUserId, courseName, grade);
                    gradesList.add(new GradesObject(courseName, grade));
                    courseColumn.setCellValueFactory(new PropertyValueFactory<>("course_names"));
                    gradeColumn.setCellValueFactory(new PropertyValueFactory<>("course_grades"));
                    gradeTable.setItems(gradesList);
                    courseCodeField.clear();
                }
            }
        }
    }

    public void backButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) backButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminAddStudentScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdminAddStudentController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void removeButton(ActionEvent actionEvent) {
        if (gradeTable.getSelectionModel().getSelectedItem() == null) {
            notifLabel.setVisible(true);
            notifLabel.setText("No grade selected.");
        } else {
            int selectedIndex = gradeTable.getSelectionModel().getSelectedIndex();
            gradeTable.getItems().remove(selectedIndex);
            GradesObject selected = gradeTable.getSelectionModel().getSelectedItem();
            String courseName = selected.getCourse_names();
            String courseGrade = selected.getCourse_grades();
            gradesList.remove(new GradesObject(courseName, courseGrade));
            ArrayList<String> courseNameList = new ArrayList<String>();
            ArrayList<String> courseGradeList = new ArrayList<String>();
            for (int i = 0; i < gradesList.size(); i++) {
                courseNameList.add(gradesList.get(i).getCourse_names());
                courseGradeList.add(gradesList.get(i).getCourse_grades());
            }
            String newCourseNames = String.join(", ", courseNameList);
            String newCourseGrades = String.join(", ", courseGradeList);
            MySQLObject sql = new MySQLObject();
            //removes the chosen grade from the database
            sql.removeGrades(newUserId, newCourseNames, newCourseGrades);
        }
    }
}
