package com.yx.housesale.common.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import lombok.extern.log4j.Log4j;
import java.nio.charset.Charset;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/09/15:14
 */
@Log4j
public class HashUtils {

    private static final String SALT = "com.yx";

    public static String encryPassword(String password){
        HashCode hashCode = Hashing.md5().hashString(password + SALT,
                Charset.forName("UTF-8"));
        String passwdSALTMD5 = hashCode.toString();
        log.debug("【passwdSALTMD5=】" + passwdSALTMD5);
        return passwdSALTMD5;
    }


}
