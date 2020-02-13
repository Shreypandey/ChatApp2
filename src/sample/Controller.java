package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class Controller {
    public void sendPerform(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TextInputDialog textInputDialog = new TextInputDialog("Enter IP Address");
                textInputDialog.setHeaderText("Enter IP Address");
                textInputDialog.showAndWait();
                String ip=textInputDialog.getEditor().getText();
                try {
                    Main.socket=new Socket(ip,6969);
                    System.out.println("Connected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

                        // smth we can explore
                        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                        while(addresses.hasMoreElements()) {
                            addr = addresses.nextElement();
                            if (!networkInterface.getDisplayName().equals("wlo1"))continue;
                            if (InetAddress.getByName(addr.getCanonicalHostName()) instanceof Inet6Address)continue;

                            System.out.println("Waiting to connect to ip :"+addr.getCanonicalHostName());
                        }
                    }
                    Main.socket=Main.serverSocket.accept();
                    System.out.println("Connected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
