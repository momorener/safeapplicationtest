package com.study.momo.safeapplicationtest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by momo on 2016/7/26.
 */
public class SpUtils {
    private static SharedPreferences sp;


    /**
     *   写入boolean变量至sp中
     * @param context  上下文环境
     * @param key    存储节点名称
     * @param value  存储节点的值 boolean
     */
    public static void putBoolean(Context context, String key, boolean value){
        //name: 存储节点文件名称
        //读写方式
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();

    }
    //读

    /**
     *    从sp中读取boolean标识
     * @param context  上下文环境
     * @param key    存储节点名称
     * @param defvalue  没有此节点默认值
     * @return    默认值或者此节点读取到的结果
     */
    public static boolean getBoolean(Context context, String key, boolean defvalue){
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defvalue);
    }

    /**
     *   写入boolean变量至sp中
     * @param context  上下文环境
     * @param key    存储节点名称
     * @param value  存储节点的值 string
     */
    public static void putString(Context context, String key, String value){
        //name: 存储节点文件名称
        //读写方式
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();

    }
    //读

    /**
     *    从sp中读取boolean标识
     * @param context  上下文环境
     * @param key    存储节点名称
     * @param defvalue  没有此节点默认值
     * @return    默认值或者此节点读取到的结果
     */
    public static String getString(Context context, String key, String defvalue){
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,defvalue);
    }

    /**
     * 从sp中移除指定节点
     * @param context 上下文环境
     * @param key  需要移除节点的名称
     */

    public static void remove(Context context, String key){
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }
}
