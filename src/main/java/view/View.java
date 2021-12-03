package view;

import cms.util.maybe.NoMaybeValue;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class View extends Application {

    protected final Controller controller = ControllerFactory.getConsoleController();
    Alert a = new Alert(Alert.AlertType.WARNING);
    GraphicsContext gc;
    ReadOnlyWorld w;
    ArrayList<String> species;
    ArrayList<Paint> paints;
    double scale = 1;

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
    private Button ZoomIn;
    @FXML
    private Button ZoomOut;
    @FXML
    private Canvas canvas;
    @FXML
    private TextFlow info;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            final URL r = getClass().getResource("scene.fxml");
            if (r == null) {
                System.out.println("No FXML resource found.");
                try {
                    stop();
                } catch (final Exception e) {
                }
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
            try {
                stop();
            } catch (final Exception e2) {
            }
        }
    }

    protected void drawHex() {
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        w = controller.getReadOnlyWorld();

        double len = scale * 15;
        double[] xPoints;
        double[] yPoints;
        double x;
        double y;

        for (int i = 0; i < w.getWidth(); i++) {
            for (int j = w.getHeight() - 1; j >= 0; j--) {
                if ((i + j) % 2 == 1) continue;
                x = 1.5 * len * i + 0.5 * len;
                y = (w.getHeight() - j) * Math.sqrt(3) / 2 * len + Math.sqrt(3) / 2 * len;
                xPoints = new double[]{x, x + len, x + 1.5 * len, x + len, x, x - 0.5 * len};
                yPoints = new double[]{y, y, y - Math.sqrt(3) / 2 * len, y - Math.sqrt(3) * len,
                        y - Math.sqrt(3) * len, y - Math.sqrt(3) / 2 * len};
                gc.strokePolygon(xPoints, yPoints, 6);
                if (w.getTerrainInfo(i, j) == -1) {
                    gc.setFill(Color.GRAY);
                    xPoints = new double[]{x + 0.1 * len, x + 0.9 * len, x + 1.36 * len,
                            x + 0.9 * len, x + 0.1 * len, x - 0.36 * len};
                    yPoints = new double[]{y - 0.1 * len, y - 0.1 * len, y - Math.sqrt(3) / 2 * len,
                            y - (Math.sqrt(3) - 0.1) * len, y - (Math.sqrt(3) - 0.1) * len,
                            y - Math.sqrt(3) / 2 * len};
                    gc.fillPolygon(xPoints, yPoints, 6);
                } else if (w.getTerrainInfo(i, j) < -1) {
                    gc.setFill(Color.GREEN);
                    gc.fillOval((xPoints[0] + xPoints[1] - len) / 2, (yPoints[0] + yPoints[3] - len) / 2, len, len);
                } else {
                    try {
                        ReadOnlyCritter c = w.getReadOnlyCritter(i, j).get();
                        Paint p;
                        if (species.indexOf(c.getSpecies()) == -1) {
                            p = Color.color(Math.random(), Math.random(), Math.random());
                            species.add(c.getSpecies());
                            paints.add(p);
                        } else {
                            p = paints.get(species.indexOf(c.getSpecies()));
                        }
                        gc.setFill(p);
                        double size = 0.5 * (Math.log(c.getMemory()[3]) + 1) * len;
                        gc.fillOval((xPoints[0] + xPoints[1] - size) / 2, (yPoints[0] + yPoints[3] - size) / 2,
                                size, size);
                        double radians = (6.0 - w.getCritterDirection(i, j)) / 3.0 * Math.PI + 0.5 * Math.PI;
                        radians -= 1.0 / 12.0 * Math.PI;
                        gc.strokeLine((xPoints[0] + xPoints[1]) / 2 + Math.cos(radians) * 0.5 * size,
                                (yPoints[0] + yPoints[3]) / 2 - Math.sin(radians) * 0.5 * size,
                                (xPoints[0] + xPoints[1]) / 2 + Math.cos(radians) * 0.7 * size,
                                (yPoints[0] + yPoints[3]) / 2 - Math.sin(radians) * 0.7 * size);
                        radians += 1.0 / 6.0 * Math.PI;
                        gc.strokeLine((xPoints[0] + xPoints[1]) / 2 + Math.cos(radians) * 0.5 * size,
                                (yPoints[0] + yPoints[3]) / 2 - Math.sin(radians) * 0.5 * size,
                                (xPoints[0] + xPoints[1]) / 2 + Math.cos(radians) * 0.7 * size,
                                (yPoints[0] + yPoints[3]) / 2 - Math.sin(radians) * 0.7 * size);
                    } catch (NoMaybeValue e) {
                    }
                }
            }
        }
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
        species = new ArrayList<>();
        paints = new ArrayList<>();
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

        int n;
        if (!nStr.matches("[0-9]+")) n = 1;
        else n = Math.max(1, Integer.valueOf(nStr));
        if (!controller.loadCritters(selectedFile.getAbsolutePath(), n)) {
            a.setContentText("Load critter failed");
            a.show();
        }
        drawHex();
    }

    @FXML
    private void playOnce(final ActionEvent ae) {
        controller.advanceTime(1);
        drawHex();
    }

    @FXML
    private void play(final ActionEvent ae) {
        LoadWorld.setDisable(true);
        LoadCritter.setDisable(true);
        PlayOnce.setDisable(true);

        String nStr = RunRate.getText();
        int n;
        if (!nStr.matches("[0-9]+")) n = 10;   //default advance rate is 10
        else n = Integer.valueOf(nStr);
        if (n <= 0) n = 10;
        exit = false;
        advanceRate = n;
        playHelper();
    }

    int advanceRate;
    long timeSince;
    double animTime;
    boolean exit;
    Timer timer = new Timer();

    TimerTask newTT() {
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                if (exit == true) return;
                controller.advanceTime(1);
                if (System.nanoTime() - timeSince > animTime) {
                    drawHex();
                    timeSince = System.nanoTime();
                }
            }
        };
        return tt;
    }

    private void playHelper() {
        animTime = Math.pow(10, 9) / 30;
        timer.schedule(newTT(), 0, (1000 / advanceRate));
    }

    @FXML
    private void stop(final ActionEvent ae) {
        LoadWorld.setDisable(false);
        LoadCritter.setDisable(false);
        PlayOnce.setDisable(false);
        exit = true;
    }

    @FXML
    private void displayInfo(final MouseEvent ae) {
        double x = ae.getX();
        double y = ae.getY();
        System.out.println(x);
        System.out.println(y);

    }

    @FXML
    private void zoom(final ActionEvent ae) {
        Button bt = (Button) ae.getSource();
        if (bt == ZoomIn) {
            scale = 1.25 * scale;
            canvas.setWidth(canvas.getWidth() * 1.25);
            canvas.setHeight(canvas.getHeight() * 1.25);
        }
        else if (bt == ZoomOut) {
            scale = 0.8 * scale;
            canvas.setWidth(canvas.getWidth() * 0.8);
            canvas.setHeight(canvas.getHeight() * 0.8);
        }
        drawHex();
    }
}

