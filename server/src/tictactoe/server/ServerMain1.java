/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.server;

import com.google.gson.JsonObject;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerMain1 extends Application {

    private Server server;
    private Thread serverThread;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setTitle("Server Main");
        ToggleButton toggleButton = new ToggleButton("off");
        toggleButton.setId("toggleButton");
        toggleButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (toggleButton.getText().equals("off")) {
                    serverThread = new Thread() {
                        public void run() {
                            server = new Server();
                        }
                    };
                    serverThread.start();
                    toggleButton.setText("on");
                } else {
                    server.turnOff();
                    serverThread.stop();
                    toggleButton.setText("off");
                }

            }
        });
        HBox hbox = new HBox(toggleButton);
        hbox.setLayoutX(250);
        hbox.setLayoutY(350);

        GridPane gr = new GridPane();
        gr.setHgap(50);
        for (int i = 0; i < 9; i++) {
            ToggleButton invite2 = new ToggleButton("Challenge");
            invite2.setId("Challenge");
            Label score2 = new Label("500");
            score2.setId("scoreLabel");
            Label imglabel2 = new Label();
            Image img2 = new Image(getClass().getResourceAsStream("/images/k.png"));
            imglabel2.setGraphic(new ImageView(img2));
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(Color.GREEN);
            gr.add(cir2, 0, i);
            gr.add(invite2, 3, i);
            gr.add(score2, 2, i);
            gr.add(imglabel2, 1, i);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gr);
        scrollPane.setId("scrolPane");

        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(650.0);
        scrollPane.setLayoutY(100.0);
        scrollPane.setMaxHeight(500.0);

        Pane root = new Pane();
        root.setId("stack");
        root.getChildren().addAll(hbox, scrollPane);
        Scene scene = new Scene(root, 1350, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
