package cliutil;

import cliutil.cosutil.CosUtil;
import cliutil.hdfsutil.HdfsUtil;
import cliutil.kafkautil.MyKafkaProduce;
import org.apache.commons.cli.*;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by zhou1 on 2019/5/27.
 */
public class CliDemo {
    private static Logger LOG = LoggerFactory.getLogger("main");

    private static Options opts = new Options();

    private static Option opt_hdfs = new Option("hd","hdfs",true ,"hdfs ip, eg:192.168.13.128:9000");
    private static Option opt_path = new Option("p","path",true ,"hdfs path, eg:/tmp");
    private static Option opt_pattern = new Option("pt","pattern",true ,"hdfs pattern, eg:.*");
    private static Option opt_name = new Option( "n","name"
            ,true, "name for this specific run" );
    private static Option opt_kfkip = new Option("kip","kafkaip",true ,"kafka broker ip, eg:192.168.13.128:9092");
    private static Option opt_kfktopic = new Option("kt","kafkatopic",true ,"kafka topic, eg:demo_topic");

    private static Option opt_aes_key = new Option( "k","aes_key"
            ,true, "key of AES" );

    private static String hdfsUrl;
    private static String hdfsPath;
    private static String filePattern;
    private static String executeName;
    private static String kafkaBrokerIp;
    private static String kafkaTopic;
    private static String AES_KEY;

    static {

        opt_hdfs.setRequired(true);
        opt_path.setRequired(true);
        opt_pattern.setRequired(false);
        opt_name.setRequired(true);
        opt_kfkip.setRequired(true);
        opt_kfktopic.setRequired(true);
        opt_aes_key.setRequired(true);

        opts.addOption(opt_hdfs);
        opts.addOption(opt_path);
        opts.addOption(opt_pattern);
        opts.addOption(opt_name);
        opts.addOption(opt_kfkip);
        opts.addOption(opt_kfktopic);
        opts.addOption(opt_aes_key);
    }

    public static void main(String[] args){

        LinkedList<FileStatus> fileList = new LinkedList<>();

        CommandLineParser parser = new BasicParser();
        CommandLine line = null;
        // 解析命令行参数
        try {
            line = parser.parse(opts, args);
        } catch (ParseException e) {
            HelpFormatter formater = new HelpFormatter();
            formater.printHelp("Main", opts);
            System.exit(1);
            e.printStackTrace();
        }

        hdfsUrl = line.getOptionValue("hd");
        hdfsPath = line.getOptionValue("p");
        executeName = line.getOptionValue("n");
        kafkaBrokerIp = line.getOptionValue("kip");
        kafkaTopic = line.getOptionValue("kt");
        filePattern = line.getOptionValue("pt")==null ? ".*":line.getOptionValue("pt");
        AES_KEY = line.getOptionValue("k");

        DateFormat simpleFormat = new SimpleDateFormat("YYMMdd-HHmmss");
        String dateString = simpleFormat.format(Calendar.getInstance().getTime());

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(dateString);
        stringBuffer.append("-");
        stringBuffer.append(executeName);
        executeName = stringBuffer.toString();


        if (hdfsUrl.indexOf("hdfs")<0){
            hdfsUrl= "hdfs://" + hdfsUrl;
        }
        LOG.info("{}: need to copy {} on {} match {}",executeName,hdfsPath,hdfsUrl,filePattern);

        HdfsUtil hdfsUtil = new HdfsUtil(hdfsUrl,hdfsPath,filePattern);
        hdfsUtil.generateFileList(fileList);

        //todo:check the total volume of all the files

        MyKafkaProduce myKafkaProducer = new MyKafkaProduce(hdfsUtil,kafkaBrokerIp,kafkaTopic,AES_KEY);
        for (FileStatus fileStatus: fileList){
            LOG.info("handling file {}",fileStatus.getPath().toString());
            myKafkaProducer.avroProduce(fileStatus);
//            CosUtil.initBackUpDir(executeName, fileStatus, hdfsUtil.fs );
            LOG.info("handling file finish {}",fileStatus.getPath().toString());
        }

        CosUtil.closeClient();
    }
}
