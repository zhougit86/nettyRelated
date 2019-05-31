package cliutil.cosutil;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.endpoint.EndpointBuilder;
import com.qcloud.cos.endpoint.SuffixEndpointBuilder;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhou1 on 2019/5/29.
 */
public class CosUtil {
    private static Logger LOG = LoggerFactory.getLogger(CosUtil.class);
    private static final String BUCKET_NAME = "test-1255000077";
    private static final String REGION = "shanghai";
    private static final String END_POINT_SUFFIX = "cos.shanghai.tce.yonghuicloud.cn";
    private static final String SEC_ID = "AKID0g0yaqxIUw2mBEsGGwjAG2SYJilkCeZ8";
    private static final String SEC_KEY = "PTBXXCVS4KEn0rMVIkqay0h21eW3E3Xf";

    private static final TransferManager transferManager;

    static {
        COSCredentials cred = new BasicCOSCredentials(SEC_ID, SEC_KEY);

        ClientConfig clientConfig = new ClientConfig(new Region(REGION));
        EndpointBuilder yonghuiEndpointBuilder = new SuffixEndpointBuilder(END_POINT_SUFFIX);
        clientConfig.setEndpointBuilder(yonghuiEndpointBuilder);
        COSClient cosClient = new COSClient(cred, clientConfig);
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        transferManager = new TransferManager(cosClient, threadPool);
    }

    public static void initBackUpDir(String runName, FileStatus fileStatus, FileSystem fileSystem){

        LOG.info("begin upload the file {}",fileStatus.getPath().toString());
        try{
            FSDataInputStream localFileStream = fileSystem.open(fileStatus.getPath());
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(fileStatus.getLen());

            String filePath = fileStatus.getPath().toString();
            filePath = filePath.substring(filePath.lastIndexOf(":"));
            System.out.println(filePath);
            String hdfsPath = filePath.substring(filePath.indexOf("/"));
            System.out.println(hdfsPath);

            String key = String.format("%s/%s",runName,hdfsPath);
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(BUCKET_NAME, key, localFileStream,meta);
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForCompletion();
        }catch (IOException e){
            LOG.error("upload cos failed {}",fileStatus.getPath());
        }catch (InterruptedException e){
            LOG.error("upload cos failed {}",fileStatus.getPath());
        }
    }

    public static void closeClient(){
        transferManager.shutdownNow(true);
    }
}
