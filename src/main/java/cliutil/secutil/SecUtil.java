package cliutil.secutil;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by zhou1 on 2019/5/30.
 */
public class SecUtil {

//    public static final String src = "rsa testkjalsdnflknalskdjfh;kalsjdklfnklansd;jkfjlaksdmnvkjnalskdfnlkajmsdk;lvnjlkansdlkvna;lksdjflk;ansdl;knfl;kasdjnfl;kjas;ldkfj;lajksdflknasdlvkn";
//
//    public static final String pubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJFEBU5BHGWHDl0vKvFlWBEbaZfyyIat75A182mmbCTCZRPw/ADOTMcrp4ZCm5HmrNbWACXkme+YvK3MeJsmt7kCAwEAAQ==";
//    public static final String priKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkU" +
//            "QFTkEcZYcOXS8q8WVYERtpl/LIhq3vkDXzaaZsJMJlE/D8AM5MxyunhkKbkeas1tYAJeSZ75i8rcx4mya3u" +
//            "QIDAQABAkAtCAUNCSogXwmY4XJSerQeOX02WVDIT0sgTVkzux9zFUcIm7QnhP/ICtV6A6eJcSDFSimPxwIepY3+" +
//            "cjyFpK4BAiEA4GNbtN4MAynaKQMmudCtso63Wj5sa1Gna4jG/cp3aVECIQCluxU+OWApF1ADanKBkiMgAOmwNmT0VFuCY5HEGoLN6QIhAIShFea" +
//            "dTLpd9Y5CR5STTOf6xVN5DDqO5AeCrCP8lYJRAiA0w4A65EdyLaLiw4QbLmySZtf/qwGvLCBKPesNlScp6QIgK" +
//            "Tmpw8F0EPj6Rp4jZbbjISN9eEZd1dofsWhRvvgRk2Q=";
//
//    public static byte[] pubkeyStream = Base64.decodeBase64(pubKey);
//    public static byte[] prikeyStream = Base64.decodeBase64(priKey);

    public static void main(String[] args) throws Exception{
        String content = "content test";

        KeyGenerator keygen=KeyGenerator.getInstance("AES");
        //2.根据ecnodeRules规则初始化密钥生成器
        //生成一个128位的随机源,根据传入的字节数组
        keygen.init(128, new SecureRandom("forrandom".getBytes()));
        //3.产生原始对称密钥
        SecretKey original_key=keygen.generateKey();

        System.err.println();


        byte [] raw=original_key.getEncoded();
        //5.根据字节数组生成AES密钥
        SecretKey key=new SecretKeySpec(raw, "AES");
        //6.根据指定算法AES自成密码器
        Cipher cipher=Cipher.getInstance("AES");
        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
        byte [] byte_encode=content.getBytes("utf-8");
        //9.根据密码器的初始化方式--加密：将数据加密
        byte [] byte_AES=cipher.doFinal(byte_encode);
        //10.将加密后的数据转换为字符串
        //这里用Base64Encoder中会找不到包
        //解决办法：
        //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
        String AES_encode=new String(new BASE64Encoder().encode(byte_AES));

        System.err.println(AES_encode);


//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(2048);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
//
////        System.err.println(rsaPublicKey.getClass());
////        System.err.println(rsaPrivateKey.getClass());
//
//        System.out.println("Public Key:" + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
//        System.out.println("Private Key:" + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
//
//
//        byte[] pubkeyStream = Base64.decodeBase64(pubKey);
//        byte[] prikeyStream = Base64.decodeBase64(priKey);
//
//
//        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(prikeyStream);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//        byte[] result = cipher.doFinal(src.getBytes());
////        System.out.println("私钥加密、公钥解密 ---- 加密:" + new String(result));
//        System.out.println("私钥加密、公钥解密 ---- 加密:" + Base64.encodeBase64String(result));
//
//        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubkeyStream);
//        keyFactory = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
//        cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, publicKey);
//        result = cipher.doFinal(result);
//        System.out.println("私钥加密、公钥解密 ---- 解密:" + new String(result));
    }
}
