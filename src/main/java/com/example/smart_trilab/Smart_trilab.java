package com.example.smart_trilab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;

public class Smart_trilab extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Smart_trilab.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1430, 850);
//        stage.setFullScreen(true);
        stage.setTitle("Smart Trilab");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}