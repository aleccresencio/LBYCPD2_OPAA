package com.company;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
        else loadScreen.adviserHomeButton(backButton, currentUser);
    }

}
