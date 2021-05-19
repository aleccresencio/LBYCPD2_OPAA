package com.company;

import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudentViewCalendarController {
    public buttonFunctions loadScreen;
    public UserObject currentUser;
    public Button logoutButton, meetingsButton, calendarButton, profileButton, homeButton;
    public DatePicker calendarInterface;
    public TableView<EventObject> eventTable;
    public TableColumn<EventObject, String> timeColumn;
    public TableColumn<EventObject, String> titleColumn;
    public ComboBox<String> hourDropDown, minDropDown;
    public TextField titleField;
    public Label notifLabel;

    public void transferCurrentUser(UserObject currentUser) {
        this.currentUser = currentUser;
        hourDropDown.getItems().add("00");
        hourDropDown.getItems().add("01");
        hourDropDown.getItems().add("02");
        hourDropDown.getItems().add("03");
        hourDropDown.getItems().add("04");
        hourDropDown.getItems().add("05");
        hourDropDown.getItems().add("06");
        hourDropDown.getItems().add("07");
        hourDropDown.getItems().add("08");
        hourDropDown.getItems().add("09");
        for(int i=10; i<=24; i++){
            hourDropDown.getItems().add(String.valueOf(i));
        }
        minDropDown.getItems().add("00");
        minDropDown.getItems().add("01");
        minDropDown.getItems().add("02");
        minDropDown.getItems().add("03");
        minDropDown.getItems().add("04");
        minDropDown.getItems().add("05");
        minDropDown.getItems().add("06");
        minDropDown.getItems().add("07");
        minDropDown.getItems().add("08");
        minDropDown.getItems().add("09");
        for(int i=10; i<=59; i++){
            minDropDown.getItems().add(String.valueOf(i));
        }
        hourDropDown.setVisibleRowCount(5);
        minDropDown.setVisibleRowCount(5);
    }
    public String formattedDate;
    public  ObservableList<EventObject> eventsList = FXCollections.observableArrayList();
    public void getDate(ActionEvent actionEvent) {

        LocalDate myDate = calendarInterface.getValue();
        formattedDate = myDate.format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        MySQLObject sql = new MySQLObject();
        eventsList = sql.getStudentEvents(currentUser.getUser_id(), formattedDate);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("eventTitle"));
        eventTable.setItems(eventsList);
        calendarInterface.show();
    }

    public void addEventButton(ActionEvent actionEvent){
        if(formattedDate == null){
            notifLabel.setText("Please pick a date for the event.");
            notifLabel.setVisible(true);
        }else if(hourDropDown.getSelectionModel().getSelectedItem()==null || minDropDown.getSelectionModel().getSelectedItem()==null){
            notifLabel.setText("Please pick a time for the event.");
            notifLabel.setVisible(true);
        }else if(titleField.getText() == null){
            notifLabel.setText("Please input a title for the event.");
            notifLabel.setVisible(true);
        }else if(titleField.getText().length()>100){
            notifLabel.setText("Event title is too long.");
            notifLabel.setVisible(true);
        } else{
            String title = titleField.getText();
            String eventSched = formattedDate+" "+hourDropDown.getSelectionModel().getSelectedItem()+":"+minDropDown.getSelectionModel().getSelectedItem();
            MySQLObject sql = new MySQLObject();
            int eventId = sql.addEvent(currentUser.getUser_id(), title, eventSched);
            eventsList.add(new EventObject(eventId, hourDropDown.getSelectionModel().getSelectedItem()+":"+minDropDown.getSelectionModel().getSelectedItem(), title));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("eventTitle"));
            eventTable.setItems(eventsList);
            hourDropDown.getSelectionModel().clearSelection();
            minDropDown.getSelectionModel().clearSelection();
            titleField.clear();
        }
    }

    public void removeButton(ActionEvent actionEvent){
        if(eventTable.getSelectionModel().getSelectedItem() == null){
            notifLabel.setVisible(true);
            notifLabel.setText("Please select an event");
        }else if(eventTable.getSelectionModel().getSelectedItem().getEvent_id() == 0){
            notifLabel.setVisible(true);
            notifLabel.setText("Can only remove events not meetings");
        }else{
            MySQLObject sql = new MySQLObject();
            sql.removeEvent(eventTable.getSelectionModel().getSelectedItem().getEvent_id());
            int selectedIndex = eventTable.getSelectionModel().getSelectedIndex();
            System.out.println(selectedIndex);
            eventTable.getItems().remove(selectedIndex);
        }
    }

    public void meetingsButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentMeetingButton(meetingsButton, currentUser);
    }

    public void calendarButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentCalendarButton(calendarButton, currentUser);
    }

    public void profileButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.profileButton(profileButton, currentUser);
    }

    public void homeButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.studentHomeButton(homeButton, currentUser);
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        buttonFunctions loadScreen = new buttonFunctions();
        loadScreen.logoutButton(logoutButton);
    }


}
