package com.company;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class buttonFunctions {
    public void adviserHomeButton(Button button, UserObject currentUser) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adviserHomeScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdviserHomeScreenController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void adviserMeetingsButton(Button button, UserObject currentUser) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adviserMeetingScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdviserMeetingScreenController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void studentHomeButton(Button button, UserObject currentUser) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentHomeScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        StudentHomeScreenController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void studentMeetingButton(Button button, UserObject currentUser) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentMeetingScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        StudentMeetingsScreenController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void studentCalendarButton(Button button, UserObject currentUser) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentViewCalendar.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        StudentViewCalendarController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void adviserCalendarButton(Button button, UserObject currentUser) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adviserViewCalendar.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdviserViewCalendarController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void profileButton(Button button, UserObject currentUser) throws  IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editProfile.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        EditProfileController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

    public void changePasswordButton(Button button, UserObject currentUser) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
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

    public void logoutButton(Button button) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
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

    public void gradesButton(Button button, UserObject currentUser) throws IOException {
        Stage stage1 = (Stage) button.getScene().getWindow();
        stage1.close();
        //loads new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adviserViewGradesScreen.fxml"));
        Parent root = loader.load();
        //transfers the current user to other controller
        AdviserViewGradesController scene2Controller = loader.getController();
        scene2Controller.transferCurrentUser(currentUser);
        //Show new scene in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root,1000,600));
        stage.setTitle("OPAA");
        stage.show();
    }

}
