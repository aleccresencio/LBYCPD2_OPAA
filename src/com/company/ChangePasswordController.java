package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangePasswordController {
    public UserObject currentUser;

    public Button updatePasswordButton;

    public Label usernameLabel;

    public PasswordField oldPasswordFld;
    public PasswordField newPasswordFld;
    public PasswordField confirmPasswordFld;

    public Label errorLabel;

    public Button backButton;

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;

        if (currentUser.getDivision().equals("Student")) {
            usernameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());

        } else if (currentUser.getDivision().equals("Adviser")) {
            usernameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        }else{
            usernameLabel.setText(currentUser.getFirstName());
        }
    }

    public void handleUpdatePassword() {
        // Gets inputs from text fields
        String oldPassword = oldPasswordFld.getText();
        String newPassword = newPasswordFld.getText();
        String confirmPassword = confirmPasswordFld.getText();

        // Checker for Mis-inputs
        if ((oldPasswordFld.getText().isEmpty()) || (newPasswordFld.getText().isEmpty()) || (confirmPasswordFld.getText().isEmpty())) {
            errorLabel.setVisible(true);
            errorLabel.setText("Incomplete Input. Please fill up all text fields.");
        }

        else if (!oldPassword.equals(currentUser.getPassword())) {
            errorLabel.setVisible(true);
            errorLabel.setText("Incorrect old Password entered.");
        }

        else if (!newPassword.equals(confirmPassword)) {
            errorLabel.setVisible(true);
            errorLabel.setText("Password entered does not match.");
        }

        else if (oldPassword.equals(newPassword)) {
            errorLabel.setVisible(true);
            errorLabel.setText("Same Password Inputted. No changes made.");
        }

        else {
            MySQLObject password = new MySQLObject();
            password.updatePassword(newPassword, currentUser.getUser_id());

            errorLabel.setVisible(true);
            errorLabel.setText("Successfully updated password.");
        }

        //clears the text fields
        oldPasswordFld.clear();
        newPasswordFld.clear();
        confirmPasswordFld.clear();
    }

    public void goBackButton(ActionEvent actionEvent) throws IOException {
        if(currentUser.getDivision().equals("Admin")){
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
        }else{
            buttonFunctions loadScreen = new buttonFunctions();
            loadScreen.profileButton(backButton, currentUser);
        }
    }
}
