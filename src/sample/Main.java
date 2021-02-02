package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        String os = System.getProperty("os.name");
        System.out.println(os);


        primaryStage.setTitle("Modpack Installer");
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

        button.setOnAction(e -> install(os));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean install(String os) {
        if (os.contains("Mac")) {
          MacOS();

        }

        if (os.contains("Windows")) {
            Windows();
        }

        if ((!(os.contains("Windows"))) && (!(os.contains("Mac")))) {
            return false;
        }

        return true;
    }


    public static void MacOS() {
        System.out.println("Initializing MacOS Installation of Modpack");
        try {
            Process pwd = Runtime.getRuntime().exec("pwd");
            printResults(pwd);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void Windows() {

    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }


}
