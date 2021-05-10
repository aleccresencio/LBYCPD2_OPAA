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
import java.util.Arrays;
import java.util.List;

public class AdminEditGradesController {
    public UserObject currentUser;
    public Button logoutButton, addGradeButton, backButton, removeButton;
    public Label studentNameLabel, notifLabel;
    public TextField courseCodeField;
    public ChoiceBox<String> gradeDropDown;
    public TableView<GradesObject> gradeTable;
    public TableColumn<GradesObject, String> courseColumn;
    public TableColumn<GradesObject, String> gradeColumn;
    public int userId;

    public void transferCurrentUser(UserObject currentUser, int userId, String firstName, String lastName) {
        //gets the user id of the newly added student
        studentNameLabel.setText("EDIT GRADES OF "+firstName+" "+lastName);
        this.userId = userId;
        this.currentUser = currentUser;
        MySQLObject sql = new MySQLObject();
        String courseNames = sql.getCourseNamesOrGrades(userId, "names");
        String courseGrades = sql.getCourseNamesOrGrades(userId, "grades");
        if(courseGrades != null && courseNames != null) {
            String[] arrayNames = courseNames.split(", ", -2);
            String[] arrayGrades = courseGrades.split(", ", -2);
            List<String> listNames = Arrays.asList(arrayNames);
            List<String> listGrades = Arrays.asList(arrayGrades);
            for (int i = 0; i < listNames.size(); i++) {
                gradesList.add(new GradesObject(listNames.get(i), listGrades.get(i)));
            }
            courseColumn.setCellValueFactory(new PropertyValueFactory<>("course_names"));
            gradeColumn.setCellValueFactory(new PropertyValueFactory<>("course_grades"));
            gradeTable.setItems(gradesList);
        }else{
            gradeTable.getItems().clear();
            gradeTable.setPlaceholder(new Label("The student has no grades yet"));
        }
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
    }
    public ObservableList<GradesObject> gradesList = FXCollections.observableArrayList();
    public void addGradeButton(ActionEvent actionEvent) {
        String courseName = courseCodeField.getText().toUpperCase();
        String grade = gradeDropDown.getSelectionModel().getSelectedItem();
        if(courseCodeField.getText().isEmpty()){
            notifLabel.setVisible(true);
            notifLabel.setText("Please specify the course name");
        }else {
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
                sql.addGrades(userId, courseName, grade);
                gradesList.add(new GradesObject(courseName, grade));
                courseColumn.setCellValueFactory(new PropertyValueFactory<>("course_names"));
                gradeColumn.setCellValueFactory(new PropertyValueFactory<>("course_grades"));
                gradeTable.setItems(gradesList);
                courseCodeField.clear();
            }
        }
    }

    public void backButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) backButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminEditStudentScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdminEditStudentController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void removeButton(ActionEvent actionEvent) {
        int selectedIndex = gradeTable.getSelectionModel().getSelectedIndex();
        gradeTable.getItems().remove(selectedIndex);
        GradesObject selected = gradeTable.getSelectionModel().getSelectedItem();
        String courseName = selected.getCourse_names();
        String courseGrade = selected.getCourse_grades();
        gradesList.remove(new GradesObject(courseName, courseGrade));
        ArrayList<String> courseNameList = new ArrayList<String>();
        ArrayList<String> courseGradeList = new ArrayList<String>();
        for(int i=0; i<gradesList.size(); i++){
            courseNameList.add(gradesList.get(i).getCourse_names());
            courseGradeList.add(gradesList.get(i).getCourse_grades());
        }
        String newCourseNames = String.join(", ", courseNameList);
        String newCourseGrades = String.join(", ", courseGradeList);
        MySQLObject sql = new MySQLObject();
        //removes the chosen grade from the database
        sql.removeGrades(userId, newCourseNames, newCourseGrades);
    }
}
