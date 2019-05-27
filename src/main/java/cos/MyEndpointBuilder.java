package cos;

import com.qcloud.cos.endpoint.EndpointBuilder;

/**
 * Created by zhou1 on 2019/5/23.
 */
public class MyEndpointBuilder implements EndpointBuilder {

    public String buildGeneralApiEndpoint(String bucketName) {
        return "cos.shanghai.tce.yonghuicloud.cn";
    }

    public String buildGetServiceApiEndpoint() {
        return "cos.shanghai.tce.yonghuicloud.cn";
    }
}
