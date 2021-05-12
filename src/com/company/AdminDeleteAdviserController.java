package com.company;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDeleteAdviserController {
    public UserObject currentUser;
    public Button backButton, editAdviserButton, editGradesButton, deleteAdviserButton;
    public Label userNameLabel, notifLabel;
    public ListView<String> adviserListView;
    public ObservableList<UserObject> adviserList = FXCollections.observableArrayList();
    public ComboBox<String> replaceDropDown;
    public ObservableList<UserObject> replacementAdvisers = FXCollections.observableArrayList();
    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
        MySQLObject sql = new MySQLObject();
        adviserList = sql.getAdvisers();
        adviserListView.setPlaceholder(new Label("There are no advisers in the database."));
        for(int i = 0; i < adviserList.size(); i++) {
            adviserListView.getItems().add(adviserList.get(i).getFirstName()+" "+adviserList.get(i).getLastName());
        }
        adviserListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                replaceDropDown.getItems().clear();
                int selected = adviserListView.getSelectionModel().getSelectedIndex();
                int selectedAdviserId = adviserList.get(selected).getUser_id();
                replacementAdvisers = sql.getPossibleAdvisers(selectedAdviserId);
                for(int i = 0; i<replacementAdvisers.size(); i++){
                    replaceDropDown.getItems().add(replacementAdvisers.get(i).getFirstName()+" "+replacementAdvisers.get(i).getLastName());
                }
            }
        });
    }

    public void deleteAdviserButton(ActionEvent actionEvent) {
        if(adviserListView.getSelectionModel().getSelectedItem() == null){
            notifLabel.setVisible(true);
            notifLabel.setText("No adviser selected.");
        }else if(replaceDropDown.getSelectionModel().getSelectedItem() == null){
            notifLabel.setVisible(true);
            notifLabel.setText("Select an adviser where the advisees of the adviser who is about to deleted will go to.");
        } else {
            int chosenIndex = adviserListView.getSelectionModel().getSelectedIndex();
            int user_id = adviserList.get(chosenIndex).getUser_id();
            int replaceIndex = replaceDropDown.getSelectionModel().getSelectedIndex();
            MySQLObject sql = new MySQLObject();
            sql.deleteAdviser(user_id, replacementAdvisers.get(replaceIndex).getUser_id());
            adviserList.remove(chosenIndex);
            adviserListView.getItems().remove(chosenIndex);
            adviserListView.getSelectionModel().clearSelection();
            replaceDropDown.getSelectionModel().clearSelection();
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
