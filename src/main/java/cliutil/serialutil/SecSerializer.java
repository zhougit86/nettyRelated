package cliutil.serialutil;

import cliutil.secutil.SecUtil;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

/**
 * Created by zhou1 on 2019/5/30.
 */
public class SecSerializer extends StringSerializer {
    private String encoding = "UTF8";

    public SecSerializer(){
        super();
    }

    public byte[] serialize(String topic, String data){
        byte[] unSecuredStream = super.serialize(topic,data);
        byte[] securedStream = new byte[0];

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(SecUtil.prikeyStream);
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            cipher.doFinal(unSecuredStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return securedStream;
    }

}
