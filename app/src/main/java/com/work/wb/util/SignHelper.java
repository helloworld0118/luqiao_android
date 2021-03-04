package com.work.wb.util;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * 签名加密工具类
 */
public class SignHelper {

	private static  HashMap<String,String> dealRequestMap(Map<String, String[]> paramMap) {
		//request对象封装的参数是以Map的形式存储的
        HashMap<String,String> params = new HashMap<>();
        for(Map.Entry<String, String[]> entry :paramMap.entrySet()){
            String paramName = entry.getKey();
            String paramValue = "";
            String[] paramValueArr = entry.getValue();
            for (int i = 0; paramValueArr!=null && i < paramValueArr.length; i++) {
                if (i == paramValueArr.length-1) {
                    paramValue+=paramValueArr[i];
                }else {
                    paramValue+=paramValueArr[i]+",";
                }
            }
            params.put(paramName, paramValue);
        }
        return params;
	}

	public static String getSignature(Map<String, String> params, String token,String SALT) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
		//HashMap<String,String> params = dealRequestMap(paramMap);
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起

        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            //sign参数 和 空值参数 不加入算法

            if(param.getValue()!=null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
               // Log.e("UtilsSignHelper",param.getKey().trim()+"===----==="+param.getValue().trim());
                baseString.append(param.getKey().trim()).append("=").append(param.getValue().trim()).append("&");
            }
        }
        if(baseString.length() > 0 ) {
            baseString.deleteCharAt(baseString.length() - 1).append(token);
        }

        // 使用MD5对待签名串求签
        return getMD5WithSalt(baseString.toString(),SALT);
    }

	
    /**
     * 获取MD5字符串
     */
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            try {
                digest.update(content.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //private static final String SALT = "0fdfa5e5a88bebae640a5d88e7c84708";

    /**
     * 获取加盐的MD5字符串
     */
    private static String getMD5WithSalt(String content,String SALT) {
        return getMD5(getMD5(content) + SALT);
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }


}