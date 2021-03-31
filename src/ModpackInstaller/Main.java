package ModpackInstaller;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main extends Application {

    // Universal Variable Declarations
    public static VBox pane = new VBox();
    public static Font font = Font.font("Verdana", FontWeight.BOLD, 27);

    // Start of JavaFX Application
    @Override
    public void start(Stage primaryStage) throws IOException {

        // Set icon
        primaryStage.getIcons().add(new Image("file:icon.png"));

        // Get Operating System
        String os = System.getProperty("os.name");
        System.out.println(os);


        primaryStage.setTitle("Modpack Installer");
        pane.setPrefSize(800, 400);

        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.show();


        Platform.runLater(Main::loadList);
    }


    // Main function to start JavaFX Application
    public static void main(String[] args) {
        launch(args);
    }

    // prints results of terminal/cmd commands
    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void loadList(){
        try {
            Process curl = Runtime.getRuntime().exec("curl https://modpackinstaller.netlify.app/modpacks.json -O -L -J");
            printResults(curl);
            System.out.println("Downloaded JSON List");
        }
        catch(IOException error){
            System.out.println(error);
        }

        try {
            String json = null;

            FileReader file = new FileReader("modpacks.json");
            BufferedReader reader = new BufferedReader(file);

            // don't declare it here
            // String key = "";
            String line = reader.readLine();

            while (line != null) {
                json += line;
                line = reader.readLine();
            }
            System.out.println(json);

            // Now do the magic.
            JSON_Data data = new Gson().fromJson(json, JSON_Data.class);

            // Show it.
            System.out.println(data);
        }
        catch(IOException error){
            System.out.println(error);
        }

        ListView listView = new ListView();

        listView.getItems().add("Item 1");
        listView.getItems().add("Item 2");
        listView.getItems().add("Item 3");

        pane.getChildren().add(listView);
    }

}

class JSON_Data {
    private String name;
    private String image;
    private String downloadLink;
    private List<JSON_Data> modpacks;

    public List<JSON_Data> getModpacks() { return modpacks; }
    public String getName() { return name; }
    public String getImage() { return image; }
    public String getDownloadLink() { return downloadLink; }

    public String toString() {
        return String.format("modpacks:%s", modpacks);
    }
}