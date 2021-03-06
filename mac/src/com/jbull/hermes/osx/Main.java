package com.jbull.hermes.osx;

import com.aquafx_project.AquaFx;
import com.aquafx_project.controls.skin.styles.TextFieldType;
import com.jbull.hermes.desktop.CommunicationCenter;
import com.jbull.hermes.desktop.DefaultNotification;
import com.jbull.hermes.desktop.State;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        AquaFx.style();
        long start = System.currentTimeMillis();
        final CommunicationCenter communicationCenter = new CommunicationCenter(new MacNotification(), new DefaultNotification());
        AquaFx.createTextFieldStyler().setType(TextFieldType.SEARCH).style(communicationCenter.getContactSearch());
        final State state = new State(communicationCenter);
        System.out.println(System.currentTimeMillis() - start);
        primaryStage.setTitle("HermesIO");
        primaryStage.setScene(new Scene(communicationCenter, 800, 600));
        primaryStage.setMinWidth(communicationCenter.getPrefWidth());
        primaryStage.setMinHeight(communicationCenter.getPrefHeight());
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    state.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
