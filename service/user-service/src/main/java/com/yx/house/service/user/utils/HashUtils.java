package com.yx.house.service.user.utils;

import com.google.common.base.Throwables;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class HashUtils {

    private static final String SALT = "com.yx";

    public static String encryPassword(String password){
        HashCode hashCode = Hashing.md5().hashString(password + SALT,
                Charset.forName("UTF-8"));
        String passwdSALTMD5 = hashCode.toString();
        return passwdSALTMD5;
    }

    public static String hashString(String input){
        HashCode code = null;
        try {
            code = Hashing.murmur3_128().hashBytes(input.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            Throwables.propagate(e);
        }
        return code.toString();
    }
}
