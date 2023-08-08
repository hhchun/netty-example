package com.qqs.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

public class MoresIoReadWrite {
    public static void main(String[] args) throws IOException {
        File file = new File("/var/tep/1.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        byte[] buffer = new byte[(int) file.length()];
        int len = raf.read(buffer);
        if (len > 0) {
            Socket socket = new ServerSocket(8080).accept();
            socket.getOutputStream().write(buffer);
        }
    }
}
