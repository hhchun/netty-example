package com.qqs.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class LocalFileRead {
    public static void main(String[] args) {
        FileInputStream is = null;
        FileChannel channel = null;
        try {
            File file = new File("/var/tmp/file01.txt");
            is = new FileInputStream(file);
            channel = is.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
            channel.read(buffer);
            String data = new String(buffer.array());
            System.out.println("data = " + data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
