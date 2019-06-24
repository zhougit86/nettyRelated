package netty.bufferdemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by zhou1 on 2019/6/23.
 */
public class ByteBufDemo2 {
    public static void main(String[] args){
        ByteBuf buffer = Unpooled.directBuffer(); //get reference form somewhere
        for (int i = 0 ; i<10 ; i++){
            buffer.writeByte(i);
        }

//        while (buffer.readableBytes() >0) {
//            System.err.println(buffer.readableBytes());
////            byte b = buffer.getByte(i);// 不改变readerIndex值
//            System.out.println(buffer.readByte());
////            System.out.printf("%s",(char) b);
//        }

        for (int j =0;j<5;j++){
            buffer.readByte();
        }
        buffer.discardReadBytes();
        System.err.println(buffer.writerIndex());

        System.err.println(buffer.indexOf(0,6,(byte)1));
//        https://blog.csdn.net/thinking_fioa/article/details/80795673
    }
}
