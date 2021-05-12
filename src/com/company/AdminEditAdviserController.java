package com.company;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminEditAdviserController {
    public UserObject currentUser;
    public Button logoutButton, addButton, backButton, confirmButton;
    public Label studentNameLabel, notifLabel, adviserNameLabel;
    public TextField firstNameField, lastNameField, emailField;
    public ComboBox<String> adviserDropDown;
    public int user_id, adviser_id;
    public String firstName, lastName;
    ObservableList<UserObject> adviserList;

    public void transferCurrentUser(UserObject currentUser, int userId, String firstName, String lastName, int adviserId) {
        this.adviser_id = adviserId;
        this.currentUser = currentUser;
        this.user_id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        studentNameLabel.setText(firstName+" "+lastName);
        MySQLObject sql = new MySQLObject();
        adviserNameLabel.setText(sql.getAdviserName(adviserId));
        adviserList = sql.getPossibleAdvisers(adviserId);
        for(int i = 0; i<adviserList.size();i++) {
            adviserDropDown.getItems().add(adviserList.get(i).getFirstName()+" "+adviserList.get(i).getLastName());
        }
        adviserDropDown.setVisibleRowCount(5);
    }

    public void confirmButton(ActionEvent actionEvent) throws IOException {
        if(adviserDropDown.getSelectionModel().getSelectedItem() == null){
            notifLabel.setVisible(true);
            notifLabel.setText("No adviser selected.");
        }else {
            int newAdviser = adviserList.get(adviserDropDown.getSelectionModel().getSelectedIndex()).getUser_id();
            MySQLObject sql = new MySQLObject();
            sql.changeAdviser(user_id, newAdviser);
            notifLabel.setVisible(true);
            notifLabel.setText("Succesfully changed adviser");

            //load screen where admin can add grades of the new student
            Stage stage1 = (Stage) confirmButton.getScene().getWindow();
            stage1.close();
            //loads new stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminEditAdviserScreen.fxml"));
            Parent root = loader.load();
            //transfers the current user to other controller
            AdminEditAdviserController scene2Controller = loader.getController();
            scene2Controller.transferCurrentUser(currentUser, user_id, firstName, lastName, newAdviser);
            //Show new scene in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1000, 600));
            stage.setTitle("OPAA");
            stage.show();
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

}
