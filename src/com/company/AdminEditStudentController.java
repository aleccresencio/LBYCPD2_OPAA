package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminEditStudentController {
    public UserObject currentUser;
    public Button backButton, editAdviserButton, editGradesButton, deleteStudentButton;
    public Label userNameLabel, notifLabel;
    public ListView<String> studentsListView;
    public ObservableList<UserObject> studentList = FXCollections.observableArrayList();

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
        MySQLObject sql = new MySQLObject();
        studentList = sql.getAllStudents();
        studentsListView.setPlaceholder(new Label("There are no students in the database."));
        for(int i = 0; i < studentList.size(); i++) {
            studentsListView.getItems().add(studentList.get(i).getFirstName()+" "+studentList.get(i).getLastName());
        }
    }

    public void editAdviserButton(ActionEvent actionEvent) throws IOException {
        if(studentsListView.getSelectionModel().getSelectedItem() == null){
            notifLabel.setVisible(true);
            notifLabel.setText("No student selected.");
        }else {
            int chosenIndex = studentsListView.getSelectionModel().getSelectedIndex();
            int user_id = studentList.get(chosenIndex).getUser_id();
            int adviserId = studentList.get(chosenIndex).getAdviser();
            String firstName = studentList.get(chosenIndex).getFirstName();
            String lastName = studentList.get(chosenIndex).getLastName();
            Stage stage1 = (Stage) editAdviserButton.getScene().getWindow();
            stage1.close();
            //loads new stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminEditAdviserScreen.fxml"));
            Parent root = loader.load();
            //transfers the current user to other controller
            AdminEditAdviserController scene2Controller = loader.getController();
            scene2Controller.transferCurrentUser(currentUser, user_id, firstName, lastName, adviserId);
            //Show new scene in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("OPAA");
            stage.show();
        }
    }

    public void editGradesButton(ActionEvent actionEvent) throws IOException {
        if(studentsListView.getSelectionModel().getSelectedItem() == null){
            notifLabel.setVisible(true);
            notifLabel.setText("No student selected.");
        }else {
            int chosenIndex = studentsListView.getSelectionModel().getSelectedIndex();
            int user_id = studentList.get(chosenIndex).getUser_id();
            int adviserId = studentList.get(chosenIndex).getAdviser();
            String firstName = studentList.get(chosenIndex).getFirstName();
            String lastName = studentList.get(chosenIndex).getLastName();
            Stage stage1 = (Stage) editGradesButton.getScene().getWindow();
            stage1.close();
            //loads new stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminEditGrades.fxml"));
            Parent root = loader.load();
            //transfers the current user to other controller
            AdminEditGradesController scene2Controller = loader.getController();
            scene2Controller.transferCurrentUser(currentUser, user_id, firstName, lastName);
            //Show new scene in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("OPAA");
            stage.show();
        }
    }

    public void deleteStudentButton(ActionEvent actionEvent){
        if(studentsListView.getSelectionModel().getSelectedItem() == null){
            notifLabel.setVisible(true);
            notifLabel.setText("No student selected.");
        }else {
            int chosenIndex = studentsListView.getSelectionModel().getSelectedIndex();
            int user_id = studentList.get(chosenIndex).getUser_id();
            MySQLObject sql = new MySQLObject();
            sql.deleteStudent(user_id);
            studentList.remove(chosenIndex);
            studentsListView.getItems().remove(chosenIndex);
        }
    }

    public void backButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) editAdviserButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminHomeScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdminHomeController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }


}
