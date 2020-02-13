package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class Main extends Application {

    public volatile static ServerSocket serverSocket;
    public volatile static Socket socket;
    public volatile static FileReceiver nioServer;
    public volatile static File selectedFile;
    public volatile static String username;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ChatAPP");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        username=System.getProperty("user.name");
        launch(args);
    }
}
