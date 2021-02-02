package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
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
}
