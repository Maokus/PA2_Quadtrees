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

public class UI extends Application{
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
        Button range = new Button("Set Range");
        toolbar.getChildren().add(range);
        range.setMinHeight(50);
        //Create Min Label
        Label min = new Label("Min: ");
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

        Scene scene = new Scene(anchor, W, H);
        primaryStage.setTitle(appName);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static void regenGrid(int Nx, int Ny, GridPane grid, int tfw, int tfh){
        grid.getChildren().clear();
        for (int i = 0; i < Nx; i++){
            for (int j = 0; j < Ny; j++){
                int number = Nx * i + j;
                TextField tf = new TextField(""+number);
                tf.setAlignment(Pos.CENTER);
                tf.setPrefSize(tfw, tfh);
                grid.add(tf, i, j);
            }
        }
    }

    public static void main(String args[]){
        launch(args);
    }
}
