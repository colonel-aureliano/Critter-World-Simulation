package view;

import controller.Controller;
import controller.ControllerFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ReadOnlyWorld;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class View extends Application {

    private final Controller controller = ControllerFactory.getConsoleController();
    Alert a = new Alert(Alert.AlertType.WARNING);
    GraphicsContext gc;
    ReadOnlyWorld w;

    @FXML
    private Button LoadWorld;
    @FXML
    private Button LoadCritter;
    @FXML
    private TextField nCritters;
    @FXML
    private Button PlayOnce;
    @FXML
    private Button Play;
    @FXML
    private Button Stop;
    @FXML
    private TextField RunRate;
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

    private void drawHex() {
        gc = canvas.getGraphicsContext2D();
        w = controller.getReadOnlyWorld();
        //gc.fillPolygon();
        gc.fillRect(0,0,canvas.getHeight(), canvas.getHeight());
    }

    @FXML
    private void loadWorld(final ActionEvent ae) {
        System.out.println("World button pressed");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open World File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (!controller.loadWorld(selectedFile.getAbsolutePath(), false, false)) {
            a.setContentText("Load world failed.");
            a.show();
        }
        drawHex();
    }

    @FXML
    private void loadCritter(final ActionEvent ae) {
        System.out.println("Critter button pressed");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Critter File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);
        String nStr = nCritters.getText();
        if (!nStr.matches("[0-9]+") ||
                !controller.loadCritters(selectedFile.getAbsolutePath(), Integer.valueOf(nStr))) {
            a.setContentText("Load critter failed");
            a.show();
        }
        drawHex();
    }

    @FXML
    private void playOnce(final ActionEvent ae) {
        controller.advanceTime(1);
    }

    @FXML
    private void play(final ActionEvent ae) {

    }

    @FXML
    private void stop(final ActionEvent ae) {

    }


}
