package sample;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Class for receiving File between two machines
 */
public class FileReceiver {

    ServerSocketChannel serverSocketChannel;

    /**
     * Method for creating ServerSocketChannel to receive file across network
     * @return SocketChannel Object created to share file between two machines
     */
    public SocketChannel createServerSocketChannel() {

        SocketChannel socketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            try {
                serverSocketChannel.socket().bind(new InetSocketAddress(9999));
            }
            catch (BindException e){
                e.printStackTrace();
            }
            socketChannel = serverSocketChannel.accept();
            System.out.println("Connection established...." + socketChannel.getRemoteAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return socketChannel;
    }

    /**
     * Method to read file from Socket Channel and write to a local file
     * @param socketChannel SocketChannel object across which file is received
     * @return String response denoting SUCCESS or FAILED
     */
    public String readFileFromSocket(SocketChannel socketChannel) {
        RandomAccessFile aFile = null;
        try {
            ServerSocket serversocket=new ServerSocket(6547);
            Socket socket=serversocket.accept();
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            String name= (String) ois.readObject();
            String home = System.getProperty("user.home");
            String fileName = home+File.separator+name;
            File file=new File(fileName);
            file.createNewFile();
            aFile = new RandomAccessFile(fileName, "rw");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            FileChannel fileChannel = aFile.getChannel();
            while (socketChannel.read(buffer)> 0) {
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
            Thread.sleep(1000);
            fileChannel.close();
            System.out.println("End of file reached..Closing channel");
            socketChannel.close();
            serverSocketChannel.close();
            return "SUCCESS";

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "FAILED";
        } catch (IOException e) {
            e.printStackTrace();
            return "FAILED";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "FAILED";
        } catch (Exception e){
            return "FAILED";
        }
    }
}
