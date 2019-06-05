package netty.ch4.oio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;

import java.nio.charset.Charset;

/**
 * Created by zhou1 on 2019/6/4.
 */
public class NettyOioServer {
    public void server(int port) throws Exception{
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"))
        );
        //netty修改程序是同步还是异步只需要将group和channel从Nio和Oio之间修改就可以了
        EventLoopGroup group = new OioEventLoopGroup();


    }
}
