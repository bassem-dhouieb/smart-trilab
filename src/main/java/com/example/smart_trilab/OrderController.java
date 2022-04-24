package com.example.smart_trilab;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private TextField message_field;

    @FXML
    private Button send_btn;

    @FXML
    private ScrollPane sp_main;

    @FXML
    private VBox vbox_messages;


    Server server;

    public  VBox getVbox_messages() {
        return vbox_messages;
    }

    public void setVbox_messages(VBox vbox_messages) {
        this.vbox_messages = vbox_messages;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server = new Server(new ServerSocket(9000),vbox_messages);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            System.out.println("server lunch");



        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });


        send_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = message_field.getText();
                if (!messageToSend.isEmpty()) {
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                            " -fx-background-color:rgb(15,125,242);" +
                            " -fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));
                    hbox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hbox);

                    server.sendMessageToSupplier(messageToSend);
                    message_field.clear();

                }
            }
        });


    }

    public static void addLabel(String messageFromSupplier, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        Text text = new Text(messageFromSupplier);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(" -fx-background-color:rgb(239,233,235);" +
                " -fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));

        hBox.getChildren().add(textFlow);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });



    }
}
