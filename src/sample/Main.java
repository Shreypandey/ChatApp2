package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main class for declaring all the static variables
 * and invoking main method
 */
public class Main extends Application {

    public volatile static ServerSocket serverSocket;
    public volatile static Socket socket;
    public volatile static FileReceiver nioServer;
    public volatile static File selectedFile;
    public volatile static String username;

    /**
     * Method to invoke Javafx Stage
     * @param primaryStage Stage which is used across the application
     * @throws Exception FileNotFoundException
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ChatAPP");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /**
     * Main for launching the code
     * @param args Command Line Arguments
     */
    public static void main(String[] args) {
        username=System.getProperty("user.name");
        launch(args);
    }
}
