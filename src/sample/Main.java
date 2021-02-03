package sample;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;


public class Main extends Application {
    public static String version = "v1.0";
    public static VBox pane = new VBox();
    public static Font font = Font.font("Verdana", FontWeight.BOLD, 27);
    public static Text install = new Text("Installation Failed");



    @Override
    public void start(Stage primaryStage) throws Exception{

        String os = System.getProperty("os.name");
        System.out.println(os);
        BackgroundImage myBI= new BackgroundImage(new Image("https://i.ibb.co/qsb2kGJ/shaderwheat.png",900,500,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);


        primaryStage.setTitle("Modpack Installer");
        pane.setPrefSize(800,400);
        pane.setBackground(new Background(myBI));

        Button button = new Button("Install");
        button.setStyle("-fx-background-color: #1fc127");
        button.setTextFill(Color.WHITE);
        button.setMaxSize(300, 300);


        Text welcomeText = new Text("Welcome to the Modpack Installer!");
        button.setFont(font);
        welcomeText.setFont(font);
        welcomeText.setFill(Color.LIGHTGREEN);


        pane.getChildren().add(welcomeText);
        pane.getChildren().add(button);


        pane.setMargin(button, new Insets(30, 10, 10, 250));
        pane.setMargin(welcomeText, new Insets(20, 10, 10, 140));
        primaryStage.setScene(new Scene(pane));
        primaryStage.setResizable(false);
        primaryStage.show();

        button.setOnAction(e -> {
            button.setDisable(true);
            StartText();

            install(os);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean install(String os) {

        if (os.contains("Mac")) {
            MacOS();
            rickroll(os);
        }

        if (os.contains("Windows")) {
            Windows();
            rickroll(os);
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

            Process curl = Runtime.getRuntime().exec("curl https://modpackinstaller.page.link/mods -O -L -J");
            printResults(curl);

            Process unzip = Runtime.getRuntime().exec("unzip mods.zip");
            printResults(unzip);

            Process remove = Runtime.getRuntime().exec("rm mods.zip");
            printResults(remove);

            Process user = Runtime.getRuntime().exec("id -un");
            String id = returnResults(user);
            System.out.println(id);

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("mkdir", "minecraftModded2021").directory(new File(("/Users/" + id + "/Library/Application Support/minecraft/")));
            Process mkdir = processBuilder.start();
            printResults(mkdir);

            ProcessBuilder mv = new ProcessBuilder();
            mv.command("mv", "mods", ("/Users/" + id + "/Library/Application Support/minecraft/minecraftModded2021"));
            Process mvp = mv.start();
            printResults(mvp);

            Process forgecurl = Runtime.getRuntime().exec("curl -L https://modpackinstaller.page.link/forge -o forge.jar");
            printResults(forgecurl);

            Process forgeinstaller = Runtime.getRuntime().exec("java -jar forge.jar");
            printResults(forgeinstaller);

            Process forgeremove = Runtime.getRuntime().exec("rm forge.jar");
            printResults(forgeremove);

            Process forgelog = Runtime.getRuntime().exec("rm forge.jar.log");
            printResults(forgelog);


            install.setText("Modpack Installed!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }



    public static void Windows() {
        System.out.println("Initializing Windows Installation of Modpack");
        try {
            Process download = Runtime.getRuntime().exec("curl -L https://modpackinstaller.page.link/mods -o mods.zip");
            printResults(download);
            System.out.println("Download Mods Done");

            Process unzip = Runtime.getRuntime().exec("powershell -command \"Expand-Archive -Force mods.zip\"");
            printResults(unzip);
            System.out.println("Unzip Done");

            Process removeZip = Runtime.getRuntime().exec("rm mods.zip");
            printResults(removeZip);
            System.out.println("Remove Zip Done");

            String dataFolder = System.getenv("APPDATA");
            String modpackFolder = dataFolder + "\\.minecraftModded2021";
            Process rename = Runtime.getRuntime().exec("cmd.exe /c ren mods .minecraftModded2021");
            printResults(rename);
            System.out.println("Rename Done");

            Process moveMods = Runtime.getRuntime().exec("cmd.exe /c move .minecraftModded2021 " + dataFolder);
            printResults(moveMods);
            System.out.println("Move Done");

            Process removeDir = Runtime.getRuntime().exec("rm .minecraftModded2021");
            printResults(removeDir);
            System.out.println("Remove Mods Dir Done");

            Process downloadForge = Runtime.getRuntime().exec("curl -L https://modpackinstaller.page.link/forge -o forge.jar");
            //Process downloadForge = Runtime.getRuntime().exec("powershell -command \"Invoke-WebRequest -Uri \"https://modpackinstaller.page.link/forge\" -OutFile \"forge.jar\"");
            printResults(downloadForge);
            System.out.println("Download Forge Done");

            // TODO Add popup here to tell the user to click install on the forge prompt. Continue with the code on them clicking ok.
            Process runForge = Runtime.getRuntime().exec("cmd /c java -jar forge.jar");
            printResults(runForge);
            System.out.println("Run Forge Done");

            Process removeJar = Runtime.getRuntime().exec("rm forge.jar");
            printResults(removeJar);
            System.out.println("Remove Forge Jar Done");

            Process removeLog = Runtime.getRuntime().exec("rm forge.jar.log");
            printResults(removeLog);
            System.out.println("Remove Forge Log Done");


            // TODO Edit launcherProfiles.json in the .minecraft folder to include a new profile entry with .minecraftModded2021 as the game directory and the correct version of forge as the version number.
            // TODO Add success function to be called when either os function is complete with no failures

            install.setText("Modpack Installed!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static String returnResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        String out = "";
        while ((line = reader.readLine()) != null) {
            out = out + line;
        }
        return out;
    }

    public static void rickroll(String os) {
        try {
            Process rickroll = Runtime.getRuntime().exec("curl -L https://modpackinstaller.page.link/rickroll -o rickroll.mp3");
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

    public static void StartText() {
        install.setFont(font);
        install.setFill(Color.LIGHTGREEN);
        pane.setMargin(install, new Insets(60, 10, 10, 250));
        pane.getChildren().add(install);
    }


    public static void EditJson() {
        // TODO EXPERIMENTAL DO NOT USE ON REAL Launcher_Profiles.Json

    }
}


