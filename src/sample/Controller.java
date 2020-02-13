package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class Controller {

    @FXML
    Button sendButton;
    @FXML
    Button recieveButton;

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
            }
        });

    }

    public void recievePerform(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.serverSocket=new ServerSocket(6969);
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
                            Stage primaryStage = (Stage) recieveButton.getScene().getWindow();
                            Parent root = null;
                            try {

                                root = FXMLLoader.load(getClass().getResource("reg.fxml"));
                            }catch(IOException e){
                                e.printStackTrace();
                            }
                            primaryStage.setScene(new Scene(root, 1081, 826));
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
