package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Chat {

    volatile static ObjectOutputStream oos=null;
    volatile static ObjectInputStream ois=null;
    @FXML
    ListView chatView;

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
                while (true){
                    String msg=recieveMessage();
                    System.out.println(msg);
                    chat.add(msg);
                    chatView.getItems().setAll(chat);
                }
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

    public String recieveMessage(){
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
        }
        return msg;
    }

    public void sendClicked(ActionEvent actionEvent) {

    }
}
