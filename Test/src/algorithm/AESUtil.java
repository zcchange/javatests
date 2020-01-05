package algorithm;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * 用于登录密码，用户名加密
 */
public class AESUtil {
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    public static final String KEY = "phYtW1OSN9DhQxLm";


    /**
     *
     * */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        byte[] arrayByte = cipher.doFinal(content.getBytes("utf-8"));
        return arrayByte;
    }

    /**
     * 若有异常返回空字符串
     * @param decryptStr
     * @return
     */
    public static String decrypt(String decryptStr) {
        try {
            return aesDecrypt(decryptStr,KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 若有异常返回空字符串
     * @param encryptStr
     * @return
     */
    public static String encrypt(String encryptStr) {
        try {
            return aesEncrypt(encryptStr,KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 加密
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return parseByte2HexStr(aesEncryptToBytes(content, encryptKey));
    }

    /**
     *
     * */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }


    /**
     * @param encryptStr
     * @param decryptKey
     * @return
     * @throws Exception 原方法系统会报出如下异常：javax.crypto.IllegalBlockSizeException: Input length must be multiple of 16 when decrypting with padded cipher
     *                   <p>
     *                   这主要是因为加密后的byte数组是不能强制转换成字符串的, 换言之,字符串和byte数组在这种情况下不是互逆的,
     *                   要避免这种情况，将二进制数据转换成十六进制表示,
     *                   主要有两个方法:将二进制转换成16进制(见方法parseByte2HexStr)或是将16进制转换为二进制(见方法parseHexStr2Byte)
     *                   解密
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        //return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);

        /**
         * fixed by qinqinyan on 2017/10/24
         * 将16进制转换为二进制
         * */
        return aesDecryptByBytes(parseHexStr2Byte(encryptStr), decryptKey);
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     * @throws
     * @method parseByte2HexStr
     * @since v1.0
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     * @throws
     * @method parseHexStr2Byte
     * @since v1.0
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    /**
     * 测试
     */
    public static void main(String[] args) throws Exception {
        //String key = RandomStringUtils.randomAlphanumeric(16);

        String content = AESUtil.encrypt("root");
        System.out.println(content);
        String pre = AESUtil.decrypt(content);
        System.out.println(pre);
        System.out.println(AESUtil.encrypt("123456"));
        System.out.println(AESUtil.encrypt("AAA"));
        System.out.println(AESUtil.encrypt("4A"));
        System.out.println(AESUtil.encrypt("XYtu34!7"));
        System.out.println(AESUtil.encrypt("aac123456"));
        System.out.println(AESUtil.encrypt("ifsnmeyiassfddff"));
        System.out.println(AESUtil.encrypt("XYtu34!0"));
        System.out.println(AESUtil.encrypt("cmcc2019"));
//        String content = "123456";
//        String password = "phYtW1OSN9DhQxLm";
//        System.out.println("key ：" + password);
//
//        System.out.println("key length：" + password.length());
//        // 加密
//        System.out.println("加密前：" + content);
//        String content1 = aesEncrypt(content, password);
//        System.out.println("加密后的字符串:" + content1);
//
//        // 解密
//        String content2 = aesDecrypt(content1, password);
//        //转成string解密
//        System.out.println("转成string解密后：" + content2);

    }

}
