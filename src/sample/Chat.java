package sample;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Chat {

    volatile static ObjectOutputStream oos=null;
    volatile static ObjectInputStream ois=null;
    @FXML
    ListView chatView;
    @FXML
    TextField textarea;

    List<String> chat=new ArrayList<String>();

    public void initialize(){
        chatView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        chatView.getItems().setAll(chat);
        System.out.println("yahan aaya");
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Main.socket);
                while (Main.socket==null);
                System.out.println(Main.socket);
                try {
                    Chat.oos=new ObjectOutputStream(Main.socket.getOutputStream());
                    Chat.ois=new ObjectInputStream(Main.socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (true){
                    String msg=recieveMessage();
                    System.out.println(msg);
                    if(msg==null)
                        break;
                    chat.add(msg);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            chatView.getItems().setAll(chat);
                        }
                    });
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Main.socket);
                while (Main.socket==null);
                System.out.println(Main.socket);
                SocketChannel socketChannel=Main.nioServer.createServerSocketChannel();
                Main.nioServer.readFileFromSocket(socketChannel);
            }
        }).start();
    }

    public void sendMessage(String msg){
        if (oos==null) {
            try {
                oos=new ObjectOutputStream(Main.socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String recieveMessage(){
        if (ois==null) {
            try {
                ois=new ObjectInputStream(Main.socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String msg=null;
        try {
            msg= (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return msg;
    }

    public void sendClicked(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String msg=Main.username+":\t"+textarea.getText();
                chat.add(msg);
                chatView.getItems().setAll(chat);
                textarea.setText("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(msg);
                    }
                }).start();
            }
        });


    }

    public void sendFileClicked(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileSender nioClient = new FileSender();
                        System.out.println("File sender initialised");
                        SocketChannel socketChannel = nioClient.createChannel(Main.socket.getInetAddress().getCanonicalHostName());
                        System.out.println("Channel created");
                        System.out.println("Transfer starting");
                        nioClient.sendFile(socketChannel);
                    }
                }).start();
            }
        });
    }
}
