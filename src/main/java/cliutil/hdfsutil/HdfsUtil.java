package cliutil.hdfsutil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by zhou1 on 2019/5/27.
 */
public class HdfsUtil {
    private static Logger LOG = LoggerFactory.getLogger(HdfsUtil.class);

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

    private void traverseDir(LinkedList<FileStatus> list, Path inputPath){
        try{
            FileStatus[] fileStatus = fs.listStatus(inputPath);
            for(FileStatus f:fileStatus){
                if (f.isFile() && f.getPath().getName().matches(hdfsPattern)){
                    LOG.info("{} matches the pattern",f.getPath());
                    list.add(f);
                }
                if (f.isDirectory()){
                    traverseDir(list,f.getPath());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void generateFileList(LinkedList<FileStatus> list){
        traverseDir(list,new Path(hdfsPath));
    }

    public void copyFileToSnapshot(String source, String target) throws IOException{
        Path targetPath = new Path(target);
        boolean mkdirResult = fs.mkdirs(targetPath.getParent());
        if (!mkdirResult){
            LOG.error("创建目录 {} 失败",targetPath.getParent());
        }

        if (fs.rename(new Path(source),new Path(target))){
            LOG.info("{} 快照移动成功", source);
        }else {
            LOG.error("{} 快照移动失败", source);
        }

//        fs.copyToLocalFile(new Path(source),new Path(target));
//        DistCp myDistCp = new DistCp(config, new DistCpOptions(new Path(source),new Path(target)));
//        myDistCp.execute();
    }

}
