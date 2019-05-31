package cliutil.serialutil;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import static cliutil.secutil.SecUtil.pubkeyStream;

/**
 * Created by zhou1 on 2019/5/30.
 */
public class SecDeSerializer extends StringDeserializer {
    private String encoding = "UTF8";

    public SecDeSerializer() {
    }

    public String deserialize(String topic, byte[] data) {

        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubkeyStream);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            data = cipher.doFinal(data);


        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return super.deserialize(topic,data);
    }
}
