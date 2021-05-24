package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminHomeController {
    public UserObject currentUser;
    public Button logoutButton, addStudentButton, editStudentButton, addAdviserButton, deleteAdviserButton, changePwButton;
    public Label userNameLabel;
    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) logoutButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginScreen.fxml"));
        Parent root = loader.load();
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void addStudentButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) addStudentButton.getScene().getWindow();
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

    public void addAdviserButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) addAdviserButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminAddAdviserScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdminAddAdviserController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void editStudentButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) addAdviserButton.getScene().getWindow();
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

    public void changePwButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) addAdviserButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("changePassword.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        ChangePasswordController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void deleteAdviserButton(ActionEvent actionEvent) throws IOException {
        Stage stage1 = (Stage) deleteAdviserButton.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminDeleteAdviserScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdminDeleteAdviserController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }
}
