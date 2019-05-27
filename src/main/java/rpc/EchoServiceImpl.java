package rpc;

/**
 * Created by zhou1 on 2019/5/20.
 */
public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String ping){
//        ClassLoader
        return ping!=null ? ping+"--> i am ok." : "i am not ok.";
    }
}
