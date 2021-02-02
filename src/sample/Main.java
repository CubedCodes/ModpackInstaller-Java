package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        VBox pane = new VBox();
        pane.setPrefSize(500,200);
        pane.setSpacing(10);

        Button button = new Button("Install");
        pane.getChildren().add(button);

        Text welcomeText = new Text("Welcome to the modpack installer!");
        pane.getChildren().add(welcomeText);

        VBox.setMargin(button, new Insets(20, 20, 20, 20));
        VBox.setMargin(welcomeText, new Insets(20, 20, 20, 20));

        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
