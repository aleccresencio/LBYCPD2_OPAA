package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    public TextField usernameField;
    public PasswordField pwField;
    public Label incorrectLabel;
    public UserObject currentUser;
    public Button loginButton;

    public void loginButton(ActionEvent actionEvent) throws IOException {
        //hello
        //gets username and password from the text fields
        String user = usernameField.getText();
        String pw = pwField.getText();
        //creates login object
        MySQLObject login = new MySQLObject();
        //function checks the database if the user is in it
        currentUser = login.checkLogin(user, pw);
        //clears the text fields
        usernameField.clear();
        pwField.clear();
        //determines what type of user has logged in
        if(currentUser == null){
            incorrectLabel.setVisible(true);
            System.out.println("An error has occured");
        } else if(currentUser.getDivision().equals("Student")) {
            Stage stage1 = (Stage) loginButton.getScene().getWindow();
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
            System.out.println(currentUser.getDivision());
        } else if(currentUser.getDivision().equals("Adviser")) {
            Stage stage1 = (Stage) loginButton.getScene().getWindow();
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
            System.out.println(currentUser.getDivision());
        } else if(currentUser.getDivision().equals("Admin")) {
            Stage stage1 = (Stage) loginButton.getScene().getWindow();
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
            System.out.println(currentUser.getDivision());
        }
    }
}
