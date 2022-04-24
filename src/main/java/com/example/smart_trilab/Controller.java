package com.example.smart_trilab;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.FXPermission;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private BorderPane borderpane;

    @FXML
    private Label localDate;

    @FXML
    private Label localTime;

    private final boolean stop = false;

    @FXML
    private Button dashboard;

    @FXML
    private Button clients;

    @FXML
    private Button suppliers;

    @FXML
    private Button depot;

    @FXML
    private Button order;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AnchorPane view = null;
        try {
            view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderpane.setCenter(view);
        Timenow();
    }




    @FXML
    private void clickDashboard(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("dashboard.fxml")));
        borderpane.setCenter(view);

    }



    @FXML
    private void clickOrder(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("order.fxml")));
        borderpane.setCenter(view);

    }

    @FXML
    private void clickDepot(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("depot.fxml")));
        borderpane.setCenter(view);

    }

    @FXML
    private void clickClients(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("clients.fxml")));
        borderpane.setCenter(view);

    }

    @FXML
    private void clickSuppliers(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("suppliers.fxml")));
        borderpane.setCenter(view);

    }

    private void Timenow(){
        Thread thread = new Thread(() ->{
            SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat sdfDate = new SimpleDateFormat("EEE, dd MMM yyyy");
            while(!stop){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    System.out.println(e);
                }
                final String timenow = sdfTime.format(new Date());
                final String today = sdfDate.format(new Date());
                Platform.runLater(()->{
                    localTime.setText(timenow);
                    localDate.setText(today);
                });
            }
        });
        thread.start();
    }


}

