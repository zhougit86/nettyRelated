package cliutil.hdfsutil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhou1 on 2019/5/27.
 */
public class MyReader {
    private FSDataInputStream hdfsInputStream;
//    private InputStreamReader reader;
//    private BufferedReader bufferedReader;
//    private FileInputStream fileInputStream

    public MyReader(FileStatus fileStatus, FileSystem fs) throws IOException {
            hdfsInputStream = fs.open(fileStatus.getPath());
//            reader = new InputStreamReader(localFileStream);
//            bufferedReader = new BufferedReader(reader);
    }

    public int read(ByteBuffer byteBuffer) throws IOException{
        return hdfsInputStream.read(byteBuffer);
    }

    public void close() throws IOException{
        hdfsInputStream.close();
    }

    public static void main(String[] args){
        String location = "hdfs://192.168.13.128:9000/input/myfile.txt";
        String url = "hdfs://192.168.13.128:9000";

        Configuration config = new Configuration();
        config.set("fs.defaultFS",url);
        FileSystem fs= null;
        try {
            fs = FileSystem.get(config);
            FSDataInputStream localFileStream = fs.open(new Path(location));


            ByteBuffer byteBuffer = ByteBuffer.allocate(20);
            byte[] bytes = new byte[20];


            while(true){
                int size = localFileStream.read(byteBuffer);
                System.err.println(size);
                if (size == -1){
                    break;
                }
                byteBuffer.flip();
                byteBuffer.get(bytes,0,size);
                byteBuffer.clear();
                System.err.println(bytes.toString());

                Thread.sleep(200L);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }


    }
}
