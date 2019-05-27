package cliutil.hdfsutil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by zhou1 on 2019/5/27.
 */
public class HdfsUtil {
    private String hdfsUrl;
    private String hdfsPath;
    private String hdfsPattern;

    private Configuration config;
    public FileSystem fs;

    public HdfsUtil(String url,String path,String pattern){
        hdfsUrl = url;
        hdfsPath = path;
        hdfsPattern = pattern;

        config = new Configuration();
        config.set("fs.defaultFS",hdfsUrl);
        try {
            fs = FileSystem.get(config);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void generateFileList(LinkedList<FileStatus> list){
        try{
            FileStatus[] fileStatus = fs.listStatus(new Path(hdfsPath));
            for(FileStatus f:fileStatus){
//                System.out.println(f.getPath().getName());
                if (f.isFile() && f.getPath().getName().matches(hdfsPattern)){
                    System.out.println(f.getPath());
                    list.add(f);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
