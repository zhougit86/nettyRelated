package cliutil;

import cliutil.config.CKConfig;
import cliutil.cosutil.CosUtil;
import cliutil.hdfsutil.HdfsUtil;
import cliutil.kafkautil.MyKafkaProduce;
import org.apache.commons.cli.*;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.Configuration;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Created by zhou1 on 2019/5/27.
 */
public class CliDemo {
    private static Logger LOG = LoggerFactory.getLogger("main");

    private static Options opts = new Options();

//    private static Option opt_hdfs = new Option("hd", "hdfs", true, "hdfs ip, eg:192.168.13.128:9000");
//    private static Option opt_path = new Option("p", "path", true, "hdfs path, eg:/tmp");
//    private static Option opt_pattern = new Option("pt", "pattern", true, "hdfs pattern, eg:.*");
//    private static Option opt_name = new Option("n", "name"
//            , true, "name for this specific run");
//    private static Option opt_kfkip = new Option("kip", "kafkaip", true, "kafka broker ip, eg:192.168.13.128:9092");
//    private static Option opt_kfktopic = new Option("kt", "kafkatopic", true, "kafka topic, eg:demo_topic");

    private static Option opt_prop = new Option("prop", "properties"
            , true, "properties file");

    private static String hdfsUrl;
    private static String hdfsPath;
    private static String hdfsSnapshotDir;
    private static String filePattern;
    private static String executeName;
    private static String kafkaBrokerIp;
    private static String kafkaTopic;
    private static String propFile;

    private static final String HDFS_IP = "hdfs.ip";
    private static final String HDFS_PATH = "hdfs.path";
    private static final String HDFS_SNAP_DIR = "hdfs.snapshot.dir";
    private static final String FILE_PATTERN = "file.pattern";
    private static final String EXECUTE_NAME = "execute.name";
    private static final String KAFKA_IP = "kafka.ip";
    private static final String KAFKA_TOPIC = "kafka.topic";

    private static Properties prop = new Properties();
    private static final String DATE_FORMAT = "YYMMdd_HHmmss";

    static {
        opt_prop.setRequired(true);
        opts.addOption(opt_prop);
    }

    private static String initConfig(String configName){
        String configValue = prop.getProperty(configName);
        if (StringUtils.isEmpty(configValue)){
            LOG.error("{} 配置不能为空");
            System.exit(1);
        }
        LOG.info("{} 配置的值为 {}",configName,configValue);

        return configValue;
    }

    public static void main(String[] args) throws IOException {

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
        }

        propFile = line.getOptionValue("prop");
        prop = CKConfig.getProp(propFile);

        hdfsUrl = initConfig(HDFS_IP);
        hdfsPath = initConfig(HDFS_PATH);
        hdfsSnapshotDir = initConfig(HDFS_SNAP_DIR);
        executeName = initConfig(EXECUTE_NAME);
        kafkaBrokerIp = initConfig(KAFKA_IP);
        kafkaTopic = initConfig(KAFKA_TOPIC);
        filePattern = initConfig(FILE_PATTERN);

        DateFormat simpleFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateString = simpleFormat.format(Calendar.getInstance().getTime());

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(dateString);
        stringBuffer.append("_");
        stringBuffer.append(executeName);
        executeName = stringBuffer.toString();


        if (hdfsUrl.indexOf("hdfs") < 0) {
            hdfsUrl = "hdfs://" + hdfsUrl;
        }
        if (!hdfsSnapshotDir.startsWith("/")){
            LOG.error("快照文件路径非法 {}",hdfsSnapshotDir);
            System.exit(1);
        }
        hdfsSnapshotDir = String.format("%s/%s",hdfsSnapshotDir,executeName);

        LOG.info("{}: 需要同步 {} 目录 {} 符合文件名样式 {}", executeName, hdfsPath, hdfsUrl, filePattern);

        HdfsUtil hdfsUtil = new HdfsUtil(hdfsUrl, hdfsPath, filePattern);
        hdfsUtil.generateFileList(fileList);

        fileList.stream().forEach(fileStatus -> LOG.info("需要同步的文件： {}",fileStatus.getPath()));


        //todo:check the total volume of all the files


        for (FileStatus fileStatus : fileList) {
            MyKafkaProduce myKafkaProducer = new MyKafkaProduce(hdfsUtil, kafkaBrokerIp, kafkaTopic, prop);
            String sourceFileLocation = fileStatus.getPath().toString();
            String targetFileLocation =String.format("%s%s",hdfsSnapshotDir,sourceFileLocation.replace(hdfsUrl,""));
            LOG.info("handling file {}", sourceFileLocation);
            myKafkaProducer.avroProduce(fileStatus);
//            CosUtil.initBackUpDir(executeName, fileStatus, hdfsUtil.fs );

            try{
                hdfsUtil.copyFileToSnapshot(sourceFileLocation, targetFileLocation);
            }catch (Exception e){
                LOG.error("{} 移动异常 {}",sourceFileLocation,e.getMessage());
            }
            LOG.info("handling file finish {}", sourceFileLocation);
        }

        CosUtil.closeClient();
    }
}
