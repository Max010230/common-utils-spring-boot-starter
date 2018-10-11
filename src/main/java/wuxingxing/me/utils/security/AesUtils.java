package wuxingxing.me.utils.security;

import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * aes加密工具类
 * @author wll
 */
public class AesUtils {

    /**
     * 加密
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static String aesEncrypt(String content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            //构建Cipher,设置模式，解密的时候也必须使用同样的模式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //使用生成的密钥初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //执行加密过程
            byte[] encode = cipher.doFinal(content.getBytes("utf-8"));
            return Base64Utils.encodeToString(encode);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | UnsupportedEncodingException
                | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String aesDecrypt(String content, String password) {
        //根据给出的密钥（字节数组）生成一个Key对象
        SecretKey key = new SecretKeySpec(password.getBytes(), "AES");
        try {
            //生成一个和加密时候使用的模式一样的Cipher
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            //执行解密步骤
            byte[] decode = cipher.doFinal(Base64Utils.decodeFromString(content));
            return new String(decode);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    // ==== md5 ====

    /**
     * 获取byte[]的md5值
     * @param bytes byte[]
     * @return md5
     * @throws Exception
     */
    public static byte[] md5(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        return md.digest();
    }

    public static String toMD5(String str, String key) {
        if(key == null) {
            key = "";
        }
        return toMD5(str + "&key=" + key);
    }

    /***
     * MD5 生成32位md5码
     */
    public static String toMD5(String str) {
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes){
                int bt = b&0xff;
                if (bt < 16){
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }
}
