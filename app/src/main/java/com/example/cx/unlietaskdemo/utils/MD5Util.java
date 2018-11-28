package com.example.cx.unlietaskdemo.utils;

import java.security.MessageDigest;

public class MD5Util {
    public static byte[] md5(byte[] info) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(info);
        return md.digest();
    }

    public static String md5(String info) {
        String reStr;
        try {
            byte[] bytes = md5(info.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (Exception e) {
            return null;
        }
        return reStr;
    }


}

