package com.xiezhiai.wechatplugin.utils.encrypt;

import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by shijiwei on 2018/10/16.
 *
 * @Desc:
 */
public class MD5 {

    public static String getMD5Str(String value) {
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if (md5 != null) {
                md5.reset();
                md5.update(value.getBytes("UTF-8"));
                byte[] data = md5.digest();

                for (int i = 0; i < data.length; i++) {
                    if ((Integer.toHexString(0xFF & (int) data[i])).length() == 1)
                        md5StrBuff.append("0").append(Integer.toHexString(0xFF & (int) data[i]));
                    else
                        md5StrBuff.append(Integer.toHexString(0xFF & (int) data[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return md5StrBuff.toString();
        }
    }


    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    public static String getSalt() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int p = new Random().nextInt(chars.length);
            buffer.append(chars[p]);
        }
        return buffer.toString();
    }


    public static String getMD5String(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes("UTF-8"));
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 判断字符串的编码
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode[] = new String[]{
                "UTF-8",
                "ISO-8859-1",
                "GB2312",
                "GBK",
                "GB18030",
                "Big5",
                "Unicode",
                "ASCII"
        };
        for (int i = 0; i < encode.length; i++) {
            try {
                if (str.equals(new String(str.getBytes(encode[i]), encode[i]))) {
                    return encode[i];
                }
            } catch (Exception ex) {
            }
        }

        return "";
    }

}
