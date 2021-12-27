package com.example.demo1.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String passToDbPass(String formPass, String randSalt) {
        String str = "" + randSalt.charAt(0) + randSalt.charAt(2) + formPass + randSalt.charAt(5) + randSalt.charAt(4);
        return md5(str);
    }

    public static void main(String[] args) {
        System.out.println(passToDbPass("123456", "tew46f"));
    }
}
