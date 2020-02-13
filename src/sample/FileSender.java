package sample;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * Class for sending file between two machines
 */
public class FileSender {

    /**
     * Establishes a socket channel connection
     * @param ip IP Address of the Machine receiving the file
     * @return SocketChannel object after connection is made
     */
    public SocketChannel createChannel(String ip) {

        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(ip, 9999);
            socketChannel.connect(socketAddress);
            System.out.println("Connected..Now sending the file");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return socketChannel;
    }

    /**
     * Method to send file between two machine
     * @param socketChannel SocketChannel object across which file is sent
     * @return String response denoting SUCCESS or FAILED
     */
    public String sendFile(SocketChannel socketChannel) {
        RandomAccessFile aFile = null;
        try {
            File file = Main.selectedFile;
            Socket socket=new Socket(Main.socket.getInetAddress().getCanonicalHostName(),6547);
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            oos.writeObject(file.getName());
            oos.flush();
            aFile = new RandomAccessFile(file, "r");
            FileChannel inChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
            }
            System.out.println("End of file reached..");
            socketChannel.close();
            aFile.close();
            return "SUCCESS";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "SUCCESS";
    }

}