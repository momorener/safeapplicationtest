package com.study.momo.safeapplicationtest.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by momo on 2016/7/8.
 */
public class StreamUtil {
    /**
     * 流转换成字符串
     * @param is 流对象
     * @return 流转换成对的字符串
     */
    public static String streamToString(InputStream is){
        //在读取过程中，将读取的内容存储在缓存中，然后一次性的转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //读流操作，读到没有为止(循环
        byte[] buffer = new byte[1024];
        //记录读取内容的临时变量
        int temp = -1;
        try {
            while ((temp = is.read(buffer)) != -1){
                bos.write(buffer, 0, temp);
            }
            return bos.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }
}
