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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class View extends Application {

    final Controller controller = ControllerFactory.getConsoleController();
    File selectedFile;
    Alert a = new Alert(Alert.AlertType.WARNING);
    GraphicsContext gc;
    ReadOnlyWorld w;
    ReadOnlyCritter selectedC;
    int[] selectedHex;
    ArrayList<String> species = new ArrayList<>();
    ArrayList<Paint> paints = new ArrayList<>();
    double scale = 1;
    double len;

    @FXML
    private Button LoadWorld;
    @FXML
    private Text WorldName;
    @FXML
    private CheckBox EnforceManna;
    @FXML
    private CheckBox EnforceMutation;
    @FXML
    private Button SubmitWorld;
    @FXML
    private Button RandomWorld;
    @FXML
    private Button LoadCritter;
    @FXML
    private CheckBox SelectLocation;
    @FXML
    private Text CritterName;
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
    private Text TimeStep;
    @FXML
    private Text AliveCritters;
    @FXML
    private Text mem0;
    @FXML
    private Text mem1;
    @FXML
    private Text mem2;
    @FXML
    private Text mem3;
    @FXML
    private Text mem4;
    @FXML
    private Text mem5;
    @FXML
    private Text mem6;
    @FXML
    private Text critterFile;
    @FXML
    private Text lastRule;

    @Override
    public void start(Stage primaryStage) {
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

        len = Math.min(canvas.getWidth() / (w.getWidth() * 1.5 + 0.5),
                canvas.getHeight() / ((w.getHeight()) + 1) * 2 / Math.sqrt(3));
        len = scale * Math.min(15, len);
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
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(1.0);
                gc.strokePolygon(xPoints, yPoints, 6);

                if (selectedHex != null && selectedHex[0] == i && selectedHex[1] == j) {
                    gc.setLineWidth(2.5);
                    gc.strokePolygon(xPoints, yPoints, 6);
                }

                if (w.getTerrainInfo(i, j) == -1) {
                    gc.setFill(Color.GRAY);
                    gc.fillPolygon(smallX(x), smallY(y), 6);
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
                        double radians = (6.0 - w.getCritterDirection(i, j)) / 3.0 * Math.PI + 0.5 * Math.PI
                                - 1.0 / 12.0 * Math.PI;
                        gc.setStroke(Color.BLACK);
                        gc.setLineWidth(1.0);
                        for (int r = 0; r < 2; r++) {
                            gc.strokeLine((xPoints[0] + xPoints[1]) / 2 + Math.cos(radians) * 0.5 * size,
                                    (yPoints[0] + yPoints[3]) / 2 - Math.sin(radians) * 0.5 * size,
                                    (xPoints[0] + xPoints[1]) / 2 + Math.cos(radians) * 0.7 * size,
                                    (yPoints[0] + yPoints[3]) / 2 - Math.sin(radians) * 0.7 * size);
                            radians += 1.0 / 6.0 * Math.PI;
                        }
                        if (c == selectedC) {
                            gc.setStroke(p);
                            gc.setLineWidth(2);
                            gc.strokePolygon(smallX(x), smallY(y), 6);
                        }
                    } catch (NoMaybeValue e) {
                    }
                }
            }
            if (selectedC != null) displayInfo();
        }

        TimeStep.setText("Time Step: " + w.getSteps());
        AliveCritters.setText("# Alive Critters: "+ w.getNumberOfAliveCritters());

    }

    private double[] smallX(double x) {
        return new double[]{x + 0.07 * len, x + 0.93 * len, x + 1.365 * len,
                x + 0.93 * len, x + 0.07 * len, x - 0.365 * len};
    }

    private double[] smallY(double y) {
        return new double[]{y - 0.1 * len, y - 0.1 * len, y - Math.sqrt(3) / 2 * len,
                y - (Math.sqrt(3) - 0.1) * len, y - (Math.sqrt(3) - 0.1) * len,
                y - Math.sqrt(3) / 2 * len};
    }

    @FXML
    private void loadWorld(final ActionEvent ae) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open World File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        selectedFile = fileChooser.showOpenDialog(new Stage());
        WorldName.setText(selectedFile.getName());
        EnforceManna.setDisable(false);
        EnforceMutation.setDisable(false);
        SubmitWorld.setDisable(false);
    }

    @FXML
    private void SubmitWorld(final ActionEvent ae) {
        if (controller.loadWorld(selectedFile.getAbsolutePath(),
                EnforceManna.isSelected(), EnforceMutation.isSelected())) {
            newWorld();
        } else {
            WorldName.setText("");
            a.setContentText("Load World Failed.");
            a.show();
        }
        EnforceManna.setDisable(true);
        EnforceMutation.setDisable(true);
        SubmitWorld.setDisable(true);
    }

    @FXML
    private void randomWorld(final ActionEvent ae) {
        controller.newWorld();
        EnforceManna.setSelected(false);
        EnforceMutation.setSelected(false);
        WorldName.setText("");
        newWorld();
    }

    private void newWorld() {
        selectedHex = null;
        selectedC = null;
        scale = 1;
        species = new ArrayList<>();
        paints = new ArrayList<>();
        drawHex();
        clearInfo();
        if (Play.isDisabled()) {
            LoadCritter.setDisable(false);
            SelectLocation.setDisable(false);
            Play.setDisable(false);
            PlayOnce.setDisable(false);
            Stop.setDisable(false);
            RunRate.setDisable(false);
            ZoomOut.setDisable(false);
            ZoomIn.setDisable(false);
        }
    }

    @FXML
    private void loadCritter(final ActionEvent ae) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Critter File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null) return;

        if (SelectLocation.isSelected()) {
            if (selectedHex != null
                    && controller.loadCritters(selectedFile.getAbsolutePath(), selectedHex[0], selectedHex[1])) {
                CritterName.setText(selectedFile.getName());
                drawHex();
            } else {
                a.setContentText("Load Critter Failed");
                a.show();
            }
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Load Critters");
        dialog.setHeaderText("Input number of critters to load: ");
        dialog.setContentText("");
        String nStr;
        try {
            nStr = dialog.showAndWait().get();
        } catch (NoSuchElementException e) {
            nStr = "1";
        }
        int n;
        if (!nStr.matches("[0-9]+")) n = 1;
        else n = Math.max(1, Integer.valueOf(nStr));
        if (controller.loadCritters(selectedFile.getAbsolutePath(), n)) {
            CritterName.setText(selectedFile.getName());
        } else {
            CritterName.setText("");
            a.setContentText("Load Critter Failed");
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
        RandomWorld.setDisable(true);
        LoadCritter.setDisable(true);
        PlayOnce.setDisable(true);
        RunRate.setDisable(true);

        String nStr = RunRate.getText();
        int n;
        if(nStr.equals("")) n = 10;
        else {
            try {
                n = Integer.valueOf(nStr);
            } catch (NumberFormatException e) {
                n = 0;
            }
        }
        if (n <= 0) {
            RunRate.setText("0");
            return;
        }
        RunRate.setText(Integer.toString(n));
        advanceRate = n;
        exit = false;
        playHelper();
    }

    int advanceRate = 30;
    long timeSince;
    double animTime;
    boolean exit;
    Timer timer = new Timer();;

    TimerTask newTT() {
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                if (exit == true){
                    drawHex();
                    cancel();
                    return;
                }
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
        RandomWorld.setDisable(false);
        LoadCritter.setDisable(false);
        PlayOnce.setDisable(false);
        RunRate.setDisable(false);
        exit = true;
    }

    @FXML
    private void selectHex(final MouseEvent me) {
        double x = me.getX();
        double y = me.getY();
        int[] hexInfo = getHex(x, y);
        try {
            selectedC = w.getReadOnlyCritter(hexInfo[0], hexInfo[1]).get();
            selectedHex = null;
        } catch (NoMaybeValue e) {
            selectedC = null;
            selectedHex = hexInfo;
            clearInfo();
        }
        drawHex();
    }

    /**
     * Display information of Critter c
     * Requires: c is the selected critter
     */
    private void displayInfo() {
        mem0.setText("MEMSIZE: " + selectedC.getMemory()[0]);
        mem1.setText("DEFENSE: " + selectedC.getMemory()[1]);
        mem2.setText("OFFENSE: " + selectedC.getMemory()[2]);
        mem3.setText("SIZE: " + selectedC.getMemory()[3]);
        mem4.setText("ENERGY: " + selectedC.getMemory()[4]);
        mem5.setText("PASS: " + selectedC.getMemory()[5]);
        mem6.setText("POSTURE: " + selectedC.getMemory()[6]);
        critterFile.setText('\n' + "Critter Program" + '\n' + selectedC.getProgramString());
        critterFile.setWrappingWidth(RunRate.getWidth());
        try {
            lastRule.setText("Last Executed Rule" + '\n' + selectedC.getLastRuleString().get());
            lastRule.setWrappingWidth(RunRate.getWidth());
        } catch (NoMaybeValue e) {
            lastRule.setText("No Rule Executed");
        }
    }

    private void clearInfo() {
        mem0.setText("No Critter Selected");
        mem1.setText("");
        mem2.setText("");
        mem3.setText("");
        mem4.setText("");
        mem5.setText("");
        mem6.setText("");
        critterFile.setText("");
        lastRule.setText("");
    }

    /**
     * return the Hex number [i, j] at point (x, y)
     */
    private int[] getHex(double x, double y) {
        int recX = (int) Math.floor((x + 0.5 * len) / (1.5 * len));
        int recY = (int) Math.floor(y / (Math.sqrt(3) / 2 * len));
        int hexX = recX;
        int hexY = w.getHeight() - recY;
        if ((hexX + hexY) % 2 == 1) hexY--;
        x = (x + 0.5 * len) % (1.5 * len);
        y = (Math.sqrt(3) / 2 * len) - y % (Math.sqrt(3) / 2 * len);
        if ((recX + recY + w.getHeight()) % 2 == 0) {
            if (y < Math.sqrt(3) * len - Math.sqrt(3) * x) {
                hexX--;
                hexY--;
            }
        } else {
            if (y > -(Math.sqrt(3) / 2 * len) + Math.sqrt(3) * x) {
                hexX--;
                hexY++;
            }
        }
        return new int[]{hexX, hexY};
    }

    @FXML
    private void zoom(final ActionEvent ae) {
        Button bt = (Button) ae.getSource();
        if (bt == ZoomIn) {
            scale = 1.25 * scale;
            canvas.setWidth(canvas.getWidth() * 1.25);
            canvas.setHeight(canvas.getHeight() * 1.25);
        } else if (bt == ZoomOut) {
            scale = 0.8 * scale;
            canvas.setWidth(canvas.getWidth() * 0.8);
            canvas.setHeight(canvas.getHeight() * 0.8);
        }
        if (scale > 10) ZoomIn.setDisable(true);
        else ZoomIn.setDisable(false);
        if (scale < 0.1) ZoomOut.setDisable(true);
        else ZoomOut.setDisable(false);
        drawHex();
    }
}

