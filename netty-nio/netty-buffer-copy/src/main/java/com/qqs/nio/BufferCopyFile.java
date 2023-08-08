package com.qqs.nio;

import sun.nio.ch.IOStatus;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferCopyFile {
    public static void main(String[] args) {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream("/var/tmp/1.txt");
            os = new FileOutputStream("/var/tmp/2.txt");
            FileChannel inChannel = is.getChannel();
            FileChannel outChannel = os.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(512);
            while (true) {
                buffer.clear();
                int len = inChannel.read(buffer);
                System.out.println("len = " + len);
                if (len == IOStatus.EOF) {
                    break;
                }
                buffer.flip();
                outChannel.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
