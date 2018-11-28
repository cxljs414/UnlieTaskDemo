package com.example.cx.unlietaskdemo.utils;

public class JniUtil {
    static {
        System.loadLibrary("JniUtil");
    }
    public native String encrypt(String plainText);
    public native String decrypt(String cipherText);

}
