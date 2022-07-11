package com.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangchao on 21-8-24.
 */
@Slf4j
public class RSACoder {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 默认的公钥
     */
    private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbxh6LxGIAArtSR+uWZ+7S0EEyxdLr5vgZU44036RFtKxBqoVzevypxwQky75ndUozpi6FjitaZw4PW8eyVQPqWZLHinfLdVFZ+/rsnFsKnM/wZ4mYkFoNpshQk2qlehPODgzEEGTcyCrqjWQPdLeZtg90AQjNIBwdn/P79tCavQIDAQAB";

    /**
     * 默认的私钥
     */
    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJvGHovEYgACu1JH65Zn7tLQQTLF0uvm+BlTjjTfpEW0rEGqhXN6/KnHBCTLvmd1SjOmLoWOK1pnDg9bx7JVA+pZkseKd8t1UVn7+uycWwqcz/BniZiQWg2myFCTaqV6E84ODMQQZNzIKuqNZA90t5m2D3QBCM0gHB2f8/v20Jq9AgMBAAECgYBganw1nlPaFdbD0ugqrCHSMjxopLEsekVNx73ZVCNlUbLc4iK/m3wPybLy43jyqFJJLa7V021aAzdcoj40TxKgX5hLbRLPV3DaQasA8GHtG49c8j1L0uVEN4+bW4g0cJJcLb3jR3faOOatM8wtnrDVmI2o0aUu+XPBdQvDKlK1gQJBAMua0NJvmprdp5AqKsJJm8kHA11BvRy7QV/LPYmzNOOO+46QLR0PA3BEkqYhW0P9yodFU+wK49TfgAy/YwU6YuUCQQDD3Eb2CuOrBGKb2NGiG/NGAuIS+coiDMRFC798TVbViRvoNhOY3bojDuwr3X3SX49bZodilFY0k1xqQHxuFCL5AkBcPQTBcOH/qs8QlqnMXwB+b/jsNK3x6DtTpIV6bn9VYB9ClLp7YaPTJp2xOYdZJA6ufY/pbX2NoLsoC0RWx/6pAkEAvnXMg6KfNkSzZ332jT/E2AYhtfY1+9qSWdpMwSGsGUaVXsCHWhXz1ffC7f0bR6Ipk+YmLVS2Z7YNHj3LQnhOcQJBAMTy5DbK1MxpuXIidjKLgaoAQmNPxtJgGBAW0f1Gi/J/I+9MHLyS7alTlxWXHLzH8C8JU0ltwp4ECRYaKDsARvU=";


    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception{
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data) {
        String res;
        try {
            byte[] bytes = decryptByPrivateKey(decryptBASE64(data), privateKey);
            res = new String(bytes);
        }catch (Exception e){
            throw new RuntimeException("解密数据出错",e);
        }
        return res;
    }

    /**
     * 解密<br>
     * 用私钥分段解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptSubsectionByPrivateKey(String data) {
        String res;
        try {
            // 对密钥解密
            byte[] keyBytes = decryptBASE64(privateKey);
            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] bytes = decryptBASE64(data);
            int inputLen = bytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(bytes, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            res = new String(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("解密数据出错", e);
        }
        return res;
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data){
        // 对公钥解密
        try {
            byte[] keyBytes = decryptBASE64(publicKey);
            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return encryptBASE64(cipher.doFinal(data.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密<br>
     * 用公钥分段加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptSubsectionByPublicKey(String data) {
        // 对公钥解密
        try {
            byte[] keyBytes = decryptBASE64(publicKey);
            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            int inputLen = data.getBytes().length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data.getBytes(), offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data.getBytes(), offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptBASE64(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, Key> keyMap = new HashMap(2);
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
        return keyMap;
    }

//    public static void main(String[] args) throws Exception {
//        System.err.println("公钥加密——私钥解密");
//        String inputStr = "zhch8660305";
//        String encodedData = encryptBASE64(RSACoder.encryptByPublicKey(inputStr, publicKey));
//        byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData, privateKey);
//        String outputStr = new String(decodedData);
//        System.err.println("加密前: " + inputStr + "\n\r" + "加密后: " + encodedData + "\n\r" + "解密后: " + outputStr);
//    }

//    public static void main(String[] args) throws Exception {
//        Map<String, Key> keyMap = RSACoder.initKey();
//        String publicKey = RSACoder.getPublicKey(keyMap);
//        String privateKey = RSACoder.getPrivateKey(keyMap);
//        System.err.println("公钥: \n\r" + publicKey);
//        System.err.println("私钥： \n\r" + privateKey);
//    }
}
