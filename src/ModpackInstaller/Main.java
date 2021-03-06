package ModpackInstaller;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.python.util.PythonInterpreter;

import java.awt.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    // Universal Variable Declarations
    public static VBox pane = new VBox();
    public static Font font = Font.font("Verdana", FontWeight.BOLD, 27);
    public static Text install = new Text("");
    public static Text uninstallText = new Text("");
    public static ProgressBar progressBar = new ProgressBar();

    // Start of JavaFX Application
    @Override
    public void start(Stage primaryStage) throws IOException {
        /*
         * Parent root = FXMLLoader.load(getClass().getResource("page.fxml"));
         * primaryStage.setScene(new Scene(root, 600, 400)); primaryStage.show();
         */

        // Set icon
        primaryStage.getIcons().add(new Image("file:icon.png"));

        // Get Operating System
        String os = System.getProperty("os.name");
        System.out.println(os);

        // Get Background Image
        BackgroundImage myBI = new BackgroundImage(
                new Image("https://i.ibb.co/qsb2kGJ/shaderwheat.png", 900, 500, false, true), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));

        if (os.contains("Mac")) {
            myBI = new BackgroundImage(new Image("https://i.ibb.co/qsb2kGJ/shaderwheat.png", 1700, 1300, false, true),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
        }

        // Create JavaFX objects in JavaFX window
        primaryStage.setTitle("Modpack Installer");
        pane.setPrefSize(800, 400);
        pane.setBackground(new Background(myBI));
        Button button = new Button("Install");
        button.setStyle("-fx-background-color: #1fc127");
        button.setTextFill(Color.WHITE);
        button.setMaxSize(300, 300);
        Button uninstall = new Button("Uninstall");
        uninstall.setStyle("-fx-background-color: #1fc127");
        uninstall.setTextFill(Color.WHITE);
        uninstall.setMaxSize(300, 300);
        Text welcomeText = new Text("Welcome to the Modpack Installer!");
        button.setFont(font);
        welcomeText.setFont(font);
        welcomeText.setFill(Color.LIGHTGREEN);
        uninstall.setFont(font);

        progressBar.setVisible(false);

        // Add elements to window
        pane.getChildren().add(welcomeText);
        pane.getChildren().add(button);
        pane.getChildren().add(uninstall);
        pane.setMargin(button, new Insets(30, 10, 10, 250));
        pane.setMargin(welcomeText, new Insets(20, 10, 10, 140));
        pane.setMargin(uninstall, new Insets(30, 10, 10, 250));

        StartBar();

        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Add actions for "Install" and "Uninstall" buttons
        button.setOnAction(e -> {
            button.setDisable(true);
            StartText();

            // install(os);
            Install installMethod = new Install(os);
            Thread t = new Thread(installMethod);
            t.start();
        });

        uninstall.setOnAction(b -> {
            uninstall.setDisable(true);
            UninstallTextFunc();

            if (os.contains("Mac")) {
                UninstallMac();
            }

            if (os.contains("Windows")) {
                UninstallWindows();
            }

        });
    }

    public static class StepProgress implements Runnable {

        private double value;

        public StepProgress(double value) {
            this.value = value;
        }

        public void run() {
            for (double i = (progressBar.getProgress() * 100); i < (value * 100); i++) {
                progressBar.setProgress(i / 100);
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Main function to start JavaFX Application
    public static void main(String[] args) {
        launch(args);
    }

    public class Install implements Runnable {

        private String os;

        public Install(String os) {
            this.os = os;
        }

        public void run() {
            if (os.contains("Mac")) {
                MacOS();
                rickroll(os);
            }

            if (os.contains("Windows")) {
                Windows();
                rickroll(os);
            }

            if ((!(os.contains("Windows"))) && (!(os.contains("Mac")))) {
                System.out.println("Unrecognized OS");
            }
        }
    }

    // MacOS Installation
    public static void MacOS() {
        System.out.println("Initializing MacOS Installation of Modpack");
        try {

            progressBar.setVisible(true);
            StepProgress stepProgress = new StepProgress(0.1);
            Thread t = new Thread(stepProgress);
            t.start();

            // Get current username
            Process user = Runtime.getRuntime().exec("id -un");
            String id = returnResults(user);
            System.out.println(id);

            // Get current directory
            Process pwd = Runtime.getRuntime().exec("pwd");
            String currentDir = returnResults(pwd);
            printResults(pwd);

            // Download ZIP of mod directory
            Process curl = Runtime.getRuntime().exec("curl https://modpackinstaller.page.link/mods -O -L -J");
            printResults(curl);
            System.out.println("downloaded zip archive");

            // Set progressbar
            StepProgress stepProgress2 = new StepProgress(0.4);
            Thread t2 = new Thread(stepProgress2);
            t2.start();

            // create directory for unzip
            Process mkmodsdir = Runtime.getRuntime().exec("mkdir minecraftModded2021");
            printResults(mkmodsdir);
            System.out.println("made game directory");

            // move directory
            String minecraftPath = "/Users/" + id + "/Library/Application Support/minecraft/";
            ProcessBuilder mv = new ProcessBuilder();
            mv.command("mv", "minecraftModded2021", minecraftPath);
            Process mvp = mv.start();
            printResults(mvp);
            System.out.println("moved game directory");

            // Unzip archive
            Process unzipMods = Runtime.getRuntime().exec(("unzip " + currentDir + "/mods.zip"), null,
                    new File((minecraftPath + "minecraftModded2021")));
            printResults(unzipMods);
            System.out.println("unzipped mods.zip");

            // Set progressbar
            StepProgress stepProgress3 = new StepProgress(0.5);
            Thread t3 = new Thread(stepProgress3);
            t3.start();

            // remove archive
            Process removemodszip = Runtime.getRuntime().exec("rm mods.zip");
            printResults(removemodszip);
            System.out.println("removed mods.zip");

            // set progressbar
            StepProgress stepProgress4 = new StepProgress(0.6);
            Thread t4 = new Thread(stepProgress4);
            t4.start();

            // Download Forge installer
            Process forgecurl = Runtime.getRuntime()
                    .exec("curl -L https://modpackinstaller.page.link/forge -o forge.jar");
            printResults(forgecurl);
            System.out.println("downloaded forge installer");

            // set progressbar
            StepProgress stepProgress6 = new StepProgress(0.8);
            Thread t6 = new Thread(stepProgress6);
            t6.start();

            // Run forge installer
            Process forgeinstaller;
            File java = new File("jdk1.8.0_281.jdk");
            if (java.exists()) {
                System.out.println("Using bundled Java");
                forgeinstaller = Runtime.getRuntime().exec("jdk1.8.0_281.jdk/Contents/Home/bin/java -jar forge.jar");
                printResults(forgeinstaller);
                System.out.println("installed forge");
            }
            else {
                System.out.println("Using system install of Java");
                forgeinstaller = Runtime.getRuntime().exec("java -jar forge.jar");
                printResults(forgeinstaller);
                System.out.println("installed forge");
            }

            // set progressbar
            StepProgress stepProgress7 = new StepProgress(0.9);
            Thread t7 = new Thread(stepProgress7);
            t7.start();

            // remove Forge installer
            Process forgeremove = Runtime.getRuntime().exec("rm forge.jar");
            printResults(forgeremove);
            System.out.println("removed forge installer");

            // remove Forge installer log
            Process forgelog = Runtime.getRuntime().exec("rm forge.jar.log");
            printResults(forgelog);
            System.out.println("removed forge log");

            // Add profile to the Minecraft launcher
            EditJsonMac(minecraftPath);
            System.out.println("injected profile into minecraft launcher");

            t.stop();
            t2.stop();
            t3.stop();
            t4.stop();
            t6.stop();
            t7.stop();

            progressBar.setProgress(1);
            progressBar.setVisible(false);
            pane.setMargin(progressBar, new Insets(0, 10, 10, 250));
            install.setText("Modpack Installed!");
            pane.setMargin(install, new Insets(60, 10, 10, 250));
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void Windows() {
        System.out.println("Initializing Windows Installation of Modpack");
        try {
            progressBar.setVisible(true);
            StepProgress stepProgress = new StepProgress(0.1);
            Thread t = new Thread(stepProgress);
            t.start();

            // progressBar.setProgress(0.1);

            // Download the zip archive
            Process download = Runtime.getRuntime().exec("curl -L https://modpackinstaller.page.link/mods -o mods.zip");
            printResults(download);
            System.out.println("Download Mods Done");
            // progressBar.setProgress(0.4);
            StepProgress stepProgress2 = new StepProgress(0.4);
            Thread t2 = new Thread(stepProgress2);
            t2.start();
            // unzip the zip archive
            Process unzip = Runtime.getRuntime().exec("powershell -command \"Expand-Archive -Force mods.zip\"");
            printResults(unzip);
            System.out.println("Unzip Done");
            StepProgress stepProgress3 = new StepProgress(0.5);
            Thread t3 = new Thread(stepProgress3);
            t3.start();
            // remove the zip archive
            Process removeZip = Runtime.getRuntime().exec("powershell -command \"rm mods.zip\"");
            printResults(removeZip);
            System.out.println("Remove Zip Done");
            StepProgress stepProgress4 = new StepProgress(0.6);
            Thread t4 = new Thread(stepProgress4);
            t4.start();
            // rename and move the game directory
            String dataFolder = System.getenv("APPDATA");
            String modpackFolder = dataFolder + "\\.minecraftModded2021";
            String minecraftDir = dataFolder + "\\.minecraft";
            String jsonPath = minecraftDir + "\\launcher_profiles.json";
            Process rename = Runtime.getRuntime().exec("cmd /c rename mods .minecraftModded2021");
            printResults(rename);
            System.out.println("Rename Done");

            Process moveMods = Runtime.getRuntime().exec("cmd /c move .minecraftModded2021 " + dataFolder);
            printResults(moveMods);
            System.out.println("Move Done");
            StepProgress stepProgress5 = new StepProgress(0.7);
            Thread t5 = new Thread(stepProgress5);
            t5.start();
            // Download the Forge installer
            Process downloadForge = Runtime.getRuntime()
                    .exec("curl -L https://modpackinstaller.page.link/forge -o forge.jar");
            // Process downloadForge = Runtime.getRuntime().exec("powershell -command
            // \"Invoke-WebRequest -Uri \"https://modpackinstaller.page.link/forge\"
            // -OutFile \"forge.jar\"");
            printResults(downloadForge);
            System.out.println("Download Forge Done");
            StepProgress stepProgress6 = new StepProgress(0.8);
            Thread t6 = new Thread(stepProgress6);
            t6.start();
            // run the Forge installer
            Process runForge;
            File java = new File("Java");
            if (java.isDirectory()) {
                System.out.println("Using bundled Java");
                runForge = Runtime.getRuntime().exec("cmd /c Java\\jre1.8.0_281\\bin\\java.exe -jar forge.jar");
                printResults(runForge);
                System.out.println("Run Forge Done");
            }
            else {
                System.out.println("Using system Java");
                runForge = Runtime.getRuntime().exec("cmd /c java -jar forge.jar");
                printResults(runForge);
                System.out.println("Run Forge Done");
            }

            StepProgress stepProgress7 = new StepProgress(0.9);
            Thread t7 = new Thread(stepProgress7);
            t7.start();

            String newdir = minecraftDir.replace('\\', '/');
            String newdir2 = modpackFolder.replace('\\', '/');
            System.out.println(newdir);
            // Add profile to the Minecraft launcher
            EditJsonWindows(newdir, newdir2);
            System.out.println("profile injected into minecraft launcher");
            t.stop();
            t2.stop();
            t3.stop();
            t4.stop();
            t5.stop();
            t6.stop();
            t7.stop();

            progressBar.setProgress(1);
            //progressBar.setVisible(false);
            //progressBar.setMaxSize(0, 0);
            //progressBar.setLayoutX(0);
            //progressBar.relocate(0, 0);
            //install.relocate(10, 10);

            progressBar.setVisible(false);
            pane.setMargin(progressBar, new Insets(0, 10, 10, 250));
            install.setText("Modpack Installed!");
            pane.setMargin(install, new Insets(60, 10, 10, 250));

        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // prints results of terminal/cmd commands
    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    // returns results of terminal/cmd commands
    public static String returnResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String out = "";
        while ((line = reader.readLine()) != null) {
            out = out + line;
        }
        return out;
    }

    // Rickrolls the user haha
    public static void rickroll(String os) {
        try {
            Process rickroll = Runtime.getRuntime()
                    .exec("curl -L https://modpackinstaller.page.link/rickroll -o rickroll.mp3");
            printResults(rickroll);
            String path = "";
            if (os.contains("Mac")) {
                Process rickrolldir = Runtime.getRuntime().exec("pwd");
                path = returnResults(rickrolldir);
                path = path + "/rickroll.mp3";
            }
            if (os.contains("Windows")) {
                path = "rickroll.mp3";
            }
            Media media = new Media(new File(path).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.1);
            mediaPlayer.setAutoPlay(true);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Initializes text
    public static void StartText() {
        install.setFont(font);
        install.setFill(Color.LIGHTGREEN);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        install.setStyle("-fx-text-alignment: center");
        pane.setMargin(install, new Insets(60, 10, 10, 320));
        pane.getChildren().add(install);
    }

    // Initializes bar
    public static void StartBar() {
        progressBar.setMaxSize(300, 10);
        progressBar.setProgress(0);
        pane.getChildren().add(progressBar);
        pane.setMargin(progressBar, new Insets(50, 10, 10, 250));
    }

    // Initializes text
    public static void UninstallTextFunc() {
        uninstallText.setFont(font);
        uninstallText.setFill(Color.LIGHTGREEN);
        pane.setMargin(uninstallText, new Insets(60, 10, 10, 250));
        pane.getChildren().add(uninstallText);
    }

    // Adds profile to the Minecraft launcher on MacOS
    public static void EditJsonMac(String path) {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            pyInterp.exec("import json \n" + "  \n" + "  \n" + "# function to add to JSON \n"
                    + "def write_json(data, filename='" + path + "/launcher_profiles.json'): \n"
                    + "    with open(filename,'w') as f: \n" + "        json.dump(data, f, indent=4) \n" + "      \n"
                    + "      \n" + "with open('" + path + "/launcher_profiles.json') as json_file: \n"
                    + "    data = json.load(json_file) \n" + "      \n" + "    temp = data['profiles'] \n" + "  \n"
                    + "    # python object to be appended \n" + "    y = {\n"
                    + "        \"created\" : \"2021-02-04T01:56:14.530Z\",\n" + "        \"gameDir\" : \"" + path
                    + "minecraftModded2021\",\n"
                    + "        \"icon\" : \"data:image/png;base64,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\",\n"
                    + "        \"javaArgs\" : \"-Xmx8G -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M\",\n"
                    + "        \"lastUsed\" : \"2021-03-01T00:00:00.000Z\",\n"
                    + "        \"lastVersionId\" : \"1.12.2-forge-14.23.5.2854\",\n"
                    + "        \"name\" : \"Minecraft Modded 2021\",\n" + "        \"type\" : \"custom\"\n"
                    + "        }\n" + "  \n" + "  \n" + "    # appending data to emp_details  \n"
                    + "    temp[\"modded2021\"] = y\n" + "      \n" + "write_json(data)  ')");
        }
    }

    // Adds profile to the Minecraft launcher on Windows
    public static void EditJsonWindows(String path, String gameDir) {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            pyInterp.exec("import json \n" + "  \n" + "  \n" + "# function to add to JSON \n"
                    + "def write_json(data, filename='" + path + "/launcher_profiles.json'): \n"
                    + "    with open(filename,'w') as f: \n" + "        json.dump(data, f, indent=4) \n" + "      \n"
                    + "      \n" + "with open('" + path + "/launcher_profiles.json') as json_file: \n"
                    + "    data = json.load(json_file) \n" + "      \n" + "    temp = data['profiles'] \n" + "  \n"
                    + "    # python object to be appended \n" + "    y = {\n"
                    + "        \"created\" : \"2021-02-04T01:56:14.530Z\",\n" + "        \"gameDir\" : \"" + gameDir
                    + "\",\n"
                    + "        \"icon\" : \"data:image/png;base64,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\",\n"
                    + "        \"javaArgs\" : \"-Xmx8G -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M\",\n"
                    + "        \"lastUsed\" : \"2021-10-01T00:00:00.000Z\",\n"
                    + "        \"lastVersionId\" : \"1.12.2-forge-14.23.5.2854\",\n"
                    + "        \"name\" : \"Minecraft Modded 2021\",\n" + "        \"type\" : \"custom\"\n"
                    + "        }\n" + "  \n" + "  \n" + "    # appending data to emp_details  \n"
                    + "    temp[\"modded2021\"] = y\n" + "      \n" + "write_json(data)  ')");
        }
    }

    // Uninstalls the modpack on MacOS
    public static void UninstallMac() {
        try {

            // Gets the current username
            Process user1 = Runtime.getRuntime().exec("id -un");
            String id1 = returnResults(user1);

            // deletes the game directory
            String minecraftPath1 = "/Users/" + id1 + "/Library/Application Support/minecraft/minecraftModded2021";
            System.out.println(minecraftPath1);
            File minecraftPath2 = new File(minecraftPath1);
            deleteFolder(minecraftPath2);
            System.out.println("Uninstall Done!");

            uninstallText.setText("Uninstalled Successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Uninstalls the modpack on Windows
    public static void UninstallWindows() {

        // deletes the game directory
        String dataFolder1 = System.getenv("APPDATA");
        String modpackFolder1 = dataFolder1 + "\\.minecraftModded2021";
        File minecraftPath3 = new File(modpackFolder1);
        deleteFolder(minecraftPath3);
        System.out.println("Uninstall Done!");

        uninstallText.setText("Uninstalled Successfully");
    }

    // method for deleting folders with subfolders inside them
    static void deleteFolder(File file) {
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        file.delete();
    }
}
