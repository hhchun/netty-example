package com.qqs.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BIOServer {
    public static void main(String[] args) throws IOException {
        final ServerSocket server = new ServerSocket(6666);
        final ExecutorService executor = Executors.newCachedThreadPool();
        System.out.println("BIOServer start!");
        try {
            while (true) {
                System.out.println("current thread id =" + Thread.currentThread().getId() +
                        ",name = " + Thread.currentThread().getName());
                System.out.println("waiting for connection...");
                final Socket socket = server.accept();
                System.out.println("connect to client");
                executor.execute(() -> {
                    handler(socket);
                });
            }
        } finally {
            server.close();
        }
    }

    public static void handler(Socket socket) {
        try {
            System.out.println("current thread id =" + Thread.currentThread().getId() +
                    ",name = " + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true) {
                System.out.println("current thread id =" + Thread.currentThread().getId() +
                        ",name = " + Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("close socket");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
