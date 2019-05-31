package cliutil.secutil;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    /*
   * 加密
   * 1.构造密钥生成器
   * 2.根据ecnodeRules规则初始化密钥生成器
   * 3.产生密钥
   * 4.创建和初始化密码器
   * 5.内容加密
   * 6.返回字符串
   */
    public  String AESEncode(String encodeRules,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte [] raw=original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key=new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher=Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
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
            //11.将字符串返回
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }
    /*
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public  String AESDncode(String encodeRules,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte [] raw=original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key=new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher=Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte [] byte_decode=cipher.doFinal(byte_content);
            String AES_decode=new String(byte_decode,"utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    public static void main(String[] args) throws Exception{
        SecUtil se = new SecUtil();
        String encodeRules = "encodeRules";
        String content = "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T" +
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T" +
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T"+
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T"+
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T"+
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T"+
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T"+
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T"+
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T"+
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T"+
                "设计方案 电风扇地方舒服的 DSFSDKFJLSDJFSKJFLSDKFJLSKJFJSDLFJSDFJSJDFKDSFLKDSJLFJSDLKFJSLJ（）P「AS……T";
        System.out.println(se.AESEncode(encodeRules,content));
        String result = "9X80zmi58z4dYLFSOE2V06RO9f6eAPjI7LNo3OKeUINKQlwP6t/8SOOy/g82BdokOEct4i9eKg9f\n" +
                "bvwPOJOl0ih/vuE5VPWW6NfVA2kSzD2MTUUd/vSintvqNpQ78F4v07tNHBK+UeLvj6N+4evayj1A\n" +
                "tTdQ0bQNOGlxiSNgF5z/ChZdvt2e0TbGwt9CC1L5dgj5I5eNi2fP7Ftk/WQZRa42USB/aLxwkHJh\n" +
                "Ls66qiMX2OGRAqLFTL8Eq82xIks+8qvJ1AQrxpUQd7DU2moPTFG5BYoRYa/H4RTR66MI3ZVH+Rlg\n" +
                "Y7SB1hog+HCJ9oF09X80zmi58z4dYLFSOE2V06RO9f6eAPjI7LNo3OKeUINKQlwP6t/8SOOy/g82\n" +
                "BdokOEct4i9eKg9fbvwPOJOl0ih/vuE5VPWW6NfVA2kSzD2MTUUd/vSintvqNpQ78F4v07tNHBK+\n" +
                "UeLvj6N+4evayj1AtTdQ0bQNOGlxiSNgF5z/ChZdvt2e0TbGwt9CC1L5dgj5I5eNi2fP7Ftk/WQZ\n" +
                "Ra42USB/aLxwkHJhLs66qiMX2OGRAqLFTL8Eq82xIks+8qvJ1AQrxpUQd7DU2moPTFG5BYoRYa/H\n" +
                "4RTR66MI3ZVH+RlgY7SB1hog+HCJ9oF09X80zmi58z4dYLFSOE2V06RO9f6eAPjI7LNo3OKeUINK\n" +
                "QlwP6t/8SOOy/g82BdokOEct4i9eKg9fbvwPOJOl0ih/vuE5VPWW6NfVA2kSzD2MTUUd/vSintvq\n" +
                "NpQ78F4v07tNHBK+UeLvj6N+4evayj1AtTdQ0bQNOGlxiSNgF5z/ChZdvt2e0TbGwt9CC1L5dgj5\n" +
                "I5eNi2fP7Ftk/WQZRa42USB/aLxwkHJhLs66qiMX2OGRAqLFTL8Eq82xIks+8qvJ1AQrxpUQd7DU\n" +
                "2moPTFG5BYoRYa/H4RTR66MI3ZVH+RlgY7SB1hog+HCJ9oF09X80zmi58z4dYLFSOE2V06RO9f6e\n" +
                "APjI7LNo3OKeUINKQlwP6t/8SOOy/g82BdokOEct4i9eKg9fbvwPOJOl0ih/vuE5VPWW6NfVA2kS\n" +
                "zD2MTUUd/vSintvqNpQ78F4v07tNHBK+UeLvj6N+4evayj1AtTdQ0bQNOGlxiSNgF5z/ChZdvt2e\n" +
                "0TbGwt9CC1L5dgj5I5eNi2fP7Ftk/WQZRa42USB/aLxwkHJhLs66qiMX2OGRAqLFTL8Eq82xIks+\n" +
                "8qvJ1AQrxpUQd7DU2moPTFG5BYoRYa/H4RTR66MI3ZVH+RlgY7SB1hog+HCJ9oF09X80zmi58z4d\n" +
                "YLFSOE2V06RO9f6eAPjI7LNo3OKeUINKQlwP6t/8SOOy/g82BdokOEct4i9eKg9fbvwPOJOl0ih/\n" +
                "vuE5VPWW6NfVA2kSzD2MTUUd/vSintvqNpQ78F4v07tNHBK+UeLvj6N+4evayj1AtTdQ0bQNOGlx\n" +
                "iSNgF5z/ChZdvt2e0TbGwt9CC1L5dgj5I5eNi2fP7Ftk/WQZRa42USB/aLxwkHJhLs66qiMX2OGR\n" +
                "AqLFTL8Eq82xIks+8qvJ1AQrxpUQd7DU2moPTFG5BYoRYa/H4RTR66MI3ZVH+RlgY7SB1hog+HCJ\n" +
                "9oF09X80zmi58z4dYLFSOE2V06RO9f6eAPjI7LNo3OKeUINKQlwP6t/8SOOy/g82BdokOEct4i9e\n" +
                "Kg9fbvwPOJOl0ih/vuE5VPWW6NfVA2kSzD2MTUUd/vSintvqNpQ78F4v07tNHBK+UeLvj6N+4eva\n" +
                "yq/TwhhegaapLX+A+e1K26o=";
        System.out.println(se.AESDncode(encodeRules,result));
    }
}
