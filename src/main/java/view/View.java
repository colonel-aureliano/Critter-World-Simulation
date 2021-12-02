package view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class View extends Application {

    @FXML
    private Button LoadWorld;
    @FXML
    private Button LoadCritter;
    @FXML
    private Label label1;
    @FXML
    private TextField nCritters;
    @FXML
    private Button PlayOnce;
    @FXML
    private Button Play;
    @FXML
    private Button Stop;
    @FXML
    private Label label2;
    @FXML
    private JTextField RunRate;
    @FXML
    private Canvas canvas;



    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            final URL r = getClass().getResource("scene.fxml");
            if (r == null) {
                System.out.println("No FXML resource found.");
                try {stop();} catch (final Exception e) {}
                return;
            }
            final Parent node = FXMLLoader.load(r);
            final Scene scene = new Scene(node);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.show();

        } catch (final IOException e) {
            System.out.println("Can't load FXML file.");
            e.printStackTrace();
            try { stop(); } catch (final Exception e2) {}
        }
    }
}
