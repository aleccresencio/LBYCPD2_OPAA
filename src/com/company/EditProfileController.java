package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class EditProfileController {

    public buttonFunctions loadScreen;
    public UserObject currentUser;

    public Button dpButton;
    public Button changePasswordButton;
    public Button backButton;

    public Label usernameLabel;
    public Label userRole;

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;

        if (currentUser.getDivision().equals("Student")) {
            usernameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            userRole.setText(currentUser.getDivision());

        } else if (currentUser.getDivision().equals("Adviser")) {
            usernameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
            userRole.setText(currentUser.getDivision());
        } else{
            usernameLabel.setText(currentUser.getFirstName());
            userRole.setVisible(false);
        }
    }

    public void changeDpButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
    }

    public void passwordButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.changePasswordButton(changePasswordButton, currentUser);
    }

    public void goBackButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        if (currentUser.getDivision().equals("Student")) loadScreen.studentHomeButton(backButton, currentUser);
        else if (currentUser.getDivision().equals("Adviser")) loadScreen.adviserHomeButton(backButton, currentUser);
        else{
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

}
