package cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.endpoint.EndpointBuilder;
import com.qcloud.cos.endpoint.SuffixEndpointBuilder;
import com.qcloud.cos.endpoint.UserSpecifiedEndpointBuilder;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhou1 on 2019/5/23.
 */
public class CosDriver {
    static String bucketName = "test-1255000077";
    static String region = "shanghai";
    static String endPointSuffix = "cos.shanghai.tce.yonghuicloud.cn";

    static COSCredentials cred = null;
    static TransferManager transferManager = null;
    static COSClient cosClient = null;

    public static void main(String[] args) {
        String SecretId = "AKID0g0yaqxIUw2mBEsGGwjAG2SYJilkCeZ8";
        String SecretKey = "PTBXXCVS4KEn0rMVIkqay0h21eW3E3Xf";
        cred = new BasicCOSCredentials(SecretId, SecretKey);

        ClientConfig clientConfig = new ClientConfig(new Region(region));
        EndpointBuilder yonghuiEndpointBuilder = new SuffixEndpointBuilder(endPointSuffix);
        clientConfig.setEndpointBuilder(yonghuiEndpointBuilder);

        cosClient = new COSClient(cred, clientConfig);
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        transferManager = new TransferManager(cosClient, threadPool);

//        List<Bucket> myList = cosClient.listBuckets();
//        System.err.println(myList);

//        ObjectListing objectListing = cosClient.listObjects(bucketName);
//        System.err.println(objectListing.getBucketName());
//        objectListing.getObjectSummaries().stream().forEach(x->{
//            System.err.println(x.getKey());
//            System.err.println(x.getSize());
//        });

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://192.168.13.128:9000");
        try{
            FileSystem fs = FileSystem.get(conf);
            FileStatus fileStatus = fs.getFileStatus(new Path("/zk.gz"));


            FSDataInputStream localFileStream = fs.open(new Path("/zk.gz"));
//            File localFile = new File("src/main/resources/test.txt");

            System.err.println(fs.getClass());


            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(fileStatus.getLen());

//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "haha.txt", localFile);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "haha1.txt", localFileStream,meta);
            Upload upload = transferManager.upload(putObjectRequest);
            try{
                System.err.println(Calendar.getInstance().getTime());
                upload.waitForCompletion();
                System.err.println(Calendar.getInstance().getTime());
            }catch (Exception e){
                e.printStackTrace();
            }
            transferManager.shutdownNow();
        }catch (Exception e){
            e.printStackTrace();
        }


//        File localFile = new File("src/main/resources/test.txt");
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "haha.txt", localFile);
//        Upload upload = transferManager.upload(putObjectRequest);
//        try{
//            upload.waitForCompletion();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        transferManager.shutdownNow();

    }


}
