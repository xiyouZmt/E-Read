package com.zmt.e_read.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by MintaoZhu on 2016/10/22.
 */
public class MD5Utils {

    private String url;

    public static MD5Utils getInstance(String url){
        return new MD5Utils(url);
    }

    public MD5Utils(String url) {
        this.url = url;
    }

    public String hashKeyFromUrl(){
        String cacheKry = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKry = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cacheKry;
    }

    public String bytesToHexString(byte [] bytes){
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xff & aByte);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }
}
