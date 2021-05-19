package com.company;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminAddAdviserController {
    public UserObject currentUser;
    public Button logoutButton, addButton, backButton;
    public Label userNameLabel, notifLabel;
    public TextField firstNameField, lastNameField, emailField;

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
    }

    public void addButton(ActionEvent actionEvent) throws IOException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        if(firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || emailField.getText().isEmpty()){
            notifLabel.setVisible(true);
            notifLabel.setText("Please do not leave any blank items.");
        }else if(!firstNameField.getText().matches("^[ A-Za-z]+$") || !lastNameField.getText().matches("^[ A-Za-z]+$")){
            notifLabel.setVisible(true);
            notifLabel.setText("Name should only contain letters and spaces.");
        }else if(firstNameField.getText().length() > 19 || lastNameField.getText().length() > 19){
            notifLabel.setVisible(true);
            notifLabel.setText("First name and last name should only contain 20 characters.");
        } else if(!emailField.getText().endsWith("@dlsu.edu.ph")){
            notifLabel.setVisible(true);
            notifLabel.setText("Email must end with '@dlsu.edu.ph'");
        }else if(emailField.getText().length()>40){
            notifLabel.setVisible(true);
            notifLabel.setText("Email is too long.");
        }else{
            MySQLObject sql = new MySQLObject();
            boolean checkEmailDuplicate = sql.addAdviser(firstName, lastName, email);
            notifLabel.setVisible(true);
            if(checkEmailDuplicate == false){
                notifLabel.setText("There is already an adviser with the inputted email in the database");
            }else {
                notifLabel.setText("You have succesfully added " + firstNameField.getText() + " " + lastNameField.getText());
                firstNameField.clear();
                lastNameField.clear();
                emailField.clear();
            }
        }
    }

    public void backButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) backButton.getScene().getWindow();
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
