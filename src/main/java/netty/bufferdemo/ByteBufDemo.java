package netty.bufferdemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

import java.nio.charset.Charset;

/**
 * Created by zhou1 on 2019/6/4.
 */
public class ByteBufDemo {
    public static void main(String[] args){
        ByteBuf heapBuf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("abcdefghijklmnopqrstuvwxyz ", Charset.forName("UTF-8"))
        );

        ByteBuf sliceBuf = heapBuf.slice(0,15);
        ByteBuf copyBuf = heapBuf.copy(0,15);

        heapBuf.setByte(0,'b');
        System.err.printf("%c,%c\n",sliceBuf.getByte(0),copyBuf.getByte(0));

        System.err.println(heapBuf.capacity());
        System.err.println(heapBuf.forEachByte(ByteProcessor.FIND_ASCII_SPACE));

        if (heapBuf.hasArray()){
            byte[] array = heapBuf.array();
            System.err.println(new String(array));

            byte[] array1 = heapBuf.array();
            System.err.println(new String(array1));

            heapBuf.discardReadBytes();

            byte[] array2 = heapBuf.array();
            System.err.println(new String(array2));

            System.err.println("==========================");

            byte[] array3 = new byte[26];
            heapBuf.readBytes(array3);

            System.err.println(new String(array3));

            System.err.println(heapBuf.isReadable());

            System.err.println(heapBuf.readerIndex());
            heapBuf.discardReadBytes();//clear()在这个地方就轻量很多，把read,write都置零
            System.err.println(heapBuf.readerIndex());
        }

        System.err.println("==========================");

        for (int i = 0; i<heapBuf.writerIndex(); i++){
            byte b = heapBuf.getByte(i);
            System.err.println((char)b);
        }



        //hasArray是否为true代表这个缓存是否在堆上分配
//        CompositeByteBuf comBuf = Unpooled.compositeBuffer();
    }
}
