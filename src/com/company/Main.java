package com.company;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
//hello
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loginScreen.fxml"));
        primaryStage.setTitle("OPAA");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }
    // hi
    public static void main(String[] args) {
        launch(args);
    }
}
