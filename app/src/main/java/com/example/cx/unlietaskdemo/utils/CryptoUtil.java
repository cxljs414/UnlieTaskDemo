package com.example.cx.unlietaskdemo.utils;

public class CryptoUtil {

    /**
     * 签名验证
     * @param md5Str
     * @return true:验证通过,false:验证没有通过
     */
    public static boolean verify(String str, String timestamp, String md5Str){
        if(generate(str, timestamp).equals(md5Str)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 生成签名
     * @return
     */
    public static String generate(String str, String timestamp){
        StringBuilder strb = new StringBuilder(str);
        strb.append("Abc_").append(timestamp).append("_xnZ");
        String md5Str = new MD5().getMD5ofStr(strb.toString());
        return md5Str;
    }
}
