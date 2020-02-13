package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * Controller class for Start Page
 */
public class Controller {

    @FXML
    Button sendButton;
    @FXML
    Button recieveButton;

    /**
     * Method invoked when Send button is clicked
     * Application can work as a sender application
     * @param actionEvent
     */
    public void sendPerform(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TextInputDialog textInputDialog = new TextInputDialog("Enter IP Address");
                textInputDialog.setHeaderText("Enter IP Address");
                textInputDialog.showAndWait();
                String ip=textInputDialog.getEditor().getText();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Main.socket=new Socket(ip,6969);
                            System.out.println("Connected");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                FileChooser fileChooser = new FileChooser();
                Stage primaryStage = (Stage) sendButton.getScene().getWindow();
                Main.selectedFile = fileChooser.showOpenDialog(primaryStage);
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("chat.fxml"));
                }catch(IOException e){
                    e.printStackTrace();
                }
                primaryStage.setScene(new Scene(root, 600, 400));
            }
        });

    }

    /**
     * Method invoked when Receive button is clicked
     * Application can work as a receiver application
     * @param actionEvent
     */
    public void recievePerform(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.serverSocket=new ServerSocket(6969);
                    Main.nioServer = new FileReceiver();
                    InetAddress localhost = (InetAddress) InetAddress.getLocalHost();
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    InetAddress addr;
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface networkInterface = interfaces.nextElement();
                        // drop inactive
                        if (!networkInterface.isUp())
                            continue;
                        if (!networkInterface.getDisplayName().equals("wlo1"))continue;
                        // smth we can explore
                        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                        while(addresses.hasMoreElements()) {
                            addr = addresses.nextElement();

                            if (InetAddress.getByName(addr.getCanonicalHostName()) instanceof Inet6Address)continue;

                            System.out.println("Waiting to connect to ip :"+addr.getCanonicalHostName());
                        }
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Main.socket=Main.serverSocket.accept();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Connected");
                        }
                    }).start();
                    Stage primaryStage = (Stage) recieveButton.getScene().getWindow();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("chat.fxml"));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    primaryStage.setScene(new Scene(root, 600, 400));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
