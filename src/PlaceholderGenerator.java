import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

import java.awt.Graphics;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.geometry.HPos.RIGHT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PlaceholderGenerator extends Application {

    public static void main(String[] args) {
      launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Placeholder Generator");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcom!");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        // setup width text field
        Label widthLabel = new Label("Width:");
        grid.add(widthLabel, 0, 1);

        TextField widthTextField = new TextField();
        widthTextField.setPromptText("100");
        widthTextField.setFocusTraversable(false);
        grid.add(widthTextField, 1, 1);

        // setup height text field
        Label heightLabel = new Label("Height:");
        grid.add(heightLabel, 0, 2);

        TextField heightTextField = new TextField();
        heightTextField.setPromptText("100");
        heightTextField.setFocusTraversable(false);
        grid.add(heightTextField, 1, 2);

        // setup generate button
        Button btn = new Button("Generate!");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 0, 6);
        grid.setColumnSpan(actionTarget, 2);
        grid.setHalignment(actionTarget, RIGHT);
        actionTarget.setId("actionTarget");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actionTarget.setFill(Color.FIREBRICK);
                String res = validateTextField(widthTextField.getText(), heightTextField.getText());
                if (res != null) {
                    actionTarget.setText(res);
                } else {
                    int width, height;
                    try {
                        width = Integer.parseInt(widthTextField.getText());
                        height = Integer.parseInt(heightTextField.getText());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        actionTarget.setText("Failure! Try again.");
                        return;
                    }

                    // validate image size
                    if ((1 > width) || (1 > height)) {
                        actionTarget.setText("Error: Image size is out of range.");
                        return;
                    }

                    // generate placeholder image
                    try {
                        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                        Graphics g = img.getGraphics();
                        g.setColor(java.awt.Color.WHITE);
                        g.fillRect(0, 0, width, height);
                        g.setColor(java.awt.Color.BLACK);
                        g.drawLine(0, 0, width, height);
                        g.drawLine(width, 0, 0, height);
                        g.dispose();
                        String outputName = "placeholder" + width + "x" + height + ".png";
                        ImageIO.write(img, "png", new File(outputName));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    // finished normally
                    actionTarget.setText("Success!");
                }
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String validateTextField(String width, String height) {
        if (width.equals("") || height.equals("")) {
            return "Error: There are unentered data items.";
        } else if (isNotNumber(width) || isNotNumber(height)) {
            return "Error: Please enter a number.";
        }
        return null;
    }

    private boolean isNotNumber(String val) {
        try {
            Integer.parseInt(val);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
