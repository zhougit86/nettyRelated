package netty.ch4.oio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by zhou1 on 2019/6/3.
 */
public class PlainOioServer {
    public void serve(int port) throws IOException{
        final ServerSocket socket = new ServerSocket(port);

        try {
            for (; ; ) {
                final Socket clientSocket = socket.accept();
                System.out.println(
                        "Accepted connection from " + clientSocket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes(
                                    Charset.forName("UTF-8")));
                            out.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {
// ignore on close
                            }
                        }
                    }
                }).start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
