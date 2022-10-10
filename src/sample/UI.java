package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    static int[][] terrain;
    static QuadNode node;

    static int Nx = 10, Ny = 8;

    public void start(Stage primaryStage){
        String appName = "I love Trees 🌲🌳🌴";
        int padding = 10;
        int W = 800, H = 500;
        int Smin = 1, Smax = 50;
        int iniNx = 10, iniNy = 8;
        Nx = iniNx;
        Ny = iniNy;
        int scw = 500, sch = 250;
        int tfw = 35, tfh = 20;
        int incre = 1;
        clearTerrain(Nx, Ny);
        reconNode();

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
            terrain = padArray(terrain, 0, X, Y);
            Nx = X;
            Ny = Y;
            regenGrid(X, Y, grid, tfw, tfh);
        });
        sliderY.valueProperty().addListener(ov -> {
            int X = (int) Math.round(sliderX.getValue());
            int Y = (int) Math.round(sliderY.getValue());
            sltfY.setText(Y + "");
            terrain = padArray(terrain, 0, X, Y);
            Nx = X;
            Ny = Y;
            regenGrid(X, Y, grid, tfw, tfh);
        });
        sliderX.setValue(iniNx);
        sliderY.setValue(iniNy);

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

    private static void clearTerrain(int Nx, int Ny){
        terrain = new int[Ny][Nx];
        for (int i = 0; i < Ny; i++){
            Arrays.fill(terrain[i], 0);
        }
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
                final int x = i, y = j;
                TextField tf = new TextField(""+terrain[y][x]);
                Double[] minPos = null;
                if (minimum != null){
                    minPos = minimum.getBoundingBox().getRegion()[0];
                }
                if ((i <= Math.max(x1, x2) && i >= Math.min(x1, x2))
                    && (j <= Math.max(y1, y2) && j >= Math.min(y1, y2))){
                    tf.setStyle("-fx-background: green;");
                    if (minPos != null && i == (int) Math.round(minPos[0]) && j == (int) Math.round(minPos[1])){
                        tf.setStyle("-fx-background: orange;");
                    }
                }
                if (x1 != -1 && y1 != -1 && x2 != -1 && y2 != -1){
                    minimum = node.getRectMin(new BoundingBox(new Double[][]{{(double) Math.min(x1,x2), (double) Math.min(y1, y2)},{
                            (double) Math.max(x1,x2) + 1, (double) Math.max(y1,y2) + 1
                    }}));
                    System.out.println(minimum);
                    min.setText("Min: " + minimum.getMin());
                }
                if (i == x2 && j == y2){
                    tf.setStyle("-fx-background: red;");
                }
                tf.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                    if (!newPropertyValue)
                    {
                        try{
                            int temp = Integer.parseInt(tf.getText());
                            terrain[y][x] = Math.max(temp, 0);
                            tf.setText(Integer.toString(Math.max(temp, 0)));
                        }
                        catch(Exception e){
                            terrain[y][x] = 0;
                            tf.setText("0");
                        }
                        reconNode();
                        System.out.println("set the things");
                    }
                });
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

    private static void reconNode(){
        node = QuadNode.constructQuadNode(padArray(terrain, round(Math.max(Nx, Ny))));
    }

    private static int[][] padArray(int[][] arr, int value, int X, int Y){
        int[][] temp = new int[Y][X];
        for (int[] ints : temp) {
            Arrays.fill(ints, value);
        }
        for (int i = 0; i < Math.min(temp.length, arr.length); i++) {
            System.arraycopy(arr[i], 0, temp[i], 0, Math.min(arr[i].length, temp[i].length));
        }
        return temp;
    }

    private static int[][] padArray(int[][] arr, int numOfPads) {
        return padArray(arr, Integer.MAX_VALUE, numOfPads, numOfPads);
    }

    public static void main(String args[]){
        launch(args);
    }
}