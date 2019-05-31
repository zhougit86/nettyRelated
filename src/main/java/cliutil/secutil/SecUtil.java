package cliutil.secutil;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by zhou1 on 2019/5/30.
 */
public class SecUtil {

    public static final String src = "rsa testkjalsdnflknalskdjfh;kalsjdklfnklansd;jkfjlaksdmnvkjnalskdfnlkajmsdk;lvnjlkansdlkvna;lksdjflk;ansdl;knfl;kasdjnfl;kjas;ldkfj;lajksdflknasdlvkn";

    public static final String pubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJFEBU5BHGWHDl0vKvFlWBEbaZfyyIat75A182mmbCTCZRPw/ADOTMcrp4ZCm5HmrNbWACXkme+YvK3MeJsmt7kCAwEAAQ==";
    public static final String priKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkU" +
            "QFTkEcZYcOXS8q8WVYERtpl/LIhq3vkDXzaaZsJMJlE/D8AM5MxyunhkKbkeas1tYAJeSZ75i8rcx4mya3u" +
            "QIDAQABAkAtCAUNCSogXwmY4XJSerQeOX02WVDIT0sgTVkzux9zFUcIm7QnhP/ICtV6A6eJcSDFSimPxwIepY3+" +
            "cjyFpK4BAiEA4GNbtN4MAynaKQMmudCtso63Wj5sa1Gna4jG/cp3aVECIQCluxU+OWApF1ADanKBkiMgAOmwNmT0VFuCY5HEGoLN6QIhAIShFea" +
            "dTLpd9Y5CR5STTOf6xVN5DDqO5AeCrCP8lYJRAiA0w4A65EdyLaLiw4QbLmySZtf/qwGvLCBKPesNlScp6QIgK" +
            "Tmpw8F0EPj6Rp4jZbbjISN9eEZd1dofsWhRvvgRk2Q=";

    public static byte[] pubkeyStream = Base64.decodeBase64(pubKey);
    public static byte[] prikeyStream = Base64.decodeBase64(priKey);

    public static void main(String[] args) throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

//        System.err.println(rsaPublicKey.getClass());
//        System.err.println(rsaPrivateKey.getClass());

        System.out.println("Public Key:" + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
        System.out.println("Private Key:" + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));


        byte[] pubkeyStream = Base64.decodeBase64(pubKey);
        byte[] prikeyStream = Base64.decodeBase64(priKey);


        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(prikeyStream);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(src.getBytes());
//        System.out.println("私钥加密、公钥解密 ---- 加密:" + new String(result));
        System.out.println("私钥加密、公钥解密 ---- 加密:" + Base64.encodeBase64String(result));

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubkeyStream);
        keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        result = cipher.doFinal(result);
        System.out.println("私钥加密、公钥解密 ---- 解密:" + new String(result));
    }
}
