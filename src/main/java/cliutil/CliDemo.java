package cliutil;

import cliutil.hdfsutil.HdfsUtil;
import cliutil.kafkautil.MyKafkaProduce;
import org.apache.commons.cli.*;
import org.apache.hadoop.fs.FileStatus;

import java.util.LinkedList;

/**
 * Created by zhou1 on 2019/5/27.
 */
public class CliDemo {
    private static Options opts = new Options();

    private static Option opt_hdfs = new Option("h","help",true ,"hdfs ip");
    private static Option opt_path = new Option("p","path",true ,"hdfs path");
    private static Option opt_pattern = new Option("pt","help",true ,"hdfs pattern");

    private static String hdfsUrl;
    private static String hdfsPath;
    private static String filePattern;

    static {

        opt_hdfs.setRequired(true);
        opt_path.setRequired(true);
        opt_pattern.setRequired(false);

        opts.addOption(opt_hdfs);
        opts.addOption(opt_path);
        opts.addOption(opt_pattern);
    }

    public static void main(String[] args){
        LinkedList<FileStatus> fileList = new LinkedList<>();

        CommandLineParser parser = new BasicParser();
        CommandLine line = null;
        // 解析命令行参数
        try {
            line = parser.parse(opts, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }



        hdfsUrl = line.getOptionValue("h");
        hdfsPath = line.getOptionValue("p");
        filePattern = line.getOptionValue("pt")==null ? ".*":line.getOptionValue("pt");

        if (hdfsUrl.indexOf("hdfs")<0){
            hdfsUrl= "hdfs://" + hdfsUrl;
        }
        System.out.println(String.format("need to copy %s on %s match %s",hdfsPath,hdfsUrl,filePattern));

        HdfsUtil hdfsUtil = new HdfsUtil(hdfsUrl,hdfsPath,filePattern);
        hdfsUtil.generateFileList(fileList);

        //todo:check the total volume of all the files

        MyKafkaProduce myKafkaProducer = new MyKafkaProduce(hdfsUtil);
        for (FileStatus fileStatus: fileList){
            myKafkaProducer.produce(fileStatus);
        }
    }
}
