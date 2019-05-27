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
    static String bucketName = "test-1255000077"; //桶的名称
    static String region = "shanghai";//区域北京则  beijing
    static String endPointSuffix = "cos.shanghai.tce.yonghuicloud.cn";

    static COSCredentials cred = null;
    static TransferManager transferManager = null;
    static COSClient cosClient = null;

    public static void main(String[] args) {
        // 1 初始化用户身份信息(secretId, secretKey)
        //SecretId 是用于标识 API 调用者的身份
        String SecretId = "AKID0g0yaqxIUw2mBEsGGwjAG2SYJilkCeZ8";
        //SecretKey是用于加密签名字符串和服务器端验证签名字符串的密钥
        String SecretKey = "PTBXXCVS4KEn0rMVIkqay0h21eW3E3Xf";
        cred = new BasicCOSCredentials(SecretId, SecretKey);

        // 2 设置bucket的区域,
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        EndpointBuilder yonghuiEndpointBuilder = new SuffixEndpointBuilder(endPointSuffix);
        clientConfig.setEndpointBuilder(yonghuiEndpointBuilder);

        // 3 生成cos客户端
        cosClient = new COSClient(cred, clientConfig);
        // 指定要上传到 COS 上的路径
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池, 默认 TransferManager 中会生成一个单线程的线程池。
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
