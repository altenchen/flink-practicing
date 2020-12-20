package javabasic.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @create: 2020/12/20
 * @author: altenchen
 */
public class FileChannelTxt {

    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("/Users/chenchen/bitnei/testdetailjob.sh", "rw");

        FileChannel inChannel = raf.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);

//        ByteBuffer buf2 = ByteBuffer.allocate(48);
//
//        buf2.put("fileChannel test".getBytes());
//        buf2.flip();

        //inChannel.write(buf);

        while (bytesRead != -1) {
            buf.flip();

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }

            //清空缓冲区
            buf.clear();

            bytesRead = inChannel.read(buf);
        }

        raf.close();
        inChannel.close();
    }


}
