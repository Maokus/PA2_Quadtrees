package sample;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class UI extends Application{

    static ArrayList<Pair<Integer, Integer>> selection = new ArrayList<>();
    static int[][] points = new int[0][0];
    static Label min = new Label("Min: ");
    static QuadNode minimum;

    public void start(Stage primaryStage){
        String appName = "Unnamed App";
        int padding = 10;
        int Nx = 10, Ny = 8;
        int W = 800, H = 500;
        int Smin = 1, Smax = 50;
        int scw = 500, sch = 250;
        int tfw = 35, tfh = 20;
        int incre = 1;

        //Define gridpane
        ScrollPane sp = new ScrollPane();
        sp.setPrefSize(scw, sch);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(padding));
        grid.setHgap(padding);
        grid.setVgap(padding);
        sp.setContent(grid);

        //Define anchor pan
        AnchorPane anchor = new AnchorPane();
        //Label
        Label title = new Label(appName);
        title.setFont(new Font("Times New Roman", 30));
        title.setAlignment(Pos.CENTER);
        anchor.getChildren().add(title);
        AnchorPane.setTopAnchor(title, 15.0);
        AnchorPane.setRightAnchor(title, 10.0);
        AnchorPane.setLeftAnchor(title, 10.0);
        //anchor the scroll pane
        anchor.getChildren().add(sp);
        AnchorPane.setTopAnchor(sp, 80.0);
        AnchorPane.setRightAnchor(sp, 45.0);
        AnchorPane.setLeftAnchor(sp, 30.0);
        AnchorPane.setBottomAnchor(sp, 100.0);
        //Add ToolBar
        HBox toolbar = new HBox(50);
        toolbar.setAlignment(Pos.CENTER);
        anchor.getChildren().add(toolbar);
        AnchorPane.setBottomAnchor(toolbar, 30.0);
        AnchorPane.setRightAnchor(toolbar, 20.0);
        AnchorPane.setLeftAnchor(toolbar, 20.0);
        //Create range button
        Button range = new Button("Clear Range");
        toolbar.getChildren().add(range);
        range.setMinHeight(50);
        //Create Min Label
        toolbar.getChildren().add(min);
        //Create Slider bars
        Slider sliderX = new Slider(Smin, Smax, incre);
        Slider sliderY = new Slider(Smin, Smax, incre);
        TextField sltfX = new TextField(Nx + "");
        sltfX.setPrefSize(tfw, tfh);
        sltfX.setAlignment(Pos.CENTER);
        TextField sltfY = new TextField(Ny + "");
        sltfY.setPrefSize(tfw, tfh);
        sltfY.setAlignment(Pos.CENTER);
        sliderY.setOrientation(Orientation.VERTICAL);
        anchor.getChildren().add(sltfX);
        anchor.getChildren().add(sltfY);
        anchor.getChildren().add(sliderX);
        anchor.getChildren().add(sliderY);
        //Orientate slider Y
        AnchorPane.setRightAnchor(sliderY, 15.0);
        AnchorPane.setTopAnchor(sliderY, 80.0);
        AnchorPane.setBottomAnchor(sliderY, 145.0);
        //Orientate slider X
        AnchorPane.setTopAnchor(sliderX, 55.0);
        AnchorPane.setRightAnchor(sliderX, 35.0);
        AnchorPane.setLeftAnchor(sliderX, 70.0);
        //Orientate slider text fields
        AnchorPane.setBottomAnchor(sltfY, 110.0);
        AnchorPane.setRightAnchor(sltfY, 5.0);
        AnchorPane.setTopAnchor(sltfX, 50.0);
        AnchorPane.setLeftAnchor(sltfX, 35.0);

        //Value Listeners
        sltfX.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                try{
                    int value = Integer.parseInt(sltfX.getText());
                    sliderX.setValue(value);
                } catch (NumberFormatException exp) {
                    sltfX.setText(Nx+"");
                }
            }
        });
        sltfY.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                try{
                    int value = Integer.parseInt(sltfY.getText());
                    sliderY.setValue(value);
                } catch (NumberFormatException exp) {
                    sltfY.setText(Ny+"");
                }
            }
        });
        sliderX.valueProperty().addListener(ov -> {
            int X = (int) Math.round(sliderX.getValue());
            int Y = (int) Math.round(sliderY.getValue());
            sltfX.setText(X + "");
            regenGrid(X, Y, grid, tfw, tfh);
        });
        sliderY.valueProperty().addListener(ov -> {
            int X = (int) Math.round(sliderX.getValue());
            int Y = (int) Math.round(sliderY.getValue());
            sltfY.setText(Y + "");
            regenGrid(X, Y, grid, tfw, tfh);
        });
        sliderX.setValue(Nx);
        sliderY.setValue(Ny);

        //Event handlers
        range.setOnAction(e -> {
            selection.clear();
            regenGrid(Nx, Ny, grid, tfw, tfh);
        });

        Scene scene = new Scene(anchor, W, H);
        primaryStage.setTitle(appName);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static void regenGrid(int Nx, int Ny, GridPane grid, int tfw, int tfh){
        grid.getChildren().clear();
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
        if (selection.size() == 2) {
            Pair<Integer, Integer> p1 = selection.get(0);
            Pair<Integer, Integer> p2 = selection.get(1);
            x1 = p1.getKey(); y1 = p1.getValue(); x2 = p2.getKey(); y2 = p2.getValue();
            points = new int[Math.max(y1, y2) - Math.min(y1, y2) + 1][Math.max(x1, x2) - Math.min(x1, x2) + 1];
        }
        for (int i = 0; i < Nx; i++){
            for (int j = 0; j < Ny; j++){
                int number = Nx * i + j;
                final int x = i, y = j;
                TextField tf = new TextField(""+number);
                if ((i <= Math.max(x1, x2) && i >= Math.min(x1, x2))
                    && (j <= Math.max(y1, y2) && j >= Math.min(y1, y2))){
                    tf.setStyle("-fx-background: green;");
                    if (minimum != null){
                        BoundingBox things = minimum.minNode.getBoundingBox();
                    }
                    points[j - Math.min(y1, y2)][i - Math.min(x1, x2)] = Integer.parseInt(tf.getText());
                }
                if (points.length > 0) {
                    points = padArray(points, round(Math.max(points.length, points[0].length)));
                    QuadNode node = QuadNode.constructQuadNode(points);
                    minimum = node.getRectMin(node.getBoundingBox());
                    min.setText("Min: " + minimum.getMin());
                }
                if (i == x2 && j == y2){
                    tf.setStyle("-fx-background: red;");
                }
                tf.setOnMouseClicked(e -> {
                    if (e.isControlDown()){
                        tf.setStyle("-fx-background: red; -fx-focus-color: red");
                        selection.add(new Pair<>(x, y));
                        if (selection.size() > 2){
                            selection.remove(0);
                        }
                        if (selection.size() == 2) regenGrid(Nx, Ny, grid, tfw, tfh);
                    }
                });
                tf.setAlignment(Pos.CENTER);
                tf.setPrefSize(tfw, tfh);
                grid.add(tf, i, j);
            }
        }
    }

    private static int round(int n){
        int m = 0x8000;
        n = n - 1;
        while((m & n) == 0){
            m >>= 1;
        }
        return m << 1;
    }

    private static int[][] padArray(int[][] arr, int numOfPads) {
        int[][] temp = new int[numOfPads][numOfPads];
        for (int[] ints : temp) {
            Arrays.fill(ints, Integer.MAX_VALUE);
        }
        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, temp[i], 0, arr[i].length);
        }
        return temp;
    }

    public static void main(String args[]){
        launch(args);
    }
}